package main;

public class World {
	
	private int grid[][];
	private int backGrid[][];
	private int xGrid, yGrid;
	
	public World(int x, int y){
		grid = new int[x][y];
		backGrid = new int[x][y];
		xGrid = grid.length;
		yGrid = grid[0].length;
		initWorld();
	}
	
	private void initWorld(){
		//Render world pieces
		renderWorld(yGrid / 16, 1);
		renderWorld(yGrid / 8, 3);
		renderWorld(yGrid - 75, 4);
		//Caves
		spawnBlock((float)0.0003, 8, 0, yGrid, 0);
		//Dirt
		spawnBlock((float)0.0003, 4, yGrid / 8, yGrid - 150, 1);
		//Gems
		spawnBlock((float)0.00025, (float)0.6, yGrid / 8, yGrid - 75, 5);
		spawnBlock((float)0.00022, (float) 0.4, yGrid / 5, yGrid - 75, 6);
		spawnBlock((float)0.00022, (float) 0.4, yGrid / 2, yGrid - 75, 7);
		spawnBlock((float)0.00015, (float) 0.4, yGrid - 100, yGrid - 75, 8);
		int grass = yGrid / 16 + 50;
		for(int x = 0; x < xGrid; x++){
			for(int y = 0; y < grass; y++){
				if(grid[x][y] == 1){
					if(grid[x][y - 1] == 0){
						grid[x][y] = 2;
					}
				}
			}
		}
	}
	
	private void renderWorld(int height, int type){
		int y = height;
		
		int maxHeight = 5;		
		float flatBias = 50 * height / 100;
		int randMax =(int) (height * 2.38 + 1);
		float rand; float amp;

		for(int x = 0; x < xGrid; x++){
			rand = (float)(Math.random() * randMax);
			amp = (float)(Math.sqrt(Math.random() * maxHeight));
			
			if(rand + flatBias < y){
				y -= amp;
			} else if(rand - flatBias > y){
				y += amp;
			}
			try{
				grid[x][y] = type;
				backGrid[x][y] = type;
			} catch (ArrayIndexOutOfBoundsException e){
				int errorFix;
				if(y >= yGrid){
					errorFix = y - yGrid;
					y -= errorFix;
				} else if(y < 0){
					errorFix = y * -1;
					y += errorFix;
				}
			}
			below(x, y, type);
		}
	}
	
	private void below(int x, int y, int type){
		for (int gy = y; gy < yGrid; gy++){
			try{
				grid[x][gy] = type;
				backGrid[x][gy] = type;
			} catch (ArrayIndexOutOfBoundsException e){
				return;
			}
		}
	}
	
	private void spawnBlock(float spawnChance, float spawnNum, int minHeight, int maxHeight, int type){
		for(int x = 0; x < xGrid; x++){
			for(int y = 0; y < yGrid; y++){
				if(y > minHeight && y < maxHeight){
					if(grid[x][y] != 0){
						float rand = (float)Math.random();
						if(rand < spawnChance){
							grid[x][y] = type;
							spawnCave(x, y, type, 0, spawnNum + (y / yGrid * 6));
						}
					}
				}
			}
		}
	}
	
	private void spawnCave(int x, int y, int type, int depth, float spawnNum){
		float chance = (float) Math.random() * depth / spawnNum;
		float rand = (float) Math.random();
		if(rand > chance){
			int d = depth + 1;
			for(int gx = x - 2; gx <= x + 2; gx++){
				for(int gy = y - 2; gy <= y + 2; gy++){
					try{
					if(grid[gx][gy] != 0 && grid[gx][gy] != type){
					double distance = Math.sqrt(Math.pow(x - gx, 2) + Math.pow(y - gy, 2));
						if(distance <= 2){
							grid[gx][gy] = type;
							spawnCave(gx, gy, type, d, spawnNum);
						}
					}
					} catch(ArrayIndexOutOfBoundsException | StackOverflowError e){}
				}
			}
		}
	}
	
	public void matchWorld(int selectGrid[][], int selectBackGrid[][]){
		for (int x = 0; x < xGrid; x++){
			for(int y = 0; y < yGrid; y++){
				try{
					selectGrid[x][y] = grid[x][y];
					selectBackGrid[x][y] = backGrid[x][y];
				} catch (ArrayIndexOutOfBoundsException e){
					throw e;
				}
			}
		}
	}
}
