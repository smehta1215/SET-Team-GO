package states;

import gameplay.EnemyFormation;
import gameplay.Grid;
import graphics.SpriteSheet;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import main.GamePanel;
import main.Logging;

public class GameStateManager {

	public BufferedWriter writer;
	public int[] scores;
	public String[] names;

	public GamePanel panel;
	public Thread thread;
	public Graphics2D g;

	public int height;
	public int width;

	private SpriteSheet spriteSheet;
	public int[] pixels;
	public Grid grid;

	private ArrayList<GameState> gameStates;
	public int currentState;
	public static final int LEVELSTATE = 0;
	public static final int CREDITSSTATE = 1;
	public static final int MENUSTATE = 2;
	public static final int DEATHSTATE = 3;
	public static final int SCORESSTATE = 4;
	public static final int TUTORIALSTATE = 5;
	public static final int CONTROLSTATE = 6;
	public static final int STORYONESTATE = 7;
	public static final int STORYTWOSTATE = 8;

	public boolean firstCredits;

	public String name;

	/**
	 * Creates object to manage states and progression of game.
	 * 
	 * @param panel
	 *            GamePanel with thread
	 * @param thread
	 *            thread from panel
	 * @param g
	 *            graphics variable
	 * @param height
	 *            screen height
	 * @param width
	 *            screen width
	 * @param pixels
	 *            pixels array
	 * @param grid
	 *            piece grid
	 */
	public GameStateManager(GamePanel panel, Thread thread, Graphics2D g,
			int height, int width, int[] pixels, Grid grid) {

		this.scores = new int[5];
		this.names = new String[5];

		this.readInData();

		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("resources/scores.txt"), "utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.panel = panel;
		this.thread = thread;
		this.g = g;
		this.grid = grid;

		this.setHeight(height);
		this.setWidth(width);
		this.setPixels(pixels);
		firstCredits = true;

		spriteSheet = new SpriteSheet("/spritesheet.png");

		gameStates = new ArrayList<GameState>();
		addAllStates();

		this.setState(CREDITSSTATE);
	}

