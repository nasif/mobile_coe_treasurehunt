package com.tavant.mobilecoe.treasurehunt;

import java.io.UTFDataFormatException;

import com.tavant.mobilecoe.treasurehunt.ui.HelpActivity;
import com.tavant.mobilecoe.treasurehunt.ui.ResultActivity;
import com.tavant.mobilecoe.treasurehunt.ui.ScanActivity;
import com.tavant.mobilecoe.treasurehunt.ui.TreasureActivity;
import com.tavant.mobilecoe.treasurehunt.util.Utils;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;




public class HomeActivity extends TabActivity {
	
	
	private final String HELP_TAB = "Help";
	private final String SCAN_TAB = "Scan";
	private final String TREASURE_TAB = "Treasure";
	private final String SCORE_TAB = "Result";

	private  TabHost mTabHost;
	private TextView mPageTitle;
	private int mRequestedTab = 0;      // Integer for representing the tab default is zero(means GuyScreenActivity).
	public static int mNowShowingTab = 0;  
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		if(!Utils.hasHoneycomb())
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		createInitialTabs();
		//DListPreferences.getInstance().load(this);
	
		//startActivityForResult(new Intent().setAction(DListIntents.SPLASH),REQUEST_CODE_SPLASH); // Starting the Splashscreen activity                                           
		//setPageTitle(0);  // Since the default tab is GuyScreen
		getTabHost().setCurrentTab(0);
	}

private void createInitialTabs() {

	mTabHost = getTabHost();
	mTabHost.setup(getLocalActivityManager());

	//adding each tabs
	mTabHost.addTab(mTabHost.newTabSpec(HELP_TAB).setIndicator(HELP_TAB,
			getResources().getDrawable(R.drawable.ic_action_help))
			.setContent(new Intent(this, HelpActivity.class)));

	mTabHost.addTab(mTabHost.newTabSpec(SCAN_TAB).setIndicator(
			SCAN_TAB,
			getResources().getDrawable(R.drawable.ic_action_camera))
			.setContent(new Intent(this, ScanActivity.class)));

	mTabHost.addTab(mTabHost.newTabSpec(SCORE_TAB).setIndicator(SCORE_TAB,
			getResources().getDrawable(R.drawable.ic_action_share))
			.setContent(new Intent(this, ResultActivity.class)));

	

	mNowShowingTab = mTabHost.getCurrentTab();
	mTabHost.getTabContentView().setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {

		}
	});
     
	//Registering Tab changed Listener to Tab host.
	mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
		@Override
		public void onTabChanged(String tabId) {
			mRequestedTab = getTabHost().getCurrentTab();
			mNowShowingTab = getTabHost().getCurrentTab();	
		}

	});
}

}
