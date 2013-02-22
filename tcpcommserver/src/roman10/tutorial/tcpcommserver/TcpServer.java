package roman10.tutorial.tcpcommserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class TcpServer extends Activity {
	
	private final static String DEBUG_TAG = "TakePictureActivity";
	private TextView textDisplay;
    private static final int TCP_SERVER_PORT = 4444;
    private static int count = 0;
    int filesize = 6022386;
    int bytesRead;
    ServerSocket ss;
    Socket s;
    
    // camera related stuff
    private int cameraId = 0;
    private Camera mCamera;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        textDisplay = (TextView) this.findViewById(R.id.text1);
        runTcpServerThread();
        textDisplay.setText("Created server");
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
    void runTcpServerThread()
    {
    	new Thread(new Runnable() {
            public void run() {
            	runTcpServer();// do stuff that doesn't touch the UI here
            }
    }).start();
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
    
    private void startCamera()
    {
    	 if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
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
    }
    
    public void showMsgonui(final String str)
    {
    	runOnUiThread(
				new Runnable()
				{	
					public void run() 
					{
						Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
						textDisplay.append(str);
					}
				}
				);
    }
    
    private void runTcpServer() {
    	try {
			ss = new ServerSocket(TCP_SERVER_PORT);
			
			while(true){
			//accept connections
			s = ss.accept();

			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			         
			//receive a message
			String incomingMsg = in.readLine() + System.getProperty("line.separator");
			//Log.i("TcpServer", "received: " + incomingMsg);
			//textDisplay.append("received: " + incomingMsg);
			showMsgonui("received: " + incomingMsg);
			
			if(incomingMsg.equals("click"));
			{
				startCamera();
				mCamera.takePicture(null, null, new PictureHandler(getApplicationContext(),s,this));
				showMsgonui("Got Click");
							
			}
			
			//send a message
			String outgoingMsg = "$@#* aarrrg !" + System.getProperty("line.separator");
			showMsgonui("Recieved $@#* from somebody");
			
			out.write(outgoingMsg);
			out.flush();
			
			Log.i("TcpServer", "sent: " + outgoingMsg);
			showMsgonui("sent: " + outgoingMsg);
			//textDisplay.append("sent: " + outgoingMsg);
			//SystemClock.sleep(5000);
			s.close();
			}
		} catch (InterruptedIOException e) {
			//if timeout occurs
			e.printStackTrace();
    	} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ss != null) {
				try {
					ss.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    }
}