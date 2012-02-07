package se.leiflandia.loclog;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LocLogService extends Service {
	private static final String TAG = LocLogService.class.getSimpleName();
	public static final String KEY_MIN_TIME = "minTime";
	public static final String KEY_MIN_DISTANCE = "minDistance";
	public static final long DEFAULT_MIN_TIME = 5000;
	public static final float DEFAULT_MIN_DISTANCE = 1;
	private LocationManager lm;
	private LocDbAdapter db = null;
	
	private class LocLogLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			if (location == null)
				return;
			db.insertLocation(location);
			Log.d(TAG, "Logged " + location.toString());
		}

		@Override
		public void onProviderDisabled(String provider) {
			Log.w(TAG, provider + " disabled by user, position logging cancelled.");
			// TODO Do more?
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
    public void onCreate() {
		super.onCreate();
		Log.d(TAG, "Created service.");
		// TODO 
	}
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) throws SQLException {
        super.onStartCommand(intent, flags, startId);
		Log.d(TAG, "Received start id " + startId + ": " + intent);
		
		// Open database
		// TODO Handle exceptions differently?
		db  = new LocDbAdapter(getApplicationContext());
		db.open();
		
		// Start logging
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(
        		LocationManager.GPS_PROVIDER, 
        		intent.getLongExtra(KEY_MIN_TIME, DEFAULT_MIN_TIME), 
        		intent.getFloatExtra(KEY_MIN_DISTANCE, DEFAULT_MIN_DISTANCE), 
        		new LocLogLocationListener());
        Log.d(TAG, "Location logging started.");
        
        return START_STICKY;
	}
	
	@Override
    public void onDestroy() {
		super.onDestroy();
		
		// TODO Handle exception?
		db.close();
		
		Log.d(TAG, "Destroyed service.");
	}

}
