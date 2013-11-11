package com.tavant.mobilecoe.treasurehunt.ui;

import com.tavant.mobilecoe.treasurehunt.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class ScanActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan_layout);
	}

	
	@Override
	protected void onStart() {
		super.onStart();
		parsedailyTest();
		
	}
	
	private void parsedailyTest() {
	
	}


	@Override
	protected void onResume() {
		super.onResume();
		Log.i("TAG","onResume called in ScanActivity");
	}

}
