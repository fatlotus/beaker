package beaker;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PluginCollection implements Plugin {
	private Plugin[] plugins;
	
	public PluginCollection(Plugin... pluginArray) {
		plugins = Arrays.copyOf(pluginArray, pluginArray.length);
	}
	
	private static Plugin[] getPluginArrayFromClassNames(String[] classNames)
	  throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		Plugin[] plugins = new Plugin[classNames.length];
		
		for (int i = 0; i < classNames.length; i++) {
			plugins[i] = (Plugin)Class.forName(classNames[i]).newInstance();
		}
		
		return plugins;
	}
	
	public PluginCollection(String... classNames)
	  throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		this(getPluginArrayFromClassNames(classNames));
	}
	
	@Override
	public boolean hasMoreTasksInStage(String stageName) {
		for (Plugin p : plugins) {
			if (p.hasMoreTasksInStage(stageName))
				return true;
		}
		
		return false;
	}
	
	@Override
	public boolean performTasksInStage(String stageName) {
		boolean any = false, all = true;
		
		for (Plugin p : plugins) {
			if (p.hasMoreTasksInStage(stageName)) {
				any = true;
				
				if (!p.performTasksInStage(stageName)) {
					all = false;
					break;
				}
			}
		}
		
		return any && all;
	}
	
	@Override
	public String[] getDefinedStages() {
		Set<String> stages = new HashSet<String>();
		
		for (Plugin p : plugins) {
			for (String stage : p.getDefinedStages()) {
				stages.add(stage);
			}
		}
		
		return stages.toArray(new String[0]);
	}
	
	@Override
	public void setProject(Project project) {
		for (Plugin p : plugins) {
			p.setProject(project);
		}
	}
}