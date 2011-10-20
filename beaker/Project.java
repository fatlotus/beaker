package beaker;

import beaker.Plugin;
import java.util.List;
import java.util.ArrayList;
import java.io.File;

public class Project {
	private File sourcePath;
	private File dataPath;
	private List<Plugin> pluginList;
	
	public Project(File path) {
		sourcePath = path;
		dataPath = new File(sourcePath, ".beaker");
		pluginList = new ArrayList<Plugin>();
	}
	
	public void addPlugin(Plugin p) {
		if (p == null)
			throw new NullPointerException();
		
		p.setProject(this);
		pluginList.add(p);
	}
	
	public void addPluginFromClass(Class<?> p)
	  throws InstantiationException, ClassCastException, IllegalAccessException {
		addPlugin((Plugin)p.newInstance());
	}
	
	public File getSourceDirectory() {
		return sourcePath;
	}
	
	public File getResourcePathFor(String type) {
		return new File(dataPath, type);
	}
	
	public File getResourceDirectory() {
		return dataPath;
	}
	
	public boolean isInitialized() {
		return dataPath.isDirectory();
	}
	
	public void initialize() {
		dataPath.mkdir();
	}
	
	protected Plugin getRootPlugin() {
		return new PluginCollection(pluginList.toArray(new Plugin[0]));
	}
	
	public void executeCommand(String[] arguments) {
		List<String> stageNames = new ArrayList<String>();
		boolean showUsage = false;
		boolean verbose = false;
		boolean force = false;
		
		for (int i = 0; i < arguments.length; i++) {
			String arg = arguments[i];
			
			if (arg.equals("--tasks") || arg.equals("-t")) {
				if (stageNames.size() == 0) {
					showUsage = true;
				} else {
					System.err.println("ERROR: " +
					  " Cannot run tasks and list help at the same time.");
					return;
				}
			} else if (arg.charAt(0) == '-') {
				if (arg.equals("-v") || arg.equals("--verbose")) {
					verbose = true;
				} else if (arg.equals("-f") || arg.equals("--force")) {
					force = true;
				} else {
					System.err.println("ERROR: Unknown option '" + arg + "'");
				}
			} else {
				if (showUsage) {
					System.err.println("ERROR: " +
					  " Cannot run tasks and list help at the same time.");
				} else {
					stageNames.add(arg);
				}
			}
		}
		
		if (showUsage) {
			System.err.println("# Commands:");
			System.err.println("# ");
		
			for (String stageName : getRootPlugin().getDefinedStages()) {
				System.err.println("#    " + stageName);
			}
		
			System.err.println("# ");
		} else {
			for (String stageName : stageNames) {
				if (verbose)
					System.err.println("Executing stage '" + stageName + "' -");
				
				if (stageName.equals("init")) {
					if (!isInitialized()) {
						initialize();
						System.err.println("# Initial project setup complete.");
					} else {
						System.err.println("# There is already a project here; are");
						System.err.println("# you sure you want to clear it?");
						System.err.println("#");
						System.err.println("# Run `beaker -f init` to erase cached");
						System.err.println("# data. This will not erase any code.");
						System.err.println("#");
						System.err.println("ERROR: Project already initialized.");
						return;
					}
				} else {
					if (!isInitialized()) {
						System.err.println("# This doesn't look like a project");
						System.err.println("# directory yet; ");
						System.err.println("# ");
						System.err.println("# You may want run `beaker init`.");
						System.err.println("# ");
						System.err.println("ERROR: Project uninitialized.");
						return;
					}
					try {
						executeStage(stageName);
					} catch (Throwable t) {
						if (verbose) {
							t.printStackTrace();
						} else {
							System.err.println("ERROR: " + t.getMessage());
						}
						return;
					}
				}
				
				if (verbose)
					System.err.println("- finished stage '" + stageName + "'");
			}
		}
	}
	
	public void executeStage(String stageName) {
		Plugin root = getRootPlugin();
		
		String[] definedStages = root.getDefinedStages();
		boolean found = false;
		
		for (String stage : definedStages) {
			if (stage.equals(stageName)) {
				found = true;
				break;
			}
		}
		
		if (!found)
			throw new IllegalArgumentException(
			  "Unknown build stage '" + stageName + "'");
		
		int i = 10;
		
		while (i-- > 0) {
			if (root.hasMoreTasksInStage(stageName)) {
				if (root.performTasksInStage(stageName)) {
					continue; // success; do it again.
				} else {
					break; // failure
				}
			} else {
				break; // nothing more to do.
			}
		}
	}
}