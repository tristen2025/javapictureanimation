package staticimagetest;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;

public class StaticGenerator extends JComponent implements Runnable {
	static JFileChooser imageChooser = null;
	byte[] data;
	BufferedImage image;
	Random random;
	int[][] pixels;
	int x;
	public StaticGenerator(){
		x =0;
	}
	
	public void initialize(){
		random = new Random();
		int w = getSize().width,h = getSize().height;
		
		
		
		
		//image = new BufferedImage(cm,wr,false,null);
		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		
		pixels = new int[image.getWidth()][image.getHeight()];
		for(int i =0; i<pixels.length; i++){
			for(int j =0; j<pixels[i].length;j++){
				pixels[i][j] = random.nextInt(255);
		}
	}
			
	
		
		
		for(int i =0; i<pixels.length; i++){
			for(int j =0; j<pixels[i].length;j++){
				int red = redValue(pixels[i][j]);
				int blue = blueValue(pixels[i][j]);
				int green = greenValue(pixels[i][j]);
				/*if(blue>red&&blue>green)pixels[i][j] = rgbValue(i*j-x,i+j+x,(i*j)%x*(i+j));
				else if(red<blue&&red>green)pixels[i][j] = rgbValue((i+j)*x,i+j-x,(i+j)-x);
				else if(green<blue&&green>red)pixels[i][j] = rgbValue(i+j-x,i+j*x,(i+j)-x);
				*/
				//else pixels[i][j] = pixels[i][j];
				pixels[i][j] = rgbValue((i+j)%(x+1),(x+j)%(i+1),(i+x)%(j+1));
				
				
			}
			
		}
		 image = setRGBPixels(image,pixels);
		 try {
			saveImage(image,new File("Mana_U5.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
			
			
		
		
		
	}

	
	public void run() {
		if(random == null)
			initialize();
		while(true){
			//initialize();
			initialize();
			  
			x++;
			if(x >255)x=0;
			repaint();
			try{Thread.sleep(1000/24);}
			catch(InterruptedException e){/* die */}
			
			
		}
		   
		
	}
	public void paint(Graphics g){
		if(image == null)initialize();
		g.drawImage(image, 0, 0, this);
		
		
	}
	public void setBounds(int x,int y,int width,int height){
		super.setBounds(x,y,width,height);
		initialize();
		
		
	}
	public static void main(String args[]){
		JFrame frame = new JFrame("Static Generator");
		StaticGenerator staticGen = new StaticGenerator();
		frame.add(staticGen);
		frame.setSize(3000,1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		new Thread(staticGen).start();
		
	
	
		
		
	}
	public static BufferedImage setRGBPixels(BufferedImage img, int[][] pixels) {
		// assumes width >0
		if (pixels == null) {
			//failure("Your pixel array is null!");
			return img;
		}
		int width = pixels.length, height = pixels[0].length;
		BufferedImage target = (img != null && 
				img.getWidth() == pixels.length && 
				img.getHeight() == pixels[0].length) ? img : new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				target.setRGB(i, j, pixels[i][j]);
		return target;
	}

	/**
	 * Gets the BufferedImage as a 2D array of RGB pixel values.
	 */
	public static int[][] getRGBPixels(BufferedImage img) {
		if (img == null)
			return null;
		int[][] result = null;
		try {
			PixelGrabber g = new PixelGrabber(img, 0, 0, -1, -1, true);
			g.grabPixels();
			int[] pixels = (int[]) g.getPixels();

			int w = g.getWidth(), h = g.getHeight();
			result = new int[w][h];

			for (int j = 0, count = 0; j < h; j++)
				for (int i = 0; i < w; i++)
					result[i][j] = pixels[count++];

			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	public static void saveImage(BufferedImage img, File file)
			throws IOException {
		
		if (img == null)
			System.out.println("Failed");
		else {
			String format = file.getPath().toLowerCase();
			int lastIndex = format.lastIndexOf('.');
			if (lastIndex >= 0)
				format = format.substring(lastIndex + 1);
			if (format.equals("jpg"))
				format = "jpeg";
			//loading("Writing " + format + " at " + file.getPath());
			ImageIO.write(img, format, file);
			//success("Write completed.");
		}
	}
	public static int redValue(int rgb) {
		return (rgb>>16) & 0xff;
	}

	public static int greenValue(int rgb) {
		return (rgb>>8) & 0xff;
	}

	public static int blueValue(int rgb) {
		return rgb & 0xff;
	}

	public static int rgbValue(int r, int g, int b) {
		return (r<<16 | g<<8 | b);
	}

	public static int getWidth(int[][] image) {
		return image.length;
	}

	public static int getHeight(int[][] image) {
		return image[0].length;
	}



	

}
