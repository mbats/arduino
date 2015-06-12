package fr.obeo.dsl.arduino.simulator.debug;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EObject;

import fr.obeo.dsl.arduino.Connector;
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
			variable(threadName,
					simulator.getProject().getHardware().getName(), "variable",
					entry.getKey().getName(), entry.getValue(), true);
		}
		if (!nextSuspendVariables.isEmpty()) {
			lastSuspendVariables = nextSuspendVariables;
			nextSuspendVariables = new HashMap<Variable, Object>();
		}
		for (Entry<Pin, Integer> entry : nextSuspendPins.entrySet()) {
			variable(threadName,
					simulator.getProject().getHardware().getName(), "pin",
					String.valueOf(entry.getKey().getId()), entry.getValue(),
					true);
		}
		if (!nextSuspendPins.isEmpty()) {
			lastSuspendPins = nextSuspendPins;
			nextSuspendPins = new HashMap<Pin, Integer>();
		}
	}

	public void variableChanged(Variable variable, Object value) {
		final Object lastValue = lastSuspendVariables.get(variable);
		if ((lastValue == null && value != null)
				|| (lastValue != null && !lastValue.equals(value))) {
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

	public boolean validateVariableValue(String threadName,
			String variableName, String value) {
		final boolean res;

		final Pin pin = lookForPin(variableName);
		if (pin != null) {
			Integer level = null;
			try {
				level = Integer.valueOf(value);
			} catch (Exception e) {
				// nothing to do here.
			}
			res = level != null;
		} else {
			final Variable variable = lookForVariable(variableName);
			res = getValue(variable, value) != null;
		}

		return res;
	}

	public Object getVariableValue(String threadName, String stackName,
			String variableName, String value) {
		final Object res;

		final Pin pin = lookForPin(variableName);
		if (pin != null) {
			Integer level = Integer.valueOf(value);
			res = level;
		} else {
			final Variable variable = lookForVariable(variableName);
			final Object valueObject = getValue(variable, value);
			res = valueObject;
		}

		return res;
	}

	public void setVariableValue(String threadName, String stackName,
			String variableName, Object value) {
		final Pin pin = lookForPin(variableName);
		if (pin != null) {
			simulator.setPinLevel(pin, (Integer) value);
		} else {
			final Variable variable = lookForVariable(variableName);
			simulator.setVariableValue(variable, value);
		}

	}

	private Pin lookForPin(String variableName) {
		Pin pin = null;

		for (Connector connector : simulator.getProject().getSketch()
				.getHardware().getConnectors()) {
			if (String.valueOf(connector.getPin().getId()).equals(variableName)) {
				pin = connector.getPin();
				break;
			}
		}

		return pin;
	}

	private Variable lookForVariable(String variableName) {
		Variable variable = null;

		final Iterator<EObject> it = simulator.getProject().getSketch()
				.eAllContents();
		while (it.hasNext()) {
			final EObject eObj = it.next();
			if (eObj instanceof Variable
					&& ((Variable) eObj).getName().equals(variableName)) {
				variable = (Variable) eObj;
				break;
			}
		}

		return variable;
	}

	private Object getValue(Variable variable, String value) {
		final Object res;

		final Object currentValue = simulator.getVariableValue(variable);
		if (currentValue instanceof String) {
			res = value;
		} else if (currentValue instanceof Integer) {
			Integer integerValue = null;
			try {
				integerValue = Integer.decode(value);
			} catch (Exception e) {
				// nothing to do here
			}
			res = integerValue;
		} else if (currentValue instanceof Double) {
			Double doubleValue = null;
			try {
				doubleValue = Double.parseDouble(value);
			} catch (Exception e) {
				// nothing to do here
			}
			res = doubleValue;
		} else if (currentValue instanceof Boolean) {
			res = Boolean.valueOf(value);
		} else {
			res = null;
		}

		return res;
	}

}
