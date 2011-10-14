package beaker.plugins.java;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import beaker.AbstractPlugin;

public class Plugin extends AbstractPlugin {
	private JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	
	public Plugin() {
	}
	
	@Override
	public boolean compileSourceFile(File file) {
		int result = compiler.run(null, null, null, file.getAbsolutePath());
		
		return (result == 0);
	}
	
	@Override
	public String getSourceExtension() {
		return ".java";
	}
	
	@Override
	public String getCompiledExtension() {
		return ".class";
	}
}