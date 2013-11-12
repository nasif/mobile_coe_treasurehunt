package com.tavant.mobilecoe.treasurehunt.camera;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import org.opencv.android.OpenCVLoader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Toast;

import com.tavant.mobilecoe.treasurehunt.camera.CameraPreviewProxy.MediaRecorderHelper;
import com.tavant.mobilecoe.treasurehunt.util.CameraPictureStatus;




public class CameraActivity extends Activity implements  OnTouchListener,CameraPictureStatus{
	public static final int CAMERA_BACK=0;
	public static final int CAMERA_FRONT=1;
	public static final int PICTURE_FORMAT_JPEG=Bitmap.CompressFormat.JPEG.ordinal();
	public static final int PICTURE_FORMAT_PNG=Bitmap.CompressFormat.PNG.ordinal();  //Bitmap.CompressFormat.PNG
	private CameraPreviewProxy proxy=null;
	private MediaRecorderHelper helper=null;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("TAG","getExternalFilesDir(null)"+getExternalFilesDir(null).toString());
		if(!isRearCameraAvilable()){
			Toast.makeText(CameraActivity.this, "Device dont have video recording capabilities", Toast.LENGTH_LONG);
			finish();
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		proxy=new CameraPreviewProxy(CameraActivity.this, CAMERA_BACK);
		helper=proxy.getPreview();
		helper.setOnTouchListener(this);
		setContentView(helper);
	}
	public boolean isRearCameraAvilable(){
		@SuppressWarnings("rawtypes")
		Class infoCls=null;
		@SuppressWarnings("rawtypes")
		Class cameracls=null;
		Object infoObject=null;
		Method m1=null;
		Field id1,id2=null;
		boolean isrearcamAvilable=false;
		try {
			infoCls=Class.forName("android.hardware.Camera$CameraInfo");
			id1=infoCls.getField("CAMERA_FACING_BACK");
			id1.setAccessible(true);
			infoObject=infoCls.newInstance();
			id2=infoCls.getDeclaredField("facing");
			id2.setAccessible(true);
			cameracls=Class.forName("android.hardware.Camera");
			m1=cameracls.getMethod("getCameraInfo", Integer.TYPE,infoObject.getClass());
			for(int i=0;i<2;i++){
				m1.invoke(cameracls, i,infoObject);
				if((Integer.parseInt(id2.get(infoObject).toString())==Integer.parseInt(id1.get(infoCls).toString()))){
					isrearcamAvilable=true;
					return isrearcamAvilable;
				}
			}	
		} catch ( Exception e) {
			e.printStackTrace();
			return isrearcamAvilable;
		}
		return isrearcamAvilable;
	}
	 @Override
	    public boolean onTouch(View v, MotionEvent event) {
		    /*
	        Log.i("TAG","onTouch event");
	        Calendar date=Calendar.getInstance();
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
			String todaydate=format.format(date.getTime());
			HashMap<String , Object>params=new HashMap<String, Object>();
			params.put("outputFile", todaydate);
			params.put("outputFolder", getExternalFilesDir(null).toString());
			params.put("outputFormat", ""+PICTURE_FORMAT_JPEG);
			proxy.takePicture(params,status);
			*/
	        return false;
	    }
	 
	 CameraPictureStatus status=new CameraPictureStatus() {
		@Override
		public void onPicturetaken(String path) {
			Log.i("TAG","picture captured"+path);
			File outFile = new File(getExternalFilesDir(null), "quiz_test_3.jpg");
			double correletaion=ComapareImage.compareImage(outFile.getAbsolutePath(), path);
			Log.i("TAG","correletaion"+correletaion);
		}
		@Override
		public void onPicturefailed(HashMap error) {
		  Log.i("TAG","error status"+error.get("error"));

		}
	};
	static {
	    if (!OpenCVLoader.initDebug()) {
	        // Handle initialization error
	    } else {
	    	try{
	        System.loadLibrary("my_lib");
	    	}catch(UnsatisfiedLinkError e){
	    		e.printStackTrace();
	    	}
	    }
	}
	@Override
	public void onPicturetaken(String path) {
		Log.i("TAG","picture captured"+path);
		File outFile = new File(getExternalFilesDir(null), "quiz_test_3.jpg");
		double correletaion=ComapareImage.compareImage(outFile.getAbsolutePath(), path);
		Log.i("TAG","correletaion"+correletaion);
		
	}
	@Override
	public void onPicturefailed(HashMap error) {
		  Log.i("TAG","error status"+error.get("error"));
		
	}
	
}
