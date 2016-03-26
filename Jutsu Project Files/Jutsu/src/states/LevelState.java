package states;

import gameplay.EnemyFormation;
import gameplay.Entity;
import gameplay.Formation;
import gameplay.Grid;
import gameplay.Samurai;
import graphics.Screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import main.Logging;

public class LevelState extends GameState {

	private Screen screen;
	private BufferedImage backgroundImage;
	private BufferedImage pauseImage;
	private BufferedImage activeBonus;
	private BufferedImage inactiveBonus;
	private ArrayList<Entity> entities;

	private Grid grid;

	public int pieceRep;

	protected Formation activePiece;
	public int newPieceSpeed;

	public String playerName;
	public int level;
	public int playerScore;
	public int playerHealth;
	public int scoreMultiplier;

	public boolean anythingChanged;

	/**
	 * Constructs level for gameplay.
	 * 
	 * @param gsm
	 *            GameStateManager
	 */
	public LevelState(GameStateManager gsm) {
		this.gsm = gsm;
	}

	@Override
	public void initialize() {
		threadController = 0;
		this.pausing = false;
		this.pauseOption = 0;
		this.anythingChanged = true;

		this.playerName = gsm.getName();
		this.playerScore = 0;
		this.playerHealth = 3000;
		this.scoreMultiplier = 1;
		this.level = 1;

		this.newPieceSpeed = 1;

		this.pieceRep = 0;

		try {
			this.pauseImage = ImageIO.read(LevelState.class
					.getResourceAsStream("/pause.png"));
			this.activeBonus = ImageIO.read(LevelState.class
					.getResourceAsStream("/activeBonus.png"));
			this.inactiveBonus = ImageIO.read(LevelState.class
					.getResourceAsStream("/inactiveBonus.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		screen = new Screen(gsm.getWidth(), gsm.getHeight(),
				gsm.getSpriteSheet());
		screen.loadBackground("/background.png");
		this.backgroundImage = screen.getImage();
		this.grid = gsm.getGrid();

		entities = new ArrayList<Entity>();

		play("resources/Jutsu8bit.wav");
		slash = null;

	}

	long timeTicker = System.currentTimeMillis();
	public long damageTicker = System.currentTimeMillis();

	public long bonusTicker = 0;
	public int bonusType;
	public boolean endBonus;
	public int[] shiftCols;

	public Clip clip;
	public Clip slash;

	/**
	 * plays the background music when gameplay starts
	 * 
	 * @param filename
	 *            path to the background music
	 */
	public void play(String filename) {
		try {
			clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File(filename)));
			clip.loop(clip.LOOP_CONTINUOUSLY);
			clip.start();
		} catch (Exception exc) {
			exc.printStackTrace(System.out);
			Logging.debug(exc, "Error in" + LevelState.class.getName());
		}
	}

