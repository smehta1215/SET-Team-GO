package states;

import graphics.Screen;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Logging;

public class ControlState extends GameState {

	private Screen screen;
	private BufferedImage controlsImage;

	private int width;
	private int height;

	int pixels[];

	public long controlsTimer;

	/**
	 * displays game controls before tutorial
	 * 
	 * @param gsm
	 *            GameStateManager
	 */
	public ControlState(GameStateManager gsm) {
		this.gsm = gsm;

		initialize();
	}

	@Override
	public void initialize() {
		screen = new Screen(gsm.getWidth(), gsm.getHeight(), null);
		screen.loadBackground("/control.png");
		this.controlsImage = screen.getImage();

		try {
			this.makePixelArray();
		} catch (Exception e) {
			e.printStackTrace();
		}

		gsm.setPixels(pixels);

		controlsTimer = System.currentTimeMillis();
	}

	public void makePixelArray() {
		width = controlsImage.getWidth();
		height = controlsImage.getHeight();

		pixels = controlsImage.getRGB(0, 0, width, height, null, 0, width);
	}

	@Override
	public void update() {
		if (System.currentTimeMillis() - this.controlsTimer >= 3500) {
			gsm.setState(gsm.TUTORIALSTATE);
		}

		gsm.setPixels(pixels);

	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(controlsImage, 0, 608, width, height, 0, 608, width,
				height, null);
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
