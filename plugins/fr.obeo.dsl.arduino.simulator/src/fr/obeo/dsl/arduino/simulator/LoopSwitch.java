package fr.obeo.dsl.arduino.simulator;

import org.eclipse.emf.ecore.EObject;

import fr.obeo.dsl.arduino.ArduinoUtils;
import fr.obeo.dsl.arduino.BooleanOperator;
import fr.obeo.dsl.arduino.Constant;
import fr.obeo.dsl.arduino.Delay;
import fr.obeo.dsl.arduino.FunctionCall;
import fr.obeo.dsl.arduino.If;
import fr.obeo.dsl.arduino.Instruction;
import fr.obeo.dsl.arduino.Level;
import fr.obeo.dsl.arduino.Library;
import fr.obeo.dsl.arduino.MathOperator;
import fr.obeo.dsl.arduino.NumericalOperator;
import fr.obeo.dsl.arduino.Parameter;
import fr.obeo.dsl.arduino.ParameterCall;
import fr.obeo.dsl.arduino.Pin;
import fr.obeo.dsl.arduino.Repeat;
import fr.obeo.dsl.arduino.Sensor;
import fr.obeo.dsl.arduino.Set;
import fr.obeo.dsl.arduino.Sketch;
import fr.obeo.dsl.arduino.Status;
import fr.obeo.dsl.arduino.Value;
import fr.obeo.dsl.arduino.Variable;
import fr.obeo.dsl.arduino.While;
import fr.obeo.dsl.arduino.util.ArduinoSwitch;

/**
 * Simulates main loop.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 *
 */
public class LoopSwitch extends ArduinoSwitch<Object> {

	/**
	 * The {@link Simulator} to run.
	 */
	private final Simulator simulator;

	/**
	 * Constructor.
	 * 
	 * @param simulator
	 *            the {@link Simulator}
	 */
	public LoopSwitch(Simulator simulator) {
		this.simulator = simulator;
	}

	public void loop() throws SimulationStoppedException {
		final Instruction first = simulator.getProject().getSketch().getNext();
		while (true) {
			Instruction current = first;
			while (current != null) {
				if (!(current instanceof Sketch)) {
					doSwitch(current);
				}
				current = current.getNext();
			}
		}
	}

	@Override
	public Object doSwitch(EObject eObject) {
		final Object res;

		if (simulator.getDebugger() != null) {
			if (simulator.getDebugger().control(
					Thread.currentThread().getName(), eObject)) {
				res = super.doSwitch(eObject);
			} else {
				throw new SimulationStoppedException();
			}
		} else {
			res = super.doSwitch(eObject);
		}

		return res;
	}

