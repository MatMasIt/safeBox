import java.io.File;

import javax.swing.filechooser.FileFilter;

public class BoxFilter extends FileFilter{

	@Override
	public boolean accept(File f) {
		// TODO Auto-generated method stub
		if(f.isDirectory()) return true;
		return f.getName().endsWith(".sfbx");
	}

	@Override
	public String getDescription() {
		return "SafeBox Archives";
	}
	
}
