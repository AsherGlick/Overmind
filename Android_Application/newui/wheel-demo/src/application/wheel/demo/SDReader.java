package overmind.android.application;

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
//class used to read off the SD card
public class SDReader {
    //the path we are looking at on the sd card
    private String dirPath="/";
    //sets the path on the SD card
    public SDReader(String d)
    {
	   dirPath = d;
    }
    //reset the path to the beginning
    public SDReader()
    {
	   dirPath= "/";
    }
    //returns the whole list of items on the current path
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
	 return item;
    }
    //check for presence of sd card
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
    //to be implemented lated
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
