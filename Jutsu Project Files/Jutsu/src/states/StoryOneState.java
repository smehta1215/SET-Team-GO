package states;

import graphics.Screen;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Logging;

public class StoryOneState extends GameState {

	private Screen screen;
	private BufferedImage storyOneImage;

	private int width;
	private int height;

	int pixels[];

	public long controlsTimer;

	/**
	 * Constructs the intial story stage before inputting player information.
	 * 
	 * @param gsm
	 *            GameStateManager
	 */
	public StoryOneState(GameStateManager gsm) {
		this.gsm = gsm;

		initialize();
	}

	@Override
	public void initialize() {
		screen = new Screen(gsm.getWidth(), gsm.getHeight(), null);
		screen.loadBackground("/storyline1.png");
		this.storyOneImage = screen.getImage();

		try {
			this.makePixelArray();
		} catch (Exception e) {
			e.printStackTrace();
		}

		gsm.setPixels(pixels);

		controlsTimer = System.currentTimeMillis();
	}

	public void makePixelArray() {
		try {
			width = storyOneImage.getWidth();
			height = storyOneImage.getHeight();

			pixels = storyOneImage.getRGB(0, 0, width, height, null, 0, width);
		} catch (Exception e) {
			Logging.debug(e, "Error in" + StoryOneState.class.getName());
		}
	}

	@Override
	public void update() {
		if (System.currentTimeMillis() - this.controlsTimer >= 7000) {
			gsm.setState(gsm.STORYTWOSTATE);
		}

		gsm.setPixels(pixels);

	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(storyOneImage, 0, 608, width, height, 0, 608, width,
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
