package gameplay;

import graphics.SpriteSheet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import main.Logging;
import states.GameStateManager;
import states.LevelState;

public class EnemyFormation extends Formation {

	private int centerX;
	private int centerY;

	public int formationSpeed;

	public int pieceRep;

	private SpriteSheet spriteSheet;
	private Grid grid;
	private GameStateManager gsm;
	private LevelState levelState;

	private ArrayList<Entity> entities;

	Entity one, two, three, four;

	/**
	 * Constructs a puzzle object of samurai.
	 * 
	 * @param spriteSheet
	 *            object with sprite values
	 * @param grid
	 *            object with entity locations
	 * @param pieceRep
	 *            puzzle piece ID incrementer
	 * @param initialSpeed
	 *            speed of the falling piece
	 * @param gsm
	 *            GameStateManager
	 * @param state
	 *            current state, LevelState
	 */
	public EnemyFormation(SpriteSheet spriteSheet, Grid grid, int pieceRep,
			int initialSpeed, GameStateManager gsm, LevelState state) {

		this.spriteSheet = spriteSheet;
		this.grid = grid;
		this.pieceRep = pieceRep;
		this.gsm = gsm;
		this.levelState = state;

		this.entities = new ArrayList<Entity>();

		initialize();

		this.setFormationSpeed(initialSpeed);
	}

	/**
	 * creates puzzle orientation, speed, location and initial values
	 */
	public void initialize() {
		try {
			this.pieceActive = true;
			grid.setCurrentFormation(this);

			centerX = randInt(2, 13) * 32;
			centerY = -64;

			this.highestY = centerY - 32;

			int formationType = randInt(1, 7);

			while (formationType == grid.getLastFormation()) {
				formationType = randInt(1, 7);
			}

			grid.setLastFormation(formationType);

			if (formationType == 1) {
				this.makeTFormation();
			} else if (formationType == 2) {
				this.makeZFormation();
			} else if (formationType == 3) {
				this.makeRevZFormation();
			} else if (formationType == 4) {
				this.makeSquareFormation();
			} else if (formationType == 5) {
				this.makeLineFormation();
			} else if (formationType == 6) {
				this.makeLFormation();
			} else if (formationType == 7) {
				this.makeRevLFormation();
			}

		} catch (Exception e) {
			Logging.debug(e, "Error in" + EnemyFormation.class.getName());
		}
	}

	/**
	 * creates a backward L puzzle formation
	 */
	public void makeRevLFormation() {
		try {
			one = new Samurai(spriteSheet, centerX + 32, centerY - 32, grid,
					pieceRep, 0, 2, this.randInt(1, 4));
			two = new Samurai(spriteSheet, centerX + 32, centerY, grid,
					pieceRep + 1, -1, 1, this.randInt(1, 4));
			three = new Samurai(spriteSheet, centerX + 32, centerY + 32, grid,
					pieceRep + 2, -2, 0, this.randInt(1, 4));
			four = new Samurai(spriteSheet, centerX, centerY + 32, grid,
					pieceRep + 3, -1, -1, this.randInt(1, 4));

			entities.add(one);
			entities.add(four);
			entities.add(three);
			entities.add(two);
		} catch (Exception e) {
			Logging.debug(e, "Error in" + EnemyFormation.class.getName());
		}
	}

	/**
	 * creates an L puzzle formation
	 */
	public void makeLFormation() {
		try {
			one = new Samurai(spriteSheet, centerX - 32, centerY - 32, grid,
					pieceRep, 2, 0, this.randInt(1, 4));
			two = new Samurai(spriteSheet, centerX - 32, centerY, grid,
					pieceRep + 1, 1, -1, this.randInt(1, 4));
			three = new Samurai(spriteSheet, centerX - 32, centerY + 32, grid,
					pieceRep + 2, 0, -2, this.randInt(1, 4));
			four = new Samurai(spriteSheet, centerX, centerY + 32, grid,
					pieceRep + 3, -1, -1, this.randInt(1, 4));

			entities.add(one);
			entities.add(four);
			entities.add(three);
			entities.add(two);
		} catch (Exception e) {
			Logging.debug(e, "Error in" + EnemyFormation.class.getName());
		}
	}

