package main;
import java.awt.Canvas;

import graphics.Render;
import graphics.Window;

public class Main extends Canvas implements Runnable{

	private static final long serialVersionUID = 1L;
	
	private Render render;
	private Game game;
	private Events events;
	private Window window;
	
	public Main(){
		window = new Window("TestAuto", 1920, 1080);
		window.add(this);
		
		createBufferStrategy(3);
		events = new Events();
		render = new Render(events, getBufferStrategy(), window);
		game = new Game(render, events);
		events.setRender(render);
		
		addKeyListener(events);
		addMouseListener(events);
		addMouseMotionListener(events);
		
		Thread thrd = new Thread(this);
		thrd.start();
	}
	
	public void run() {
		float frameTime = 0;
		final int frameLimit = 10;
		long currTime;
		
		while(true){
			currTime = System.currentTimeMillis();
			game.setCurrTime(currTime);
			game.setFrameTime(frameTime);
			
			waitSubThreads();
			startSubThreads();
			
			frameTime = calcFrameTime(currTime, frameLimit);
			System.out.println(frameTime);
		}
	}
	
	public static void main(String[] args){
		new Main();
	}
	
	private static float calcFrameTime(long currTime, int frameLimit){
		
		long endTime = System.currentTimeMillis();
		float frameTime = (float) ((endTime - currTime) / 1000.0);
		
		if(endTime - currTime < frameLimit){
			
			try{
				Thread.sleep(frameLimit - (endTime - currTime));
			} catch(Exception e){}
			
			frameTime = (float) (frameLimit / 1000.0);
		}
		
		return frameTime;
	}

	public void waitSubThreads(){
		synchronized(render){
			while(render.getRunning()){
				try {
					render.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		synchronized(game){
			while(game.getRunning()){
				try {
					game.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void startSubThreads(){
		synchronized(render){
			render.setRunning(true);
			render.notify();
		}
		synchronized(game){
			game.setRunning(true);
			game.notify();
		}
	}	
}

	