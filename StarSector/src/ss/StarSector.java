package ss;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JFrame;

import ss.lib.Hud;
import ss.lib.SpriteSheet;
import ss.lib.Static;
import ss.type.STYPE;
import ss.util.BufferedImageLoader;


public class StarSector extends Canvas implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8384324761945636774L;
	private boolean running = false;
	
	public static final int SPRITEHEIGHT = 16;
	public static final int SPRITEWIDTH = 16;
	
	public static final int HEIGHT = 480;
	public static final int WIDTH = 720;
	
	public static final int SECTORSIZE_X = 150;
	public static final int SECTORSIZE_Y = 100;
	public static final int SECTORSIZE_Z = 100;
	
	public static final int SweepSize = 10;
	public static final int SweepLength = 60 / SweepSize;
	
	public static final double PPKM_X = WIDTH / SECTORSIZE_X;
	public static final double PPKM_Y = HEIGHT / SECTORSIZE_Y;
	public static final double PPKM_Z = HEIGHT / SECTORSIZE_Z;
	public static final double PPKM_SQ = (HEIGHT * WIDTH) / (SECTORSIZE_X * SECTORSIZE_Y);
	
	
	public static SpriteSheet Sprites;
	
	private static final int VER_MAJOR = 0;
	private static final int VER_MINOR = 0;
	private static final int VER_REV = 1;
	private static final String VER_REL = "a";
	
	private Hud hud;
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
		final double AmountOfTicks = 1.0/SweepLength;
		double ns = 1000000000 / AmountOfTicks;
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
				hud.tick();
//				System.out.println("Tick");
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
		hud = new Hud();
		this.addMouseListener(hud);
		this.addMouseMotionListener(hud);
		BufferedImageLoader BIL = new BufferedImageLoader();
		try{
			BufferedImage Temp = BIL.loadImage("../../data/sprites.png");
			Sprites = new SpriteSheet(Temp);
		} catch(IOException e){
			e.printStackTrace();
		}
		Static Test = new Static(STYPE.STATION);	
	}
	
	private void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.drawImage(bufimg, 0, 0, getWidth(), getHeight(), this);
		hud.Render(g);
//		Tracon.Render(g);
		g.dispose();
		bs.show();
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
