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
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class TcpServer extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        textDisplay = (TextView) this.findViewById(R.id.text1);
        textDisplay.setText("");
        runTcpServer();
    }
    private TextView textDisplay;
    private static final int TCP_SERVER_PORT = 21111;
    private static int count = 0;
    int filesize = 6022386;
    int bytesRead;
    private void runTcpServer() {
    	ServerSocket ss = null;
    	try {
			ss = new ServerSocket(TCP_SERVER_PORT);
			//get the application's resources  
			Resources resources = getResources();  
			//ss.setSoTimeout(10000);
			while(true){
			//accept connections
			Socket s = ss.accept();
			//byte [] mybytearray  = new byte [filesize];
			InputStream is = s.getInputStream();
			OutputStream os = s.getOutputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			
			InputStream fs;
			fs = resources.getAssets().open("images.jpeg");  
			//send a raw file
			//create a buffer that has the same size as the InputStream  
			byte[] buffer = new byte[fs.available()];  
			fs.read(buffer);
            
			//receive a message
			String incomingMsg = in.readLine() + System.getProperty("line.separator");
			Log.i("TcpServer", "received: " + incomingMsg);
			textDisplay.append("received: " + incomingMsg);
			count = count + 1;
			//send a message
			//String outgoingMsg = "goodbye from port " + count + System.getProperty("line.separator");
			//out.write(outgoingMsg);
			//out.flush();
			os.write(buffer,0,buffer.length);
            os.flush();
			//Log.i("TcpServer", "sent: " + outgoingMsg);
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