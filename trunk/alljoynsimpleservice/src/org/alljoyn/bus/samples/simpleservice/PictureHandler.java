package org.alljoyn.bus.samples.simpleservice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Semaphore;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;

import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class PictureHandler implements PictureCallback {
	private final static String DEBUG_TAG = "PictureHandler";
	private final Context mContext;
	private final Camera c;
	ImageView img;
	Semaphore sem;
	String res;
	  
	public PictureHandler(Context context,Camera c, ImageView i, Semaphore sem) {
	    this.mContext = context;
	    this.c = c;
	    this.img = i;
	    this.sem = sem;	    
	  }
	 
	 /*void sendPictureThread(final byte[] data)
	    {
	    	new Thread(new Runnable() {
	            public void run() {
	            	try{
	       			 OutputStream socketOutputStream = s.getOutputStream();
	       			 socketOutputStream.write(data);
	       			 s.close();
	       		 }
	       		 catch(IOException e)
	       		 {
	       			 Log.d(DEBUG_TAG, "Failed to Send image.");
	       		 }
	       		  
	       		 Log.d(DEBUG_TAG, "Sent image.");// do stuff that doesn't touch the UI here
	            }
	    }).start();
	    }*/
	 
	  @Override
	  public void onPictureTaken(byte[] data, Camera camera) {

		  Log.d(DEBUG_TAG, "onPictureTaken - called");   
		  if(c != null)
		  {
			  	
			Bitmap bmp=BitmapFactory.decodeByteArray(data,0,data.length);
			Bitmap resizedBitmap = Bitmap.createScaledBitmap(bmp, 1024, 1024, false);
			img.setImageBitmap(resizedBitmap);
			img.requestFocus();
			Log.i("SimpleService", "Shown image ");
			Toast.makeText(mContext, "Shown Image", Toast.LENGTH_LONG).show();
				
			c.startPreview();
		  //sendPictureThread(data);
			//System.arraycopy(data,0,res,0,data.length);
			// Convert byte array to string
			
			res = data.toString();
			
		  }
		  sem.release();
		  
		/*File pictureFileDir = getDir();
	    if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

	      Log.d(DEBUG_TAG, "Can't create directory to save image.");
	      Toast.makeText(mContext, "Can't create directory to save image.",
	          Toast.LENGTH_LONG).show();
	      return;

	    }

	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
	    String date = dateFormat.format(new Date());
	    String photoFile = "Picture_" + date + ".jpg";

	    String filename = pictureFileDir.getPath() + File.separator + photoFile;

	    File pictureFile = new File(filename);

	    try {
	      FileOutputStream fos = new FileOutputStream(pictureFile);
	      fos.write(data);
	      fos.close();
	      Toast.makeText(mContext, "New Image saved:" + photoFile,
	          Toast.LENGTH_LONG).show();
	    } catch (Exception error) {
	      Log.d(DEBUG_TAG, "File" + filename + "not saved: "
	          + error.getMessage());
	      Toast.makeText(mContext, "Image could not be saved.",
	          Toast.LENGTH_LONG).show();
	    }*/
	        	
	  }
	} 