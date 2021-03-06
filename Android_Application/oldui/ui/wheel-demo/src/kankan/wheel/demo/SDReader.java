package kankan.wheel.demo;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;

import android.os.Bundle;
import android.os.Environment;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SDReader {
	
/* private List<String> item = null;
 private List<String> path = null;
 private String root="/";
 private TextView myPath;
*/
/*
 * myPath = (TextView)findViewById(R.id.path);
        
        //should return mnt/sdcard
        root =(Environment.getExternalStorageDirectory().toString());
        //String d = (Environment.getExternalStorageDirectory().toString());
        //System.out.println(d);
        //getDir( (Environment.getExternalStorageDirectory().toString()) );
        /**
         * for testing purposes, as it is difficult to emulate sd card storage **on Windows**
         * we will use getDir("testsd");
         * on Mac/Linux, refer to http://www.brighthub.com/mobile/google-android/articles/33240.aspx
         * for information on emulating sd card storage
        **/        
        
        //root="/res/testsd";
        //getDir(root);
	private String dirPath="/";
	
    public SDReader(String d)
    {
	   dirPath = d;
    }
    public SDReader()
    {
	   dirPath= "/";
    }

    

    public ArrayList<String> returnFiles()
    {
         ArrayList<String> item = new ArrayList<String>();

	     File f = new File(dirPath);
	     File[] contents = f.listFiles(new pptFileFilter());
	
	     if (contents != null) {
	         for (File file : contents) {
	        	 item.add(file.getName());
	         }
	       }
	     else
	     {
	    	 //item.add(dirPath);//show directory path as couldn't get any file
	     }
	    	 
	     return item;
     
    }
     
    private int checkExternalMedia(){
        
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Can read and write the media
            return 1;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // Can only read the media
            return 0;
        } else {
            // Can't read or write
            return -1;
        }   
    }
    
    /*@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
		 File file = new File(path.get(position));
		  if (file.isDirectory())
		  {
		   if(file.canRead())
		    getDir(path.get(position));
		   else
		   {
		    new AlertDialog.Builder(this)
		    .setIcon(R.drawable.icon)
		    .setTitle("[" + file.getName() + "] folder can't be read!")
		    .setPositiveButton("OK", 
		      new DialogInterface.OnClickListener() {
		
		       @Override
		       public void onClick(DialogInterface dialog, int which) {
		        // TODO Auto-generated method stub
		       }
		      }).show();
		   }
		  }
		  else
		  {
		   new AlertDialog.Builder(this)
		    .setIcon(R.drawable.icon)
		    .setTitle("[" + file.getName() + "]")
		
		    .setPositiveButton("OK", 
		      new DialogInterface.OnClickListener() {
		
		       @Override
		       public void onClick(DialogInterface dialog, int which) {
		
		        // TODO Auto-generated method stub
		
		       }
		
		      }).show();
  }
  } */

}