	@Override
	public void update() {

		if (this.bonusActive
				&& (System.currentTimeMillis() - this.bonusTimer >= 3000)) {
			this.bonusActive = false;
		}

		if (System.currentTimeMillis() - this.bonusTimer >= 6000) {
			this.canBonus = true;
		}

		if (endBonus && (System.currentTimeMillis() - this.bonusTicker >= 9000)) {
			if (this.bonusType == 1) {
				this.scoreMultiplier = 1;
			} else if (this.bonusType == 2) {
				this.screen.loadBackground("/background.png");
			} else if (this.bonusType == 3) {
				this.newPieceSpeed = this.originalSpeed;
			}
			endBonus = false;
		}

		if (this.bonusEntities != null && this.bonusEntities.size() == 3
				&& this.checkForBonus()) {
			int[] bonusRows = new int[this.bonusEntities.size()];
			int[] bonusCols = new int[this.bonusEntities.size()];
			int spot = 0;
			for (Entity e : this.bonusEntities) {
				this.entities.remove(e);
				bonusRows[spot] = e.getRow();
				bonusCols[spot] = e.getCol();
				grid.clearLocation(e.getRow(), e.getCol());
				spot++;
			}

			Arrays.sort(bonusRows);
			Arrays.sort(bonusCols);

			if (bonusDirection) {
				int row = bonusRows[0];
				for (Entity e : this.entities) {
					if (!((EnemyFormation) this.activePiece).getEntities()
							.contains(e)) {
						if (e.getRow() < row
								&& ((e.getCol() <= bonusCols[this.bonusEntities
										.size() - 1]) && (e.getCol() >= bonusCols[0]))) {
							grid.removeFromGrid(e, e.getRow(), e.getCol());
							e.setY(e.getY() + 32);
						}
						((Samurai) e).lockInGrid();
					}
				}
			} else {
				int col = bonusCols[0];
				for (Entity e : this.entities) {
					if (!((EnemyFormation) this.activePiece).getEntities()
							.contains(e)) {
						if (e.getCol() == col && e.getRow() < bonusRows[0]) {
							grid.removeFromGrid(e, e.getRow(), e.getCol());
							e.setY(e.getY() + 32 * 3);
						}
						((Samurai) e).lockInGrid();
					}
				}

			}

			if (((Samurai) this.bonusEntities.get(0)).getType() == 1) {
				this.bonusTicker = System.currentTimeMillis();
				this.bonusType = 1;
				this.scoreMultiplier = this.level * 2;
				endBonus = true;
			}
			if (((Samurai) this.bonusEntities.get(0)).getType() == 2) {
				this.bonusTicker = System.currentTimeMillis();
				this.bonusType = 2;
				this.screen.loadBackground("/backgroundWithGrid.png");
				endBonus = true;
			}
			if (((Samurai) this.bonusEntities.get(0)).getType() == 3) {
				this.bonusTicker = System.currentTimeMillis();
				this.bonusType = 3;
				((EnemyFormation) this.activePiece).changeSpeed(1);
				this.newPieceSpeed = 1;
				this.originalSpeed = this.newPieceSpeed;
				endBonus = true;
			}
			if (((Samurai) this.bonusEntities.get(0)).getType() == 4) {
				this.bonusType = 4;
				this.playerHealth = this.playerHealth
						+ (int) ((3000 - this.playerHealth) / 2.0);
			}

			this.bonusEntities = new ArrayList<Entity>();
			this.anythingChanged = true;
		}

		if (this.playerHealth <= 0) {
			gsm.addHighScore(this.playerName, this.playerScore);
			gsm.writeToFile();
			try {
				gsm.getWriter().flush();
				gsm.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			clip.stop();
			this.gsm.setState(gsm.DEATHSTATE);
		}

		if (System.currentTimeMillis() - this.timeTicker >= 150000) {
			if (this.newPieceSpeed == 1) {
				this.newPieceSpeed = 2;
				this.level = 2;
			} else if (this.newPieceSpeed == 2) {
				this.newPieceSpeed = 4;
				this.level = 3;
			} else if (this.newPieceSpeed == 4) {
				this.newPieceSpeed = 8;
				this.level = 4;
			} else if (this.newPieceSpeed == 8) {
				this.newPieceSpeed = 16;
				this.level = 5;
			}
			timeTicker = System.currentTimeMillis();
		}

		if (System.currentTimeMillis() - this.damageTicker >= 5000) {

			this.anythingChanged = true;
			for (Entity e : entities) {
				if (!e.checkActive()) {
					this.playerHealth = this.playerHealth
							- (int) ((Math.sqrt((17.00 / e.getRow())) * ((Samurai) e)
									.getType()));
				}
			}
			this.damageTicker = System.currentTimeMillis();
		}

		if (grid.getDeleteGrid()) {
			this.deleteFromGrid();
		}

		if (!this.canRotate) {
			if ((System.currentTimeMillis() - this.timeRotated) > 160) {
				this.canRotate = true;
			}
		}

		if (activePiece == null) {
			activePiece = new EnemyFormation(gsm.getSpriteSheet(), grid,
					this.pieceRep, this.newPieceSpeed, gsm, this);
			for (Entity e : ((EnemyFormation) activePiece).getEntities()) {
				this.entities.add(e);
			}
		} else if (!activePiece.getPieceActive()) {
			this.pieceRep += 4;
			activePiece = new EnemyFormation(gsm.getSpriteSheet(), grid,
					this.pieceRep, this.newPieceSpeed, gsm, this);
			for (Entity e : ((EnemyFormation) activePiece).getEntities()) {
				this.entities.add(e);
			}

			this.playerScore += 50 * this.scoreMultiplier;
			this.anythingChanged = true;
		}

		for (Entity entity : entities) {
			if (entity.checkActive()) {
				entity.update();
			}
		}

		screen.setEntities(entities);
		gsm.setPixels(screen.render());

		this.damageTicker++;

	}

	public boolean bonusDirection;

	/**
	 * checks if player has clicked on three similar samurai together
	 * 
	 * @return true if player can get a bonus
	 */
	public boolean checkForBonus() {
		int rowChange = 0;
		int colChange = 0;

		for (int i = 1; i < 3; i++) {
			if (rowChange == 0 && colChange == 0) {
				rowChange = this.bonusEntities.get(i).getRow()
						- this.bonusEntities.get(i - 1).getRow();
				colChange = this.bonusEntities.get(i).getCol()
						- this.bonusEntities.get(i - 1).getCol();
			} else if ((!(this.bonusEntities.get(i).getRow()
					- this.bonusEntities.get(i - 1).getRow() == rowChange && this.bonusEntities
					.get(i).getCol() - this.bonusEntities.get(i - 1).getCol() == colChange))
					|| (((Samurai) this.bonusEntities.get(i)).getType() != ((Samurai) this.bonusEntities
							.get(i - 1)).getType())) {
				return false;
			}
		}
		if (rowChange == 0) {
			bonusDirection = true;
		} else {
			bonusDirection = false;
		}

		this.playerScore = this.playerScore + (100 * this.scoreMultiplier);

		return true;
	}

	@Override
	public void draw(Graphics2D g) {
		if (this.anythingChanged) {

			g.setColor(Color.BLACK);
			g.fillRect(0, 608, 544, 64);
			g.setColor(Color.CYAN);
			g.setFont(new Font("Magneto", Font.BOLD, 24));
			g.drawString(this.playerName, 40, 628 + 4);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD, 18));
			g.drawString("Score:  " + this.playerScore, 236, 622 + 4);
			g.drawString("Multiplier: x" + this.scoreMultiplier, 236 - 30,
					622 + 18 + 4);
			if (((EnemyFormation) activePiece) != null) {
				g.drawString("Level:  " + this.level, 236 - 4, 622 + 36 + 4);
			}

			g.setColor(Color.CYAN);
			g.setFont(new Font("Times New Roman", Font.BOLD, 12));
			g.drawString("health", 448, 628);
			g.drawString(this.playerHealth + " / 3000", 482, 664);
			g.setColor(Color.GRAY);
			g.drawRect(396, 634, 132, 16);
			g.setColor(Color.BLUE);
			g.fillRect(397, 635, 131, 15);
			g.setColor(Color.CYAN);
			g.fillRect(397, 635,
					(int) ((this.playerHealth / 3000.0) * (132.0)), 15);

			if (this.canBonus) {
				g.drawImage(this.activeBonus, 32, 638, null);
			} else {
				g.drawImage(this.inactiveBonus, 32, 638, null);
			}
			this.anythingChanged = false;
			g.dispose();
		}

