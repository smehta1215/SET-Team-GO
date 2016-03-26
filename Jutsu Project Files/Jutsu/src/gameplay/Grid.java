package gameplay;

import java.awt.Point;

import main.Logging;

public class Grid {

	public int bottomBound;
	public int leftBound;
	public int rightBound;

	public Point gridCoordinates[][];
	public Entity[][] entityGrid;

	int tilesAcross;
	int tilesDown;

	/**
	 * constructs grid for maintaining collision and movement
	 * 
	 * @param tileSize
	 *            size of sprite tiles
	 * @param tilesAcross
	 *            number of tiles across
	 * @param tilesDown
	 *            number of tiles down
	 */
	public Grid(int tileSize, int tilesAcross, int tilesDown) {
		this.bottomBound = ((tilesDown - 3) * tileSize) - tileSize;
		this.leftBound = (1) * tileSize;
		this.rightBound = ((tilesAcross - 1) * tileSize) - tileSize;

		this.tilesAcross = tilesAcross;
		this.tilesDown = tilesDown;

		this.gridCoordinates = new Point[tilesDown][tilesAcross];
		this.entityGrid = new Entity[tilesDown][tilesAcross];

		initializeGridCoordinates();
	}

	/**
	 * clears all Entity location on grid
	 */
	public void clearWholeGrid() {
		try {
			this.entityGrid = new Entity[tilesDown][tilesAcross];
		} catch (Exception e) {
			Logging.debug(e, "Error in" + Grid.class.getName());
		}
	}

	/**
	 * sets Points for the grid coordinates for reference
	 */
	public void initializeGridCoordinates() {
		try {
			for (int r = 0; r < 21; r++) {
				for (int c = 0; c < 17; c++) {
					gridCoordinates[r][c] = new Point(c * 32, r * 32);
				}
			}
		} catch (Exception e) {
			Logging.debug(e, "Error in" + Grid.class.getName());
		}
	}

	/**
	 * keeps track of the previous puzzle formation
	 */
	public int lastFormation = 0;

	/**
	 * sets the last puzzle formation that fell
	 * 
	 * @param lastForm
	 *            last location placed
	 */
	public void setLastFormation(int lastForm) {
		try {
			this.lastFormation = lastForm;
		} catch (Exception e) {
			Logging.debug(e, "Error in" + Grid.class.getName());
		}
	}

	/**
	 * @return the last formation placed
	 */
	public int getLastFormation() {
		return this.lastFormation;
	}

	/**
	 * current active Formation
	 */
	protected Formation currentFormation;

	/**
	 * sets the current formation
	 * 
	 * @param form
	 *            controllable formation
	 */
	public void setCurrentFormation(Formation form) {
		try {
			this.currentFormation = form;
		} catch (Exception e) {
			Logging.debug(e, "Error in" + Grid.class.getName());
		}
	}

	/**
	 * @return current controllable formation
	 */
	public Formation getCurrentFormation() {
		return currentFormation;
	}

	/**
	 * @return two dimensional array corresponding with Entity locations on grid
	 */
	public Entity[][] getEntityGrid() {
		return entityGrid;
	}

	/**
	 * removes specific Entity from grid
	 * 
	 * @param entity
	 *            desired entity
	 * @param row
	 *            entity row location
	 * @param col
	 *            entity column location
	 */
	public void removeFromGrid(Entity entity, int row, int col) {
		try {
			if (entity != null && this.entityGrid[row][col] != null
					&& this.entityGrid[row][col].getID() == entity.getID()) {
				this.entityGrid[row][col] = null;
			}
		} catch (Exception e) {
			Logging.debug(e, "Error in" + Grid.class.getName());
		}
	}

	/**
	 * adds Entity to spot on grid if the spot is free
	 * 
	 * @param entity
	 *            Entity to add
	 * @param row
	 *            row location
	 * @param col
	 *            column location
	 */
	public void addToGrid(Entity entity, int row, int col) {
		try {
			if (this.entityGrid[row][col] == null) {
				this.entityGrid[row][col] = entity;
			}
		} catch (Exception e) {
			Logging.debug(e, "Error in" + Grid.class.getName());
		}
	}

	/**
	 * adds Entity to grid and overrides anything in that location
	 * 
	 * @param e
	 *            Entity to add
	 * @param row
	 *            row location
	 * @param col
	 *            column location
	 */
	public void forceAddToGrid(Entity en, int row, int col) {
		try {
			this.entityGrid[row][col] = en;
		} catch (Exception e) {
			Logging.debug(e, "Error in" + Grid.class.getName());
		}
	}