	/**
	 * creates a line puzzle formation
	 */
	public void makeLineFormation() {
		try {
			one = new Samurai(spriteSheet, centerX - 32, centerY - 32, grid,
					pieceRep, 2, 0, this.randInt(1, 4));
			two = new Samurai(spriteSheet, centerX, centerY - 32, grid,
					pieceRep + 1, 1, 1, this.randInt(1, 4));
			three = new Samurai(spriteSheet, centerX + 32, centerY - 32, grid,
					pieceRep + 2, 0, 2, this.randInt(1, 4));
			four = new Samurai(spriteSheet, centerX + 64, centerY - 32, grid,
					pieceRep + 3, -1, 3, this.randInt(1, 4));

			entities.add(one);
			entities.add(four);
			entities.add(three);
			entities.add(two);
		} catch (Exception e) {
			Logging.debug(e, "Error in" + EnemyFormation.class.getName());
		}
	}

	/**
	 * creates square formation
	 */
	public void makeSquareFormation() {
		try {
			one = new Samurai(spriteSheet, centerX - 32, centerY - 32, grid,
					pieceRep, 2, 0, this.randInt(1, 4));
			two = new Samurai(spriteSheet, centerX, centerY - 32, grid,
					pieceRep + 1, 1, 1, this.randInt(1, 4));
			three = new Samurai(spriteSheet, centerX, centerY, grid,
					pieceRep + 2, 0, 0, this.randInt(1, 4));
			four = new Samurai(spriteSheet, centerX - 32, centerY, grid,
					pieceRep + 3, 1, -1, this.randInt(1, 4));

			entities.add(one);
			entities.add(four);
			entities.add(three);
			entities.add(two);
		} catch (Exception e) {
			Logging.debug(e, "Error in" + EnemyFormation.class.getName());
		}
	}

	/**
	 * creates a reverse Z puzzle formation
	 */
	public void makeRevZFormation() {
		try {
			one = new Samurai(spriteSheet, centerX + 32, centerY - 32, grid,
					pieceRep, 0, 2, this.randInt(1, 4));
			two = new Samurai(spriteSheet, centerX, centerY - 32, grid,
					pieceRep + 1, 1, 1, this.randInt(1, 4));
			three = new Samurai(spriteSheet, centerX, centerY, grid,
					pieceRep + 2, 0, 0, this.randInt(1, 4));
			four = new Samurai(spriteSheet, centerX - 32, centerY, grid,
					pieceRep + 3, 1, -1, this.randInt(1, 4));

			entities.add(one);
			entities.add(four);
			entities.add(three);
			entities.add(two);
		} catch (Exception e) {
			Logging.debug(e, "Error in" + EnemyFormation.class.getName());
		}
	}

	/**
	 * creates a T puzzle formation
	 */
	public void makeTFormation() {
		try {
			one = new Samurai(spriteSheet, centerX - 32, centerY - 32, grid,
					pieceRep, 2, 0, this.randInt(1, 4));
			two = new Samurai(spriteSheet, centerX, centerY - 32, grid,
					pieceRep + 1, 1, 1, this.randInt(1, 4));
			three = new Samurai(spriteSheet, centerX + 32, centerY - 32, grid,
					pieceRep + 2, 0, 2, this.randInt(1, 4));
			four = new Samurai(spriteSheet, centerX, centerY, grid,
					pieceRep + 3, 0, 0, this.randInt(1, 4));

			entities.add(one);
			entities.add(four);
			entities.add(three);
			entities.add(two);
		} catch (Exception e) {
			Logging.debug(e, "Error in" + EnemyFormation.class.getName());
		}
	}

	/**
	 * creates a Z puzzle formation
	 */
	public void makeZFormation() {
		try {
			one = new Samurai(spriteSheet, centerX - 32, centerY - 32, grid,
					pieceRep, 2, 0, this.randInt(1, 4));
			two = new Samurai(spriteSheet, centerX, centerY - 32, grid,
					pieceRep + 1, 1, 1, this.randInt(1, 4));
			three = new Samurai(spriteSheet, centerX, centerY, grid,
					pieceRep + 2, 0, 0, this.randInt(1, 4));
			four = new Samurai(spriteSheet, centerX + 32, centerY, grid,
					pieceRep + 3, -1, 1, this.randInt(1, 4));

			entities.add(one);
			entities.add(four);
			entities.add(three);
			entities.add(two);
		} catch (Exception e) {
			Logging.debug(e, "Error in" + EnemyFormation.class.getName());
		}
	}

