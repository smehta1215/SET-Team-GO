package states;

import graphics.Screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Logging;

public class StoryTwoState extends GameState {

	private Screen screen;
	private BufferedImage storyTwoImage;

	private int width;
	private int height;

	int pixels[];

	/**
	 * Constructs the pre game state where player inputs name.
	 * 
	 * @param gsm
	 *            GameStateManager
	 */
	public StoryTwoState(GameStateManager gsm) {
		this.gsm = gsm;

		initialize();
	}

	@Override
	public void initialize() {
		screen = new Screen(gsm.getWidth(), gsm.getHeight(), null);
		screen.loadBackground("/storyline2.png");
		this.storyTwoImage = screen.getImage();

		try {
			this.makePixelArray();
		} catch (Exception e) {
			e.printStackTrace();
		}

		gsm.setPixels(pixels);

		for (int i = 0; i < letters.length; i++) {
			letters[i] = ' ';

		}
		spot = 0;

		anythingChanged = true;
	}

	public void makePixelArray() {
		try {
			width = storyTwoImage.getWidth();
			height = storyTwoImage.getHeight();

			pixels = storyTwoImage.getRGB(0, 0, width, height, null, 0, width);
		} catch (Exception e) {
			Logging.debug(e, "Error in" + StoryTwoState.class.getName());
		}
	}

	int last = 3;

	@Override
	public void update() {
		if (gsm.getMouseX() > 204 && gsm.getMouseX() < 342
				&& gsm.getMouseY() > 302 && gsm.getMouseY() < 344) {
			if (last != 0) {
				screen.loadBackground("/storyline2HL.png");
				this.storyTwoImage = screen.getImage();

				try {
					this.makePixelArray();
				} catch (Exception e) {
					e.printStackTrace();
				}

				this.anythingChanged = true;
				last = 0;
			}

		} else {
			if (last != 1) {
				screen.loadBackground("/storyline2.png");
				this.storyTwoImage = screen.getImage();

				try {
					this.makePixelArray();
				} catch (Exception e) {
					e.printStackTrace();
				}
				this.anythingChanged = true;
				last = 1;
			}
		}

		gsm.setPixels(pixels);

	}

	public boolean anythingChanged;
	String name;

	@Override
	public void draw(Graphics2D g) {
		if (anythingChanged) {
			name = new String(letters);

			name.trim();

			g.drawImage(storyTwoImage, 0, 608, width, height, 0, 608, width,
					height, null);

			g.setColor(Color.WHITE);
			g.setFont(new Font("VERDANA", Font.BOLD, 16));
			g.drawString("ENTER YOUR NAME", 204, 624);
			g.setFont(new Font("VERDANA", Font.BOLD, 32));
			for (int i = 0; i < letters.length; i++) {
				g.drawString(letters[i] + "", 164 + (34 * i), 660);
			}

			g.fillRect(162 + (34 * spot), 632, 3, 36);

			name.replaceAll(" ", "");

			gsm.setName(name);

			anythingChanged = false;
		}
	}

	public char[] letters = { ' ', ' ', ' ', ' ', ' ', ' ', ' ' };
	public int spot = 0;