		if (pausing) {
			g.drawImage(this.pauseImage, 144, 208, null);
			gsm.stop();
		}
	}

	public int originalSpeed;

	public boolean canRotate = true;
	public long timeRotated;

	public int threadController;

	public boolean pausing;
	public int pauseOption;

	@Override
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_DOWN || k == KeyEvent.VK_S) {
			if (((EnemyFormation) this.activePiece).getHighestY() > 0) {
				this.originalSpeed = ((EnemyFormation) this.activePiece)
						.getFormationSpeed();
				((EnemyFormation) this.activePiece).makeDrop();
			}
		} else if (k == KeyEvent.VK_E || k == KeyEvent.VK_UP) {
			if (canRotate) {
				this.activePiece.rotateRight();
				canRotate = false;
				this.timeRotated = System.currentTimeMillis();
			}
		} else if (k == KeyEvent.VK_Q) {
			if (canRotate) {
				this.activePiece.rotateLeft();
				canRotate = false;
				this.timeRotated = System.currentTimeMillis();
			}
		} else if (k == KeyEvent.VK_LEFT || k == KeyEvent.VK_A) {
			this.activePiece.changeXLocation(-1);

		} else if (k == KeyEvent.VK_RIGHT || k == KeyEvent.VK_D) {
			this.activePiece.changeXLocation(1);
		} else if (k == KeyEvent.VK_P) {
			if (this.threadController == 0) {
				this.pausing = true;
				if (gsm.getGraphics() != null) {
					this.draw(gsm.getGraphics());
				}
				this.threadController = 1;
			} else if (this.threadController == 1) {
				this.pausing = false;
				if (gsm.getGraphics() != null) {
					this.draw(gsm.getGraphics());
				}
				this.threadController = 0;
				gsm.start();
			}
		}
	}

	/**
	 * Will find the minimum value in an array.
	 * 
	 * @param numbers
	 *            array of numbers
	 * @return the minimum value from list
	 */
	public static int getMinValue(int[] numbers) {
		Arrays.sort(numbers);
		for (int i = 0; i < numbers.length; i++) {
			if (numbers[i] != -1) {
				return numbers[i];
			}
		}
		return numbers[0];
	}

	/**
	 * deletes the full rows from the screen and grid
	 */
	public void deleteFromGrid() {
		try {
			int[] rows = grid.getRowsToDelete();
			if (slash == null) {
				try {
					slash = AudioSystem.getClip();
					slash.open(AudioSystem.getAudioInputStream(new File(
							"resources/slash.wav")));
				} catch (Exception exc) {
					exc.printStackTrace(System.out);
				}
			} else {
				slash.stop();
			}
			slash.setFramePosition(0);
			slash.start();

			for (int r = 0; r < rows.length; r++) {
				if (rows[r] != -1) {

					if (((int) (this.playerHealth + entities.size() * 3)) >= 3000) {
						this.playerHealth = 3000;
					} else {
						this.playerHealth = ((int) (this.playerHealth + entities
								.size() * 3));
					}
					this.playerScore += 150 * this.scoreMultiplier;
					this.anythingChanged = true;

					int row = rows[r];
					for (int i = 1; i < 16; i++) {
						this.entities.remove(grid.getEntityGrid()[row][i]);
						grid.clearLocation(row, i);
					}

					for (Entity e : this.entities) {
						if (e.getRow() < row) {
							grid.removeFromGrid(e, e.getRow(), e.getCol());
							e.setY(e.getY() + 32);
						}
						((Samurai) e).lockInGrid();
					}
				}

			}

			if (grid.isRowFull(17)) {
				int newRow = 17;
				for (int i = 1; i < 16; i++) {
					this.entities.remove(grid.getEntityGrid()[newRow][i]);
					grid.clearLocation(newRow, i);
				}

				for (Entity e : this.entities) {
					if (e.getRow() < newRow) {
						grid.removeFromGrid(e, e.getRow(), e.getCol());
						e.setY(e.getY() + 32);
					}
					((Samurai) e).lockInGrid();
				}
			}

			grid.setDeleteGrid(false);
		} catch (Exception e) {
			Logging.debug(e, "Error in" + LevelState.class.getName());
		}
	}

	@Override
	public void keyReleased(int k) {
		if (k == KeyEvent.VK_DOWN || k == KeyEvent.VK_S) {
			((EnemyFormation) this.activePiece)
					.setFormationSpeed(this.originalSpeed);
		}
	}

	/**
	 * @return all entities on the screen
	 */
	public ArrayList<Entity> getEntities() {
		return entities;
	}

	public boolean bonusActive = false;
	public boolean canBonus = true;
	public long bonusTimer = 0;
	public ArrayList<Entity> bonusEntities;

	@Override
	public void mousePressed(MouseEvent m) {

		if (checkBonusPiece(m) && !this.bonusActive && this.canBonus) {

			int row = ((m.getY() - (m.getY() % 32)) / 32);
			int col = ((m.getX() - (m.getX() % 32)) / 32);

			this.bonusActive = true;
			this.canBonus = false;
			this.anythingChanged = true;
			this.bonusTimer = System.currentTimeMillis();
			this.bonusEntities = new ArrayList<Entity>();

			this.bonusEntities.add(grid.entityGrid[row][col]);
		} else if (checkBonusPiece(m) && this.bonusActive
				&& this.bonusEntities.size() < 4) {
			int row = ((m.getY() - (m.getY() % 32)) / 32);
			int col = ((m.getX() - (m.getX() % 32)) / 32);

			this.bonusEntities.add(grid.entityGrid[row][col]);
		}

		if (this.pausing) {
			if (m.getX() > 66 + 144 && m.getX() < 144 + 198) {

				if (m.getY() > (208 + 38) && m.getY() < (208 + 78)) {
					this.pausing = false;
					if (gsm.getGraphics() != null) {
						this.draw(gsm.getGraphics());
					}
					this.threadController = 0;
					gsm.start();
				}
				if (m.getY() > 208 + 106 && m.getY() < 208 + 144) {
					this.pausing = false;
					try {
						gsm.addHighScore(playerName, playerScore);
						gsm.writeToFile();
						gsm.getWriter().flush();
						gsm.getWriter().close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					clip.stop();
					if (gsm.getGraphics() != null) {
						this.draw(gsm.getGraphics());
					}
					this.threadController = 0;
					gsm.start();
					this.gsm.setState(gsm.MENUSTATE);
				}
				if (m.getY() > 208 + 180 && m.getY() < 208 + 220) {
					try {
						gsm.addHighScore(playerName, playerScore);
						gsm.writeToFile();
						gsm.getWriter().flush();
						gsm.getWriter().close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					clip.stop();
					System.exit(0);
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent m) {

	}

	/**
	 * checks if the spot clicked has a bonus opportunity
	 * 
	 * @param m
	 *            MouseEvent
	 * @return true if the spot clicked has a samurai capable of bonus
	 */
	public boolean checkBonusPiece(MouseEvent m) {
		int row = ((m.getY() - (m.getY() % 32)) / 32);
		int col = ((m.getX() - (m.getX() % 32)) / 32);

		if (grid.entityGrid[row][col] != null
				&& !grid.entityGrid[row][col].checkActive()) {

			return true;
		}
		return false;
	}
}
