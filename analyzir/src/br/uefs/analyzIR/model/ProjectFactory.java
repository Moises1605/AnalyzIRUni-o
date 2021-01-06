package br.uefs.analyzIR.model;

import br.uefs.analyzIR.exception.InvalidProjectTypeException;

public class ProjectFactory {

	public static final int STANDARD_SYSTEM = 1;
	public static final int CLUSTER_SYSTEM = 2;
	public static final int INTERACTIVE_SYSTEM = 3;

	public Project getProject(int projectType, String name, String path)
			throws InvalidProjectTypeException {

		if (projectType == STANDARD_SYSTEM) {
			return new StandardProject(name, path);
		} else if (projectType == CLUSTER_SYSTEM){
			return new DiversityProject(name, path);
		} else if (projectType == INTERACTIVE_SYSTEM){
			return new InteractiveProject(name, path);
		} else {
			throw new InvalidProjectTypeException(
					"The selected project code is not valid. Please choose a correct code.");
		}
	}

}
