package graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import characters.Player;
import main.Events;
import characters.Character;
import characters.Entity;

public class Render extends JPanel implements Runnable{
	private static final long serialVersionUID = 1L;
	
	public Window window;
	
	private boolean game = false;
	private boolean isRunning = true;
	
	private int drawMinX = 0;
	private int drawMinY = 0;
	private int drawMaxX, drawMaxY;
	
	private int camX = 0;
	private int camY = 0;
	
	private int xWin, yWin;
	
	private int block, backBlock;
	private int[][] grid, backGrid;
	private int xGrid, yGrid;
	
	private BufferedImage[] blocks, backBlocks;
	
	private Player player;
	private int playerX, playerY;
	private Events events;
	
	private Character enemy;
	private ArrayList<Entity> entities;
	
	private int[][] inventory;
	private int[][] invNum;
	private int xInventory, yInventory;
	
	private Color invColor = new Color(20, 50, 200, 60);
	private Color healthColor = new Color(255, 255, 255, 180);
	private Color skyColor = new Color(50, 150, 255, 200);
	private Color selectColor = new Color(255, 255, 100, 150);
	private Color invSelectColor = new Color(0, 200, 0, 127);
	
	private int item;
	
	private BufferStrategy bs;
	private Graphics g;
	private Graphics2D g2d;
	private int scale = 2;
	
	private Font font = new Font("Impact", 0, 64);
	private Font invFont = new Font("Impact", 0, 18);
	private int xFont;
	FontMetrics metrics = getFontMetrics(invFont);
		
	public Render(Events setEvents, BufferStrategy setbs, Window setWindow){
		window = setWindow;
		Textures texture = new Textures();
		blocks = new BufferedImage[texture.blocks.length];
		backBlocks = new BufferedImage[texture.backBlocks.length];
		texture.matchPictures(blocks, backBlocks);
		
		events = setEvents;
		
		//Stores x and y window value that is halved and adjusted by the scale value
		xWin = (int) (window.width() / 2 / scale);
		yWin = (int) (window.height() / 2 / scale);
				
		bs = setbs;
		
		//g.setFont(font);
		
		Thread thrd = new Thread(this);
		thrd.start();
	}
	
	public void updateGame(Player p, Character e, int setGrid[][], int setBackGrid[][], ArrayList<Entity> items){
		xGrid = setGrid.length; yGrid = setGrid[0].length;
		
		grid = new int[xGrid][yGrid];
		backGrid = new int[xGrid][yGrid];
		
		player = p;
		enemy = e;
		grid = setGrid;
		backGrid = setBackGrid;
		inventory = player.inventory;
		invNum = player.invNum;
		xInventory = inventory.length;
		yInventory = inventory[0].length;
		entities = items;
		drawMaxX = grid.length * 10 - window.width() / scale;
		drawMaxY = grid[0].length * 10 - window.height() / scale;
	}
	
	public void run() {
		while(true){
			g = bs.getDrawGraphics();
			g2d = (Graphics2D) g;
			
			
			if(game){
				game();
			}
			g.dispose();
			bs.show();
			
			sleepThrd();
		}
	}
	
	private void updateCam(){
		
		camX = playerX - xWin;
		camY = playerY - yWin;
		
		if (camX > drawMaxX){
			camX = drawMaxX;
		} else if (camX < drawMinX){
			camX = drawMinX;
	    }
		
		if (camY > drawMaxY){
			camY = drawMaxY;
		} else if (camY < drawMinY){
			camY = drawMinY;
	    }
		
		//Scales the camera to the desired size
		g2d.scale(scale, scale);
		//Translates the camera to center on the player
		g2d.translate(-camX, -camY);
		
	}
	
