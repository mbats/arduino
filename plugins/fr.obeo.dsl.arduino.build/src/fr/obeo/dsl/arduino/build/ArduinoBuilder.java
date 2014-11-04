/**
 *  Copyright (c) 2013 Obeo.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *    Obeo - initial API and implementation
 */
package fr.obeo.dsl.arduino.build;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.google.common.collect.Lists;

/**
 * Arduino builder is used to cross-compile ino files in order to be able to
 * execute the code on an Arduino board.
 * 
 * @author Melanie Bats<a href="mailto:melanie.bats@obeo.fr">melanie
 *         .bats@obeo.fr</a>
 */
public class ArduinoBuilder {
	private static final String WINDOWS = "win";

	private static final String MACOS = "mac os";

	/**
	 * Arduino SDK path.
	 */
	private final String arduinoSdk;

	/**
	 * Arduino serial port.
	 */
	private final String serialPort;

	/**
	 * Board tag.
	 */
	private final String boardTag;
	/**
	 * Working directory.
	 */
	private final File directory;

	/**
	 * OS.
	 */
	private String os;

	/**
	 * Arduino builder is used to cross-compile ino files in order to be able to
	 * execute the code on an Arduino board.
	 * 
	 * @param arduinoSdk
	 *            Path to the Arduino SDK
	 * @param boardTag
	 *            Board tag
	 * @param directory
	 *            Working directory
	 * @param serialPort
	 *            the name of the Arduino serial port.
	 */
	public ArduinoBuilder(String arduinoSdk, String boardTag, String directory,
			String serialPort) {
		this.boardTag = boardTag;
		this.directory = new File(directory);
		this.os = System.getProperty("os.name").toLowerCase();
		this.arduinoSdk = getCorrectedPath(arduinoSdk, os);
		this.serialPort = serialPort;
	}

	private static String getCorrectedPath(String arduinoSdkPath,
			String currentOS) {
		String path = arduinoSdkPath;
		if (!path.endsWith(File.separator)) {
			path += File.separator;
		}
		if (currentOS.contains(MACOS) && arduinoSdkPath != null
				&& !arduinoSdkPath.contains(".app")) {
			path = path + "Arduino.app/Contents/Resources/Java/";
		}		
		return path;
	}

	/**
	 * To test.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String BOARD_TAG = "uno";
		String ARDUINO_SDK_LINUX = "/home/melanie/Obeo/dev/arduino/arduino-1.0.5/";
		String FOLDER_LINUX = "/home/melanie/Obeo/dev/arduino/workspace_sirius/fr.obeo.dsl.arduino.build/test/";
		String ARDUINO_SDK_MACOSX = "/Users/MpoObeo/Dev/arduino/arduino-1.0.6/Arduino/";
		String FOLDER_MACOSX = "/Users/MpoObeo/Dev/arduino/workspace_sirius/fr.obeo.dsl.arduino.build/test/";
		String ARDUINO_SDK_WIN = "C:\\Program Files\\Arduino\\";
		String FOLDER_WIN = "C:\\Documents and Settings\\toto\\runtime-EclipseApplication\\test\\";

		String os = System.getProperty("os.name").toLowerCase();
		String arduinoSdk = null;
		String folder = null;
		String serialPort = null;
		if (os.contains(WINDOWS)) {
			arduinoSdk = ARDUINO_SDK_WIN;
			folder = FOLDER_WIN;
			serialPort= "COM3";
		} else if (os.contains("nux")) {
			arduinoSdk = ARDUINO_SDK_LINUX;
			folder = FOLDER_LINUX;
			serialPort = "/dev/ttyACM0";
		} else if (os.contains(MACOS)) {
			arduinoSdk = ARDUINO_SDK_MACOSX;
			folder = FOLDER_MACOSX;
			serialPort = "/dev/tty.usbmodem1421";
		}
		ArduinoBuilder builder = new ArduinoBuilder(arduinoSdk, BOARD_TAG,
				folder, serialPort);
		List<String> libraries = new ArrayList<String>();
		libraries.add("Servo");
		IStatus status = builder.compile("Sketch", libraries);
		System.out.println(status);
	}

	/**
	 * Compile a sketch and its specific libraries.
	 * 
	 * @param sketchName
	 *            Sketch name
	 * @param libraries
	 *            Specific libraries names.
	 */
	public IStatus compile(String sketchName, List<String> libraries) {

		// Compile sketch
		IStatus sketchStatus = compileSketch(sketchName, libraries);
		if (sketchStatus.getSeverity() != IStatus.OK) {
			return sketchStatus;
		}

		// Compile main libraries
		IStatus mainLibrariesStatus = compileMainLibraries();
		if (mainLibrariesStatus.getSeverity() != IStatus.OK) {
			return mainLibrariesStatus;
		}

		// Compile specific libraries
		if (libraries != null) {
			for (String library : libraries) {
				String libraryName = Character.toUpperCase(library.charAt(0))
						+ library.substring(1);
				IStatus specificLibraryStatus = compileSpecificLibrary(
						arduinoSdk + "libraries" + File.separator + libraryName
								+ File.separator, libraryName);
				if (specificLibraryStatus.getSeverity() != IStatus.OK) {
					return specificLibraryStatus;
				}
			}
		}

		// Link all
		IStatus linkStatus = link(sketchName, libraries);
		if (linkStatus.getSeverity() != IStatus.OK) {
			return linkStatus;
		}

		// Generate arduino.hex
		return generateArduinoHex();
	}

