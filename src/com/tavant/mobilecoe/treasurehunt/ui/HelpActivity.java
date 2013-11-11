package com.tavant.mobilecoe.treasurehunt.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.tavant.mobilecoe.treasurehunt.R;

public class HelpActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		
	}
	@Override
	protected void onStart() {
		super.onStart();
		Log.i("TAG","onstart called in HelpActivity");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.i("TAG","onResume called in HelpActivity");
	}
	}