	@Override
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_A) {
			if (spot < 7) {
				letters[spot] = 'A';
				spot++;
				this.anythingChanged = true;
			}
		}
		if (k == KeyEvent.VK_B) {
			if (spot < 7) {
				letters[spot] = 'B';
				spot++;
				this.anythingChanged = true;
			}
		}
		if (k == KeyEvent.VK_C) {
			if (spot < 7) {
				letters[spot] = 'C';
				spot++;
				this.anythingChanged = true;
			}
		}
		if (k == KeyEvent.VK_D) {
			if (spot < 7) {
				letters[spot] = 'D';
				spot++;
				this.anythingChanged = true;
			}
		}
		if (k == KeyEvent.VK_E) {
			if (spot < 7) {
				letters[spot] = 'E';
				spot++;
				this.anythingChanged = true;
			}
		}
		if (k == KeyEvent.VK_F) {
			if (spot < 7) {
				letters[spot] = 'F';
				spot++;
				this.anythingChanged = true;
			}
		}
		if (k == KeyEvent.VK_G) {
			if (spot < 7) {
				letters[spot] = 'G';
				spot++;
				this.anythingChanged = true;
			}
		}
		if (k == KeyEvent.VK_H) {
			if (spot < 7) {
				letters[spot] = 'H';
				spot++;
				this.anythingChanged = true;
			}
		}
		if (k == KeyEvent.VK_I) {
			if (spot < 7) {
				letters[spot] = 'I';
				spot++;
				this.anythingChanged = true;
			}
		}
		if (k == KeyEvent.VK_J) {
			if (spot < 7) {
				letters[spot] = 'J';
				spot++;
				this.anythingChanged = true;
			}
		}
		if (k == KeyEvent.VK_K) {
			if (spot < 7) {
				letters[spot] = 'K';
				spot++;
				this.anythingChanged = true;
			}
		}
		if (k == KeyEvent.VK_L) {
			if (spot < 7) {
				letters[spot] = 'L';
				spot++;
				this.anythingChanged = true;
			}
		}
		if (k == KeyEvent.VK_M) {
			if (spot < 7) {
				letters[spot] = 'M';
				spot++;
				this.anythingChanged = true;
			}
		}
		if (k == KeyEvent.VK_N) {
			if (spot < 7) {
				letters[spot] = 'N';
				spot++;
				this.anythingChanged = true;
			}
		}
		if (k == KeyEvent.VK_O) {
			if (spot < 7) {
				letters[spot] = 'O';
				spot++;
				this.anythingChanged = true;
			}
		}
		if (k == KeyEvent.VK_P) {
			if (spot < 7) {
				letters[spot] = 'P';
				spot++;
				this.anythingChanged = true;
			}
		}
		if (k == KeyEvent.VK_Q) {
			if (spot < 7) {
				letters[spot] = 'Q';
				spot++;
				this.anythingChanged = true;
			}
		}
		if (k == KeyEvent.VK_R) {
			if (spot < 7) {
				letters[spot] = 'R';
				spot++;
				this.anythingChanged = true;
			}
		}
		if (k == KeyEvent.VK_S) {
			if (spot < 7) {
				letters[spot] = 'S';
				spot++;
				this.anythingChanged = true;
			}
		}
		if (k == KeyEvent.VK_T) {
			if (spot < 7) {
				letters[spot] = 'T';
				spot++;
				this.anythingChanged = true;
			}
		}
		if (k == KeyEvent.VK_U) {
			if (spot < 7) {
				letters[spot] = 'U';
				spot++;
				this.anythingChanged = true;
			}
		}
		if (k == KeyEvent.VK_V) {
			if (spot < 7) {
				letters[spot] = 'V';
				spot++;
				this.anythingChanged = true;
			}
		}
		if (k == KeyEvent.VK_W) {
			if (spot < 7) {
				letters[spot] = 'W';
				spot++;
				this.anythingChanged = true;
			}
		}
		if (k == KeyEvent.VK_X) {
			if (spot < 7) {
				letters[spot] = 'X';
				spot++;
				this.anythingChanged = true;
			}
		}
		if (k == KeyEvent.VK_Y) {
			if (spot < 7) {
				letters[spot] = 'Y';
				spot++;
				this.anythingChanged = true;
			}
		}
		if (k == KeyEvent.VK_Z) {
			if (spot < 7) {
				letters[spot] = 'Z';
				spot++;
				this.anythingChanged = true;
			}
		}
		if (k == KeyEvent.VK_BACK_SPACE) {
			if (spot != 0) {
				spot--;
				letters[spot] = ' ';
				this.anythingChanged = true;
			}
		}
	}

	@Override
	public void keyReleased(int k) {

	}

	@Override
	public void mousePressed(MouseEvent m) {
		if (m.getX() > 204 && m.getX() < 342 && m.getY() > 302
				&& m.getY() < 344) {
			try {
				name.trim();
				name.replaceAll(" ", "");
				gsm.setName(name);
			} catch (Exception e) {
				Logging.debug(e, "Error in" + StoryTwoState.class.getName());
			}
			gsm.setState(gsm.LEVELSTATE);
		}
	}

	@Override
	public void mouseReleased(MouseEvent m) {

	}
}
