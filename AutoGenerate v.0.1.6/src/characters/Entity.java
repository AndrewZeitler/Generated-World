package characters;

public class Entity {
	public int x;
	public int y;
	protected float xVel;
	protected float yVel;
	public int id;
	
	private int grid[][];
	
	private Player player;
	
	public Entity(int xp, int yp, float xpVel, int[][] setGrid, Player p, int block){
		x = xp;
		y = yp;
		xVel = xpVel;
		yVel = 50;
		grid = setGrid;
		player = p;
		id = block;
	}
	
	public void update(float frameTime){
		gravity();
		collisions(frameTime);
		velocity(frameTime);
	}
	
	private void velocity(float frameTime) {
		x = (int)(x + (xVel * frameTime));
		y = (int)(y + (yVel * frameTime));
	}
	
	private void gravity() {
		if (yVel < 100){
			yVel += 4;
		}
		if(Math.abs(xVel) < 4){
			xVel = 0;
		} else {
			if(xVel < 0){
				xVel += 4;
			}
			else if(xVel > 0){
				xVel -= 4;
			}
		}
	}
	
	private void collisions(float frameTime) {
		int xAccel, yAccel;
		int xd, yd;
		double distance;
		int gy = y + 7;
		for(int gx = x; gx < x + 8; gx += 3){
		//Calculate where the entitie's next block location will be in next frame
		xAccel = Math.round(gx + xVel * frameTime) / 10;
		yAccel = Math.round(gy + yVel * frameTime) / 10;
		//Try statement to catch any errors that may result if out of bounds
		try{
			xd = player.x + 3 - gx;
			yd = player.y + 8 - gy;
			distance = Math.pow((xd), 2) + Math.pow(yd, 2);
			//distance is left squared to save latency so it is checked within 20 blocks and 5 is when it is taken
			if (distance < 400){
				if (distance < 25){
					player.addItem(this);
					return;
				}
				xVel += xd;
				yVel += yd / 0.5;
			}else{
				if(grid[xAccel][gy / 10] != 0){
					xVel = 0;
					xAccel = gx / 10;
				}
				if(grid[gx / 10][yAccel] != 0){
					yVel = 0;
					yAccel = gy / 10;
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