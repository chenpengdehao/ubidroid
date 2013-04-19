package org.gtubicomp.ubidroid;

import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextBox;
import org.apache.poi.hslf.usermodel.RichTextRun;
import org.apache.poi.hslf.usermodel.SlideShow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.alljoyn.bus.BusAttachment;
import org.alljoyn.bus.BusException;
import org.alljoyn.bus.BusListener;
import org.alljoyn.bus.BusObject;
import org.alljoyn.bus.Mutable;
import org.alljoyn.bus.SessionOpts;
import org.alljoyn.bus.SessionPortListener;
import org.alljoyn.bus.Status;
import org.alljoyn.bus.annotation.BusMethod;


public class createNewPresentation
	{
	
	private static final short CONTACT_PORT=42;
	 private static final String SERVICE_NAME = "org.alljoyn.bus.samples.simple";
    static BusAttachment mBus;
    
    static boolean sessionEstablished = false;
    static int sessionId;
    static boolean firstTime = true;
	
    public static class SampleService implements SampleInterface, BusObject {

        /** Do not use per-object synchronization since we want Pi() to execute concurrently */
        public void preDispatch() {
        }

        /** Do not use per-object synchronization since we want Pi() to execute concurrently */
        public void postDispatch() {
        }

        /**
         * This method is thread safe, but we mark it synchronized just to
         * illustrate how one would mix and match concurrent and serialized
         * methods.
         */
        public synchronized String Ping(String inStr) {
        	change_slide(inStr);
            return inStr;
        }
        

        public String GetPicture(String inStr)  //inStr is the string
        {
        	return inStr;
        }

        public String[] GetFeature()  //inStr is the string
        {
        	String[] features = {"PC","No","idea","about","features"};
        	
        	return features;
        }

        public void RegisterClient(String clientName)
        {
        	
        }

        public void UnRegisterClient(String clientName)
        {
        	
        }

    }

    private static class MyBusListener extends BusListener {
        public void nameOwnerChanged(String busName, String previousOwner, String newOwner) {
            if ("com.my.well.known.name".equals(busName)) {
                System.out.println("BusAttachement.nameOwnerChanged(" + busName + ", " + previousOwner + ", " + newOwner);
            }
        }
    }
    
    
	static { 
        System.loadLibrary("alljoyn_java");
    }
	
	public static void change_slide(String str)
	{
			//http://www.coderanch.com/t/451836/open-source/display-powerpoint-slide-java-applet
			try{
	//		    SlideShow slideShow = new SlideShow();
	//		    Slide slide = slideShow.createSlide();
	//		    TextBox title = slide.addTitle(); 
	//		    title.setText("UbiDroid rocks! "); 
	//		    RichTextRun richtextrun = title.getTextRun().getRichTextRuns()[0]; 
	//		    richtextrun.setFontSize(100); 
	//		    richtextrun.setFontName("Arial"); 
	//		    richtextrun.setBold(true); 
	//		    richtextrun.setItalic(true); 
	//		    //richtextrun.setUnderlined(true); 
	//		    richtextrun.setFontColor(Color.blue); 
	//		    richtextrun.setAlignment(TextBox.AlignLeft); 
	//		    FileOutputStream out = new FileOutputStream("slideshow.ppt");
	//		    slideShow.write(out);
	//		    out.close();
			    
			    //convert ppt slide into image 
	
			    //FileInputStream is = new FileInputStream("slideshow.ppt"); 
				//FileInputStream is = new FileInputStream("C:/Users/Susmita/Documents/CS 4590/Lectures_Final/SpeechRecognition_2013.ppt"); 
				FileInputStream is = new FileInputStream("Project1_Presentation.ppt"); 
				SlideShow ppt = new SlideShow(is); 
			    is.close(); 
	
			    Dimension pgsize = ppt.getPageSize(); 
	
			    Slide[] slideNew = ppt.getSlides(); 
			    for (int i = 0; i < slideNew.length; i++) { 

				    BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB); 
				    Graphics2D graphics = img.createGraphics(); 
				    //clear the drawing area 
				    graphics.setPaint(Color.white); 
				    //graphics.setBackground(Color.red); 
				    graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height)); 
		
				    //render 
				    slideNew[i].draw(graphics); 
		
				    //save the output 
				    FileOutputStream outAgain = new FileOutputStream("slide-" + (i+1) + ".png"); 
				    javax.imageio.ImageIO.write(img, "png", outAgain); 
				    outAgain.close(); 
			    } 
			    
			}
			catch(Exception e){
				System.out.println("error");
			}
			
			//Display the first beginning slide
			if(firstTime){
				JFrame frame = new JFrame();
				frame.setSize(1200, 1000);
				JPanel panel = new JPanel(); 
			    panel.setSize(500,640);
			    //panel.setBackground(Color.WHITE); 
			    ImageIcon icon = new ImageIcon("slide-1.png"); 
			    JLabel label = new JLabel(); 
			    label.setIcon(icon);
			    //JButton next = new JButton();
			    //next.setText("Next slide");
			    //panel.add(next);
			    panel.add(label);
			    frame.getContentPane().add(panel); 
			    frame.setVisible(true);
			    firstTime = false;
			}
		    int slideNum = 1;
		    while(slideNum<6 && !firstTime){
		    	
			    if(str == "n" && slideNum < 5){//will not do next on the 5th slide
			    	slideNum++;
			    }
			    if(str == "p" && slideNum >1){ //will not do previous on the 1st slide
			    	slideNum--;
			    }
			    JFrame frame = new JFrame();
			    JPanel panel = new JPanel(); 
			    panel.setSize(500,640);
			    //panel.setBackground(Color.WHITE); 
			    String string = "slide-"+slideNum+".png";
			    System.out.println("String " + string);
			    ImageIcon icon = new ImageIcon(string); 
			    JLabel label = new JLabel(); 
			    label.setIcon(icon);
			    //JButton next = new JButton();
			    //next.setText("Next slide");
			    //panel.add(next);
			    panel.add(label);
			    frame.getContentPane().add(panel);
			    frame.setVisible(true);
		    
		    }
		    /*
		    for (int i = 2; i<10; i++){
			    try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    //frame.setVisible(false);
			    //frame.getContentPane().remove(panel); 
			   // frame = new JFrame();
			   // frame.setSize(1200, 1000);
			    panel = new JPanel(); 
			    panel.setSize(500,640);
			    //panel.setBackground(Color.WHITE); 
			    String string = "slide-"+i+".png";
			    System.out.println("String " + string);
			    icon = new ImageIcon(string); 
			    label = new JLabel(); 
			    label.setIcon(icon);
			    //JButton next = new JButton();
			    //next.setText("Next slide");
			    //panel.add(next);
			    panel.add(label);
			    frame.getContentPane().add(panel); 
			    
			    frame.setVisible(true);
			    //MouseEvent e = new MouseEvent()
		    }
		    */
		}
	
	public static void start_service()
	{
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				mBus.release();
			}
		});
        
        mBus = new BusAttachment("AppName", BusAttachment.RemoteMessage.Receive);

        Status status;

        SampleService mySampleService = new SampleService();

        status = mBus.registerBusObject(mySampleService, "/SimpleService");
        if (status != Status.OK) {
            System.exit(0);
            return;
        }
        System.out.println("BusAttachment.registerBusObject successful");

        BusListener listener = new MyBusListener();
        mBus.registerBusListener(listener);

        status = mBus.connect();
        if (status != Status.OK) {
            System.exit(0);
            return;
        }
        System.out.println("BusAttachment.connect successful on " + System.getProperty("org.alljoyn.bus.address"));        

        Mutable.ShortValue contactPort = new Mutable.ShortValue(CONTACT_PORT);

        SessionOpts sessionOpts = new SessionOpts();
        sessionOpts.traffic = SessionOpts.TRAFFIC_MESSAGES;
        sessionOpts.isMultipoint = false;
        sessionOpts.proximity = SessionOpts.PROXIMITY_ANY;
        sessionOpts.transports = SessionOpts.TRANSPORT_ANY;

        status = mBus.bindSessionPort(contactPort, sessionOpts, 
                new SessionPortListener() {
            public boolean acceptSessionJoiner(short sessionPort, String joiner, SessionOpts sessionOpts) {
                System.out.println("SessionPortListener.acceptSessionJoiner called");
                if (sessionPort == CONTACT_PORT) {
                    return true;
                } else {
                    return false;
                }
            }
            public void sessionJoined(short sessionPort, int id, String joiner) {
                System.out.println(String.format("SessionPortListener.sessionJoined(%d, %d, %s)", sessionPort, id, joiner));
                sessionId = id;
                sessionEstablished = true;
            }
        });
        if (status != Status.OK) {
            System.exit(0);
            return;
        }
        System.out.println("BusAttachment.bindSessionPort successful");

        int flags = 0; //do not use any request name flags
        status = mBus.requestName(SERVICE_NAME, flags);
        if (status != Status.OK) {
            System.exit(0);
            return;
        }
        System.out.println("BusAttachment.request successful "+SERVICE_NAME);

        status = mBus.advertiseName(SERVICE_NAME, SessionOpts.TRANSPORT_ANY);
        if (status != Status.OK) {
            System.out.println("Status = " + status);
            mBus.releaseName(SERVICE_NAME);
            System.exit(0);
            return;
        }
        System.out.println("BusAttachment.advertiseName successful "+SERVICE_NAME);

        while (!sessionEstablished) {
            try {
                Thread.sleep(10);
                //System.out.println("first sleep");
            } catch (InterruptedException e) {
                System.out.println("Thead Exception caught");
                e.printStackTrace();
            }
        }
        System.out.println("BusAttachment session established");

        while (true) {
            try {
                Thread.sleep(10000);
                System.out.println("second sleep");
            } catch (InterruptedException e) {
                System.out.println("Thead Exception caught");
                e.printStackTrace();
            }
        }
    }		
	
	public static void main(String str[])
	{
		// Create alljoyn service related instantiations
		start_service();	
	}

	
	}