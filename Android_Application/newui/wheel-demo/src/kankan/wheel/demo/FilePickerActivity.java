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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.os.Environment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FilePickerActivity extends ListActivity {
	
	ArrayList<String> combinedList = new ArrayList<String>();
	ArrayList<String> compPowerpoints = new ArrayList<String>();
	ArrayList<String> sdPowerpoints = new ArrayList<String>();
	ArrayAdapter<String> fileList;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.file_pick);

        combinedList.clear();
        combinedList.addAll(getPptsOnComputer());
        
        fileList = new ArrayAdapter<String>(this, R.layout.file_row, combinedList);
        setListAdapter(fileList);
        
        
        //ListView list = (ListView)findViewById(android.R.id.list);
        ListView list = getListView();
        //list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> adapterView, View v, int pos,
					long id) {
				// TODO Auto-generated method stub
				//whatWasClicked(pos);
				//setContentView(R.layout.ppt_switch);
			}
        });
    }
    
    public void whatWasClicked(int pos) {
    	Toast.makeText(this, "Hello", Toast.LENGTH_LONG).show();
    	setContentView(R.layout.login_layout);
    }	
    
    public void fillListComputer(View v)
    {
    	combinedList.clear();
    	combinedList.addAll(getPptsOnComputer());
    	fileList.notifyDataSetChanged();
    	    	
    	return;    	
    }
    
    public void fillListAndroid(View v)
    {
    	combinedList.clear();
    	//combinedList.addAll(sdPowerpoints);
    	String d = ((Environment.getExternalStorageDirectory().toString() ) + "/Download");
    	combinedList.addAll( (new SDReader(d)).returnFiles() );
    	fileList.notifyDataSetChanged();
    	
    	return;    	
    }
    public void removeList(ArrayList<String> rem)
    {
    	combinedList.removeAll(rem);
    }
	public ArrayList<String> getPptsOnComputer()
    {
		ArrayList<String> fakeList = new ArrayList<String>();	
		fakeList.add("windowsPresentation.ppt");
		fakeList.add("presentationOkay.ppt");
		return fakeList;
    }
    
}