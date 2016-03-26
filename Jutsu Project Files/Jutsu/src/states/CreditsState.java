package states;

import graphics.Screen;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.Logging;

public class CreditsState extends GameState {

	private Screen screen;
	private BufferedImage creditsImage;

	private int width;
	private int height;

	int pixels[];

	/**
	 * creates screen for introduction and viewable credits
	 * 
	 * @param gsm
	 *            GameStateManager
	 */
	public CreditsState(GameStateManager gsm) {
		this.gsm = gsm;

		initialize();
	}

	@Override
	public void initialize() {

		screen = new Screen(gsm.getWidth(), gsm.getHeight(), null);

		if (!gsm.getFirstCreds()) {
			screen.loadBackground("/creditsScreenWMenu.png");
			this.creditsImage = screen.getImage();
		} else {
			screen.loadBackground("/creditsScreen.png");
			this.creditsImage = screen.getImage();
		}

		try {
			this.makePixelArray();
		} catch (Exception e) {
			e.printStackTrace();
		}

		gsm.setPixels(pixels);

		creditsTimer = System.currentTimeMillis();
	}

	public long creditsTimer;

	public int tog;

	@Override
	public void update() {
		if (System.currentTimeMillis() - this.creditsTimer >= 3000
				&& gsm.getFirstCreds()) {
			gsm.setState(gsm.MENUSTATE);
			gsm.setFirstCreds(false);
		}

		if (!gsm.getFirstCreds()) {
			if ((gsm.getMouseX() > 10 && gsm.getMouseX() < 141
					&& gsm.getMouseY() > 621 && gsm.getMouseY() < 661)) {

				if (tog == 1) {
					screen.loadBackground("/creditsScreenWMenuHL.png");
					this.creditsImage = screen.getImage();
					try {
						this.makePixelArray();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				tog = 0;
			} else {
				if (tog == 0) {
					screen.loadBackground("/creditsScreenWMenu.png");
					this.creditsImage = screen.getImage();
					try {
						this.makePixelArray();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				tog = 1;
			}
		}

		gsm.setPixels(pixels);
	}

	public void makePixelArray() {
		try {
			width = creditsImage.getWidth();
			height = creditsImage.getHeight();

			pixels = creditsImage.getRGB(0, 0, width, height, null, 0, width);
		} catch (Exception e) {
			Logging.debug(e, "Error in" + CreditsState.class.getName());
		}
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(creditsImage, 0, 608, width, height, 0, 608, width, height,
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
		if ((m.getX() > 10 && m.getX() < 141)
				&& (m.getY() > 621 && m.getY() < 661)) {
			gsm.setState(gsm.MENUSTATE);
			gsm.panel.toDraw = 5;
		}
	}

	@Override
	public void mouseReleased(MouseEvent m) {
		if (m.getX() > 10 && m.getX() < 141 && m.getY() > 621 && m.getY() < 661) {
			gsm.panel.toDraw = 0;
			gsm.setState(gsm.MENUSTATE);
		}
	}

}
