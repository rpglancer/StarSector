package ss;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;


public class StarSector extends Canvas implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8384324761945636774L;
	private boolean running = false;
	
	private static final int HEIGHT = 768;
	private static final int WIDTH = 1024;
	
	private static final int VER_MAJOR = 0;
	private static final int VER_MINOR = 0;
	private static final int VER_REV = 1;
	private static final String VER_REL = "a";
	
	private BufferedImage bufimg = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
	private Thread thread;
	

	public static void main(String[] args) {
		StarSector SS = new StarSector();
		SS.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		SS.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		SS.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		SS.setFocusable(true);
		SS.requestFocus();
		JFrame frame = new JFrame("StarSector v" + VER_MAJOR + "." + VER_MINOR + "." + VER_REV + VER_REL);
		frame.add(SS);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		SS.start();
	}
	
	@Override
	public void run(){
		init();
		long lastTime = System.nanoTime();
		double ns = 1000000000;
		double delta = 0;
		long timer = System.currentTimeMillis();
		lastTime = System.nanoTime();
		delta = 0;
		timer = System.currentTimeMillis();
		
		while(running){
			long now = System.nanoTime();
			now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1){
				//tick();
				delta = 0;
			}
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
			}
			render();
		}
		stop();
	}
	
	private void init(){
		
	}
	
	private void render(){
		BufferStrategy BS = this.getBufferStrategy();
		if(BS == null){
			createBufferStrategy(3);
			return;
		}
		Graphics G = BS.getDrawGraphics();
		G.drawImage(bufimg, 0, 0, getWidth(), getHeight(), this);
		G.dispose();
		BS.show();
	}
	
	private synchronized void start(){
		if(running){
			return;
		}
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	private synchronized void stop(){
		if(!running){
			return;
		}
		try{
			thread.join();
		} catch(InterruptedException e){
			e.printStackTrace();
		}
		System.exit(1);
	}
	
	private void tick(){
		
	}

}
