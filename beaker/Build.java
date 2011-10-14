package beaker;

import beaker.AbstractPlugin;

public class Build {
	private Build() { }
	
	public static void main(String[] args) throws Throwable {
		AbstractPlugin pluginA = new beaker.plugins.jruby.Plugin();
		AbstractPlugin pluginB = new beaker.plugins.java.Plugin();
		
		int number = 1;
		
		while (number < 10) {
			if (!pluginA.needsCompilation() || !pluginB.needsCompilation())
				break;
			
			if (pluginA.needsCompilation())
				if (!pluginA.executeCompile())
					System.exit(1);
			
			if (pluginB.needsCompilation())
				if (!pluginB.executeCompile())
					System.exit(1);
			
			number++;
		}
	}
}