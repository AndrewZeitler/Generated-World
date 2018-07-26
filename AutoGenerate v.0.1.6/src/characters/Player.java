package characters;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import main.Events;

public class Player{
	public int x, y;
	protected float xVel, yVel;
	private int xSpawn, ySpawn;
	
	private int[][] grid;
	private ArrayList <Entity> entities;

	private boolean facingRight, facingLeft = false;
	public int health;
	
	private long mineTime = 0;
	private long hurtTime = 0;
	private long regenTime = 0;
	
	protected float damage = 0;
	private float fallDamage = 0;
	
	private int mineDelay = 100;
	private final int regenDelay = 1000;
	private final int invulnerable = 1000;
	
	public int inventory[][] = new int[10][4];
	public int invNum[][] = new int[10][4];
	public Point invSelected = new Point(-1, -1);
	public int selected = 0;
	
	private int xInventory = inventory.length;
	private int yInventory = inventory[0].length;
	
	public Player(int xp, int yp, int hp, int[][] setGrid, ArrayList<Entity> items) {
		x = xp; y = yp;
		xSpawn = xp; ySpawn = yp;
		xVel = 0;
		yVel = 0;
		facingRight = true;
		health = hp;
		grid = setGrid;
		entities = items;
	}
	//Update all of the variables for the player
	public void update(Events events, long currTime, float frameTime){
		movement(events, currTime);
		gravity();
		collisions(frameTime);
		velocity(frameTime);
	}
	
	public void draw(Graphics2D g, int x, int y){
		g.setColor(Color.WHITE);
		g.fillRect(x, y, 8, 18);
	}
	
	private void gravity() {
		if (yVel < 5){
			yVel += 0.15;
		} else {
			fallDamage += (float) 0.4;
		}
		if(Math.abs(xVel) < 0.05){
			xVel = 0;
		} else {
			if(xVel < 0){
				xVel += 0.1;
			}
			else if(xVel > 0){
				xVel -= 0.1;
			}
		}
	}

	private void velocity(float frameTime) {
		x = (x + Math.round(xVel));
		y = (y + Math.round(yVel));
	}
	
	

	private void movement(Events events, long currTime) {
		if(events.getLeft()){
			if(xVel > -2){
				xVel -= 0.15;
			}
			facingLeft = true;
			facingRight = false;
		}
		if(events.getRight()){
			if (xVel < 2){
				xVel += 0.15;
			}
			facingLeft = false;
			facingRight = true;
		}
		if(events.getJump()){
			if(yVel == 0){
				yVel = (float) -4.5;
			}
		}
		if(events.getDown()){
			if(currTime - mineTime > mineDelay){
				try{
					removeBlock(x / 10, (y + 17) / 10 + 1);
					removeBlock((x + 7) / 10, (y + 17) / 10 + 1);
				} catch(ArrayIndexOutOfBoundsException e){}
				mineTime = currTime;
			}
		}
		if(events.getDig()){
			if(currTime - mineTime > mineDelay){
					if (facingRight){
						removeBlock((x + 7) / 10 + 1, y / 10);
						removeBlock((x + 7) / 10 + 1, y / 10 + 1);
					} else if(facingLeft){
						removeBlock(x / 10 - 1, y / 10);
						removeBlock(x / 10 - 1, y / 10 + 1);
					}
				mineTime = currTime;
			}
		}
		if(health < 100){
			if(currTime - regenTime > regenDelay){
				regenTime = currTime;
				health += 1;
			}
		}
		if (health <= 0){
			health = 100;
			x = xSpawn;
			y = ySpawn;
		}
		if(currTime - hurtTime > invulnerable){
			if(damage != 0){
				health -= damage;
				damage = 0;
				hurtTime = currTime;
			}
		} else {
			damage = 0;
		}
	}

