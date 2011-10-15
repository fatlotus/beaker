package beaker;

public interface Plugin {
	public boolean hasMoreTasksInStage(String stageName);
	public boolean performTasksInStage(String stageName);
	public String[] getDefinedStages();
}