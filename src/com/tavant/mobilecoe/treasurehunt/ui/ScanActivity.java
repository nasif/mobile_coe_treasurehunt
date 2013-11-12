package com.tavant.mobilecoe.treasurehunt.ui;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tavant.mobilecoe.treasurehunt.R;
import com.tavant.mobilecoe.treasurehunt.camera.CameraActivity;
import com.tavant.mobilecoe.treasurehunt.data.QuestionData;
import com.tavant.mobilecoe.treasurehunt.parser.DailyQuestionParser;
import com.tavant.mobilecoe.treasurehunt.prefs.CommonDailyPreferences;

public class ScanActivity extends Activity implements OnClickListener {


	private DailyQuestionParser parser=null;
	private String todaydate=null;
	private HashMap<String,QuestionData>map=null;
	private CommonDailyPreferences pref=null;
	private boolean isscanbuttonshowd=false;
	private boolean iscaptured=false;
	private boolean isubmitted=false;
	private String ansString=null;
	private static final int REQ_CODE_TAKE_PICTURE=8000;


	private RelativeLayout instr_parent;
	private RelativeLayout answer_parent;
	private Button btn=null;
	private Button submit=null;

	private TextView questiontext=null;

	private RadioGroup answer=null;

	private RadioButton option1=null;
	private RadioButton option2=null;
	private RadioButton option3=null;
	private RadioButton option4=null;







	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan_layout);
		instr_parent=(RelativeLayout) findViewById(R.id.instr_parent);
		answer_parent=(RelativeLayout) findViewById(R.id.answer_parent);
		btn=(Button)findViewById(R.id.instr_btn);
		btn.setOnClickListener(this);
		submit=(Button)findViewById(R.id.sub_btn);
		submit.setOnClickListener(this);
		questiontext=(TextView)findViewById(R.id.instr_text);
		answer=(RadioGroup)findViewById(R.id.radiobtn);
		//answer.setOnCheckedChangeListener(listener);
		option1=(RadioButton)findViewById(R.id.radioans1);
		option2=(RadioButton)findViewById(R.id.radioans2);
		option3=(RadioButton)findViewById(R.id.radioans3);
		option4=(RadioButton)findViewById(R.id.radioans4);
		getFormattedDate();
		pref=CommonDailyPreferences.getInstance();
	}




	private void getFormattedDate() {
		// TODO Auto-generated method stub
		Calendar date=Calendar.getInstance();
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		todaydate=format.format(date.getTime());
		Log.i("TAG","todat dat"+todaydate);
	}


	@Override
	protected void onStart() {
		super.onStart();
		parsedailyTest();
	}

	private void parsedailyTest() {
		AssetManager assetManager = getResources().getAssets();
		InputStream inputStream = null;
		try {
			inputStream = assetManager.open("daily.xml");
			parser=new DailyQuestionParser(inputStream);
			map=parser.getData();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	protected void onResume() {
		super.onResume();
		if(todaydate.contains("12")){
			isscanbuttonshowd=pref.isIsscanbuttonshowed_12();
			iscaptured=pref.isIscaptured_12();
			isubmitted=pref.isIsubmitted_12();
			ansString=pref.getAnsString_12();
		}else if(todaydate.contains("13")){
			isscanbuttonshowd=pref.isIsscanbuttonshowed_13();
			iscaptured=pref.isIscaptured_13();
			isubmitted=pref.isIsubmitted_13();
			ansString=pref.getAnsString_13();
		}else if(todaydate.contains("14")){
			isscanbuttonshowd=pref.isIsscanbuttonshowed_14();
			iscaptured=pref.isIscaptured_14();
			isubmitted=pref.isIsubmitted_14();
			ansString=pref.getAnsString_14();
		}else if(todaydate.contains("15")){
			isscanbuttonshowd=pref.isIsscanbuttonshowed_15();
			iscaptured=pref.isIscaptured_15();
			isubmitted=pref.isIsubmitted_15();
			ansString=pref.getAnsString_15();
		}
		if(isubmitted){
			instr_parent.setVisibility(View.GONE);
			answer_parent.setVisibility(View.GONE);
		}else if(iscaptured){
			instr_parent.setVisibility(View.GONE);
			populateQuestionofday();
			answer_parent.setVisibility(View.VISIBLE);
		}else{
			instr_parent.setVisibility(View.VISIBLE);
			answer_parent.setVisibility(View.GONE);
		}
	}

	private void populateQuestionofday() {
		if(map!=null){
			QuestionData data=map.get(todaydate);
			questiontext.setText(data.getQuestion());
			ArrayList<String>answers=data.getAnswer_array();
			option1.setText(answers.get(0));
			option2.setText(answers.get(1));
			option3.setText(answers.get(2));
			option4.setText(answers.get(3));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.instr_btn:
			startActivityForResult(new Intent(this, CameraActivity.class),REQ_CODE_TAKE_PICTURE);
			break;
		case R.id.sub_btn:
			int id=answer.getCheckedRadioButtonId();
			RadioButton btn=(RadioButton)findViewById(id);
			if(btn!=null&&btn.getText().toString()!=null){ 
				ansString=btn.getText().toString();
				saveAnswerForfuture(ansString);
			}else{
				Toast.makeText(this, "You need to select atleast one answer", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}


	private void saveAnswerForfuture(String anser){
		if(todaydate.contains("12")){
			pref.setIsscanbuttonshowed_12(true);
			pref.setIscaptured_12(true);
			pref.setIsubmitted_12(true);
			pref.setAnsString_12(anser);
		}else if(todaydate.contains("13")){
			pref.setIsscanbuttonshowed_13(true);
			pref.setIscaptured_13(true);
			pref.setIsubmitted_13(true);
			pref.setAnsString_13(anser);
		}else if(todaydate.contains("14")){
			pref.setIsscanbuttonshowed_14(true);
			pref.setIscaptured_14(true);
			pref.setIsubmitted_14(true);
			pref.setAnsString_14(anser);
		}else if(todaydate.contains("15")){
			pref.setIsscanbuttonshowed_15(true);
			pref.setIscaptured_15(true);
			pref.setIsubmitted_15(true);
			pref.setAnsString_15(anser);
		}
	}


}