	private void collisions(float frameTime) {
		int xAccel; int yAccel;
		//Loop through each pixel of the player
		for(int gx = x; gx <= x + 7; gx++){
			for(int gy = y; gy <= y + 17; gy++){
				//Calculate where the player's next block location will be in next frame
				xAccel = (gx + Math.round(xVel)) / 10;
				yAccel = (gy + Math.round(yVel)) / 10;
				//Try statement to catch any errors that may result if out of bounds
				try{
					//If the player jumps and hits head on block, remove it and bounce them back
					if(grid[gx / 10][yAccel] != 0){
						if(gy == y && yVel < 0){
							removeBlock(gx / 10, yAccel);
							//removeBlock((x + 7) / 10, yAccel / 10);
							yVel *= -0.1;
						} else if(gy == y + 17 && yVel > 0){
							if(yVel > 300){
								damage += fallDamage;
								fallDamage = 0;
							}
							yVel = 0;
							//yAccel = gy / 10;
						}
					}
					if(grid[xAccel][gy / 10] != 0){
						if(grid[xAccel][y / 10] == 0 && grid[xAccel][y / 10 - 1] == 0 && yVel >= 0 && yVel < 300){
								y = (y / 10 - 1) * 10;
						}else{
							xVel = 0;
							//xAccel = gx / 10;
						}
					}
					
				/*if(grid[xAccel][yAccel] != 0){
					//If the player jumps and hits head on block, remove it and bounce them back
					if(gy == y && yVel < 0){
						removeBlock(gx / 10, gy / 10 - 1);
						yVel *= -0.25;
					} else if(gy == y + 18){
						if(yVel > 300){
							damage += fallDamage;
							fallDamage = 0;
						}
						yVel = 0;
					}
				}*/
				} catch (ArrayIndexOutOfBoundsException e){
					if(xAccel < 0 || xAccel > grid.length){
						xVel = 0;
					}
					if(yAccel < 0  && yVel < 0){
						yVel *= -0.25;
					} else if(yAccel > grid[0].length  && yVel > 0){
						if(yVel > 300){
							damage += fallDamage;
							fallDamage = 0;
						}
						yVel = 0;
					}
				}
			}
		}
	}
	
	private void removeBlock(int xBlock, int yBlock){
		try{
			int block = grid[xBlock][yBlock];
			grid[xBlock][yBlock] = 0;
			if (block != 0){
				float rand = (float)Math.random();
				Entity item = new Entity(xBlock * 10, yBlock * 10, rand * 100, grid, this, block);
				entities.add(item);
				if(entities.size() > 100){
					Entity e = entities.get(0);
					entities.remove(e);
					e = null;
				}
			}
		}catch(ArrayIndexOutOfBoundsException e){}
	}
	
	public void placeBlock(int xMouse, int yMouse){
		if(inventory[selected][0] != 0){
			for (int gx = x; gx <= x + 7; gx++){
			for (int gy = y; gy <= y + 17; gy++){
				if(xMouse == (gx / 10) && yMouse == (gy / 10)){
					return;
				}
			}
			}
			if(Math.sqrt(Math.pow((x / 10 - xMouse), 2) + Math.pow((y / 10 - yMouse), 2)) < 5){
				try{
					if(grid[xMouse][yMouse] == 0){
						grid[xMouse][yMouse] = inventory[selected][0];
						removeItem(selected, 0);
					}
				}catch(ArrayIndexOutOfBoundsException e){}
			}
		}
	}
	
	public void invManage(int x, int y){
		if(invSelected.x == -1){
			try{
				System.out.println(x);

				if(inventory[x][y] != 0){
					System.out.println("HI");

					invSelected.x = x;
					invSelected.y = y;
				}
			} catch (ArrayIndexOutOfBoundsException e){}
		} else if((invSelected.x == x && invSelected.y == y)) {
			invSelected.x = -1;
			invSelected.y = -1;
		} else {
			try{
				int i = inventory[x][y];
				int iNum = invNum[x][y];
				inventory[x][y] = inventory[invSelected.x][invSelected.y];
				invNum[x][y] = invNum[invSelected.x][invSelected.y];
				inventory[invSelected.x][invSelected.y] = i;
				invNum[invSelected.x][invSelected.y] = iNum;
				invSelected.x = -1;
				invSelected.y = -1;
			} catch(ArrayIndexOutOfBoundsException e){}
		}
	}
	
	public void addItem(Entity item){
		try{
			int block = item.id;
			for(int y = 0; y < yInventory; y++){
				for(int x = 0; x < xInventory; x++){
					if(inventory[x][y] == block && invNum[x][y] < 99){
						invNum[x][y]++;
						entities.remove(item);
						item = null;
						return;
					} else if(inventory[x][y] == 0){
						inventory[x][y] = block;
						invNum[x][y]++;
						entities.remove(item);
						item = null;
						return;
					}
				}
			}
		} catch (ArrayIndexOutOfBoundsException e){}
	}
	
	public void removeItem(int x, int y){
		invNum[x][y]--;
		if(invNum[x][y] == 0){
			inventory[x][y] = 0;
		}
	}
	
	public void setSelect(int select){
		selected = select;
	}
}
