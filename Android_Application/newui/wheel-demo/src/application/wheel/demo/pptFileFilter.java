package overmind.android.application;

import java.io.File;
import java.io.FileFilter;
//class used to filter files based on extension.
class pptFileFilter implements FileFilter {

	  public boolean accept(File pathname) {

	    if (pathname.getName().endsWith(".ppt"))
	      return true;
	    if (pathname.getName().endsWith(".pptx"))
	      return true;
	    return false;
	  }
	}