	/**
	 * sets player name from input
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return player name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * reads in data from save file for high scores
	 */
	public void readInData() {
		try {
			InputStream ips = new FileInputStream("data/scores.txt");
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);

			for (int i = 0; i < this.scores.length; i++) {
				String str = br.readLine();
				if (str != null) {
					String[] parts = str.split(" ");
					if (parts[0].equalsIgnoreCase("null")) {
						this.names[i] = null;
					} else {
						this.names[i] = parts[0];
					}
					this.scores[i] = Integer.parseInt(parts[1]);
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			Logging.debug(e, "Error in" + GameStateManager.class.getName());
		}
	}

	/**
	 * records player name and score if the score is in the top five
	 * 
	 * @param name
	 *            player name
	 * @param score
	 *            score
	 */
	public void addHighScore(String name, int score) {
		try {
			if ((score >= scores[0])) {
				scores[0] = score;
				names[0] = name;
				int temp;
				String tempName;
				for (int i = 1; i < scores.length; i++) {
					for (int j = i; j > 0; j--) {
						if (scores[j] <= scores[j - 1]) {
							temp = scores[j];
							tempName = names[j];
							scores[j] = scores[j - 1];
							names[j] = names[j - 1];
							scores[j - 1] = temp;
							names[j - 1] = tempName;
						}
					}
				}
			}
		} catch (Exception e) {
			Logging.debug(e, "Error in" + GameStateManager.class.getName());
		}
	}

	/**
	 * writes top player names and scores to save file
	 */
	public void writeToFile() {
		try {

			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("data/scores.txt"), "utf-8"));
			for (int i = 0; i < this.scores.length; i++) {
				try {
					System.err.println(this.names[i]);
					String name = this.names[i];
					int score = this.scores[i];
					this.writer.write(name + " " + score);
					this.writer.newLine();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
			Logging.debug(e, "Error in" + GameStateManager.class.getName());
		}
		return;
	}

	/**
	 * @return BufferedWriter for file saving
	 */
	public BufferedWriter getWriter() {
		return this.writer;
	}

	/**
	 * sets whether the first credits are done or not
	 * 
	 * @param set
	 *            boolean for if introduction credits are over
	 */
	public void setFirstCreds(boolean set) {
		this.firstCredits = set;
	}

	/**
	 * @return true if first credits have appeared
	 */
	public boolean getFirstCreds() {
		return this.firstCredits;
	}

	/**
	 * @return grid with Entity pieces
	 */
	public Grid getGrid() {
		return grid;
	}

	/**
	 * resumes thread
	 */
	public synchronized void start() {
		this.panel.resume();
	}

	/**
	 * stops thread
	 */
	public synchronized void stop() {
		this.panel.stop();
	}

	/**
	 * @return 2D Graphics variable
	 */
	public Graphics2D getGraphics() {
		return this.g;
	}

	/**
	 * adds all game states to the gameStates list
	 */
	public void addAllStates() {
		try {
			gameStates.add(new LevelState(this));
			gameStates.add(new CreditsState(this));
			gameStates.add(new MenuState(this));
			gameStates.add(new DeathState(this));
			gameStates.add(new ScoresState(this));
			gameStates.add(new TutorialState(this));
			gameStates.add(new ControlState(this));
			gameStates.add(new StoryOneState(this));
			gameStates.add(new StoryTwoState(this));
		} catch (Exception e) {
			Logging.debug(e, "Error in" + GameStateManager.class.getName());
		}
	}

	/**
	 * @return SpriteSheet object
	 */
	public SpriteSheet getSpriteSheet() {
		return spriteSheet;
	}

	/**
	 * changes current state and progresses game functions
	 * 
	 * @param state
	 *            next state to set
	 */
	public void setState(int state) {
		try {
			if (state == 5) {
				grid.clearWholeGrid();
				gameStates.set(state, new TutorialState(this));

				gameStates.get(state).initialize();
				currentState = state;
			} else if (state == 6) {
				gameStates.get(state).initialize();
				currentState = state;
			} else if (state != 0) {
				currentState = state;
				gameStates.get(state).initialize();

			} else {
				grid.clearWholeGrid();
				gameStates.set(state, new LevelState(this));

				gameStates.get(state).initialize();
				currentState = state;
			}
		} catch (Exception e) {
			Logging.debug(e, "Error in" + GameStateManager.class.getName());
		}

	}

	/**
	 * @return array of top scores
	 */
	public int[] getScores() {
		return this.scores;
	}

	/**
	 * @return array of top player names
	 */
	public String[] getNames() {
		return this.names;
	}

	/**
	 * sets height of main image
	 * 
	 * @param height
	 *            desired image height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return image height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return image width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * sets image width
	 * 
	 * @param width
	 *            desired width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * sets pixel array for rendering and updates
	 * 
	 * @param pixels
	 *            pixel array
	 */
	public void setPixels(int[] pixels) {
		this.pixels = pixels;
	}

	/**
	 * @return updated pixel array
	 */
	public int[] getPixels() {
		return pixels;
	}

	/**
	 * updates active game states
	 */
	public void update() {
		if (gameStates.get(currentState) != null) {
			gameStates.get(currentState).update();
		}
	}

	/**
	 * draws the current state 2D images
	 * 
	 * @param g
	 *            2D Graphics variable
	 */
	public void draw(Graphics2D g) {
		gameStates.get(currentState).draw(g);
	}

	public void keyPressed(int k) {
		gameStates.get(currentState).keyPressed(k);
	}

	public void keyReleased(int k) {
		gameStates.get(currentState).keyReleased(k);
	}

	public void mousePressed(MouseEvent m) {
		gameStates.get(currentState).mousePressed(m);
	}

	public void mouseReleased(MouseEvent m) {
		gameStates.get(currentState).mouseReleased(m);
	}

	int mouseX;
	int mouseY;

	/**
	 * sets the current postion of mouse
	 * 
	 * @param mX
	 *            mouse x
	 * @param mY
	 *            mouse y
	 */
	public void setMouseLocs(int mX, int mY) {
		this.mouseX = mX;
		this.mouseY = mY;
	}

	/**
	 * @return mouse x coordinate
	 */
	public int getMouseX() {
		return mouseX;
	}

	/**
	 * @return mouse y coordinate
	 */
	public int getMouseY() {
		return mouseY;
	}
}