	public IStatus upload() {
		System.out.println("Upload arduino.hex");
		String command = arduinoSdk + "hardware" + File.separator + "tools"
				+ File.separator;
		ProcessBuilder builder = null;
		if (os.contains(WINDOWS)) {
			command += "avr" + File.separator + "bin" + File.separator
					+ "avrdude.exe";
			builder = new ProcessBuilder(command, "-q", "-V", "-p", getMMCU(),
					"-C", arduinoSdk + "hardware" + File.separator + "tools"
							+ File.separator + "avr" + File.separator + "etc"
							+ File.separator + "avrdude.conf", "-c", "arduino",
					"-b", "115200", "-P", serialPort, "-U", "flash:w:arduino.hex:i");
		} else if (os.contains(MACOS)) {
			command += "avr" + File.separator + "bin" + File.separator
					+ "avrdude";
			builder = new ProcessBuilder(command, "-q", "-V", "-p", getMMCU(),
					"-C", arduinoSdk + "hardware" + File.separator + "tools"
							+ File.separator + "avr" + File.separator + "etc"
							+ File.separator + "avrdude.conf", "-c", "arduino",
					"-b", "115200", "-P", serialPort, "-U",
					"flash:w:arduino.hex:i");
		} else {
			command += "avrdude";
			builder = new ProcessBuilder(command, "-q", "-V", "-p", getMMCU(),
					"-C", arduinoSdk + "hardware" + File.separator + "tools"
							+ File.separator + "avrdude.conf", "-c", "arduino",
					"-b", "115200", "-P", serialPort, "-U",
					"flash:w:arduino.hex:i");
		}
		return executeCommand(directory, builder);
	}

	/**
	 * Generate arduino.hex file.
	 */
	private IStatus generateArduinoHex() {
		System.out.println("Generate arduino.hex");
		String command = arduinoSdk + "hardware" + File.separator + "tools"
				+ File.separator + "avr" + File.separator + "bin"
				+ File.separator + "avr-objcopy";
		ProcessBuilder builder = new ProcessBuilder(command, "-j", ".eeprom",
				"--set-section-flags=.eeprom=alloc,load",
				"--change-section-lma", ".eeprom=0", "-O", "ihex",
				"arduino.elf", "arduino.eep");
		executeCommand(directory, builder);
		builder = new ProcessBuilder(command, "-O", "ihex", "-R", ".eeprom",
				"arduino.elf", "arduino.hex");
		return executeCommand(directory, builder);
	}

