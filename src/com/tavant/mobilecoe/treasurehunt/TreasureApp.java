package com.tavant.mobilecoe.treasurehunt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Application;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

public class TreasureApp extends Application {
	
	
	private AsyncTask<Void, Void,Integer>task;
	
	@Override
	public void onCreate() {
		super.onCreate();
		startcopyingTheImage();
	}

	private void startcopyingTheImage() {
		task=new AsyncTask<Void, Void, Integer>() {

			@Override
			protected Integer doInBackground(Void... params) {
				AssetManager assetManager = getAssets();
			    String[] files = null;
			    try {
			        files = assetManager.list("");
			    } catch (IOException e) {	
			        Log.e("TAG", "Failed to get asset file list.", e);
			        return -1;
			    }
			    for(String filename : files) {
			    	if(!filename.contains(".jpg"))
			    		continue;
			        InputStream in = null;
			        OutputStream out = null;
			        try {
			          in = assetManager.open(filename);
			          File outFile = new File(getExternalFilesDir(null), filename);
			          if(outFile.exists()){
			        	  Log.i("TAG","outputfile exist");
			        	  continue;
			          }	  
			          out = new FileOutputStream(outFile);
			          copyFile(in, out);
			          in.close();
			          in = null;
			          out.flush();
			          out.close();
			          out = null;
			        } catch(IOException e) {
			            Log.e("TAG", "Failed to copy asset file: " + filename, e);
			            return -1;
			        }       
			    }
				
				return 0;
				
			}
			
		

			@Override
			protected void onPostExecute(Integer result) {
				super.onPostExecute(result);
				if(result==-1)
					Log.i("TAG","Error during copying files");
				else
				Log.i("TAG","All are copied");
			}
		};
		
		task.execute();
		
	}
	private void copyFile(InputStream in, OutputStream out) throws IOException {
	    byte[] buffer = new byte[1024];
	    int read;
	    while((read = in.read(buffer)) != -1){
	      out.write(buffer, 0, read);
	    }
	}

}
