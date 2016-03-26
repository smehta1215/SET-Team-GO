package states;

import graphics.Screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Logging;

public class ScoresState extends GameState {

	private Screen screen;
	private BufferedImage scoresImage;

	private int width;
	private int height;

	int pixels[];

	/**
	 * Constructs the high scores screen.
	 * 
	 * @param gsm
	 *            GameStateManager
	 */
	public ScoresState(GameStateManager gsm) {
		this.gsm = gsm;
	}

	boolean display;

	@Override
	public void initialize() {

		screen = new Screen(gsm.getWidth(), gsm.getHeight(), null);

		screen.loadBackground("/scoresScreen.png");
		this.scoresImage = screen.getImage();

		try {
			this.makePixelArray();
		} catch (Exception e) {
			e.printStackTrace();
		}

		gsm.setPixels(pixels);

		gsm.panel.setDraw(0);
	}

	public void makePixelArray() {
		try {
			width = scoresImage.getWidth();
			height = scoresImage.getHeight();

			pixels = scoresImage.getRGB(0, 0, width, height, null, 0, width);
		} catch (Exception e) {
			Logging.debug(e, "Error in" + ScoresState.class.getName());
		}
	}

	int last = 3;

	@Override
	public void update() {
		if (gsm.getMouseX() > 9 && gsm.getMouseX() < 140
				&& gsm.getMouseY() > 622 && gsm.getMouseY() < 662) {

			if (last != 0) {
				screen.loadBackground("/scoresScreenHL.png");
				this.scoresImage = screen.getImage();
				this.canDraw = true;
				last = 0;
			}

		} else {

			if (last != 1) {
				screen.loadBackground("/scoresScreen.png");
				this.scoresImage = screen.getImage();
				this.canDraw = true;
				last = 1;
			}

		}

	}

	public boolean canDraw = true;

	@Override
	public void draw(Graphics2D g) {
		if (canDraw) {
			g.drawImage(scoresImage, 0, 0, null);

			g.setFont(new Font("Arial", Font.BOLD, 32));
			g.setColor(Color.WHITE);
			int[] scores = gsm.getScores();
			String[] names = gsm.getNames();
			int top = 4;
			for (int i = 0; i < scores.length; i++) {
				if (names[top] != null) {
					g.drawString(names[top], 164, 256 + (i * 42));
					g.drawString("" + scores[top], 324, 256 + (i * 42));
				} else {
					g.drawString("- - - -" + "         " + "- - ", 180,
							256 + (i * 42));
				}
				top--;
			}
			this.canDraw = false;
		}
	}

	@Override
	public void keyPressed(int k) {

	}

	@Override
	public void keyReleased(int k) {

	}

	@Override
	public void mousePressed(MouseEvent m) {
		if (m.getX() > 9 && m.getX() < 140 && m.getY() > 622 && m.getY() < 662) {

			gsm.setState(gsm.MENUSTATE);
			gsm.panel.setDraw(608);
			gsm.panel.toDraw = 5;

		}
	}

	@Override
	public void mouseReleased(MouseEvent m) {

	}

}
