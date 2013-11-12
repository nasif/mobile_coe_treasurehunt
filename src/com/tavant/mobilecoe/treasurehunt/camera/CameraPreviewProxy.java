package com.tavant.mobilecoe.treasurehunt.camera;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.media.ExifInterface;
import android.media.MediaRecorder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.tavant.mobilecoe.treasurehunt.util.CameraPictureStatus;






public final class CameraPreviewProxy 
{
	// Standard Debugging variables
	private static final String LCAT = "TAG";
	private MediaRecorderHelper mhelper=null;
	int mcurrentCameraId=-1;
	int width=480;
	int height=800;
	private boolean isrecording=false;
	private File f=null;
	private String fileextension="";
	private Class recorder_class=null;
	private Method morientation=null;
	private int currentFacingCamera=0;
	public Bitmap orginalbmp;
	public Bitmap rotatedbmp;
	private boolean isImageProcess=false;
	private Context ctx=null;
	private HashMap mdata=null;
	private CameraPictureStatus status=null;



	public CameraPreviewProxy(Context ctx,int camerfacing){
		this.ctx=ctx;
		try {
			DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
			mhelper=new MediaRecorderHelper(ctx,camerfacing,metrics.widthPixels,metrics.heightPixels);
		} catch (CameraNotAvilableException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public MediaRecorderHelper getPreview(){
		if(mhelper!=null)
			return mhelper;
		else
			return null;
	}


	/*

	private class CameraPreviewView extends TiUIView
	{
		public CameraPreviewView(TiViewProxy proxy) {
			super(proxy);
			LayoutArrangement arrangement = LayoutArrangement.DEFAULT;	
		}

		@Override
		public void processProperties(HashMap d) 
		{	
			try{
				int camerfacing=-1;
				camerfacing=d.getInt("camera_facing");
				mSuccesscallback=(KrollFunction) d.get("success");
				merrorcallback=(KrollFunction) d.get("error");

				setNativeView(mhelper);
				mdata=new HashMap();
				mdata.put("cameraid",""+mcurrentCameraId);
				mSuccesscallback.callAsync(getKrollObject(), mdata);
			}catch(CameraNotAvilableException e){
				Log.d(LCAT, "i am catching CameraNotAvilableException exception");
				e.printStackTrace();
				mdata=new HashMap();
				mdata.put("error",e.getError());
				merrorcallback.callAsync(getKrollObject(), mdata);
				return ;
			}catch(Exception e){
				Log.d(LCAT, "i am catching Exception");
				e.printStackTrace();
				mdata=new HashMap();
				mdata.put("error",e.getLocalizedMessage());
				merrorcallback.callAsync(getKrollObject(), mdata);
				return ;
			}
			super.processProperties(d);
		}
	}



	 */





	public void openCamera(final int camfacing){
		mhelper.openCamera(camfacing);
	}

	public void takePicture(final HashMap  params,CameraPictureStatus  mstatus){
		if(!isImageProcess){
			status=mstatus;
			isImageProcess=true;
			Log.d(LCAT, "calling takepictures1");
			if(!isrecording){
				Log.d(LCAT, "calling takepictures2");
				if(!params.containsKey("outputFile")||params.get("outputFile")==null||params.get("outputFile").toString().length()==0)
				{
					SendError("no outputFile property specified or can't be null");	
					isImageProcess=false;
					return;
				}
				else if(!params.containsKey("outputFolder")||params.get("outputFolder")==null||params.get("outputFolder").toString().length()==0)		
				{
					SendError("no outputFolder property specified or can't be null");
					isImageProcess=false;
					return;
				}  
				else if (!params.containsKey("outputFormat"))
				{
					SendError("no outputFormat property specified");
					isImageProcess=false;
					return;

				}else{
					mhelper.takepicture(params);
				}
			}else{
				SendError("currently recording in progress");
				isImageProcess=false;
				return;
			}
		}
	}

	private void SendError(String errorMsg){
		mdata=new HashMap();
		mdata.put("error",errorMsg);
		mdata.put("status",false);
		status.onPicturefailed(mdata);
	}



	class MediaRecorderHelper extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback{


		SurfaceView mSurfaceView;
		SurfaceHolder mHolder;
		Size mPreviewSize;
		List<Size> mSupportedPreviewSizes;
		Camera mCamera;	   
		int numberOfCameras=-1;  
		private Context ctx;
		//private HashMap status=null;
		private boolean haveCamera=false;
		public static final int CAMERA_BACK=0;
		public static final int CAMERA_FRONT=1;
		private MediaRecorder  mMediarecorder=null;
		//private static final String TAG = "CameraPreviewProxy";
		private String mOutFolder="";
		private String mOutFile="";


		private boolean RunThread = true;
		private ImageConverter photoLoaderThread = new ImageConverter();
		private File tempFile=null;

		int winorientation=-1;

		private boolean isSurfaceCreated=false;

		private  ArrayBlockingQueue  <CameraData>queue;
		private Bitmap tempBmp=null;


		public MediaRecorderHelper(Context context,int facing,int custom_width,int custom_height) throws CameraNotAvilableException ,Exception{
			super(context);
			//	try{
			this.ctx=context;
			width=custom_width;
			height=custom_height;		
			winorientation=((Activity)context).getWindow().getWindowManager().getDefaultDisplay().getOrientation();
			Log.d(LCAT,"winorientation"+winorientation);
			numberOfCameras=checkForCamera(ctx);
			Log.d(LCAT,"numberOfCameras"+numberOfCameras);
			if(numberOfCameras>0){
				haveCamera=true;	
				if(openCamera(facing)){
					mHolder=getHolder();
					mHolder.addCallback(this);
					mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);  
				}else
				{ 
					Log.d(LCAT, "i am throwing exception");
					throw new CameraNotAvilableException("Device does n't have type of camera specifed in facing");	
				}	
			}
			else
			{
				haveCamera=false;
				throw new CameraNotAvilableException("Device does n't have recording capabilities");
			}	
			queue=new ArrayBlockingQueue<CameraData>(custom_width*custom_height);
			photoLoaderThread.setPriority(Thread.NORM_PRIORITY - 1);
		}





		/**
		 * function return the default camera ID, selecting Back camera is the
		 */
		@SuppressWarnings("unchecked")
		private int findDefaultCameraId(int facing)
		{
			@SuppressWarnings("rawtypes")
			Class infoCls=null;
			@SuppressWarnings("rawtypes")
			Class cameracls=null;
			Object infoObject=null;
			Method m1=null;
			Field id1,id2=null;
			int defaultid=0;
			try {
				infoCls=Class.forName("android.hardware.Camera$CameraInfo");
				if(facing == MediaRecorderHelper.CAMERA_BACK)	
					id1=infoCls.getField("CAMERA_FACING_BACK");
				else
					id1=infoCls.getField("CAMERA_FACING_FRONT");

				id1.setAccessible(true);
				infoObject=infoCls.newInstance();
				id2=infoCls.getDeclaredField("facing");
				id2.setAccessible(true);
				cameracls=Class.forName("android.hardware.Camera");
				m1=cameracls.getMethod("getCameraInfo", Integer.TYPE,infoObject.getClass());

				for(int i=0;i<numberOfCameras;i++){
					m1.invoke(cameracls, i,infoObject);
					if((Integer.parseInt(id2.get(infoObject).toString())==Integer.parseInt(id1.get(infoCls).toString()))){
						defaultid= i;
					}
				}	
			} catch ( Exception e) {
				e.printStackTrace();
				return 0;
			}
			return defaultid;		
		}	

		/**
		 * function for getting the numberofCamera avilable through reflection, assume all device 
		 * have atleast one camera
		 * 
		 * @return
		 */
		@SuppressWarnings("unchecked")
		private int checkForCamera(Context ctx) {
			PackageManager manager=ctx.getPackageManager();
			@SuppressWarnings("rawtypes")
			Class c1=manager.getClass();
			Method sysFeatureMethod=null;
			boolean haveCamera=true;
			try {
				sysFeatureMethod=c1.getMethod("hasSystemFeature", String.class);
				haveCamera=Boolean.parseBoolean(sysFeatureMethod.invoke(manager, "android.hardware.camera").toString());
				if(!haveCamera){
					haveCamera=Boolean.parseBoolean(sysFeatureMethod.invoke(manager, "android.hardware.camera.front").toString()); 
				}else if(!haveCamera){
					haveCamera=Boolean.parseBoolean(sysFeatureMethod.invoke(manager, "android.hardware.camera.any").toString()); 
				}

			} catch (Exception e1) {
				e1.printStackTrace();
			}
			Log.d("TAG","haveCamera"+haveCamera);
			if(!haveCamera)
				return 0;
			@SuppressWarnings("rawtypes")
			Class instance=null;
			Method method=null;
			Object noOfcamera=null;
			try {
				instance = Class.forName("android.hardware.Camera");
				method = instance.getMethod("getNumberOfCameras");
				noOfcamera=method.invoke(instance);
				Log.d("haveCamera","noOfcamera"+noOfcamera);
				return Integer.parseInt(noOfcamera.toString());
			} catch (Exception e) {
				e.printStackTrace();
				return 1;
			}
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			try {
				if (mCamera != null) {
					mHolder=holder;
					mCamera.setPreviewDisplay(holder);
					isSurfaceCreated=true;
				} 
			} catch (IOException exception) {
				Log.e(LCAT, "IOException caused by setPreviewDisplay()", exception);
			}
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			Camera.Parameters parameters = mCamera.getParameters();
			parameters.setPreviewFrameRate(10);
			parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
			requestLayout();
			mCamera.setParameters(parameters);
			mCamera.setPreviewCallback(this);
			mCamera.startPreview();
			startThread();
		}

		private void startThread() {
			try{
				if(!RunThread && photoLoaderThread.getState() == Thread.State.TERMINATED){
					RunThread = true;
					photoLoaderThread = new ImageConverter();
				}
			}catch (Exception e) {
				if(!RunThread){
					RunThread = true;
					photoLoaderThread = new ImageConverter();
				}
			}
			if (photoLoaderThread.getState() == Thread.State.NEW)
				photoLoaderThread.start();
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			Toast.makeText(ctx, "closing camera", Toast.LENGTH_SHORT).show();
			if (mCamera != null) {
				mCamera.stopPreview();
				mCamera.release();
				mCamera = null;
				isSurfaceCreated=false;
				stopThread();
			}
		}
		/**
		 * function open the camera 
		 * @param type either one of front or back 
		 * @param cameraid default cameraid
		 */
		public boolean openCamera(int type) {
			if(haveCamera){
				currentFacingCamera=type;
				mcurrentCameraId=findDefaultCameraId(type);	
				Log.d("TAG","mcurrentCameraId"+mcurrentCameraId);
				if(!isrecording){
					try{
						releaseCamera();
						if(type==CAMERA_BACK){
							mCamera=Camera.open();
						}else{
							Class instance=null;
							Method method=null;
							instance = Class.forName("android.hardware.Camera");
							method = instance.getMethod("open", Integer.TYPE);
							mCamera=(Camera)method.invoke(instance, mcurrentCameraId);
						}
						if(winorientation==0){
							Class instance=null;
							Method method=null;
							instance = Class.forName("android.hardware.Camera");
							method = instance.getMethod("setDisplayOrientation", Integer.TYPE);
							int camera_orientation=getCameraOrientation(winorientation,type,mcurrentCameraId);
							method.invoke(mCamera, camera_orientation);
						}
						if (mCamera != null) {
							mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
							requestLayout();	
						}
						Log.d(LCAT,"isSurfaceCreated"+isSurfaceCreated);
						if(isSurfaceCreated)
						{
							try {
								mCamera.setPreviewDisplay(mHolder);
							} catch (IOException exception) {
								Log.e(LCAT, "IOException caused by setPreviewDisplay()", exception);
							}

							Camera.Parameters parameters = mCamera.getParameters();
							parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
							requestLayout();
							mCamera.setParameters(parameters);
							mCamera.setPreviewCallback(this);
							mCamera.startPreview();
						}		

					}catch(Exception e){
						e.printStackTrace();
						return false;
					}  
				}
				else
				{
					return false;

				} 
				return true;
			}else
			{

				return false;
			} 

		}


		private int getCameraOrientation(int rotation, int facing,int currentId){
			int degrees = 0;
			int result=0;
			switch (rotation) {
			case Surface.ROTATION_0: degrees = 0; break;
			case Surface.ROTATION_90: degrees = 90; break;
			case Surface.ROTATION_180: degrees = 180; break;
			case Surface.ROTATION_270: degrees = 270; break;
			}
			@SuppressWarnings("rawtypes")
			Class infoCls=null;
			@SuppressWarnings("rawtypes")
			Class cameracls=null;
			Object infoObject=null;
			Method m1=null;
			Field id0 = null,id1 = null,id2=null;
			Field orientation=null;
			int defaultid=0;
			try {
				infoCls=Class.forName("android.hardware.Camera$CameraInfo");
				if(facing == MediaRecorderHelper.CAMERA_BACK){	
					id0=infoCls.getField("CAMERA_FACING_BACK");
					id0.setAccessible(true);
				}
				else{
					id1=infoCls.getField("CAMERA_FACING_FRONT");
					id1.setAccessible(true);
				}
				infoObject=infoCls.newInstance();
				id2=infoCls.getDeclaredField("facing");
				id2.setAccessible(true);
				orientation=infoCls.getDeclaredField("orientation");
				orientation.setAccessible(true);
				cameracls=Class.forName("android.hardware.Camera");
				m1=cameracls.getMethod("getCameraInfo", Integer.TYPE,infoObject.getClass());
				m1.invoke(cameracls, currentId,infoObject);
				if(id1!=null&&(Integer.parseInt(id2.get(infoObject).toString())==Integer.parseInt(id1.get(infoCls).toString()))){            //Front Facing
					int temp=Integer.parseInt(orientation.get(infoObject).toString());
					//Log.d(LCAT, "i am getting calculated temper rotation"+temp);
					result = (temp + degrees) % 360;
					result = (360 - result) % 360;  // compensate the mirror
					Log.d(LCAT, "final orientation"+result);
				}else if(id0!=null&&(Integer.parseInt(id2.get(infoObject).toString())==Integer.parseInt(id0.get(infoCls).toString()))){          // back facing
					int temp=Integer.parseInt(orientation.get(infoObject).toString());
					//Log.d(LCAT, "i am getting calculated temper rotation"+temp);
					result = (temp - degrees + 360) % 360;
					Log.d(LCAT, "final orientation"+result);
				}				
			}catch(Exception e){
				e.printStackTrace();
				//Log.d(LCAT, "i got lot of exception here");
			}	
			return result;
		}



		private void releaseCamera(){
			if (mCamera != null) {
				mCamera.stopPreview();
				mCamera.setPreviewCallback(null);
				mCamera.release();
				mCamera = null;
			}
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			if(width!=0&height!=0){
				setMeasuredDimension(width, height);
			}else
			{
				int suggestedwidth=getSuggestedMinimumWidth();
				final int width = resolveSize(suggestedwidth, widthMeasureSpec);
				int suggestedheight=getSuggestedMinimumHeight();
				final int height = resolveSize(suggestedheight, heightMeasureSpec);
				setMeasuredDimension(width, height);
			} 
			if (mSupportedPreviewSizes != null) {
				mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
				Log.d(LCAT,"optimal prewview size width"+ mPreviewSize.width);
				Log.d(LCAT,"optimal prewview size height"+ mPreviewSize.height);
			}
		}
		/// method or finding the optimal preview size for the camera based on thw given width & height
		private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
			final double ASPECT_TOLERANCE = 0.1;
			double targetRatio = (double) w / h;
			if (sizes == null) return null;

			Size optimalSize = null;
			double minDiff = Double.MAX_VALUE;

			int targetHeight = h;

			// Try to find an size match aspect ratio and size
			for (Size size : sizes) {
				double ratio = (double) size.width / size.height;
				if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}

			// Cannot find the one match the aspect ratio, ignore the requirement
			if (optimalSize == null) {
				minDiff = Double.MAX_VALUE;
				for (Size size : sizes) {
					if (Math.abs(size.height - targetHeight) < minDiff) {
						optimalSize = size;
						minDiff = Math.abs(size.height - targetHeight);
					}
				}
			}
			return optimalSize;
		}

		/**
		 * function for checking that entered file name is a valid one
		 * @param file
		 * @return
		 */
		boolean canWrite(File file) {
			if (file.exists()) {
				return file.canWrite();
			}
			else {
				try {
					file.createNewFile();
					file.delete();
					return true;
				}
				catch (Exception e) {
					return false;
				}
			}
		}		


		public void takepicture(final HashMap params) {

			try{
				mOutFolder=(String) params.get("outputFolder");
				File dirs=new File(mOutFolder);
				if(!dirs.exists())
				{
					Log.d(LCAT, "creating dirs");
					dirs.mkdirs();
				}
				Log.d(LCAT, "mOutFolder"+mOutFolder);
			}catch(Exception e){
				e.printStackTrace();
				SendError("Not a valid outputFolder property specified");
				isImageProcess=false;
				return;
			}

			try{
				Log.d(LCAT, "outputFile::::"+params.get("outputFile"));
				f=new File(mOutFolder,params.get("outputFile").toString());
				Log.d(LCAT, "absolute path"+f.getAbsolutePath());
				if(!canWrite(f))
				{
					SendError("Not a valid outputFile  or make sure specified filesystem exist in device");
					isImageProcess=false;
					return;
				}
			}catch(Exception e){
				isImageProcess=false;
				e.printStackTrace();
				SendError("Not a valid outputFile property specified");
				return;
			}		
			mCamera.takePicture(null, null, new PictureCallback() {
				@Override
				public void onPictureTaken(byte[] arg0, Camera arg1) {
					try {
						mCamera.startPreview();
						Log.d(LCAT, "tempfilename1");
						File outfile=new File(getDataDir(ctx)+""+File.separatorChar+"temp");
						//File outfile=new File("/mnt/sdcard/com.test.sample"+""+File.separatorChar+"temp");
						Log.d(LCAT, "tempfilename"+outfile.getAbsolutePath());
						FileOutputStream fons=new FileOutputStream(outfile);
						fons.write(arg0);
						fons.flush();
						fons.close();
						outfile.deleteOnExit();
						processImage(outfile.getAbsolutePath(),params,f.getCanonicalPath());		
					} catch (Exception e) {
						isImageProcess=false;
						e.printStackTrace();
						SendError("Failed to save picture");
					}
				}
			});

		}

		private String getDataDir(Context ctx) throws Exception 
		{
			return ctx.getApplicationContext().getPackageManager().getPackageInfo(ctx.getPackageName(), 0).applicationInfo.dataDir;
		}


		private void processImage(final String filString,final HashMap params,final String destPath)  {

			String fileextension=".jpeg";
			Bitmap.CompressFormat compresstype=null;
			FileOutputStream fos=null;

			int format;
			try{
				format= Integer.parseInt(params.get("outputFormat").toString());
				if(format==CameraActivity.PICTURE_FORMAT_JPEG){
					fileextension=".jpeg";
					compresstype=Bitmap.CompressFormat.JPEG;
				}	
				else if(format==CameraActivity.PICTURE_FORMAT_PNG){
					fileextension=".png";
					compresstype=Bitmap.CompressFormat.PNG;
				}
				String outFile=destPath+fileextension; 
				orginalbmp=sampleBitmap(filString, 320, 480);
				Log.d(LCAT, "after configuring bitmap size is"+orginalbmp.getWidth()+"::::"+orginalbmp.getHeight());

				int width=orginalbmp.getWidth();
				int height=orginalbmp.getHeight();

				Matrix matrix = new Matrix();
				ExifInterface exifReader = new ExifInterface(filString);
				int orientation = exifReader.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
				Log.d(LCAT, "orinettaion"+orientation);
				if (orientation ==ExifInterface.ORIENTATION_NORMAL&&currentFacingCamera==CAMERA_BACK) {
					matrix.postRotate(90);
				}if (orientation ==ExifInterface.ORIENTATION_NORMAL&&currentFacingCamera==CAMERA_FRONT) {
					matrix.postRotate(-90);
				}else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
					matrix.postRotate(90);
				} else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
					matrix.postRotate(180);
				} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
					matrix.postRotate(270);
				}else if(orientation ==ExifInterface.ORIENTATION_UNDEFINED&&currentFacingCamera==CAMERA_FRONT){  //defualt
					matrix.postRotate(-90);
				}else if(orientation ==ExifInterface.ORIENTATION_UNDEFINED&&currentFacingCamera==CAMERA_BACK){   //default
					matrix.postRotate(90);
				}
				boolean deleteFile= new File(filString).delete();
				Log.d(LCAT, "deleting tempfile"+deleteFile);

