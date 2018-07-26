package graphics;
import java.awt.Component;

import javax.swing.JFrame;

public class Window {
	protected JFrame window;
	public Window(String name, int width, int height){
		window = new JFrame(name);
		window.setVisible(true);
		window.setSize(width, height);
		window.setFocusable(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocationRelativeTo(null);
		window.setResizable(false);
		window.toFront();
	}

	public void add(Component component) {
		window.add(component);	
	}
	
	public boolean getVisible(){
		return window.isVisible();
	}
	
	public int width(){
		return window.getWidth();
	}
	public int height(){
		return window.getHeight();
	}
}
