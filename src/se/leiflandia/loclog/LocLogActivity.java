package se.leiflandia.loclog;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ToggleButton;

public class LocLogActivity extends Activity {
	private static final String TAG = LocLogActivity.class.getSimpleName();
	private Intent logLocServiceIntent;
	
	private EditText edtMinTime;
	private EditText edtMinDistance;
	// TODO onOff-button should be on if service is on.
	ToggleButton onOff;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        init();
        onOff.setOnClickListener(new OnOffListener());
    }
    
    private void init() {
        logLocServiceIntent = LocLogService.genStartIntent(getApplicationContext());
        edtMinTime = (EditText) findViewById(R.id.minTime);
        edtMinDistance = (EditText) findViewById(R.id.minDistance);
        onOff = (ToggleButton) findViewById(R.id.tglLocLogService);
        onOff.setChecked(Util.isServiceRunning((ActivityManager) getSystemService(ACTIVITY_SERVICE), LocLogService.class.getName()));
    }
    
    private void updateIntent() {
    	long minTime = -1;
    	try {
    		minTime = Long.parseLong(edtMinTime.getText().toString());
    	} catch (NumberFormatException e) {	}
    	if (minTime >= 0)
    		logLocServiceIntent.getExtras().putLong(LocLogService.KEY_MIN_TIME, minTime);
    	
    	float minDistance = -1f;
    	try {
    		minDistance = Float.parseFloat(edtMinDistance.getText().toString());
    	} catch (NumberFormatException e) {	}
    	if (minDistance >= 0f)
    		logLocServiceIntent.getExtras().putFloat(LocLogService.KEY_MIN_DISTANCE, minDistance);
    }
    
    private class OnOffListener implements OnClickListener {
    	
    	@Override
    	public void onClick(View v) {
    		if (onOff.isChecked()) {
    			Log.d(TAG, "About to start service.");
    			updateIntent();
    			startService(logLocServiceIntent);
    		} else {
    			Log.d(TAG, "About to stop service.");
    			stopService(logLocServiceIntent);
    		}
    	}
    }
}