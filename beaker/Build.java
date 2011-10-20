package beaker;

import beaker.PluginCollection;
import java.io.File;

public class Build {
	private Build() { }
	
	public static void main(String[] args) throws Throwable {
		Project p = new Project(new File("proj"));
		
		p.addPluginFromClass(beaker.plugins.jruby.Plugin.class);
		p.addPluginFromClass(beaker.plugins.java.Plugin.class);
		
		p.executeStage(args[0]);
	}
}