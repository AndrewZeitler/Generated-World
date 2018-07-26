package main;

import java.util.ArrayList;

//import Write;
import characters.*;
import characters.Character;
import characters.Entity;
import graphics.Render;

public class Game implements Runnable{
	
	//private Write write;
	
	private boolean isRunning = true;
	
	private int[][] grid, backGrid;
	private int xGrid, yGrid;
	
	private Player player;
	private Events events;
		
	private long currTime;
	private float frameTime = 0;
	
	private ArrayList<Entity> entities = new ArrayList<Entity>(0);
	private Character enemy;
		
	//String bip = "file.mp3";
	//Media hit = new Media(bip);
	//MediaPlayer mediaPlayer = new MediaPlayer(hit);
	
	///////////////////////////////////////// Initialization \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	
	public Game(Render render, Events setEvents){
		initWorld();
		events = setEvents;
		render.updateGame(player, enemy, grid, backGrid, entities);
		render.setGame(true);
		
		Thread thrd = new Thread(this);
		thrd.start();
	}
	
	private int findGround(int x, int yBase){
		for(int y = yBase; y < yGrid; y++){
			try{
				if(grid[x][y] != 0){
					if(grid[x][y - 2] == 0){
						return y - 2;
					}
				}
			} catch (ArrayIndexOutOfBoundsException e){}
		}
		return 0;
	}
	
	protected void initWorld(){
		grid = new int[1000][400];
		xGrid = grid.length;
		yGrid = grid[0].length;
		backGrid = new int[xGrid][yGrid];
		World world = new World(xGrid, yGrid);
		
		try{
			world.matchWorld(grid, backGrid);
		} catch (Exception e){
			e.printStackTrace();
		}
		
		player = new Player((xGrid / 2) * 10, findGround(xGrid / 2, 0) * 10, 100, grid, entities);
		enemy = new Zombie((xGrid - 100) * 10, findGround(xGrid - 100, 0) * 10, 100, grid);
		
		/*write = new Write(grid, player);
		try {
			write.writeWorld();
			write.writePlayer();
		} catch (IOException e) {} */
	}
	///////////////////////////////////////////////////// Game \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	
	public void run() {
		while(true){
			doLogic();	
			sleepThrd();
		}
	}
	
	private void doLogic(){
		player.update(events, currTime, frameTime);
		enemy.update(player);
		
		Entity e;
		placeBlock();
		for(int i = 0; i < entities.size(); i++){
			e = entities.get(i);
			e.update(frameTime);
		}
	}
	
	private void placeBlock(){
		if(events.getClicked()){
			int x, y;
			if(events.getInv()){
				x = (events.getMouseX() - 10) / 85;
				y = (events.getMouseY() - 10) / 85;
				player.invManage(x, y);
			} else {
				x = events.getMouseX() / 10;
				y = events.getMouseY() / 10;
				if(grid[x][y] == 0){
					player.placeBlock(x, y);
				}
			}
			events.setClicked(false);
		}
	}
	
	public synchronized void sleepThrd(){
		isRunning = false;
		notify();
			
		while(!isRunning){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean getRunning(){
		return isRunning;
	}
	
	public void setRunning(boolean run){
		isRunning = run;
	}
	
	public void setCurrTime(long setCurrTime){
		currTime = setCurrTime;
	}
	
	public void setFrameTime(float setFrameTime){
		frameTime = setFrameTime;
	}
}
