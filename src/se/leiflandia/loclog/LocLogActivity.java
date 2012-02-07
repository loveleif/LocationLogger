package se.leiflandia.loclog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.ToggleButton;

public class LocLogActivity extends Activity {
	private static final String TAG = LocLogActivity.class.getSimpleName();
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final Intent intent = LocLogService.genStartIntent(getApplicationContext(), 5000, 1);
        
        final ToggleButton togglebutton = (ToggleButton) findViewById(R.id.tglLocLogService);
        togglebutton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		if (togglebutton.isChecked()) {
        			Log.d(TAG, "About to start service.");
        			startService(intent);
        		} else {
        			Log.d(TAG, "About to stop service.");
        			stopService(intent);
        		}
        	}
        });
    }
}