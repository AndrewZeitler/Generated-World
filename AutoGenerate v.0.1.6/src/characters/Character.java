package characters;

import java.awt.Graphics2D;

public abstract class Character {
	public int x;
	public int y;
	protected float xVel;
	protected float yVel;
	
	protected int grid[][];
	
	protected double hurtTime;
	protected int damage = 0;
	public int hp;
	
	public Character(int xp, int yp, int health, int[][] setGrid){
		x = xp;
		y = yp;
		xVel = 0;
		yVel = 0;
		hp = health;
		grid = setGrid;
	}
	
	public abstract void gravity();
	public abstract void velocity();
	public abstract void movement(Player player);
	public abstract void collisions();
	public abstract void update(Player player);
	public abstract void draw(Graphics2D g, int x, int y);
}
