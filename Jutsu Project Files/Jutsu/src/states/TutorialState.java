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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class TutorialState extends GameState {

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

	public boolean anythingChanged;

	/**
	 * Constructs a tutorial state for learning controls.
	 * 
	 * @param gsm
	 *            GameStateManager
	 */
	public TutorialState(GameStateManager gsm) {
		this.gsm = gsm;
	}

	@Override
	public void initialize() {
		threadController = 0;
		this.pausing = false;
		this.pauseOption = 0;
		this.anythingChanged = true;

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
		screen.loadBackground("/backgroundGray.png");
		this.backgroundImage = screen.getImage();
		this.grid = gsm.getGrid();

		entities = new ArrayList<Entity>();
	}

	long timeTicker = System.currentTimeMillis();
	public long damageTicker = System.currentTimeMillis();

	public long bonusTicker = 0;
	public int bonusType;
	public boolean endBonus;
	public int[] shiftCols;

	public int progress = 0;

	public long progressTimer = 0;

	@Override
	public void update() {

		if (this.arrowKeys == 3 && progress == 0) {
			this.progress++;
			this.anythingChanged = true;
		}

		if (this.progress == 1) {
			progressTimer = System.currentTimeMillis();
		}
		if (progress == 2 && System.currentTimeMillis() - progressTimer >= 2000) {
			this.anythingChanged = true;
			progress++;
		}
		if (progress == 4) {
			this.progressTimer = System.currentTimeMillis();
			this.anythingChanged = true;
		}
		if (progress == 5
				&& System.currentTimeMillis() - this.progressTimer >= 2000) {
			this.anythingChanged = true;
			progress++;
		}
		if (this.progress == 7) {
			this.anythingChanged = true;
			this.progressTimer = System.currentTimeMillis();

		}
		if (this.progress == 8
				&& System.currentTimeMillis() - this.progressTimer >= 2000) {
			this.anythingChanged = true;
			this.progress++;
		}
		if (this.progress == 10) {
			this.anythingChanged = true;
			this.progressTimer = System.currentTimeMillis();
		}
		if (this.progress == 11
				&& System.currentTimeMillis() - this.progressTimer >= 2000) {
			this.anythingChanged = true;
			this.progress++;
		}
		if (this.progress == 13) {
			this.anythingChanged = true;
			this.progressTimer = System.currentTimeMillis();
		}
		if (this.progress == 14
				&& System.currentTimeMillis() - this.progressTimer >= 2000) {
			this.anythingChanged = true;
			this.progress++;
			this.progressTimer = System.currentTimeMillis();
		}
		if (progress == 16
				&& System.currentTimeMillis() - this.progressTimer >= 3000) {
			this.gsm.setState(gsm.MENUSTATE);
		}

		if (this.bonusActive
				&& (System.currentTimeMillis() - this.bonusTimer >= 3000)) {
			this.bonusActive = false;
		}

		if (System.currentTimeMillis() - this.bonusTimer >= 6000) {
			this.canBonus = true;
		}

		if (endBonus && (System.currentTimeMillis() - this.bonusTicker >= 9000)) {
			if (this.bonusType == 1) {
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
			//

			if (((Samurai) this.bonusEntities.get(0)).getType() == 1) {
				this.bonusTicker = System.currentTimeMillis();
				this.bonusType = 1;
				endBonus = true;
			}
			if (((Samurai) this.bonusEntities.get(0)).getType() == 2) {
				this.bonusTicker = System.currentTimeMillis();
				this.bonusType = 2;
				this.screen.loadBackground("/backgroundGrayWGrid.png");
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
			}

			this.bonusEntities = new ArrayList<Entity>();
			this.anythingChanged = true;
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
					this.pieceRep, this.newPieceSpeed, gsm, null);
			for (Entity e : ((EnemyFormation) activePiece).getEntities()) {
				this.entities.add(e);
			}
		} else if (!activePiece.getPieceActive()) {
			this.pieceRep += 4;
			activePiece = new EnemyFormation(gsm.getSpriteSheet(), grid,
					this.pieceRep, this.newPieceSpeed, gsm, null);
			for (Entity e : ((EnemyFormation) activePiece).getEntities()) {
				this.entities.add(e);
			}

		}

		if (entities.size() >= 40) {
			this.entities.clear();
			this.grid.clearWholeGrid();
			this.activePiece = null;
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
	 * checks for single bonus to teach player
	 * 
	 * @return true if bonus occurs
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
		if (this.progress == 12) {
			this.progress = 13;
		}
		return true;
	}

	@Override
	public void draw(Graphics2D g) {
		if (this.anythingChanged) {

			g.setColor(Color.BLACK);
			g.fillRect(0, 608, 544, 64);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD, 16));

			if (this.progress == 0) {
				g.drawString("Press LEFT and RIGHT ARROW KEYS", 132, 632);
				g.drawString("to move the samurai!", 184, 652);
			}
			if (this.progress == 1) {
				g.drawString("Good job!", 242, 632);
				progress++;
			}
			if (this.progress == 3) {
				g.drawString("Hold DOWN to slide", 184 + 32, 632);
				g.drawString("the samurai down.", 184 + 32, 652);
			}
			if (this.progress == 4) {
				g.drawString("Great!", 256, 632);
				this.progress++;
			}
			if (this.progress == 6) {
				g.drawString("Press Q or E, or the UP ARROW", 134, 632);
				g.drawString("to rotate the samurai.", 134 + 44, 652);
			}
			if (this.progress == 7) {
				g.drawString("Amazing!", 256, 632);
				this.progress++;
			}
			if (this.progress == 9) {
				g.drawString("Now try and fill a ROW!", 174, 632);
			}
			if (this.progress == 10) {
				g.drawString("You rock!", 256, 632);
				this.progress++;
			}
			if (this.progress == 12) {
				g.drawString("CLICK on THREE COLORS", 153 + 12, 632 - 4);
				g.drawString("in a ROW or COLUMN", 174 + 16, 652 - 4);
				g.drawString("for a BONUS!", 184 + 26, 672 - 4);
			}
			if (this.progress == 13) {
				g.drawString("Fantastic!", 256, 632);
				this.progress++;
			}
			if (this.progress == 15) {
				g.drawString("You're ready to defend!", 176, 632);
				this.progress++;
			}

			if (this.canBonus) {
				g.drawImage(this.activeBonus, 32, 638, null);
			} else {
				g.drawImage(this.inactiveBonus, 32, 638, null);
			}
			this.anythingChanged = false;
		}

		if (pausing) {
			g.drawImage(this.pauseImage, 144, 208, null);
		}
	}

	public int originalSpeed;

	public boolean canRotate = true;
	public long timeRotated;

	public int threadController;

	public boolean pausing;
	public int pauseOption;

	public int arrowKeys = 0;

	@Override
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_DOWN || k == KeyEvent.VK_S) {
			if (((EnemyFormation) this.activePiece).getHighestY() > 0) {
				this.originalSpeed = ((EnemyFormation) this.activePiece)
						.getFormationSpeed();
				((EnemyFormation) this.activePiece).makeDrop();
				if (this.progress == 3) {
					this.progress = 4;
				}
			}
		} else if (k == KeyEvent.VK_E || k == KeyEvent.VK_UP) {
			if (canRotate) {
				this.activePiece.rotateRight();
				canRotate = false;
				this.timeRotated = System.currentTimeMillis();
				if (this.progress == 6) {
					this.progress = 7;
				}
			}
		} else if (k == KeyEvent.VK_Q) {
			if (canRotate) {
				this.activePiece.rotateLeft();
				canRotate = false;
				this.timeRotated = System.currentTimeMillis();
				if (this.progress == 6) {
					this.progress = 7;
				}
			}
		} else if (k == KeyEvent.VK_LEFT || k == KeyEvent.VK_A) {
			this.arrowKeys++;
			this.activePiece.changeXLocation(-1);

		} else if (k == KeyEvent.VK_RIGHT || k == KeyEvent.VK_D) {
			this.arrowKeys++;
			this.activePiece.changeXLocation(1);
		} else if (k == KeyEvent.VK_P) {
			if (this.threadController == 0) {
				this.pausing = true;
				if (gsm.getGraphics() != null) {
					this.draw(gsm.getGraphics());
				}
				this.threadController = 1;
				gsm.stop();
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
		int[] rows = grid.getRowsToDelete();

		for (int r = 0; r < rows.length; r++) {
			if (rows[r] != -1) {

				if (this.progress == 9) {
					this.progress = 10;
				}

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

		//
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
		//

		grid.setDeleteGrid(false);
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

					if (gsm.getGraphics() != null) {
						this.draw(gsm.getGraphics());
					}
					this.threadController = 0;
					gsm.start();
					this.gsm.setState(gsm.MENUSTATE);
				}
				if (m.getY() > 208 + 180 && m.getY() < 208 + 220) {
					try {
						gsm.writeToFile();
						gsm.getWriter().flush();
						gsm.getWriter().close();
					} catch (IOException e) {
						e.printStackTrace();
					}
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
