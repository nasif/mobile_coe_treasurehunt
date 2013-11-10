package com.tavant.mobilecoe.treasurehunt;

import com.tavant.mobilecoe.treasurehunt.prefs.CommonDailyPreferences;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.Window;

public class SplashScreenActivity extends Activity {

	private static Handler handler=new Handler();
	private ProgressDialog mDialog=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CommonDailyPreferences.getInstance().load(getApplicationContext());
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		mDialog=ProgressDialog.show(this, "", "loading....");
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
				mDialog.dismiss();
				finish();
			}
		}, 1500);
	}
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash_screen, menu);
		return true;
	}

}