	@Override
	public Object caseDelay(Delay delay) {
		try {
			Thread.sleep(delay.getValue());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Object caseStatus(Status status) {
		if (status.getModule().getLibrary() == Library.MUSIC) {
			if (!status.isStatus()) {
				// TODO music.play();
			} else {
				// TODO music.pause();
			}
		} else {
			final Pin pin = ArduinoUtils.getPin(simulator.getProject(),
					status.getModule());
			simulator.setPinLevel(pin, getStatusValue(status));
		}
		return null;
	}

	/**
	 * Gets the value of the given {@link Status}.
	 * 
	 * @param status
	 *            the {@link Status}
	 * @return the value of the given {@link Status}
	 */
	private Integer getStatusValue(Status status) {
		final Integer res;
		if (status.getSensor() == null) {
			if (!status.isStatus()) {
				res = Simulator.HIGH;
			} else {
				res = Simulator.LOW;
			}
		} else {
			final Integer value = (Integer) doSwitch(status.getSensor());
			if (!status.isStatus()) {
				res = value.intValue() == Simulator.LOW.intValue() ? Simulator.HIGH
						: Simulator.LOW;
			} else {
				res = value.intValue();
			}
		}

		return res;
	}

	@Override
	public Object caseSensor(Sensor sensor) {
		final Pin pin = ArduinoUtils.getPin(simulator.getProject(),
				sensor.getModule());
		return interpretObjectValueToBoolean(simulator.getPinLevel(pin));
	}

	@Override
	public Object caseRepeat(Repeat repeat) {
		final Instruction first = repeat.getInstructions().get(0);
		for (int i = 0; i < repeat.getIteration(); ++i) {
			Instruction current = first;
			while (current != null) {
				doSwitch(current);
				current = current.getNext();
			}
		}
		return null;
	}

	@Override
	public Object caseWhile(While _while) {
		final Instruction first = _while.getInstructions().get(0);
		while (Boolean.TRUE.equals(doSwitch(_while.getCondition()))) {
			Instruction current = first;
			while (current != null) {
				doSwitch(current);
				current = current.getNext();
			}
		}
		return null;
	}

	@Override
	public Object caseIf(If _if) {
		final Instruction first = _if.getInstructions().get(0);
		if (Boolean.TRUE.equals(doSwitch(_if.getCondition()))) {
			Instruction current = first;
			while (current != null) {
				doSwitch(current);
				current = current.getNext();
			}
		}
		return null;
	}

	@Override
	public Object caseMathOperator(MathOperator operator) {
		final Object res;

		switch (operator.getOperator()) {
		case AND:
			if (getBooleanValue(operator.getLeft())) {
				res = Boolean.valueOf(Boolean.TRUE
						.equals(getBooleanValue(operator.getRight())));
			} else {
				res = Boolean.FALSE;
			}
			break;
		case DIFF:
			res = Boolean.valueOf(!getValue(operator.getLeft()).equals(
					getValue(operator.getRight())));
			break;
		case DIV:
			res = Double.valueOf((getNumberValue(operator.getLeft()))
					.doubleValue()
					/ getNumberValue(operator.getRight()).doubleValue());
			break;
		case EQUAL:
			res = Boolean.valueOf(getValue(operator.getLeft()).equals(
					getValue(operator.getRight())));
			break;
		case LOWER:
			res = Boolean.valueOf(getNumberValue(operator.getLeft())
					.doubleValue() < getNumberValue(operator.getRight())
					.doubleValue());
			break;
		case LOWER_OR_EQUAL:
			res = Boolean.valueOf(getNumberValue(operator.getLeft())
					.doubleValue() <= getNumberValue(operator.getRight())
					.doubleValue());
			break;
		case MAX:
			res = Double.valueOf(Math.max(getNumberValue(operator.getLeft())
					.doubleValue(), (getNumberValue(operator.getRight()))
					.doubleValue()));
			break;
		case MIN:
			res = Double.valueOf(Math.min(getNumberValue(operator.getLeft())
					.doubleValue(), (getNumberValue(operator.getRight()))
					.doubleValue()));
			break;
		case MINUS:
			res = Double.valueOf(getNumberValue(operator.getLeft())
					.doubleValue()
					- getNumberValue(operator.getRight()).doubleValue());
			break;
		case MUL:
			res = Double.valueOf(getNumberValue(operator.getLeft())
					.doubleValue()
					* (getNumberValue(operator.getRight())).doubleValue());
			break;
		case NOT:
			res = Boolean.valueOf(!getBooleanValue(operator.getRight()));
			break;
		case OR:
			if (getBooleanValue(operator.getLeft())) {
				res = Boolean.TRUE;
			} else {
				res = getBooleanValue(operator.getRight());
			}
			break;
		case PLUS:
			res = Double.valueOf(getNumberValue(operator.getLeft())
					.doubleValue()
					+ getNumberValue(operator.getRight()).doubleValue());
			break;
		case POURCENT:
			res = Double.valueOf(getNumberValue(operator.getLeft())
					.doubleValue()
					% getNumberValue(operator.getRight()).doubleValue());
			break;
		case UPPER:
			res = Boolean.valueOf(getNumberValue(operator.getLeft())
					.doubleValue() > getNumberValue(operator.getRight())
					.doubleValue());
			break;
		case UPPER_OR_EQUAL:
			res = Boolean.valueOf(getNumberValue(operator.getLeft())
					.doubleValue() >= getNumberValue(operator.getRight())
					.doubleValue());
			break;
		default:
			throw new IllegalStateException("Operator "
					+ operator.getOperator() + " not simulated yet.");
		}

		return res;
	}
	
	/**
	 * Gets the value of the given {@link Value}.
	 * 
	 * @param value
	 *            the {@link Value}
	 * @return the value of the given {@link Value} if any, <code>null</code>
	 *         otherwise
	 */
	private Object getValue(final Value value) {
		final Object res;

		if (value != null) {
			res = doSwitch(value);
		} else {
			res = null;
		}

		return res;
	}

	@Override
	public Object caseSet(Set set) {
		simulator.setVariableValue(set.getVariable(), getValue(set.getValue()));
		return null;
	}

	@Override
	public Object caseLevel(Level level) {
		final Pin pin = ArduinoUtils.getPin(simulator.getProject(),
				level.getModule());
		simulator.setPinLevel(pin,
				Integer.valueOf(getNumberValue(level.getLevel()).intValue()));
		return null;
	}

	private Number getNumberValue(Value level) {
		return interpretObjectValueToNumber(getValue(level));
	}

	private Number interpretObjectValueToNumber(Object value) {
		final Number res;

		if (value instanceof Number) {
			res = (Number) value;
		} else if (value instanceof Boolean) {
			if (Boolean.TRUE.equals(value)) {
				res = Simulator.HIGH;
			} else {
				res = Simulator.LOW;
			}
		} else if (value instanceof String) {
			Object interpreted = interpretStringToObject((String) value);
			if (interpreted instanceof String) {
				res = Simulator.LOW;
			} else {
				res = interpretObjectValueToNumber(interpreted);
			}
		} else {
			res = Simulator.LOW;
		}

		return res;
	}

	private Boolean getBooleanValue(Value value) {
		return interpretObjectValueToBoolean(getValue(value));
	}

	/**
	 * Tries to interpret the given {@link Object} as a {@link Boolean}.
	 * 
	 * @param value
	 *            the {@link Object}
	 * @return a {@link Boolean}
	 */
	private Boolean interpretObjectValueToBoolean(final Object value) {
		final Boolean res;

		if (value instanceof Boolean) {
			res = (Boolean) value;
		} else if (value instanceof Number) {
			res = ((Number) value).intValue() != Simulator.LOW;
		} else if (value instanceof String) {
			Object interpreted = interpretStringToObject((String) value);
			if (interpreted instanceof String) {
				res = Boolean.FALSE;
			} else {
				res = interpretObjectValueToBoolean(interpreted);
			}
		} else {
			res = Boolean.FALSE;
		}

		return res;
	}

	/**
	 * Tries to interpret the given {@link String}.
	 * 
	 * @param value
	 *            the {@link String}
	 * @return an interpretation of the given {@link String} if any, the
	 *         {@link String} itself otherwise
	 */
	private Object interpretStringToObject(String value) {
		Object res;

		try {
			res = Integer.valueOf(value);
		} catch (NumberFormatException e) {
			try {
				res = Double.valueOf(value);
			} catch (NumberFormatException e1) {
				if ("true".equalsIgnoreCase(value)) {
					res = Boolean.TRUE;
				} else if ("false".equalsIgnoreCase(value)) {
					res = Boolean.FALSE;
				} else {
					res = value;
				}
			}
		}

		return res;
	}

	@Override
	public Object caseConstant(Constant constant) {
		Object res;

		try {
			res = Integer.valueOf(constant.getValue());
		} catch (Exception e) {
			try {
				res = Double.valueOf(constant.getValue());
			} catch (Exception e1) {
				throw new IllegalArgumentException(
						"Constant value not supported : " + constant.getValue());
			}
		}

		return res;
	}

	@Override
	public Object caseVariable(Variable variable) {
		return simulator.getVariableValue(variable);
	}

	@Override
	public Object caseFunctionCall(FunctionCall call) {
		for (Instruction functInstr : call.getDefinition().getInstructions()) {
			if (functInstr instanceof ParameterCall) {
				for (Parameter param : call.getParameters()) {
					if (param.getDefinition() == ((ParameterCall) functInstr)
							.getDefinition()) {
						doSwitch(param);
					}
				}
			} else {
				doSwitch(functInstr);
			}
		}
		return null;
	}

	@Override
	public Object caseValue(Value value) {
		return value.getValue();
	}

}
