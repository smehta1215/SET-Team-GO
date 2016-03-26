package graphics;

import gameplay.EnemyFormation;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.Logging;

public class SpriteSheet {

	public String path;
	public int width;
	public int height;

	public int[] spriteSheetPixels;
	public int[][] spriteSheetPixels2D = new int[32 * 16][32 * 16];
	public int tileSize = 32;

	public ArrayList<int[]> basicSamuraiTiles;
	public ArrayList<int[]> medSamuraiTiles;
	public ArrayList<int[]> advSamuraiTiles;
	public ArrayList<int[]> strongSamuraiTiles;

	/**
	 * constructs SpriteSheet object with pixel tiles loaded
	 * 
	 * @param path
	 *            path to file source
	 */
	public SpriteSheet(String path) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(SpriteSheet.class.getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
			Logging.debug(e, "Error in" + SpriteSheet.class.getName());
		}

		if (image == null) {
			return;
		}

		this.path = path;
		this.width = image.getWidth();
		this.height = image.getHeight();

		spriteSheetPixels = image.getRGB(0, 0, width, height, null, 0, width);
		this.make2DArray();
		this.loadEntities();
	}

	/**
	 * makes the gathered pixels into a two dimensional observable array
	 */
	public void make2DArray() {
		try {
			int spot = 0;

			for (int r = 0; r < tileSize * 16; r++) {
				for (int c = 0; c < tileSize * 16; c++) {
					spriteSheetPixels2D[r][c] = spriteSheetPixels[spot];
					spot++;
				}
			}
		} catch (Exception e) {
			Logging.debug(e, "Error in" + SpriteSheet.class.getName());
		}
	}

	/**
	 * @param xTile
	 *            x location of tile
	 * @param yTile
	 *            y location of tile
	 * @return int array with the pixels organized
	 */
	public int[] loadSprite(int xTile, int yTile) {

		int[] tile = new int[tileSize * tileSize];
		int spot = 0;

		for (int r = yTile * tileSize; r < (yTile * tileSize) + tileSize; r++) {
			for (int c = xTile * tileSize; c < (xTile * tileSize) + tileSize; c++) {
				tile[spot] = spriteSheetPixels2D[r][c];
				spot++;
			}
		}

		return tile;
	}

	/**
	 * loads the animation tiles into each variant of enemy
	 */
	public void loadEntities() {
		try {
			basicSamuraiTiles = new ArrayList<int[]>();
			basicSamuraiTiles.add(loadSprite(0, 0));
			basicSamuraiTiles.add(loadSprite(1, 0));
			basicSamuraiTiles.add(loadSprite(2, 0));
			basicSamuraiTiles.add(loadSprite(3, 0));

			medSamuraiTiles = new ArrayList<int[]>();
			medSamuraiTiles.add(loadSprite(0, 1));
			medSamuraiTiles.add(loadSprite(1, 1));
			medSamuraiTiles.add(loadSprite(2, 1));
			medSamuraiTiles.add(loadSprite(3, 1));

			advSamuraiTiles = new ArrayList<int[]>();
			advSamuraiTiles.add(loadSprite(0, 2));
			advSamuraiTiles.add(loadSprite(1, 2));
			advSamuraiTiles.add(loadSprite(2, 2));
			advSamuraiTiles.add(loadSprite(3, 2));

			strongSamuraiTiles = new ArrayList<int[]>();
			strongSamuraiTiles.add(loadSprite(0, 3));
			strongSamuraiTiles.add(loadSprite(1, 3));
			strongSamuraiTiles.add(loadSprite(2, 3));
			strongSamuraiTiles.add(loadSprite(3, 3));
		} catch (Exception e) {
			Logging.debug(e, "Error in" + SpriteSheet.class.getName());
		}
	}

	/**
	 * @return list of the int animation arrays for the basic enemy
	 */
	public ArrayList<int[]> getBasicSamTiles() {
		return basicSamuraiTiles;
	}

	/**
	 * @return list of the int animation arrays for the medium enemy
	 */
	public ArrayList<int[]> getMedSamTiles() {
		return medSamuraiTiles;
	}

	/**
	 * @return list of the int animation arrays for the advnaced enemy
	 */
	public ArrayList<int[]> getAdvSamTiles() {
		return advSamuraiTiles;
	}

	/**
	 * @return list of the int animation arrays for the strongest enemy
	 */
	public ArrayList<int[]> getStrongSamTiles() {
		return strongSamuraiTiles;
	}
}