	/**
	 * @param row
	 *            row to check
	 * @return true if row is full, false if row is not full
	 */
	public boolean isRowFull(int row) {
		for (int c = 1; c < 16; c++) {
			if (entityGrid[row][c] == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * makes spot on grid null
	 * 
	 * @param row
	 *            row location
	 * @param col
	 *            column location
	 */
	public void clearLocation(int row, int col) {
		this.entityGrid[row][col] = null;
	}

	public boolean deleteGrid = false;

	/**
	 * sets whether grid needs a row removed
	 * 
	 * @param sets
	 *            true for if grid needs row deleted
	 */
	public void setDeleteGrid(boolean set) {
		this.deleteGrid = set;
	}

	public int[] rowsToDelete;

	/**
	 * sets rows that are full
	 * 
	 * @param rows
	 *            rows to be deleted
	 */
	public void setRowsToDelete(int[] rows) {
		this.rowsToDelete = rows;
	}

	/**
	 * @return true if rows need to be deleted
	 */
	public boolean getDeleteGrid() {
		return this.deleteGrid;
	}

	/**
	 * @return int array of all full rows
	 */
	public int[] getRowsToDelete() {
		return this.rowsToDelete;
	}

	/**
	 * checks whether Entity can move in a specific direction
	 * 
	 * @param entity
	 *            Entity to move
	 * @param xDir
	 *            x direction
	 * @param yDir
	 *            y direction
	 * @return true if Entity can move in direction
	 */
	public boolean canMove(Entity entity, int xDir, int yDir) {
		if (entity.getRow() == 17) {
			entity.setActive(false);

			((EnemyFormation) this.currentFormation).checkForGameOver();

			this.currentFormation.lockInAll();
			this.currentFormation.setPieceActive(false);
			this.currentFormation.disableAll();

			((EnemyFormation) (currentFormation)).checkRowFull();

			return false;
		} else if (((entityGrid[entity.getRow() + yDir][entity.getCol() + xDir] != null) && !(entityGrid[entity
				.getRow() + yDir][entity.getCol() + xDir].checkActive()))) {
			entity.setActive(false);

			((EnemyFormation) this.currentFormation).checkForGameOver();

			this.currentFormation.lockInAll();
			this.currentFormation.setPieceActive(false);
			this.currentFormation.disableAll();

			if ((((EnemyFormation) (currentFormation)).getEntities().get(0)
					.getY() >= 0)
					&& (((EnemyFormation) (currentFormation)).getEntities()
							.get(1).getY() >= 0)
					&& (((EnemyFormation) (currentFormation)).getEntities()
							.get(2).getY() >= 0)
					&& (((EnemyFormation) (currentFormation)).getEntities()
							.get(3).getY() >= 0))
				((EnemyFormation) (currentFormation)).checkRowFull();

			return false;
		}
		return true;
	}

	/**
	 * checks for horizontal movement
	 * 
	 * @param entity
	 *            Entity to move horizontally
	 * @param xDir
	 *            x direction
	 * @param yDir
	 *            y direction
	 * @return true if entity can move horizontally
	 */
	public boolean canMoveHorz(Entity entity, int xDir, int yDir) {
		if ((entityGrid[entity.getRow() + yDir][entity.getCol() + xDir] != null)
				&& !(entityGrid[entity.getRow() + yDir][entity.getCol() + xDir])
						.checkActive()) {
			return false;
		}
		return true;
	}

	/**
	 * updates Entity location at end of moves
	 * 
	 * @param entity
	 *            Entity to update location
	 */
	protected void updateEntity(Entity entity) {
		try {
			if (entity.getRow() == 17) {
				entity.setActive(false);
				this.currentFormation.lockInAll();
				this.currentFormation.setPieceActive(false);
				this.currentFormation.disableAll();
			} else if (((entityGrid[entity.getRow() + 1][entity.getCol() + 0] != null) && !(entityGrid[entity
					.getRow() + 1][entity.getCol() + 0].checkActive()))) {
				this.currentFormation.lockInAll();
				entity.setActive(false);
				this.currentFormation.setPieceActive(false);
				this.currentFormation.disableAll();

			}
		} catch (Exception e) {
			Logging.debug(e, "Error in" + Grid.class.getName());
		}
	}
}
