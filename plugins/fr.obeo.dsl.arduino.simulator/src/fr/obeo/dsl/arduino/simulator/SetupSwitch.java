package fr.obeo.dsl.arduino.simulator;

import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;

import fr.obeo.dsl.arduino.Pin;
import fr.obeo.dsl.arduino.Variable;
import fr.obeo.dsl.arduino.util.ArduinoSwitch;

/**
 * Simulates initialization.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 *
 */
public class SetupSwitch extends ArduinoSwitch<Void> {

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
	public SetupSwitch(Simulator simulator) {
		this.simulator = simulator;
	}

	@Override
	public Void doSwitch(EObject eObject) {
		final Void res;

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

	public void setup() throws SimulationStoppedException {
		Iterator<EObject> it = simulator.getProject().eAllContents();
		while (it.hasNext()) {
			EObject eObj = it.next();
			if (eObj instanceof Variable || eObj instanceof Pin) {
				doSwitch(eObj);
			}
		}
	}

	@Override
	public Void caseVariable(Variable variable) {
		simulator.setVariableValue(variable, Integer.valueOf(0));
		return null;
	}

	@Override
	public Void casePin(Pin pin) {
		simulator.setPinLevel(pin, Integer.valueOf(0));
		return null;
	}
	
}
