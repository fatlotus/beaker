package beaker.plugins.java;

import beaker.CompilationPlugin;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;

public class Plugin extends CompilationPlugin {
	private JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	
	public Plugin() {
	}
	
	@Override
	public boolean compileSourceFile(File file) {
		int result = compiler.run(null, null, null, "-sourcepath",
		  getSourceDirectory(), file.getAbsolutePath());
		
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