	/**
	 * Link all.
	 * 
	 * @param sketchName
	 *            Arduino sketch file name
	 */
	private IStatus link(String sketchName, List<String> libraries) {
		System.out.println("Link all");
		String command = arduinoSdk + "hardware" + File.separator + "tools"
				+ File.separator + "avr" + File.separator + "bin"
				+ File.separator + "avr-gcc";
		List<String> commands = Lists.newArrayList();
		ProcessBuilder builder = new ProcessBuilder(commands);
		commands.add(command);
		commands.add("-mmcu=" + getMMCU());
		commands.add("-Wl,--gc-sections");
		commands.add("-Os");
		commands.add("-o");
		commands.add("arduino.elf");
		commands.add(sketchName + ".o");
		commands.add("WInterrupts.o");
		commands.add("wiring_analog.o");
		commands.add("wiring.o");
		commands.add("wiring_digital.o");
		commands.add("wiring_pulse.o");
		commands.add("wiring_shift.o");
		commands.add("CDC.o");
		commands.add("HardwareSerial.o");
		commands.add("HID.o");
		commands.add("IPAddress.o");
		commands.add("main.o");
		commands.add("new.o");
		commands.add("Print.o");
		commands.add("Stream.o");
		commands.add("Tone.o");
		commands.add("USBCore.o");
		commands.add("WMath.o");
		commands.add("WString.o");
		for (String library : libraries) {
			String libraryName = Character.toUpperCase(library.charAt(0))
					+ library.substring(1);
			commands.add(libraryName + ".o");
		}
		commands.add("-lc");
		commands.add("-lm");
		return executeCommand(directory, builder);
	}

	/**
	 * Compile specific library.
	 * 
	 * @param libraryName
	 *            Library name
	 */
	private IStatus compileSpecificLibrary(String libraryPath,
			String libraryName) {
		System.out.println("Compile Specific Libraries");
		return compileCPPFile(libraryPath, libraryName);
	}

	/**
	 * Compile the main Arduino libraries.
	 */
	private IStatus compileMainLibraries() {
		System.out.println("Compile main libraries");
		String arduinoMainLibraryPath = arduinoSdk + "hardware"
				+ File.separator + "arduino" + File.separator + "cores"
				+ File.separator + "arduino" + File.separator;
		IStatus status = null;
		status = compileCFile(arduinoMainLibraryPath, "WInterrupts");
		if (status.getSeverity() != IStatus.OK)
			return status;
		status = compileCFile(arduinoMainLibraryPath, "wiring_analog");
		if (status.getSeverity() != IStatus.OK)
			return status;
		status = compileCFile(arduinoMainLibraryPath, "wiring");
		if (status.getSeverity() != IStatus.OK)
			return status;
		status = compileCFile(arduinoMainLibraryPath, "wiring_digital");
		if (status.getSeverity() != IStatus.OK)
			return status;
		status = compileCFile(arduinoMainLibraryPath, "wiring_pulse");
		if (status.getSeverity() != IStatus.OK)
			return status;
		status = compileCFile(arduinoMainLibraryPath, "wiring_shift");
		if (status.getSeverity() != IStatus.OK)
			return status;
		status = compileCPPFile(arduinoMainLibraryPath, "CDC");
		if (status.getSeverity() != IStatus.OK)
			return status;
		status = compileCPPFile(arduinoMainLibraryPath, "HardwareSerial");
		if (status.getSeverity() != IStatus.OK)
			return status;
		status = compileCPPFile(arduinoMainLibraryPath, "HID");
		if (status.getSeverity() != IStatus.OK)
			return status;
		status = compileCPPFile(arduinoMainLibraryPath, "IPAddress");

		if (status.getSeverity() != IStatus.OK)
			return status;
		status = compileCPPFile(arduinoMainLibraryPath, "main");
		if (status.getSeverity() != IStatus.OK)
			return status;
		status = compileCPPFile(arduinoMainLibraryPath, "new");
		if (status.getSeverity() != IStatus.OK)
			return status;
		status = compileCPPFile(arduinoMainLibraryPath, "Print");
		if (status.getSeverity() != IStatus.OK)
			return status;
		status = compileCPPFile(arduinoMainLibraryPath, "Stream");
		if (status.getSeverity() != IStatus.OK)
			return status;
		status = compileCPPFile(arduinoMainLibraryPath, "Tone");
		if (status.getSeverity() != IStatus.OK)
			return status;
		status = compileCPPFile(arduinoMainLibraryPath, "USBCore");
		if (status.getSeverity() != IStatus.OK)
			return status;
		status = compileCPPFile(arduinoMainLibraryPath, "WMath");
		if (status.getSeverity() != IStatus.OK)
			return status;
		compileCPPFile(arduinoMainLibraryPath, "WString");
		return status;
	}

