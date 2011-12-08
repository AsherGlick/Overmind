package kankan.wheel.demo;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class TabbingActivity extends TabActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab);
		
		/** TabHost will have Tabs */
		TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
		tabHost.setup();
		
		Intent intent1 = new Intent().setClass(this, FirstTab.class);
		Intent intent2 = new Intent().setClass(this, SecondTab.class);
		
		Resources res = getResources();
		
		TabSpec tspec1 = tabHost.newTabSpec("First Tab");
		tspec1.setIndicator("Computer",res.getDrawable(R.drawable.ic_tab_computer)).setContent(intent1);
		tabHost.addTab(tspec1);
		TabSpec tspec2 = tabHost.newTabSpec("Second Tab");
		tspec2.setIndicator("Android",res.getDrawable(R.drawable.ic_tab_android)).setContent(intent2);
		tabHost.addTab(tspec2);
		
		tabHost.setCurrentTab(2);
		
	}
}