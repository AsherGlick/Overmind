package kankan.wheel.demo;

import java.io.File;
import java.io.FileFilter;

class pptFileFilter implements FileFilter {

	  public boolean accept(File pathname) {

	    if (pathname.getName().endsWith(".ppt"))
	      return true;
	    if (pathname.getName().endsWith(".mp3"))
	      return true;
	    return false;
	  }
	}
