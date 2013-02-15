package com.example.takemypicture;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;

/*
 * <Button
        android:id="@+id/captureFront"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:layout_alignParentRight="true"
        android:onClick="onClick"
        android:text="@string/click" />
 */

public class TakePictureActivity extends Activity {
	private final static String DEBUG_TAG = "TakePictureActivity";
	
	private RelativeLayout mRelativeLayout;
	private Camera mCamera;
	private Preview mPreview;
	private RemotePreview mPreviewRemote;
	private Button mButton;
	private Button mButtonRemote;
	private int cameraId = 0;
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_take_picture);
		
		 if (!getPackageManager()
			        .hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			      Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG)
			          .show();
			    } else {
			      cameraId = findFrontFacingCamera();
			      if (cameraId < 0) {
			        Toast.makeText(this, "No front facing camera found.",
			            Toast.LENGTH_LONG).show();
			      } else {
			    	  mCamera = Camera.open(cameraId);
			      }
			    }
		 
		 mPreview = new Preview(getApplicationContext(),mCamera);
		 mRelativeLayout = new RelativeLayout(this);
		 mPreview.setX(0);
		 mPreview.setY(100);
		 mRelativeLayout.addView(mPreview,400,240);
		 
		 mPreviewRemote = new RemotePreview(getApplicationContext());
		 //mPreview.setX(0);
		 mRelativeLayout.addView(mPreviewRemote,400,240);
		 mPreviewRemote.setX(400);
		 mPreviewRemote.setY(100);
		 
		 
		 mButton = new Button(getApplicationContext());
		 mButton.setOnClickListener(new View.OnClickListener() 
		 {
			
			 public void onClick(View view) 
			 {
					mCamera.takePicture(null, null, new PictureHandler(getApplicationContext()));
			  }
		 });
		 mButton.setText("Click to take picture");
		 mButton.setX(0);
		 mRelativeLayout.addView(mButton);
		 
		 mButtonRemote = new Button(getApplicationContext());
		 mButtonRemote.setOnClickListener(new View.OnClickListener() 
		 {
			
			 public void onClick(View view) 
			 {
					//mCamera.takePicture(null, null, new PhotoHandler(getApplicationContext()));
				 Log.d(DEBUG_TAG, "Find remote Camera");
				 //mPreviewRemote.requestFocus();
				 mPreviewRemote.playvideo(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.video));
				 Log.d(DEBUG_TAG, "Sending request to remote Camera");
				 
			  }
		 });
		 mButtonRemote.setText("Click to send request to remote");
		 mButtonRemote.setX(240);
		 mRelativeLayout.addView(mButtonRemote);
		 
		 setContentView(mRelativeLayout);
	}
	
	private int findFrontFacingCamera() {
	  int cameraId = -1;
	    // Search for the front facing camera
	    int numberOfCameras = Camera.getNumberOfCameras();
	    for (int i = 0; i < numberOfCameras; i++) {
	      CameraInfo info = new CameraInfo();
	      Camera.getCameraInfo(i, info);
	      if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
	        Log.d(DEBUG_TAG, "Camera found");
	        cameraId = i;
	        break;
	      }
	    }
	    return cameraId;
	  }

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		mCamera.release();
		mCamera = null;
		super.finish();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
		    }
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if(mCamera == null)
			mCamera = Camera.open(cameraId);
		// Have to start preview here
		super.onResume();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		if(mCamera != null)
		{
		mCamera.release();
		mCamera = null;
		}
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_take_picture, menu);
		return true;
	}

}