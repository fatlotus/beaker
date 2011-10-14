package beaker;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.io.File;

public abstract class AbstractPlugin {
	public String getSourceExtension() {
		return "";
	}
	
	public String getCompiledExtension() {
		return "";
	}
	
	public File getSourceDirectory() {
		return new File("proj");
	}
	
	public boolean isSourceFile(File file) {
		return file.getName().endsWith(getSourceExtension());
	}
	
	public boolean compileSourceFile(File file) {
		return false;
	}
	
	public File getCompiledTargetFor(File file) {
		if (!isSourceFile(file))
			return null;
		
		String s = file.toString();
		
		return new File(s.substring(0, s.length() - 5) + getCompiledExtension());
	}
	
	public File[] recursiveScan(File directory) {
		List<File> results = new ArrayList<File>();
		Queue<File> toProcess = new LinkedList<File>();
		
		toProcess.add(directory);
		
		while (toProcess.peek() != null) {
			File item = toProcess.remove();
			
			if (item.isDirectory()) {
				for (File file : item.listFiles()) {
					toProcess.add(file);
				}
			} else if (item.isFile()) {
				results.add(item);
			}
		}
		
		return results.toArray(new File[0]);
	}
	
	protected boolean needsRecompilation(File source, File destination) {
		if (!destination.isFile()) return true;
		
 		long sourceTime = source.lastModified(),
		     destTime = destination.lastModified();
		
		return destTime < sourceTime;
	}
	
	public boolean executeCompile() {
		boolean any = false, all = true;
		
		for (File filename : recursiveScan(getSourceDirectory())) {
			File dest = getCompiledTargetFor(filename);
			
			if (isSourceFile(filename) && needsRecompilation(filename, dest)) {
				System.err.println("+ compile " + filename.toString());
				
				boolean result = compileSourceFile(filename);
				all = all && result;
				any = any || result;
			}
		}
		
		return any && all;
	}
	
	public boolean needsCompilation() {
		boolean any = false;
		
		for (File filename : recursiveScan(getSourceDirectory())) {
			File dest = getCompiledTargetFor(filename);
			
			if (isSourceFile(filename) && needsRecompilation(filename, dest)) {
				any = true;
				break;
			}
		}
		
		return any;
	}
}