package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import graphics.Render;

public class Events implements KeyListener, MouseListener, MouseMotionListener{	
	private boolean up, down, left, right, dig, jump, inv, clicked;
	private int mouseX, mouseY;
	private int selectedNum;
	private Render render;
	
	public Events(){		
		up = false;
		down = false;
		left = false;
		right = false;
		dig = false;
		jump = false;
		inv = false;
		clicked = false;
		
		mouseX = 0;
		mouseY = 0;
		selectedNum = 0;
	}
	
	public void setRender(Render setRender){
		render = setRender;
	}
	
	public boolean getUp(){
		return up;
	}
	
	public void setUp(boolean setUp){
		up = setUp;
	}
	
	public boolean getDown(){
		return down;
	}
	
	public void setDown(boolean setDown){
		down = setDown;
	}
	
	public boolean getRight(){
		return right;
	}
	
	public void setRight(boolean setRight){
		right = setRight;
	}
	
	public boolean getLeft(){
		return left;
	}
	
	public void setLeft(boolean setLeft){
		left = setLeft;
	}
	
	public boolean getJump(){
		return jump;
	}
	
	public void setJump(boolean setJump){
		jump = setJump;
	}
	
	public boolean getDig(){
		return dig;
	}
	
	public void setDig(boolean setDig){
		dig = setDig;
	}
	
	public boolean getInv(){
		return inv;
	}
	
	public void setInv(boolean setInv){
		inv = setInv;
	}
	
	public int getSelected(){
		return selectedNum;
	}
	
	public void setSelected(int setSelected){
		selectedNum = setSelected;
	}
	
	////////////Mouse Events\\\\\\\\\\\\\\
	
	public void setClicked(boolean setClicked){
		clicked = setClicked;
	}
	
	public boolean getClicked(){
		return clicked;
	}
	
	public int getMouseX(){
		return mouseX;
	}
	
	public void setMouseX(int setMouseX){
		mouseX = setMouseX;
	}
	
	public int getMouseY(){
		return mouseY;
	}
	
	public void setMouseY(int setMouseY){
		mouseY = setMouseY;
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		//A key
		if(key == 65){
			setLeft(true);
		}
		//D key
		if(key == 68){
			setRight(true);
		}
		//S key
		if(key == 83){
			setDown(true);
		}
		//Space key
		if(key == 32){
			setJump(true);
		}
		//E key
		if(key == 69){
			setDig(true);
		}
		//Inventory by Escape
		if(key == 27){
			if(getInv()){
				setInv(false);
			} else {
				setInv(true);
			}
		}
		if(key > 48 && key < 58){
			setSelected(key - 49);
		}
		if(key == 48){
			setSelected(9);
		}
		
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == 65){
			setLeft(false);
		}
		if(key == 68){
			setRight(false);
		}
		if(key == 83){
			setDown(false);
		}
		if(key == 32){
			setJump(false);
		}
		if(key == 69){
			setDig(false);
		}
	}

	public void keyTyped(KeyEvent e) {
		
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		setClicked(true);
		setMouseX(e.getX() + render.getCamX());
		setMouseY(e.getY() + render.getCamY());
	}

	public void mouseReleased(MouseEvent e) {
		setClicked(false);
		setMouseX(e.getX() + render.getCamX());
		setMouseY(e.getY() + render.getCamY());
	}

	public void mouseDragged(MouseEvent e) {
		
	}

	public void mouseMoved(MouseEvent e) {
		
	}
}
