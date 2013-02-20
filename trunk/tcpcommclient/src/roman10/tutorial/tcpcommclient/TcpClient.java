package roman10.tutorial.tcpcommclient;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class TcpClient extends Activity {
    /** Called when the activity is firstx created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        runTcpClient();
        finish();
    }
    
    private static final int TCP_SERVER_PORT = 21111;
	
	private void runTcpClient() {
		 int filesize=6022386;
		 int current = 0;
    	try {
			Socket s = new Socket("localhost", TCP_SERVER_PORT);
			byte [] mybytearray  = new byte [filesize];
            InputStream is = s.getInputStream();
            File pictureFileDir = getDir();
            String filename = pictureFileDir.getPath();
            File myFile = new File(filename);
            //myFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(myFile);
            //BufferedOutputStream bos = new BufferedOutputStream(fos);
            int bytesRead = is.read(mybytearray,0,mybytearray.length);
            	
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			//send output msg
			/*String outMsg = "TCP connecting to " + TCP_SERVER_PORT + System.getProperty("line.separator"); 
			out.write(outMsg);
			out.flush();
			Log.i("TcpClient", "sent: " + outMsg);*/
			Log.i("TcpClient", "receving ");
			//accept server response
			bytesRead = is.read(mybytearray,0,mybytearray.length);
            current = bytesRead;
			do {
	               bytesRead =
	                  is.read(mybytearray, current, (mybytearray.length-current));
	               if(bytesRead >= 0) current += bytesRead;
	            } while(bytesRead > -1);
			fos.write(mybytearray, 0 , current);
			fos.flush();
			fos.close();
			s.close();
			while(true);
			//String inMsg = in.readLine() + System.getProperty("line.separator");
			//Log.i("TcpClient", "received: " + inMsg);
			//close connection
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
    }
	//replace runTcpClient() at onCreate with this method if you want to run tcp client as a service
	private void runTcpClientAsService() {
		Intent lIntent = new Intent(this.getApplicationContext(), TcpClientService.class);
        this.startService(lIntent);
	}
	
	private File getDir() {
		File sdDir = Environment.getExternalStorageDirectory();
		return new File(sdDir, "CameraDemo.jpeg");
		}
}