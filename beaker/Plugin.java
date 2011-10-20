package beaker;

import beaker.Project;

public interface Plugin {
	public boolean hasMoreTasksInStage(String stageName);
	public boolean performTasksInStage(String stageName);
	public String[] getDefinedStages();
	public void setProject(Project p);
}