	/**
	 * Compile ino file.
	 * 
	 * @param filePath
	 *            Path of the ino file to compile
	 */
	private IStatus compileSketch(String fileName, List<String> libraries) {
		System.out.println("Compile Sketch");
		String command = arduinoSdk + "hardware" + File.separator + "tools"
				+ File.separator + "avr" + File.separator + "bin"
				+ File.separator + "avr-g++";

		if (os.contains(WINDOWS)) {
			command += ".exe";
		}
		List<String> commands = Lists.newArrayList();
		commands.add(command);
		commands.add("-x");
		commands.add("c++");
		commands.add("-include");
		commands.add("Arduino.h");
		commands.add("-c");
		commands.add("-mmcu=" + getMMCU());
		commands.add("-DF_CPU=" + getDFCPU());
		commands.add("-DARDUINO=" + getDArduino());
		commands.add("-I.");
		commands.add("-I" + arduinoSdk + "hardware" + File.separator
				+ "arduino" + File.separator + "cores" + File.separator
				+ "arduino");
		commands.add("-I" + arduinoSdk + "hardware" + File.separator
				+ "arduino" + File.separator + "variants" + File.separator
				+ "standard");
		for (String library : libraries) {
			String libraryName = Character.toUpperCase(library.charAt(0))
					+ library.substring(1);
			commands.add("-I" + arduinoSdk + "libraries" + File.separator
					+ libraryName);
		}
		commands.add("-g");
		commands.add("-Os");
		commands.add("-Wall");
		commands.add("-ffunction-sections");
		commands.add("-fdata-sections");
		commands.add("-fno-exceptions");
		commands.add(fileName + ".ino");
		commands.add("-o");
		commands.add(fileName + ".o");

		ProcessBuilder builder = new ProcessBuilder(commands);

		return executeCommand(directory, builder);
	}

	/**
	 * Compile C file.
	 * 
	 * @param filePath
	 *            Path of the C file to compile
	 */
	private IStatus compileCFile(String filePath, String fileName) {
		System.out.println("Compile C file : " + filePath + fileName);
		String command = arduinoSdk + "hardware" + File.separator + "tools"
				+ File.separator + "avr" + File.separator + "bin"
				+ File.separator + "avr-gcc";
		if (os.contains(WINDOWS)) {
			command += ".exe";
		}
		ProcessBuilder builder = new ProcessBuilder(command, "-c", "-mmcu="
				+ getMMCU(), "-DF_CPU=" + getDFCPU(), "-DARDUINO="
				+ getDArduino(), "-I.", "-I" + arduinoSdk + "hardware"
				+ File.separator + "arduino" + File.separator + "cores"
				+ File.separator + "arduino", "-I" + arduinoSdk + "hardware"
				+ File.separator + "arduino" + File.separator + "variants"
				+ File.separator + "standard", "-g", "-Os", "-Wall",
				"-ffunction-sections", "-fdata-sections", "-std=gnu99",
				filePath + fileName + ".c", "-o", fileName + ".o");
		return executeCommand(directory, builder);
	}

