package graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Textures {
	public BufferedImage blocks[] = new BufferedImage[9];
	public BufferedImage backBlocks[] = new BufferedImage[5];
	public BufferedImage items[] = new BufferedImage[9];
	
	public Textures(){
		initPictures();
	}
	
	private void initPictures(){
		try {
		//Blocks
		File src = new File("Images/Dirt.jpg");
		blocks[1] = ImageIO.read(src);
		src = new File("Images/Grass.jpg");
		blocks[2] = ImageIO.read(src);
		src = new File("Images/Stone.jpg");
		blocks[3] = ImageIO.read(src);
		src = new File("Images/Hellstone.jpg");
		blocks[4] = ImageIO.read(src);
		src = new File("Images/Copper.jpg");
		blocks[5] = ImageIO.read(src);
		src = new File("Images/Amethyst.jpg");
		blocks[6] = ImageIO.read(src);
		src = new File("Images/Saphire.jpg");
		blocks[7] = ImageIO.read(src);
		src = new File("Images/Diamond.jpg");
		blocks[8] = ImageIO.read(src);
		//Back Blocks
		src = new File("Images/DirtWall.jpg");
		backBlocks[1] = ImageIO.read(src);
		src = new File("Images/StoneWall.jpg");
		backBlocks[3] = ImageIO.read(src);
		src = new File("Images/HellstoneWall.jpg");
		backBlocks[4] = ImageIO.read(src);
		//Items
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	private BufferedImage toCompatibleImage(BufferedImage image)
	{
		try{
	    // obtain the current system graphical settings
	    GraphicsConfiguration gfx_config = GraphicsEnvironment.
	        getLocalGraphicsEnvironment().getDefaultScreenDevice().
	        getDefaultConfiguration();

	    /*
	     * if image is already compatible and optimized for current system 
	     * settings, simply return it
	     */
	    if (image.getColorModel().equals(gfx_config.getColorModel())){
	        return image;
	    }

	    // image is not optimized, so create a new image that is
	    BufferedImage new_image = gfx_config.createCompatibleImage(
	            image.getWidth(), image.getHeight(), image.getTransparency());
	    
	    return new_image;
		}catch(Exception e){return image;}
	}
	
	public void matchPictures(BufferedImage setBlocks[], BufferedImage setBackBlocks[]){
		for(int i = 0; i < blocks.length; i++){
			toCompatibleImage(blocks[i]);
			setBlocks[i] = blocks[i];
		}
		for(int i = 0; i < backBlocks.length; i++){
			toCompatibleImage(backBlocks[i]);
			setBackBlocks[i] = backBlocks[i];
		}
	}
}
