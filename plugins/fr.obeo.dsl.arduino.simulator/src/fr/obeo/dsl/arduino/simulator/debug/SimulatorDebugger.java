package fr.obeo.dsl.arduino.simulator.debug;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EObject;

import fr.obeo.dsl.arduino.Pin;
import fr.obeo.dsl.arduino.Value;
import fr.obeo.dsl.arduino.Variable;
import fr.obeo.dsl.arduino.simulator.Simulator;
import fr.obeo.dsl.debug.ide.AbstractDSLDebugger;
import fr.obeo.dsl.debug.ide.event.IDSLDebugEventProcessor;

/**
 * Debugger for {@link Simulator}.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 *
 */
public class SimulatorDebugger extends AbstractDSLDebugger {

	/**
	 * The {@link Simulator} we are attached to.
	 */
	private final Simulator simulator;

	/**
	 * Is the hardware stack frame pushed yet.
	 */
	private boolean hardwareFramePushed;

	/**
	 * {@link Pin} delta values.
	 */
	private Map<Pin, Integer> lastSuspendPins = new HashMap<Pin, Integer>();

	/**
	 * {@link Pin} delta values.
	 */
	private Map<Pin, Integer> nextSuspendPins = new HashMap<Pin, Integer>();

	/**
	 * {@link Value} delta values.
	 */
	private Map<Variable, Object> lastSuspendVariables = new HashMap<Variable, Object>();

	/**
	 * {@link Value} delta values.
	 */
	private Map<Variable, Object> nextSuspendVariables = new HashMap<Variable, Object>();

	public SimulatorDebugger(IDSLDebugEventProcessor target, Simulator simulator) {
		super(target);
		this.simulator = simulator;
	}

	public void start() {
		new Thread(new Runnable() {

			public void run() {
				spawnRunningThread(Thread.currentThread().getName(),
						simulator.getProject());
				simulator.init();
				simulator.loop();
			}
		}, "Arduino Simulator").start();
	}

	public void disconnect() {
		// TODO Auto-generated method stub
	}

	public boolean canStepInto(String threadName, EObject instruction) {
		return false;
	}

	public void updateData(String threadName, EObject instruction) {
		if (!hardwareFramePushed) {
			pushStackFrame(Thread.currentThread().getName(), simulator
					.getProject().getHardware().getName(), simulator
					.getProject().getHardware(), instruction);
			hardwareFramePushed = true;
		} else {
			setCurrentInstruction(Thread.currentThread().getName(), instruction);
		}
		for (Entry<Variable, Object> entry : nextSuspendVariables.entrySet()) {
			variable(threadName, "variable", entry.getKey().getName(),
					entry.getValue());
		}
		if (!nextSuspendVariables.isEmpty()) {
			lastSuspendVariables = nextSuspendVariables;
			nextSuspendVariables = new HashMap<Variable, Object>();
		}
		for (Entry<Pin, Integer> entry : nextSuspendPins.entrySet()) {
			variable(threadName, "pin", String.valueOf(entry.getKey().getId()),
					entry.getValue());
		}
		if (!nextSuspendPins.isEmpty()) {
			lastSuspendPins = nextSuspendPins;
			nextSuspendPins = new HashMap<Pin, Integer>();
		}
	}

	public void variableChanged(Variable variable, Object value) {
		final Object lastValue = lastSuspendVariables.get(variable);
		if ((lastValue == null && value != null) || (lastValue != null && !lastValue.equals(value))) {
			nextSuspendVariables.put(variable, value);
		} else {
			nextSuspendVariables.remove(variable);
		}
	}

	public void pinChanged(Pin pin, Integer level) {
		final Integer lastLevel = lastSuspendPins.get(pin);
		if ((lastLevel == null && level != null) || !lastLevel.equals(level)) {
			nextSuspendPins.put(pin, level);
		} else {
			nextSuspendPins.remove(pin);
		}
	}

}