	private void drawBlocks(){
		//Go through the x and y length of the blocks on screen and draw them
		for(int x = camX / 10; x < (camX + xWin * 2) / 10 + 1; x++){
			for(int y = camY / 10; y < (camY + yWin * 2) / 10; y++){
				try{
					block = grid[x][y];
					backBlock = backGrid[x][y];
				}
				catch(ArrayIndexOutOfBoundsException e){
					block = 0;
					backBlock = 0;
				}
				if (block != 0){
					g2d.drawImage(blocks[block], x * 10, y * 10, null);
				} else {
					if(backBlock != 0){
						g2d.drawImage(backBlocks[backBlock], x * 10, y * 10, null);
					}
				}
			}
		}
	}
	
	private void drawEntities(){
		//Draw entities and characters
		Entity e;
		int size = entities.size();
		g2d.setColor(Color.BLACK);
		for(int i = 0; i < size; i++){
			e = entities.get(i);
			g2d.drawRect(e.x, e.y, 10, 10);
			g2d.drawImage(blocks[e.id], e.x, e.y, null);		
		}
		
		player.draw(g2d, playerX, playerY);
		enemy.draw(g2d, enemy.x, enemy.y);
		
	}
	
	private void drawInventory(){
		
		invFont = new Font("Impact", 0, 18 / scale);
		g.setFont(invFont);
		//Draw the inventory if the player has it open
		if(events.getInv()){
			//Go through inventory x length
			for(int x = 0; x < xInventory; x++){
				//Go through inventory y length
				for(int y = 1; y < yInventory; y++){
					g2d.setColor(invColor);
					g2d.fillRoundRect(camX + (x * 85 + 10) / scale, camY + (y * 85 + 10) / scale, 80 / scale, 80 / scale, 20 / scale, 20 / scale);
					item = inventory[x][y];
					if(item != 0){
						g2d.drawImage(blocks[item], camX + (x * 85 + 30) / scale, camY + (y * 85 + 30) / scale, 40 / scale, 40 / scale, null);
						g2d.setColor(Color.WHITE);
						xFont = metrics.stringWidth(Integer.toString(invNum[x][0]));
						g2d.drawString(Integer.toString(invNum[x][y]), camX + (x * 85 + 80 - xFont) / scale, (y * 85 + 75) / scale);
					}
				}
			}
			if(player.invSelected.x != -1){
				g2d.setColor(invSelectColor);
				g2d.fillRoundRect((player.invSelected.x * 85 + 10) / scale, (player.invSelected.y * 85 + 10) / scale, 80 / scale, 80 / scale, 20, 20);
			}
		}
		//Draw hot-bar portion of inventory
		//Go through inventory x length
		for(int x = 0; x < xInventory; x++){
			g2d.setColor(invColor);
			if(x == player.selected){
				g2d.setColor(selectColor);
			}
			g2d.fillRoundRect(camX + (x * 85 + 10) / scale, camY + 10 / scale, 80 / scale, 80 / scale, 20 / scale, 20 / scale);
			item = inventory[x][0];
			if(item != 0){
				g2d.drawImage(blocks[item], camX + (x * 85 + 30) / scale, camY + 30 / scale, 40 / scale, 40 / scale, null);
				g2d.setColor(Color.WHITE);
				xFont = metrics.stringWidth(Integer.toString(invNum[x][0]));
				g2d.drawString(Integer.toString(invNum[x][0]), camX + (x * 85 + 80 - xFont) / scale, camY + 75 / scale);
			}
		}
		
	}
	
	private void game(){
		//Draw here
		playerX = player.x;
		playerY = player.y;
		
		updateCam();
		
		g2d.setBackground(skyColor);
		g2d.clearRect(camX, camY, xWin * 2, yWin * 2);
		
		drawBlocks();
		drawEntities();
		drawInventory();
		
		g2d.setColor(healthColor);
		font = new Font("Impact", 0, 64 / scale);
		g2d.setFont(font);
		g2d.drawString(Integer.toString(player.health), camX + xWin * 2 - 150 / scale, camY + 75 / scale);
		
		//Finish here
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
	
	public void setGame(boolean run){
		game = run;
	}
	
	public boolean getGame(){
		return game;
	}
	
	public int getCamX(){
		return camX;
	}
	public int getCamY(){
		return camY;
	}
	
}
