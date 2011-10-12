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
    }
    public void selfDestruct(View view) {
        // Kablam
    	setContentView(R.layout.page2_main);
    	
    }
}