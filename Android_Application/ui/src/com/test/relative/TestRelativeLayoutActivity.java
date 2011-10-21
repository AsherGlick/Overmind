package com.test.relative;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class TestRelativeLayoutActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /* the following is code for creating a MultiAutoCompleteTextView
         * (basically an adaption of the auto complete text box)
         * - custom suggestions are set in COUNTRIES, but the suggestions
         * appear to show above the text box, while normal suggestions still
         * appear below as usual
         * 
         * ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        MultiAutoCompleteTextView textView = (MultiAutoCompleteTextView) findViewById(R.id.edit);
        textView.setAdapter(adapter);
        textView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());*/
        
    }
    public void testPage(View view) {
        // Kablam
    	setContentView(R.layout.page2_main);
    	
    }
    /*private static final String[] COUNTRIES = new String[] {
        "Belgium", "France", "Italy", "Germany2", "Spain"
    };*/
}