package fr.obeo.dsl.arduino.simulator.design.services;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.ecore.EObject;
import org.osgi.framework.Bundle;

import fr.obeo.dsl.arduino.ArduinoUtils;
import fr.obeo.dsl.arduino.Connector;
import fr.obeo.dsl.arduino.Module;
import fr.obeo.dsl.arduino.Pin;
import fr.obeo.dsl.arduino.Platform;
import fr.obeo.dsl.arduino.design.services.ArduinoServices;
import fr.obeo.dsl.arduino.simulator.Simulator;
import fr.obeo.dsl.arduino.simulator.design.launcher.LauncherDelegate;
import fr.obeo.dsl.debug.ide.sirius.ui.services.AbstractDSLDebuggerServices;

public class SimulatorServices extends AbstractDSLDebuggerServices {

	@Override
	protected List<StringCouple> getRepresentationRefreshList() {
		final List<StringCouple> res = new ArrayList<StringCouple>();

		res.add(new StringCouple("Hardware", "Simulator"));
		res.add(new StringCouple("Sketch", "Simulator"));
		res.add(new StringCouple("Function", "Simulator"));

		return res;
	}

	@Override
	public String getModelIdentifier() {
		return LauncherDelegate.MODEL_IDENTIFIER;
	}

	/**
	 * Gets the current {@link Simulator}.
	 * 
	 * @return the current {@link Simulator}
	 */
	private Simulator getSimulator() {
		return Simulator.getCurrent();
	}

	/**
	 * Tells if a simulation is running.
	 * 
	 * @param eObj
	 *            any {@link EObject}
	 * @return <code>true</code> if a simulation is running, <code>false</code>
	 *         otherwise
	 */
	public boolean isSimulating(EObject eObj) {
		return isSimulating();
	}

	/**
	 * Tells if a simulation is running.
	 * 
	 * @return <code>true</code> if a simulation is running, <code>false</code>
	 *         otherwise
	 */
	private boolean isSimulating() {
		return getSimulator() != null;
	}

	/**
	 * Gets the level of the given {@link Pin}.
	 * 
	 * @param pin
	 *            the {@link Pin}
	 * @return the level of the given {@link Pin}
	 */
	public int getLevel(Pin pin) {
		final int res;

		if (isSimulating()) {
			res = getSimulator().getPinLevel(pin).intValue();
		} else {
			res = 0;
		}

		return res;
	}

	/**
	 * Gets the level of the given {@link Connector}.
	 * 
	 * @param connector
	 *            the {@link Connector}
	 * @return the level of the given {@link Connector}
	 */
	public int getLevel(Connector connector) {
		return getLevel(connector.getPin());
	}

	/**
	 * Gets the level of the given {@link Module}.
	 * 
	 * @param module
	 *            the {@link Module}
	 * @return the level of the given {@link Module}
	 */
	public int getLevel(Module module) {
		final int res;

		if (isSimulating()) {
			res = getLevel(ArduinoUtils.getPin(getSimulator().getProject(),
					module));
		} else {
			res = 0;
		}

		return res;
	}

	/**
	 * Gets the level of the given {@link Platform}.
	 * 
	 * @param platform
	 *            the {@link Platform}
	 * @return 0
	 */
	public int getLevel(Platform platform) {
		return 0;
	}

	public String getSimulatingImage(Module module) {
		int level = getLevel(module);
		String imageName = getImageLevel(module, level);
		Bundle bundle = org.eclipse.core.runtime.Platform
				.getBundle("fr.obeo.dsl.arduino.simulator.design");
		URL fileURL = bundle.getEntry("images/" + imageName);
		File file;
		try {
			file = new File(FileLocator.resolve(fileURL).toURI());
			if (!file.exists()) {
				ArduinoServices service = new ArduinoServices();
				return service.getImage(module);
			}
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "fr.obeo.dsl.arduino.simulator.design/images/" + imageName;
	}

	private String getImageLevel(Module module, int level) {
		String imageName = module.getImage().substring(0,
				module.getImage().indexOf(".jpg"))
				+ "_" + level + ".jpg";
		return imageName;
	}
}
