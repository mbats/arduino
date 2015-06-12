package fr.obeo.dsl.arduino.simulator.design;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sirius.business.api.dialect.command.RefreshRepresentationsCommand;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DView;

import fr.obeo.dsl.arduino.Pin;
import fr.obeo.dsl.arduino.Project;
import fr.obeo.dsl.arduino.Variable;
import fr.obeo.dsl.arduino.simulator.Simulator;

/**
 * A Sirius aware {@link Simulator}.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 *
 */
public class SiriusSimulator extends Simulator {

	private final Session session;

	public SiriusSimulator(Project project, Session session) {
		super(project);
		this.session = session;
	}

	@Override
	public void init() {
		super.init();
		refresh();
	}

	@Override
	public void loop() {
		super.loop();
		refresh();
	}

	@Override
	public void setPinLevel(Pin pin, Integer level) {
		boolean shouldRefresh = !level.equals(getPinLevel(pin));
		super.setPinLevel(pin, level);

		if (shouldRefresh) {
			refresh();
		}
	}

	@Override
	public void setVariableValue(Variable variable, Object value) {
		boolean shouldRefresh = !value.equals(getVariableValue(variable));
		super.setVariableValue(variable, value);

		if (shouldRefresh) {
			refresh();
		}
	}

	/**
	 * Refreshes Sirius.
	 * 
	 */
	public void refresh() {
		final TransactionalEditingDomain transactionalEditingDomain = session
				.getTransactionalEditingDomain();
		final List<DRepresentation> representations = new ArrayList<DRepresentation>();
		for (DView view : session.getSelectedViews()) {
			for (DRepresentation representation : view
					.getOwnedRepresentations()) {
				representations.add(representation);
			}
		}
		if (representations.size() != 0) {
			final RefreshRepresentationsCommand refresh = new RefreshRepresentationsCommand(
					transactionalEditingDomain, new NullProgressMonitor(),
					representations);
			transactionalEditingDomain.getCommandStack().execute(refresh);
		}
	}

}
