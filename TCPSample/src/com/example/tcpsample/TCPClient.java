package com.example.tcpsample;

import android.util.Log;
import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.Collections;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;


public class TCPClient {

    private String serverMessage;

    //public static String SERVERIP = "";
    public static final String SERVERIP = "192.168.1.231"; //your computer IP address

    //public static final String SERVERIP = "128.61.46.210"; //your computer IP address

    public static final int SERVERPORT = 4444;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;

    PrintWriter out;
    BufferedReader in;

    /**
     *  Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TCPClient(OnMessageReceived listener) {
        mMessageListener = listener;
    }

    /**
     * Sends the message entered by client to the server
     * @param message text entered by client
     */
    public void sendMessage(String message){
        if (out != null && !out.checkError()) {
            out.println(message);
            out.flush();
        }
    }

    public void stopClient(){
        mRun = false;
    }

    public void run() {

        mRun = true;

        try {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(SERVERIP);
        	//SERVERIP = getIPAddress();
        	//Log.d("DEBUG", "Susmita's SERVERIP "+SERVERIP);
            Log.e("TCP Client", "C: Connecting...");

            //create a socket to make the connection with the server
            Socket socket = new Socket(serverAddr, SERVERPORT);

            try {

                //send the message to the server
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                Log.e("TCP Client", "C: Sent.");

                Log.e("TCP Client", "C: Done.");

                //receive the message which the server sends back
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //in this while the client listens for the messages sent by the server
                while (mRun) {
                    serverMessage = in.readLine();

                    if (serverMessage != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(serverMessage);
                    }
                    serverMessage = null;

                }


                Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");


            } catch (Exception e) {

                Log.e("TCP", "S: Error", e);

            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
            }

        } catch (Exception e) {

            Log.e("TCP", "C: Error", e);

        }

    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
    
    /**
     * Get IP address from first non-localhost interface
     * @param ipv4  true=return ipv4, false=return ipv6
     * @return  address or empty string
     */
    /*
    public String getIPAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        Log.d("DEBUG", "sAddr version 1 "+sAddr);
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr); 
                        if (true) {
                            if (isIPv4){ 
                            	//System.out.println(sAddr);
                            	Log.d("DEBUG", "sAddr version 2 "+sAddr);
                                return sAddr;
                            }
                        } 
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        
        return "";
    }
    */
}
