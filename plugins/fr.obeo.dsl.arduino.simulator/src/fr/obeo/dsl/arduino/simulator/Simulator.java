package fr.obeo.dsl.arduino.simulator;

import java.util.HashMap;
import java.util.Map;

import fr.obeo.dsl.arduino.ArduinoUtils;
import fr.obeo.dsl.arduino.InputModule;
import fr.obeo.dsl.arduino.Module;
import fr.obeo.dsl.arduino.Pin;
import fr.obeo.dsl.arduino.Project;
import fr.obeo.dsl.arduino.Value;
import fr.obeo.dsl.arduino.Variable;
import fr.obeo.dsl.arduino.simulator.debug.SimulatorDebugger;

/**
 * Simulate the execution of a given {@link Project}.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 *
 */
public class Simulator {

	/**
	 * The current {@link Simulator} if any simulation is running,
	 * <code>null</code> otherwise.
	 */
	private static Simulator current;

	/**
	 * Low {@link Pin} level.
	 */
	public static final Integer LOW = Integer.valueOf(0);

	/**
	 * High {@link Pin} level.
	 */
	public static final Integer HIGH = Integer.valueOf(1023);

	/**
	 * The {@link Project} to simulate.
	 */
	private final Project project;

	/**
	 * {@link Pin} levels.
	 */
	private final Map<Pin, Integer> pins = new HashMap<Pin, Integer>();

	/**
	 * {@link Value} values.
	 */
	private final Map<Variable, Object> variables = new HashMap<Variable, Object>();

	private final SetupSwitch initSwitch = new SetupSwitch(this);

	private final LoopSwitch loopSwitch = new LoopSwitch(this);

	private SimulatorDebugger debugger;

	/**
	 * Constructor.
	 * 
	 * @param project
	 *            the {@link Project} to simulate
	 */
	public Simulator(Project project) {
		this.project = project;
	}

	/**
	 * The initialization.
	 */
	public void init() {
		current = this;
		try {
			initSwitch.setup();
		} catch (SimulationStoppedException e) {
			current = null;
		}
	}

	/**
	 * The main loop.
	 */
	public void loop() {
		try {
			loopSwitch.loop();
		} catch (SimulationStoppedException e) {
			current = null;
		}
	}

	/**
	 * Gets {@link Pin} level.
	 * 
	 * @param pin
	 *            the {@link Pin}
	 * @return {@link Pin} level
	 */
	public Integer getPinLevel(Pin pin) {
		final Integer res;

		if (pins.get(pin) != null) {
			res = pins.get(pin);
		} else {
			final Module module = ArduinoUtils.getModule(getProject(), pin);
			if (module instanceof InputModule) {
				throw new IllegalStateException("Reading input module "
						+ module.getName()
						+ "but as not set its value in the simulator.");
			} else {
				res = LOW;
				pins.put(pin, res);
			}
		}

		return res;
	}

	/**
	 * Sets the given level to the given {@link Pin}.
	 * 
	 * @param pin
	 *            the {@link Pin}
	 * @param level
	 *            the level
	 */
	public void setPinLevel(Pin pin, Integer level) {
		pins.put(pin, level);
		if (debugger != null) {
			debugger.pinChanged(pin, level);
		}
	}

	/**
	 * Gets the given {@link Variable} value.
	 * 
	 * @param variable
	 *            the {@link Variable}
	 * @return the given {@link Variable} value if any, <code>null</code>
	 *         otherwise
	 */
	public Object getVariableValue(Variable variable) {
		return variables.get(variable);
	}

	/**
	 * Sets the given value to the given {@link Variable}.
	 * 
	 * @param variable
	 *            the {@link Variable}
	 * @param value
	 *            the value
	 */
	public void setVariableValue(Variable variable, Object value) {
		variables.put(variable, value);
		if (debugger != null) {
			debugger.variableChanged(variable, value);
		}
	}

	/**
	 * Gets the {@link Project} to simulate.
	 * 
	 * @return the {@link Project} to simulate
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * Sets the {@link SimulatorDebugger}.
	 * 
	 * @param debugger
	 *            the {@link SimulatorDebugger}
	 */
	public void setDebugger(SimulatorDebugger debugger) {
		this.debugger = debugger;
	}

	/**
	 * Gets the {@link SimulatorDebugger}.
	 * 
	 * @return the {@link SimulatorDebugger}
	 */
	public SimulatorDebugger getDebugger() {
		return debugger;
	}

	/**
	 * Gets the current {@link Simulator}.
	 * 
	 * @return the current {@link Simulator} if any simulation is running,
	 *         <code>null</code> otherwise
	 */
	public static Simulator getCurrent() {
		return current;
	}

}
