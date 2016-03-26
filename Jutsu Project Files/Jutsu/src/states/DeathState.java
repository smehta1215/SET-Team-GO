package states;

import graphics.Screen;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Logging;

public class DeathState extends GameState {

	private Screen screen;
	private BufferedImage deathImage;

	private int width;
	private int height;

	int pixels[];

	/**
	 * displays game over screen when player dies
	 * 
	 * @param gsm
	 *            GameStateManager
	 */
	public DeathState(GameStateManager gsm) {
		this.gsm = gsm;

		initialize();
	}

	@Override
	public void initialize() {
		screen = new Screen(gsm.getWidth(), gsm.getHeight(), null);
		screen.loadBackground("/deathScreen.png");
		this.deathImage = screen.getImage();

		try {
			this.makePixelArray();
		} catch (Exception e) {
			e.printStackTrace();
		}

		gsm.setPixels(pixels);

		this.deathTimer = System.currentTimeMillis();
	}

	public void makePixelArray() {
		try {
			width = deathImage.getWidth();
			height = deathImage.getHeight();

			pixels = deathImage.getRGB(0, 0, width, height, null, 0, width);
		} catch (Exception e) {
			Logging.debug(e, "Error in" + DeathState.class.getName());
		}
	}

	public long deathTimer;

	@Override
	public void update() {
		if (System.currentTimeMillis() - this.deathTimer >= 7000) {
			gsm.setState(gsm.MENUSTATE);
		}

		gsm.setPixels(pixels);
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(deathImage, 0, 608, width, height, 0, 608, width, height,
				null);
	}

	@Override
	public void keyPressed(int k) {

	}

	@Override
	public void keyReleased(int k) {

	}

	@Override
	public void mousePressed(MouseEvent m) {

	}

	@Override
	public void mouseReleased(MouseEvent m) {

	}

}
