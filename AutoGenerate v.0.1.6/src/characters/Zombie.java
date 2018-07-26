package characters;

import java.awt.Color;
import java.awt.Graphics2D;

public class Zombie extends Character{
	
	private boolean dig;
	private boolean hit = false;
	
	public Zombie(int xp, int yp, int health, int[][] grid) {
		super(xp, yp, health, grid);
	}

	public void gravity() {
		if (yVel < 5){
			yVel += 0.15;
		}
		if(Math.abs(xVel) < 0.05){
			xVel = 0;
		} else {
			if(xVel < 0){
				xVel += 0.02;
			}
			else if(xVel > 0){
				xVel -= 0.02;
			}
		}
	}

	public void velocity() {
		x = x +  Math.round(xVel);
		y = y +  Math.round(yVel);
	}
	
	public void movement(Player player) {
		int px = (int) (player.x + player.xVel); int py = (int) (player.y + player.yVel);
		if(dig){
			if(x != px){
				try{
				if(x > px){
					grid[(x / 10) - 1][y / 10] = 0;
					grid[(x / 10) - 1][(y / 10) + 1] = 0;
				} else if(x + 7 < px){
					grid[((x + 7) / 10) + 1][y / 10] = 0;
					grid[((x + 7) / 10) + 1][(y / 10) + 1] = 0;
				}
				}catch(ArrayIndexOutOfBoundsException e){}
			}
		}
		if(xVel == 0){
			if(yVel == 0){
				if(y == py){
					yVel -= 4.5;
				}
				dig = true;
			}
		}
		if(x > px){
			if(xVel > -2){
				xVel -= 0.05;
			}
		} else if(x < px){
			if(xVel < 2){
				xVel += 0.05;
			}
		}
		if(x > px - 20 && x < px + 27){
			if(y > py + 17){
				if(yVel == 0){
					yVel -= 4.5;
				}
			} else if(y + 17 < py){
				if(yVel < 5){
					yVel += 0.1;
					try{
					grid[x / 10][((y + 17) / 10) + 1] = 0;
					grid[(x + 7) / 10][((y + 17) / 10) + 1] = 0;
					} catch (ArrayIndexOutOfBoundsException e){}
				}
			}
		}
		if(x >= px && x <= px + 7){
			if(y >= py && y <= py + 17){
				hit = true;
			} else if(y + 17 >= py && y + 17 <= py + 17){
				hit = true;
			}
		} else if(x + 7 >= px && x + 7 <= px + 7){
			if(y >= py && y <= py + 17){
				hit = true;
			} else if(y + 17 >= py && y + 17 <= py + 17){
				hit = true;
			}
		}
		if(hit){
			player.damage += 20;
			player.yVel += 1;
			if(xVel > 0){
				player.xVel += 2;
			} else {
				player.xVel -= 2;
			}
			xVel *= -1;
			yVel *= -1;
			hit = false;
		}
	}

	public void collisions() {
		int xAccel; int yAccel;
		
		/*for(int gx = x; gx <= x + 7; gx++){
			for(int gy = y; gy <= y + 17; gy++){
				xAccel = gx + Math.round(xVel * frameTime) / 10;
				yAccel = gy + Math.round(yVel * frameTime) / 10;
				try{
					if(grid[xAccel][gy / 10] != 0){
						if(gy == y + 17){
							if(grid[xAccel][gy / 10 - 1] == 0 && grid[xAccel][gy / 10 - 2] == 0){
								y = (gy / 10 - 1) * 10 - 9;
							}
						}else {
							xVel = 0;
						}
					}
					if(grid[gx / 10][yAccel] != 0){
						if(gy == y && yVel < 0){
							grid[gx / 10][gy / 10 - 1] = 0;
						}
						yVel = 0;
					}
					if(grid[xAccel][yAccel] != 0){
						if(gy == y && yVel < 0){
							grid[gx / 10][gy / 10 - 1] = 0;
							yVel = 0;
						} else if(gy == y + 17){
							if(grid[xAccel][yAccel - 1] == 0 && grid[xAccel][yAccel - 2] == 0){
								y = (yAccel - 1) * 10 - 9;
							}
							yVel = 0;
						}
					}
				} catch (ArrayIndexOutOfBoundsException e){
					if(xAccel < 0 || xAccel > grid.length){
						xVel = 0;
					}
					if(yAccel < 0  && yVel < 0){
						yVel *= -0.25;
					} else if(yAccel > grid[0].length  && yVel > 0){
						yVel = 0;
					}
				}
			}
		} */
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
							grid[gx / 10][yAccel] = 0;
						} else {
							yVel = 0;
						}
					}
					if(grid[xAccel][gy / 10] != 0){
						if(gy == y + 17){
							if(grid[xAccel][gy / 10 - 1] == 0 && grid[xAccel][gy / 10 - 2] == 0){
								y = (gy / 10 - 1) * 10 - 9;
							}
						}else {
							xVel = 0;
						}
					}
				} catch (ArrayIndexOutOfBoundsException e){
					if(xAccel < 0 || xAccel > grid.length){
						xVel = 0;
					}
					if(yAccel < 0  && yVel < 0){
						yVel *= -0.25;
					} else if(yAccel > grid[0].length  && yVel > 0){
						yVel = 0;
					}
				}
			}
		}
	}

	public void draw(Graphics2D g, int x, int y) {
		g.setColor(Color.BLACK);
		g.fillRect(x, y, 8, 18);
	}

	public void update(Player player) {
		movement(player);
		gravity();
		collisions();
		velocity();
	}
}
