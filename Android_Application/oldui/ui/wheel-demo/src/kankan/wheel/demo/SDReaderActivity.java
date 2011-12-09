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

public class SDReaderActivity extends ListActivity {
	
 private List<String> item = null;
 private List<String> path = null;
 private String root="/";
 private TextView myPath;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.file_picker);

        myPath = (TextView)findViewById(R.id.path);
        
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
        getDir(root);
        
    }

    

    private void getDir(String dirPath)
    {
     myPath.setText("Location: " + dirPath);

     item = new ArrayList<String>();
     path = new ArrayList<String>();

    
     File f = new File(dirPath);
     File[] files = f.listFiles();

     if(!dirPath.equals(root))
     {

      item.add(root);
      path.add(root);

      item.add("../");
      path.add(f.getParent());

     }

     

     for(int i=0; i < files.length; i++)
     {
       File file = files[i];
       path.add(file.getPath());
       if(file.isDirectory())
        item.add(file.getName() + "/");
       else
        item.add(file.getName());
     }

     ArrayAdapter<String> fileList =
     new ArrayAdapter<String>(this, R.layout.file_row, item);
     setListAdapter(fileList);

    }

    /** checks the state of external storage (ie. the sd card)
     *  returns  1: can read & write
     *  returns  0: read-only
     *  returns -1: can't read or write
     * @return 
     */

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
    
    @Override
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
 }
}