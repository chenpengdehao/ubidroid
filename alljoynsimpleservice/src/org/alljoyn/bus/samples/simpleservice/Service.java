/*
 * Copyright 2010-2011, Qualcomm Innovation Center, Inc.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.alljoyn.bus.samples.simpleservice;

import java.util.concurrent.Semaphore;

import org.alljoyn.bus.BusAttachment;
import org.alljoyn.bus.BusListener;
import org.alljoyn.bus.BusObject;
import org.alljoyn.bus.Mutable;
import org.alljoyn.bus.SessionOpts;
import org.alljoyn.bus.SessionPortListener;
import org.alljoyn.bus.Status;
//import org.alljoyn.bus.p2p.WifiDirectAutoAccept;

import android.app.Activity;
import android.content.Context;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class Service extends Activity {
    /* Load the native alljoyn_java library. */
    static {
        System.loadLibrary("alljoyn_java");
    }
    
    private static final String TAG = "SimpleService";
    
    private static final int MESSAGE_PING = 1;
    private static final int MESSAGE_PING_REPLY = 2;
    private static final int MESSAGE_POST_TOAST = 3;

    private ArrayAdapter<String> mListViewArrayAdapter;
    private ListView mListView;
    private Menu menu;
    
    Camera mCamera;
    private int cameraId = 0;
    ImageView image; 
    private final Semaphore isImageCaptured = new Semaphore(1);
    public PictureHandler pich;
        
    //private WifiDirectAutoAccept mWfdAutoAccept;

    private Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                case MESSAGE_PING:
                    String ping = (String) msg.obj;
                    mListViewArrayAdapter.add("Ping:  " + ping);
                    break;
                case MESSAGE_PING_REPLY:
                    String reply = (String) msg.obj;
                    mListViewArrayAdapter.add("Reply:  " + reply);
                    break;
                case MESSAGE_POST_TOAST:
                    Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
                }
            }
        };
    
    /* The AllJoyn object that is our service. */
    private SimpleService mSimpleService;

    /* Handler used to make calls to AllJoyn methods. See onCreate(). */
    private Handler mBusHandler;
    
    /* Camera related code */
    private int findFrontFacingCamera() {
    	  int cameraId = -1;
    	    // Search for the front facing camera
    	    /*int numberOfCameras = Camera.getNumberOfCameras();
    	    for (int i = 0; i < numberOfCameras; i++) {
    	      CameraInfo info = new CameraInfo();
    	      Camera.getCameraInfo(i, info);
    	      if (info.facing == CameraInfo.CAMERA_FACING_FRONT || info.facing == CameraInfo.CAMERA_FACING_BACK) {
    	        Log.d(DEBUG_TAG, "Camera found");
    	        cameraId = i;
    	        break;
    	      }
    	    }*/
    	    return 0;
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
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mListViewArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
        mListView = (ListView) findViewById(R.id.ListView);
        mListView.setAdapter(mListViewArrayAdapter);
        
        /* Prepare the auto-accept object.  It will not automatically
         * accept any connections until its intercept() method is called.
         */
        //mWfdAutoAccept = new WifiDirectAutoAccept(getApplicationContext());

        /* Make all AllJoyn calls through a separate handler thread to prevent blocking the UI. */
        HandlerThread busThread = new HandlerThread("BusHandler");
        busThread.start();
        mBusHandler = new BusHandler(busThread.getLooper());
        
        /* image view */
        image = (ImageView)findViewById(R.id.imageView1);
        /* Start Camera */
        startCamera();
        
        /* Start our service. */
        mSimpleService = new SimpleService();
        mBusHandler.sendEmptyMessage(BusHandler.CONNECT);
        
        /* create a picture handler object */
        pich = new PictureHandler(getApplicationContext(),mCamera, image, isImageCaptured);
    }

    @Override
    public void onResume() {
    	super.onResume();
    	if(mCamera == null)
			mCamera = Camera.open(cameraId);
		// Have to start preview here
		mCamera.startPreview();

        /* The auto-accept handler is automatically deregistered
         * when the application goes in to the background, so
         * it must be registered again here in onResume().
         *
         * Since any push-button group formation request will be
         * accepted while the auto-accept object is intercepting
         * requests, only call intercept(true) when the application is
         * expecting incoming connections.  Call intercept(false) as soon
         * as incoming connections are not expected.
         */
        //mWfdAutoAccept.intercept(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        this.menu = menu;
        return true;
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.quit:
	    	finish();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
    
    @Override
    protected void onStop() {
        super.onStop();
    	if(mCamera != null)
		{
			mCamera.stopPreview();
		mCamera.release();
		mCamera = null;
		}
        /* While the auto-accept handler can automatically de-register
         * when the app goes in to the background or stops, it's a
         * good idea to explicitly de-register here so the handler is
         * in a known state if the application restarts.
         */
        //mWfdAutoAccept.intercept(false);
    }

    @Override
    protected void onDestroy() {
		super.onDestroy();
    	if(mCamera != null)
    	{
    		mCamera.stopPreview();
    		mCamera.release();
    		mCamera = null;
    	}
               
        //mWfdAutoAccept.intercept(false);

        /* Disconnect to prevent any resource leaks. */
        mBusHandler.sendEmptyMessage(BusHandler.DISCONNECT);        
    }
    
    @Override
	protected void onPause() {
    	super.onPause();
		// TODO Auto-generated method stub
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		    }
		
    }
    
    @Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}
    /* The class that is our AllJoyn service.  It implements the SimpleInterface. */
    class SimpleService implements SimpleInterface, BusObject {

        /*
         * This is the code run when the client makes a call to the Ping method of the
         * SimpleInterface.  This implementation just returns the received String to the caller.
         *
         * This code also prints the string it received from the user and the string it is
         * returning to the user to the screen.
         */
        public String Ping(String inStr) {
            sendUiMessage(MESSAGE_PING, inStr);

            /* Simply echo the ping message. */
            sendUiMessage(MESSAGE_PING_REPLY, inStr);
            return inStr;
        }        

        /* Helper function to send a message to the UI thread. */
        private void sendUiMessage(int what, Object obj) {
            mHandler.sendMessage(mHandler.obtainMessage(what, obj));
        }
        
        /*
         * This is the code run when the client makes a call to the Gt Picture method of the
         * SimpleInterface.  This implementation just returns the received String to the caller.
         *
         * This code also prints the string it received from the user and the string it is
         * returning to the user to the screen.
         */
        
        public String GetPicture(String inStr)
        {
        	
        	
        	try
        	{
        		isImageCaptured.acquire();
        	}
        	catch(InterruptedException e)
        	{
        		Log.e(TAG, "acquire - exception -- 1");
        	}
			    if(mCamera != null)
			    {
			    	mCamera.takePicture(null, null, pich);
			    }      	
        	try
        	{
        		isImageCaptured.acquire();
        	}
        	catch(InterruptedException e)
        	{
        		Log.e(TAG, "acquire - exception -- 2");
        	}
        	
        	isImageCaptured.release();
        	
        	return pich.res;
        }
        public String  GetFeature(String inStr){
        	PackageManager pm = getApplicationContext().getPackageManager();
		    FeatureInfo[] featureInfoList = pm.getSystemAvailableFeatures();
		    String featureStringList = null;
		    if(featureInfoList == null)
		    {
			  Log.e("ERROR","No feature is available");
		    }
		    else
		    {
		    	//FIX ME -  Create a long string with features seperated by '#'
		    	// i.e. we would use a long string with '#' as delimiter
		    	/*for(int i = 0; i<featureInfoList.length; i++){
		    		featureStringList[i]=featureInfoList[i].toString();
		    	}*/
	        	
	        }
		    return featureStringList;
        }
    }

    /* This class will handle all AllJoyn calls. See onCreate(). */
    class BusHandler extends Handler {
        /*
         * Name used as the well-known name and the advertised name.  This name must be a unique name
         * both to the bus and to the network as a whole.  The name uses reverse URL style of naming.
         */
        private static final String SERVICE_NAME = "org.alljoyn.bus.samples.simple";
        private static final short CONTACT_PORT=42;
        
        private BusAttachment mBus;

        /* These are the messages sent to the BusHandler from the UI. */
        public static final int CONNECT = 1;
        public static final int DISCONNECT = 2;

        public BusHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            /* Connect to the bus and start our service. */
            case CONNECT: { 
            	org.alljoyn.bus.alljoyn.DaemonInit.PrepareDaemon(getApplicationContext());
                /*
                 * All communication through AllJoyn begins with a BusAttachment.
                 *
                 * A BusAttachment needs a name. The actual name is unimportant except for internal
                 * security. As a default we use the class name as the name.
                 *
                 * By default AllJoyn does not allow communication between devices (i.e. bus to bus
                 * communication).  The second argument must be set to Receive to allow
                 * communication between devices.
                 */ 
                mBus = new BusAttachment(getPackageName(), BusAttachment.RemoteMessage.Receive);
                
                /*
                 * Create a bus listener class  
                 */
                mBus.registerBusListener(new BusListener());
                
                /* 
                 * To make a service available to other AllJoyn peers, first register a BusObject with
                 * the BusAttachment at a specific path.
                 *
                 * Our service is the SimpleService BusObject at the "/SimpleService" path.
                 */
                Status status = mBus.registerBusObject(mSimpleService, "/SimpleService");
                logStatus("BusAttachment.registerBusObject()", status);
                if (status != Status.OK) {
                    finish();
                    return;
                }
                
                
                
                /*
                 * The next step in making a service available to other AllJoyn peers is to connect the
                 * BusAttachment to the bus with a well-known name.  
                 */
                /*
                 * connect the BusAttachement to the bus
                 */
                status = mBus.connect();
                logStatus("BusAttachment.connect()", status);
                if (status != Status.OK) {
                    finish();
                    return;
                }
                
                /*
                 * Create a new session listening on the contact port of the chat service.
                 */
                Mutable.ShortValue contactPort = new Mutable.ShortValue(CONTACT_PORT);
                
                SessionOpts sessionOpts = new SessionOpts();
                sessionOpts.traffic = SessionOpts.TRAFFIC_MESSAGES;
                sessionOpts.isMultipoint = false;
                sessionOpts.proximity = SessionOpts.PROXIMITY_ANY;

                /*
                 * Explicitly add the Wi-Fi Direct transport into our
                 * advertisements.  This sample is typically used in a "cable-
                 * replacement" scenario and so it should work well over that
                 * transport.  It may seem odd that ANY actually excludes Wi-Fi
                 * Direct, but there are topological and advertisement/
                 * discovery problems with WFD that make it problematic to
                 * always enable.
                 */
                sessionOpts.transports = SessionOpts.TRANSPORT_ANY + SessionOpts.TRANSPORT_WFD;

                status = mBus.bindSessionPort(contactPort, sessionOpts, new SessionPortListener() {
                    @Override
                    public boolean acceptSessionJoiner(short sessionPort, String joiner, SessionOpts sessionOpts) {
                        if (sessionPort == CONTACT_PORT) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
                logStatus(String.format("BusAttachment.bindSessionPort(%d, %s)",
                          contactPort.value, sessionOpts.toString()), status);
                if (status != Status.OK) {
                    finish();
                    return;
                }
                
                /*
                 * request a well-known name from the bus
                 */
                int flag = BusAttachment.ALLJOYN_REQUESTNAME_FLAG_REPLACE_EXISTING | BusAttachment.ALLJOYN_REQUESTNAME_FLAG_DO_NOT_QUEUE;
                
                status = mBus.requestName(SERVICE_NAME, flag);
                logStatus(String.format("BusAttachment.requestName(%s, 0x%08x)", SERVICE_NAME, flag), status);
                if (status == Status.OK) {
                	/*
                	 * If we successfully obtain a well-known name from the bus 
                	 * advertise the same well-known name
                	 */
                	status = mBus.advertiseName(SERVICE_NAME, sessionOpts.transports);
                    logStatus(String.format("BusAttachement.advertiseName(%s)", SERVICE_NAME), status);
                    if (status != Status.OK) {
                    	/*
                         * If we are unable to advertise the name, release
                         * the well-known name from the local bus.
                         */
                        status = mBus.releaseName(SERVICE_NAME);
                        logStatus(String.format("BusAttachment.releaseName(%s)", SERVICE_NAME), status);
                    	finish();
                    	return;
                    }
                }
                
                break;
            }
            
            /* Release all resources acquired in connect. */
            case DISCONNECT: {
                /* 
                 * It is important to unregister the BusObject before disconnecting from the bus.
                 * Failing to do so could result in a resource leak.
                 */
                mBus.unregisterBusObject(mSimpleService);
                mBus.disconnect();
                mBusHandler.getLooper().quit();
                break;   
            }

            default:
                break;
            }
        }
    }

    private void logStatus(String msg, Status status) {
        String log = String.format("%s: %s", msg, status);
        if (status == Status.OK) {
            Log.i(TAG, log);
        } else {
            Message toastMsg = mHandler.obtainMessage(MESSAGE_POST_TOAST, log);
            mHandler.sendMessage(toastMsg);
            Log.e(TAG, log);
        }
    }
}
