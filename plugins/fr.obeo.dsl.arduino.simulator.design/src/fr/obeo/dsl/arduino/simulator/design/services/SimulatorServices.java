package fr.obeo.dsl.arduino.simulator.design.services;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import fr.obeo.dsl.arduino.ArduinoUtils;
import fr.obeo.dsl.arduino.Connector;
import fr.obeo.dsl.arduino.Module;
import fr.obeo.dsl.arduino.Pin;
import fr.obeo.dsl.arduino.Platform;
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

}