	/**
	 * Compile CPP file.
	 * 
	 * @param filePath
	 *            Path of the CPP file to compile
	 */
	private IStatus compileCPPFile(String filePath, String fileName) {
		System.out.println("Compile CPP file : " + filePath + fileName);
		String command = arduinoSdk + "hardware" + File.separator + "tools"
				+ File.separator + "avr" + File.separator + "bin"
				+ File.separator + "avr-g++";
		if (os.contains(WINDOWS)) {
			command += ".exe";
		}
		List<String> commands = new ArrayList<>();
		commands.add(command);
		commands.add("-c");
		commands.add("-mmcu=" + getMMCU());
		commands.add("-DF_CPU=" + getDFCPU());
		commands.add("-DARDUINO=" + getDArduino());
		commands.add("-I.");
		commands.add("-I" + arduinoSdk + "hardware" + File.separator
				+ "arduino" + File.separator + "cores" + File.separator
				+ "arduino");
		commands.add("-I" + arduinoSdk + "hardware" + File.separator
				+ "arduino" + File.separator + "variants" + File.separator
				+ "standard");
		commands.add("-g");
		commands.add("-Os");
		commands.add("-Wall");
		commands.add("-ffunction-sections");
		commands.add("-fdata-sections");
		commands.add("-fno-exceptions");
		commands.add(filePath + fileName + ".cpp");
		commands.add("-o");
		commands.add(fileName + ".o");
		ProcessBuilder builder = new ProcessBuilder(commands);
		return executeCommand(directory, builder);
	}

	/**
	 * Execute a command.
	 * 
	 * @param directory
	 *            Current directory
	 * @param builder
	 *            Command to execute
	 */
	private IStatus executeCommand(File directory, ProcessBuilder builder) {
		System.out.println(builder.command());
		builder.directory(directory);
		try {
			Process process = builder.start();
			inheritIO(process.getInputStream(), System.out);
			// inheritIO(process.getErrorStream(), System.err);
			process.waitFor();
			if (process.exitValue() > 0) {
				String error = convertStreamToString(process.getErrorStream());
				return new Status(IStatus.ERROR,
						ArduinoBuilderActivator.PLUGIN_ID, error);
			}
		} catch (IOException e) {
			return new Status(IStatus.ERROR, ArduinoBuilderActivator.PLUGIN_ID,
					e.getMessage(), e);
		} catch (InterruptedException e) {
			return new Status(IStatus.ERROR, ArduinoBuilderActivator.PLUGIN_ID,
					e.getMessage(), e);
		}
		return new Status(IStatus.OK, ArduinoBuilderActivator.PLUGIN_ID, null);
	}

	/**
	 * Get information from {$ARDUINO_SDK}/lib/version.txt in the Arduino SDK.
	 * 
	 * @return
	 */
	private String getDArduino() {
		// TODO Auto-generated method stub
		return "106";
	}

	/**
	 * Get information from {$ARDUINO_SDK}/hardware/arduino/boards.txt file in
	 * the Arduino SDK : {$BOARD_TAG}.build.mcu=xxxx.
	 * 
	 * @return
	 */
	private String getMMCU() {
		// TODO Auto-generated method stub
		return "atmega328p";
	}

	/**
	 * Get information from b{$ARDUINO_SDK}/hardware/arduino/boards.txtt file in
	 * the Arduino SDK : {$BOARD_TAG}.build.f_cpu=xxxx.
	 * 
	 * @return
	 */
	private String getDFCPU() {
		// TODO Auto-generated method stub
		return "16000000L";
	}

	/**
	 * Inherit IO in console.
	 * 
	 * @param src
	 *            Source stream
	 * @param dest
	 *            Destination stream
	 */
	private static void inheritIO(final InputStream src, final PrintStream dest) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Scanner sc = new Scanner(src);
				while (sc.hasNextLine()) {
					dest.println(sc.nextLine());
				}
				sc.close();
			}
		}).start();
	}

	private static String convertStreamToString(java.io.InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}
}