				rotatedbmp=Bitmap.createBitmap(orginalbmp, 0, 0, width,height ,matrix,false);
				fos = new FileOutputStream(outFile);
				rotatedbmp.compress(compresstype, 60, fos);
				//mdata=new HashMap();
				//mdata.put("status",true);
				//FileProxy fileproxy=new FileProxy(new File(outFile).getParent()+"/", new String[]{new File(outFile).getName()});
				//mdata.put("filepath", fileproxy);
				//mSuccesscallback.callAsync(getKrollObject(), mdata);
				Log.i("TAG","outFile"+outFile);
				status.onPicturetaken(outFile);
				isImageProcess=false;
			}catch (Exception e) {
				SendError("Failed to save picture");
				e.printStackTrace();
				isImageProcess=false;
			}finally{
				if(orginalbmp!=null&&!orginalbmp.isRecycled()){
					Log.d(LCAT, "recycling orginalbmp");
					orginalbmp.recycle();
				}
				if(rotatedbmp!=null&&!rotatedbmp.isRecycled()){
					Log.d(LCAT, "recycling rotatedbmp");
					rotatedbmp.recycle();}
				if( fos!=null)
					try {
						fos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}

		}



		@Override
		public void onPreviewFrame(byte[] data, Camera camera) {
			try {
				queue.put(new CameraData(data,mPreviewSize.width,mPreviewSize.height));
				Log.i("TAG","getting previewframe");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}




		public class CameraData{

			byte mdata[]=null;
			int mWidth=0;
			int mHeight=0;
			public CameraData(byte []data,int width,int height){
				this.mdata=data;
				this.mWidth=width;
				this.mHeight=height;
			}
		}



		class ImageConverter extends Thread {

			public void run() {
				while (RunThread) {
					try {
						CameraData data=queue.take();
						Log.i("TAG","getting data");
						yuvtoJpegConverter(data.mdata,data.mWidth,data.mHeight);
						Log.i("TAG","Absolute path of file"+tempFile.getAbsolutePath());
						((CameraPictureStatus)ctx).onPicturetaken(tempFile.getAbsolutePath());
						if (Thread.interrupted())
							break;
					}catch (Exception e) {
						e.printStackTrace();
					}
				} 
			}
		}
		public void stopThread() {
			RunThread = false;
			photoLoaderThread.interrupt();
		}
		private void yuvtoJpegConverter(byte data[],int width,int height) throws Exception{
			int[] mIntArray = new int[width*height];
			decodeYUV420SP(mIntArray,data,width,height);
			if(tempBmp!=null&&!tempBmp.isRecycled())
				tempBmp.recycle();
			Bitmap bmp = Bitmap.createBitmap(mIntArray, width, height, Bitmap.Config.ARGB_8888);   // TODO Check with
			Matrix matrix = new Matrix();
			matrix.postRotate(90);
			tempBmp= Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
			bmp.recycle();
			tempFile=new File(ctx.getExternalFilesDir(null), "temp1.jpg");
			FileOutputStream fons=new FileOutputStream(tempFile);
			tempBmp.compress(Bitmap.CompressFormat.JPEG, 30, fons);
			((CameraPictureStatus)(ctx)).onPicturetaken(tempFile.getAbsolutePath());
		}



		private void decodeYUV420SP(int[] rgba, byte[] yuv420sp, int width,
				int height) {
			final int frameSize = width * height;

			for (int j = 0, yp = 0; j < height; j++) {
				int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
				for (int i = 0; i < width; i++, yp++) {
					int y = (0xff & ((int) yuv420sp[yp])) - 16;
					if (y < 0)
						y = 0;
					if ((i & 1) == 0) {
						v = (0xff & yuv420sp[uvp++]) - 128;
						u = (0xff & yuv420sp[uvp++]) - 128;
					}

					int y1192 = 1192 * y;
					int r = (y1192 + 1634 * v);
					int g = (y1192 - 833 * v - 400 * u);
					int b = (y1192 + 2066 * u);

					if (r < 0)
						r = 0;
					else if (r > 262143)
						r = 262143;
					if (g < 0)
						g = 0;
					else if (g > 262143)
						g = 262143;
					if (b < 0)
						b = 0;
					else if (b > 262143)
						b = 262143;

					// rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) &
							// 0xff00) | ((b >> 10) & 0xff);
					// rgba, divide 2^10 ( >> 10)
					rgba[yp] = ((r << 14) & 0xff000000) | ((g << 6) & 0xff0000)
							| ((b >> 2) | 0xff00);
				}
			}
		}

		private Bitmap sampleBitmap(String file,int width,int height){
			BitmapFactory.Options bitopt=new BitmapFactory.Options();
			bitopt.inJustDecodeBounds=true;
			Bitmap bit=BitmapFactory.decodeFile(file, bitopt);

			int h=(int) Math.ceil(bitopt.outHeight/(float)height);
			int w=(int) Math.ceil(bitopt.outWidth/(float)width);
			int samplesize=0;
			if(h>1 || w>1){
				if(h>w){
					samplesize=h;
				}else{
					samplesize=w;
				}
			}
			if(samplesize!=0){
				// sampling rate is calculating again
				if(samplesize>=16)
					bitopt.inSampleSize=samplesize-8;
				else if(samplesize>=8)	
					bitopt.inSampleSize=samplesize-4;   
				else if(samplesize>=4)
					bitopt.inSampleSize=samplesize-2;      	   
				else if(samplesize>=2)
					bitopt.inSampleSize=samplesize-1;
				else
					bitopt.inSampleSize=samplesize;
			}
			Log.d(LCAT, "inboundbitmap h ="+h+" and width w="+w+" samplesize="+samplesize+"");
			bitopt.inJustDecodeBounds=false;
			bitopt.inPreferredConfig=Bitmap.Config.RGB_565;
			bit=BitmapFactory.decodeFile(file, bitopt);
			return bit;
		}



	}

}




