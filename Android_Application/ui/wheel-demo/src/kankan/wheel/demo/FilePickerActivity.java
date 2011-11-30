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
import android.content.Intent;

import android.os.Bundle;
import android.os.Environment;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

        fillFileList();
        
        fileList = new ArrayAdapter<String>(this, R.layout.file_row, combinedList);
        setListAdapter(fileList);
        
    }
    public void fillFileList()
    {
    	//fill compPowerpoints with an ArrayList of file names on the computer
    	compPowerpoints = getPptsOnComputer();
    	//fill sdPowerpoints with an ArrayList of file names on the sd card (ie Android)
    	sdPowerpoints =   getPptsOnSDCard();
        
    	combinedList.addAll(compPowerpoints);
        combinedList.addAll(sdPowerpoints);
        
        return;
    }
    
    public void fillListComputer(View v)
    {
    	combinedList.clear();
    	combinedList.addAll(compPowerpoints);
    	fileList.notifyDataSetChanged();
    	    	
    	return;    	
    }
    
    public void fillListAndroid(View v)
    {
    	combinedList.clear();
    	combinedList.addAll(sdPowerpoints);
    	fileList.notifyDataSetChanged();
    	
    	return;    	
    }
    
    
    
    public void removeList(ArrayList<String> rem)
    {
    	combinedList.removeAll(rem);
    }
    
    public ArrayList<String> getPptsOnSDCard() {
		// 
		ArrayList<String> fakeList = new ArrayList<String>();
		fakeList.add("paper.doc");
		fakeList.add("image.jpg");
		fakeList.add("presentationOld.ppt");
		fakeList.add("song.mp3");
		fakeList.add("presentationFinal.ppt");
		return fakeList;
	}

	public ArrayList<String> getPptsOnComputer()
    {
		ArrayList<String> fakeList = new ArrayList<String>();	
		fakeList.add("windowsPresentation.ppt");
		fakeList.add("presentationOkay.ppt");
		return fakeList;
    }
    
}