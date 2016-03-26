package graphics;

import gameplay.EnemyFormation;
import gameplay.Entity;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.Logging;

public class Screen {

	public int mapWidth;
	public int mapHeight;

	public int height;
	public int width;

	public SpriteSheet spriteSheet;

	private BufferedImage backgroundImage;
	private int[] pixels;

	private ArrayList<Entity> entities;

	/**
	 * creates object that interacts with SpriteSheet object and loads images
	 * 
	 * @param width
	 *            width of screen
	 * @param height
	 *            height of screen
	 * @param sheet
	 *            SpriteSheet object
	 */
	public Screen(int width, int height, SpriteSheet sheet) {
		this.width = width;
		this.height = height;
		this.spriteSheet = sheet;
	}

	/**
	 * sets Entity array with current entities
	 * 
	 * @param entities
	 *            Entity array
	 */
	public void setEntities(ArrayList<Entity> entities) {
		try {
			this.entities = entities;
		} catch (Exception e) {
			Logging.debug(e, "Error in" + Screen.class.getName());
		}
	}

	/**
	 * loads image to be read into a pixel array
	 * 
	 * @param backgroundPath
	 *            path to loading image
	 */
	public void loadBackground(String backgroundPath) {
		try {
			backgroundImage = ImageIO.read(Screen.class
					.getResourceAsStream(backgroundPath));
		} catch (IOException e) {
			e.printStackTrace();
			Logging.debug(e, "Error in" + Screen.class.getName());
		}

		if (backgroundImage == null) {
			return;
		}
	}

	/**
	 * @return loaded image
	 */
	public BufferedImage getImage() {
		return this.backgroundImage;
	}

	/**
	 * organizes the pixel array with the loaded image
	 * 
	 * @return int array with updated pixels
	 */
	public int[] render() {
		pixels = backgroundImage.getRGB(0, 0, width, 608, null, 0, width);

		for (Entity entity : entities) {
			int spotX = entity.getX();
			int spotY = entity.getY();
			int tileSpot;
			int yExtent;
			int yPush = 0;
			if (spotY < 0) {
				yExtent = 32 + spotY;
				tileSpot = (32 * 32) - ((32 + spotY) * 32);
			} else {
				yExtent = 32;
				tileSpot = 0;
				yPush = width * spotY;
			}
			for (int y = 0; y < yExtent; y++) {
				for (int x = 0; x < 32; x++) {
					if (isAlpha(entity.returnActiveTile()[tileSpot])) {
						pixels[(yPush) + spotX + x] = entity.returnActiveTile()[tileSpot];
					}
					tileSpot++;
				}
				spotX += width;
			}
		}

		return pixels;
	}

	/**
	 * @param color
	 *            color value to be checked
	 * @return true if color is transparent
	 */
	public boolean isAlpha(int color) {
		Color pixel = new Color(color, true);
		return pixel.getAlpha() > 0;
	}
}
