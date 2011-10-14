package beaker.plugins.jruby;

import beaker.AbstractPlugin;
import java.io.File;
import java.io.IOException;

public class Plugin extends AbstractPlugin {
	@Override
	public boolean isSourceFile(File f) {
		return f.getName().endsWith(".rb");
	}
	
	@Override
	public boolean compileSourceFile(File file) {
		try {
			String[] command = new String[] { "jrubyc", file.toString() };
			Process p = Runtime.getRuntime().exec(command);
			
			return p.waitFor() == 0;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}