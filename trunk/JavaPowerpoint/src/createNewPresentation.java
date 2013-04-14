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
public class createNewPresentation
	{
		public static void main(String str[])
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
			FileInputStream is = new FileInputStream("SpeechRecognition_2013.ppt"); 
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
			catch(Exception e){}
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
		}
	}