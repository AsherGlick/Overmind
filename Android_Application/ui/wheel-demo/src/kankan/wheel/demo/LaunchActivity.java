package kankan.wheel.demo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Very simple activity that simply launches the appropriate Activity based on
 * user preferences.
 */
public class LaunchActivity extends Activity {
	/**
	 * Launch either the PlaybackActivity or LibraryActivity, depending on user
	 * settings.
	 */
	@Override
	public void onCreate(Bundle state)
	{
		super.onCreate(state);
		startActivity(new Intent(this, LoginActivity.class));
		finish();
	}
}
