package states;

import graphics.Screen;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import main.Logging;

public class MenuState extends GameState {

	private Screen screen;
	private BufferedImage menuImage;

	private Font font;

	private int width;
	private int height;

	private int currentChoice;
	private boolean choiceHasChanged;

	int pixels[];

	/**
	 * Constructs the menu screen with options for the rest of the game.
	 * 
	 * @param gsm
	 *            GameStateManager
	 */
	public MenuState(GameStateManager gsm) {
		this.gsm = gsm;

	}

	@Override
	public void initialize() {
		screen = new Screen(gsm.getWidth(), gsm.getHeight(), null);
		screen.loadBackground("/play.png");
		this.menuImage = screen.getImage();
		font = new Font("Lucida Handwriting", Font.PLAIN, 24);

		currentChoice = 0;
		choiceHasChanged = true;

		try {
			this.makePixelArray();
		} catch (Exception e) {
			e.printStackTrace();
		}

		play("resources/JutsuPiano.wav");

	}

	public Clip clip;

	/**
	 * plays the menu theme music
	 * 
	 * @param filename
	 *            path to menu music
	 */
	public void play(String filename) {
		try {
			clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File(filename)));
			clip.loop(clip.LOOP_CONTINUOUSLY);
			clip.start();
		} catch (Exception exc) {
			exc.printStackTrace(System.out);
		}
	}

	public void makePixelArray() {
		try {
			width = menuImage.getWidth();
			height = menuImage.getHeight();

			pixels = menuImage.getRGB(0, 0, width, height, null, 0, width);
		} catch (Exception e) {
			Logging.debug(e, "Error in" + MenuState.class.getName());
		}
	}

	@Override
	public void update() {

		if (gsm.getMouseX() > 27 && gsm.getMouseX() < 160) {
			if (gsm.getMouseY() > 280 && gsm.getMouseY() < 320) {
				currentChoice = 0;
				this.choiceHasChanged = true;
			}
			if (gsm.getMouseY() > 334 && gsm.getMouseY() < 374) {
				currentChoice = 1;
				this.choiceHasChanged = true;
			}
			if (gsm.getMouseY() > 388 && gsm.getMouseY() < 428) {
				currentChoice = 2;
				this.choiceHasChanged = true;
			}
			if (gsm.getMouseY() > 442 && gsm.getMouseY() < 482) {
				currentChoice = 3;
				this.choiceHasChanged = true;
			}
			if (gsm.getMouseY() > 501 && gsm.getMouseY() < 539) {
				currentChoice = 4;
				this.choiceHasChanged = true;
			}
		}

		if (choiceHasChanged) {
			if (currentChoice == 0) {
				screen.loadBackground("/play.png");
				this.menuImage = screen.getImage();
				try {
					this.makePixelArray();
				} catch (Exception e) {
					e.printStackTrace();
				}
				gsm.setPixels(pixels);
			} else if (currentChoice == 1) {
				screen.loadBackground("/tutorial.png");
				this.menuImage = screen.getImage();
				try {
					this.makePixelArray();
				} catch (Exception e) {
					e.printStackTrace();
				}
				gsm.setPixels(pixels);
			} else if (currentChoice == 2) {
				screen.loadBackground("/scores.png");
				this.menuImage = screen.getImage();
				try {
					this.makePixelArray();
				} catch (Exception e) {
					e.printStackTrace();
				}
				gsm.setPixels(pixels);
			} else if (currentChoice == 3) {
				screen.loadBackground("/credits.png");
				this.menuImage = screen.getImage();
				try {
					this.makePixelArray();
				} catch (Exception e) {
					e.printStackTrace();
				}
				gsm.setPixels(pixels);
			} else if (currentChoice == 4) {
				screen.loadBackground("/exit.png");
				this.menuImage = screen.getImage();
				try {
					this.makePixelArray();
				} catch (Exception e) {
					e.printStackTrace();
				}
				gsm.setPixels(pixels);
			}
			this.choiceHasChanged = false;
		}

	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(menuImage, 0, 608, width, height, 0, 608, width, height,
				null);
	}

	/**
	 * performs action that directs user to their choice
	 */
	private void select() {
		clip.stop();
		if (currentChoice == 0) {
			gsm.setState(gsm.STORYONESTATE);
		}
		if (currentChoice == 1) {
			gsm.setState(gsm.CONTROLSTATE);
		}
		if (currentChoice == 2) {
			gsm.setState(gsm.SCORESSTATE);
		}
		if (currentChoice == 3) {
			gsm.setState(gsm.CREDITSSTATE);
		}
		if (currentChoice == 4) {
			clip.stop();
			try {
				gsm.writeToFile();
				gsm.getWriter().flush();
				gsm.getWriter().close();
			} catch (Exception e) {
				System.err.println("ERROR IN HANDLING");
			}
			System.exit(0);
		}
	}

	@Override
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_ENTER) {
			select();
		}
		if (k == KeyEvent.VK_UP) {
			currentChoice--;
			if (currentChoice == -1) {
				currentChoice = 4;
			}
			this.choiceHasChanged = true;
		}
		if (k == KeyEvent.VK_DOWN) {
			currentChoice++;
			if (currentChoice == 5) {
				currentChoice = 0;
			}
			this.choiceHasChanged = true;
		}

	}

	@Override
	public void keyReleased(int k) {

	}

	@Override
	public void mousePressed(MouseEvent m) {
		int mX = m.getX();
		int mY = m.getY();

		if (mX > 27 && mX < 160) {
			if (mY > 280 && mY < 320) {
				select();
			}
			if (mY > 334 && mY < 374) {
				select();
			}
			if (mY > 388 && mY < 428) {
				select();
			}
			if (mY > 442 && mY < 482) {
				select();
			}
			if (mY > 501 && mY < 539) {
				select();
			}
		}

	}

	@Override
	public void mouseReleased(MouseEvent m) {

	}
}
