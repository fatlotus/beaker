package beaker.plugins.jruby;

import beaker.AbstractPlugin;
import java.io.File;
import java.io.IOException;

public class Plugin extends AbstractPlugin {
	@Override
	public String getSourceExtension() {
		return ".java";
	}
	
	@Override
	public String getCompiledExtension() {
		return ".class";
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