	/**
	 * @param highY
	 *            y coordinate of the highest piece
	 */
	public void setHighestY(int highY) {
		try {
			this.highestY = highY;
		} catch (Exception e) {
			Logging.debug(e, "Error in" + EnemyFormation.class.getName());
		}
	}

	/**
	 * @return highest y coordinate
	 */
	public int getHighestY() {
		return this.one.getY();
	}

	/**
	 * @return speed of current puzzle piece
	 */
	public int getFormationSpeed() {
		return this.formationSpeed;
	}

	/**
	 * sets speed of all pieces in puzzle formation
	 * 
	 * @param speed
	 *            desired speed value
	 */
	public void setFormationSpeed(int speed) {
		try {
			this.formationSpeed = speed;

			one.setSpeed(formationSpeed);
			two.setSpeed(formationSpeed);
			three.setSpeed(formationSpeed);
			four.setSpeed(formationSpeed);
		} catch (Exception e) {
			Logging.debug(e, "Error in" + EnemyFormation.class.getName());
		}
	}

	/**
	 * @return list of Entity objects in puzzle
	 */
	public ArrayList<Entity> getEntities() {
		return entities;
	}

	/**
	 * @param min
	 *            minimum bound of random number
	 * @param max
	 *            maximum bound of random number
	 * @return random value between min and max
	 */
	public int randInt(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	@Override
	public void disableAll() {
		try {
			one.setActive(false);
			two.setActive(false);
			three.setActive(false);
			four.setActive(false);
		} catch (Exception e) {
			Logging.debug(e, "Error in" + EnemyFormation.class.getName());
		}
	}

	@Override
	public void changeXLocation(int xDir) {
		try {
			if (one.getY() > 0) {
				boolean edge = true;

				for (Entity e : entities) {
					if (e.getX() >= 480 && xDir == 1) {
						edge = false;
					}
					if (e.getX() <= 32 && xDir == -1) {
						edge = false;
					}
				}

				for (Entity e : entities) {
					if (e.getY() % 32 == 0 && !grid.canMoveHorz(e, xDir, 0)) {
						edge = false;
					} else if (!grid.canMoveHorz(e, xDir, 1)) {
						edge = false;
					}
				}

				if (edge) {
					for (Entity e : entities) {
						((Samurai) (e)).setHorzMove(true);
						((Samurai) (e)).setXDir(xDir);
					}
				}
			}
		} catch (Exception e) {
			Logging.debug(e, "Error in" + EnemyFormation.class.getName());
		}
	}

	/**
	 * identification of last rotation
	 */
	public int lastRot = 0;

	@Override
	public void rotateRight() {
		try {
			if (lastRot == 2) {
				for (Entity e : entities) {
					((Samurai) (e)).swapRotDirToRight();
				}
			}

			if (one.getY() > 0) {
				boolean coll = true;
				for (Entity e : entities) {
					int xDir = ((Samurai) (e)).getXRotID();
					int yDir = ((Samurai) (e)).getYRotID();
					if (e.getX() + (xDir * 32) >= 480
							|| e.getX() + (xDir * 32) <= 32) {
						coll = false;
					}
					if (e.getY() + (yDir * 32) >= 544) {
						coll = false;
					}
					if (e.getY() % 32 == 0 && !grid.canMoveHorz(e, xDir, yDir)) {
						coll = false;
					} else if (!grid.canMoveHorz(e, xDir, yDir + 1)) {
						coll = false;
					}
				}
				if (coll) {
					for (Entity e : entities) {
						((Samurai) (e)).setRotRight(true);
					}
				}

			}
			this.lastRot = 1;
		} catch (Exception e) {
			Logging.debug(e, "Error in" + EnemyFormation.class.getName());
		}
	}

	@Override
	public void lockInAll() {
		try {
			for (Entity e : entities) {
				((Samurai) (e)).lockInGrid();
			}
		} catch (Exception e) {
			Logging.debug(e, "Error in" + EnemyFormation.class.getName());
		}
	}

	@Override
	public void rotateLeft() {
		try {
			if (lastRot == 1 || lastRot == 0) {
				for (Entity e : entities) {
					((Samurai) (e)).swapRotDirToLeft();
				}
			}

			if (one.getY() > 0) {
				boolean coll = true;
				for (Entity e : entities) {
					int xDir = ((Samurai) (e)).getXRotID();
					int yDir = ((Samurai) (e)).getYRotID();
					if (e.getX() + (xDir * 32) >= 480
							|| e.getX() + (xDir * 32) <= 32) {
						coll = false;
					}
					if (e.getY() + (yDir * 32) >= 544) {
						coll = false;
					}
					if (e.getY() % 32 == 0 && !grid.canMoveHorz(e, xDir, yDir)) {
						coll = false;
					} else if (!grid.canMoveHorz(e, xDir, yDir + 1)) {
						coll = false;
					}
				}
				if (coll) {
					for (Entity e : entities) {
						((Samurai) (e)).setRotLeft(true);
					}
				}

			}
			this.lastRot = 2;
		} catch (Exception e) {
			Logging.debug(e, "Error in" + EnemyFormation.class.getName());
		}
	}

	/**
	 * makes puzzle piece drop
	 */
	public void makeDrop() {
		try {
			for (Entity e : this.entities) {
				((Samurai) (e)).setDrop(true);
			}
		} catch (Exception e) {
			Logging.debug(e, "Error in" + EnemyFormation.class.getName());
		}
	}

	/**
	 * changes speed of piece while falling
	 * 
	 * @param newSpeed
	 *            desired speed
	 */
	public void changeSpeed(int newSpeed) {
		try {
			for (Entity e : this.entities) {
				((Samurai) (e)).setChangeSpeed(true, newSpeed);
			}
		} catch (Exception e) {
			Logging.debug(e, "Error in" + EnemyFormation.class.getName());
		}
	}

	/**
	 * checks if pieces have hit top of screen
	 */
	public void checkForGameOver() {
		try {
			if (one.getY() <= 0 || two.getY() <= 0 || three.getY() <= 0
					|| four.getY() <= 0) {
				gsm.addHighScore(this.levelState.playerName,
						this.levelState.playerScore);
				gsm.writeToFile();
				try {
					gsm.getWriter().flush();
					gsm.getWriter().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				this.levelState.clip.stop();
				gsm.setState(gsm.DEATHSTATE);
			}
		} catch (Exception e) {
			Logging.debug(e, "Error in" + EnemyFormation.class.getName());
		}
	}

	/**
	 * removes duplicate values from array
	 * 
	 * @param arr
	 *            array with duplicates
	 * @return array with duplicate values removed
	 */
	public static int[] removeDuplicates(int[] arr) {
		Set<Integer> alreadyPresent = new HashSet<>();
		int[] whitelist = new int[arr.length];
		int i = 0;

		for (int element : arr) {
			if (alreadyPresent.add(element)) {
				whitelist[i++] = element;
			}
		}

		return Arrays.copyOf(whitelist, i);
	}

	/**
	 * checks if row is full
	 */
	public void checkRowFull() {
		try {

			int[] allRowsToCheck = new int[entities.size()];

			for (int i = 0; i < allRowsToCheck.length; i++) {
				allRowsToCheck[i] = entities.get(i).getRow();
			}

			int[] rowsToCheck = this.removeDuplicates(allRowsToCheck);

			boolean initiateDelete = false;

			for (int i = 0; i < rowsToCheck.length; i++) {
				if (grid.isRowFull(rowsToCheck[i])) {
					initiateDelete = true;
				} else {
					rowsToCheck[i] = -1;
				}
			}

			if (initiateDelete) {
				grid.setRowsToDelete(rowsToCheck);
				grid.setDeleteGrid(true);
			}
		} catch (Exception e) {
			Logging.debug(e, "Error in" + EnemyFormation.class.getName());
		}
	}
}
