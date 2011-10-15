package beaker;

import beaker.PluginCollection;
import beaker.AbstractPlugin;
import java.util.Arrays;

public class Build {
	private Build() { }
	
	public static void main(String[] args) throws Throwable {
		Class<?> c;
		c = beaker.plugins.jruby.Plugin.class;
		c = beaker.plugins.java.Plugin.class;
		c = beaker.PluginCollection.class;
		c = beaker.CompilationPlugin.class;
		
		Plugin all = new PluginCollection("beaker.plugins.jruby.Plugin",
		  "beaker.plugins.java.Plugin");
		
		int number = 1;
		
		while (number < 10) {
			if (!all.hasMoreTasksInStage("build"))
				break;
			
			if (!all.performTasksInStage("build"))
				break;
			
			number++;
		}
	}
}