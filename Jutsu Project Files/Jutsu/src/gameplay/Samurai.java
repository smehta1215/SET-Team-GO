package gameplay;

import java.util.ArrayList;

import graphics.SpriteSheet;

public class Samurai extends Entity {

	private SpriteSheet spriteSheet;
	private int activeTile;

	private Grid grid;

	private int xRotID;
	private int yRotID;

	public int samuraiType;

	/**
	 * creates samurai enemy with advanced characteristics
	 * 
	 * @param spriteSheet
	 *            SpriteSheet object with animation tiles
	 * @param x
	 *            x coordinate location
	 * @param y
	 *            y coordinate location
	 * @param grid
	 *            movement grid
	 * @param id
	 *            enemy ID
	 * @param xRotID
	 *            x rotational position ID
	 * @param yRotID
	 *            y rotational position ID
	 * @param type
	 *            class of enemy samurai
	 */
	public Samurai(SpriteSheet spriteSheet, int x, int y, Grid grid, int id,
			int xRotID, int yRotID, int type) {
		this.spriteSheet = spriteSheet;
		this.animations = new ArrayList<int[]>();
		this.setX(x);
		this.setY(y);
		this.grid = grid;
		this.xRotID = xRotID;
		this.yRotID = yRotID;
		this.id = id;
		this.samuraiType = type;

		activeTile = 0;

		initialize();
	}

	/**
	 * @return samurai type
	 */
	public int getType() {
		return this.samuraiType;
	}

	/**
	 * @return x rotational ID
	 */
	public int getXRotID() {
		return this.xRotID;
	}

	/**
	 * @return y rotational ID
	 */
	public int getYRotID() {
		return this.yRotID;
	}

	/**
	 * @param xID
	 *            sets x rotational ID
	 */
	public void setXRotID(int xID) {
		this.xRotID = xID;
	}

	/**
	 * @param yID
	 *            sets y rotational ID
	 */
	public void setYRotID(int yID) {
		this.yRotID = yID;
	}

	@Override
	public void initialize() {
		this.speed = 2;
		this.elapsedTime = 0;

		this.drop = false;

		if (this.getY() >= 0) {
			this.gridCol = this.getX() / 32;
			this.gridRow = this.getY() / 32;
			grid.addToGrid(this, gridRow, gridCol);
		}
		if (this.getType() == 1) {
			for (int i = 0; i < spriteSheet.getBasicSamTiles().size(); i++) {
				this.addAnimationTiles(spriteSheet.getBasicSamTiles().get(i));
			}
		}
		if (this.getType() == 2) {
			for (int i = 0; i < spriteSheet.getMedSamTiles().size(); i++) {
				this.addAnimationTiles(spriteSheet.getMedSamTiles().get(i));
			}
		}
		if (this.getType() == 3) {
			for (int i = 0; i < spriteSheet.getMedSamTiles().size(); i++) {
				this.addAnimationTiles(spriteSheet.getAdvSamTiles().get(i));
			}
		}
		if (this.getType() == 4) {
			for (int i = 0; i < spriteSheet.getMedSamTiles().size(); i++) {
				this.addAnimationTiles(spriteSheet.getStrongSamTiles().get(i));
			}
		}

		this.isActive = true;
	}

	private boolean shouldMove = false;

	protected boolean horzMove = false;
	protected int xDir = 0;

	protected boolean rotRight = false;
	protected boolean rotLeft = false;

	/**
	 * sets boolean for horizontal movement
	 * 
	 * @param horz
	 *            boolean for whether to move sideways
	 */
	public void setHorzMove(boolean horz) {
		this.horzMove = horz;
	}

	/**
	 * sets boolean for right rotation
	 * 
	 * @param right
	 *            boolean for whether to rotate right
	 */
	public void setRotRight(boolean right) {
		this.rotRight = right;
	}

	/**
	 * sets boolean for left rotation
	 * 
	 * @param left
	 *            boolean for whether to rotate left
	 */
	public void setRotLeft(boolean left) {
		this.rotLeft = left;
	}

	/**
	 * sets direction for x movement in relation to current x
	 * 
	 * @param xDir
	 *            desired xDir
	 */
	public void setXDir(int xDir) {
		this.xDir = xDir;
	}

	/**
	 * resets rotational IDs after rotation has occurred
	 */
	public void resetRotRightIDs() {
		if (this.getXRotID() == 2 || this.getXRotID() == -2) {
			this.setYRotID(this.getXRotID());
			this.setXRotID(0);
		} else if (this.getXRotID() == 0 && this.getYRotID() != 0) {
			this.setXRotID((this.getYRotID() * -1));
			this.setYRotID(0);
		} else if (this.getXRotID() != 0
				&& this.getXRotID() == this.getYRotID()) {
			this.setXRotID(this.getXRotID() * -1);
		} else if (this.getXRotID() == (this.getYRotID() * -1)) {
			this.setYRotID(this.getYRotID() * -1);
		} else if (this.getYRotID() == 3 || this.getYRotID() == -3) {
			this.setXRotID(this.getXRotID() * 3);
			this.setYRotID(this.getYRotID() / -3);
		} else if (this.getXRotID() == 3 || this.getXRotID() == -3) {
			this.setXRotID(this.getXRotID() / -3);
			this.setYRotID(this.getYRotID() * 3);
		}
	}

	/**
	 * toggles for rotating to left after right
	 */
	public void swapRotDirToLeft() {
		int tempX = this.getXRotID();
		int tempY = this.getYRotID();
		this.setXRotID(-1 * tempY);
		this.setYRotID(tempX);
	}

	/**
	 * toggles for rotating to right after left
	 */
	public void swapRotDirToRight() {
		int tempX = this.getXRotID();
		int tempY = this.getYRotID();
		this.setXRotID(tempY);
		this.setYRotID(-1 * tempX);

	}

	/**
	 * resets rotational IDs after rotation has occurred
	 */
	public void resetRotLeftIDs() {
		if (this.getXRotID() == 2 || this.getXRotID() == -2) {
			this.setYRotID(-1 * this.getXRotID());
			this.setXRotID(0);
		} else if (this.getXRotID() == 0 && this.getYRotID() != 0) {
			this.setXRotID((this.getYRotID()));
			this.setYRotID(0);
		} else if (this.getXRotID() != 0
				&& this.getXRotID() == this.getYRotID()) {
			this.setYRotID(this.getYRotID() * -1);
		} else if (this.getXRotID() == (this.getYRotID() * -1)) {
			this.setXRotID(this.getXRotID() * -1);
		} else if (this.getYRotID() == 3 || this.getYRotID() == -3) {
			this.setXRotID(this.getXRotID() * -3);
			this.setYRotID(this.getYRotID() / 3);
		} else if (this.getXRotID() == 3 || this.getXRotID() == -3) {
			this.setXRotID(this.getXRotID() / 3);
			this.setYRotID(this.getYRotID() * -3);
		}
	}

	/**
	 * locks in all pieces into the grid for stability
	 */
	public void lockInGrid() {
		this.gridCol = this.getX() / 32;
		this.gridRow = this.getY() / 32;
		if (gridCol >= 1 && gridRow >= 0) {
			this.grid.forceAddToGrid(this, this.gridRow, this.gridCol);
		}
	}

	/**
	 * alters the x and y coordinates of samurai depending on the horizontal and
	 * vertical movement
	 */
	protected void move() {

		if (this.getY() >= 0 && this.rotRight) {
			if (this.getY() % 32 == 0
					&& grid.canMoveHorz(this, this.getXRotID(),
							this.getYRotID())) {
				this.grid.removeFromGrid(this, this.gridRow, this.gridCol);
				this.setX(this.currentX + (this.getXRotID() * 32));
				this.setY(this.currentY + (this.getYRotID() * 32));
				gridCol = this.getX() / 32;
				gridRow = this.getY() / 32;
				this.grid.addToGrid(this, this.gridRow, this.gridCol);

				this.resetRotRightIDs();

				this.setRotRight(false);
				this.setHorzMove(false);
				this.setRotLeft(false);
			} else if (grid.canMoveHorz(this, this.getXRotID(),
					this.getYRotID() + 1)) {

				this.grid.removeFromGrid(this, this.gridRow, this.gridCol);
				this.setX(this.currentX + (this.getXRotID() * 32));
				this.setY(this.currentY + (this.getYRotID() * 32));
				gridCol = this.getX() / 32;
				this.grid.addToGrid(this, this.gridRow, this.gridCol);

				this.resetRotRightIDs();
				this.setRotRight(false);
				this.setHorzMove(false);
				this.setRotLeft(false);
			}
		}

		if (this.getY() >= 0 && this.rotLeft) {
			if (this.getY() % 32 == 0
					&& grid.canMoveHorz(this, this.getXRotID(),
							this.getYRotID())) {
				this.grid.removeFromGrid(this, this.gridRow, this.gridCol);
				this.setX(this.currentX + (this.getXRotID() * 32));
				this.setY(this.currentY + (this.getYRotID() * 32));
				gridCol = this.getX() / 32;
				gridRow = this.getY() / 32;
				this.grid.addToGrid(this, this.gridRow, this.gridCol);

				this.resetRotLeftIDs();

				this.setRotLeft(false);
				this.setRotRight(false);
				this.setHorzMove(false);
			} else if (grid.canMoveHorz(this, this.getXRotID(),
					this.getYRotID() + 1)) {

				this.grid.removeFromGrid(this, this.gridRow, this.gridCol);
				this.setX(this.currentX + (this.getXRotID() * 32));
				this.setY(this.currentY + (this.getYRotID() * 32));
				gridCol = this.getX() / 32;
				this.grid.addToGrid(this, this.gridRow, this.gridCol);

				this.resetRotLeftIDs();
				this.setRotLeft(false);
				this.setRotRight(false);
				this.setHorzMove(false);
			}
		}

		if (this.getY() >= 0 && this.horzMove) {
			if (this.getY() % 32 == 0 && grid.canMoveHorz(this, this.xDir, 0)) {
				this.grid.removeFromGrid(this, this.gridRow, this.gridCol);
				this.setX(this.currentX + (this.xDir * 32));
				gridCol = this.getX() / 32;
				this.grid.addToGrid(this, this.gridRow, this.gridCol);
				this.setHorzMove(false);
				this.setXDir(0);
			} else if (grid.canMoveHorz(this, this.xDir, 1)) {
				this.grid.removeFromGrid(this, this.gridRow, this.gridCol);
				this.setX(this.currentX + (this.xDir * 32));
				gridCol = this.getX() / 32;
				this.grid.addToGrid(this, this.gridRow, this.gridCol);
				this.setHorzMove(false);
				this.setXDir(0);
			}
		} else if (this.getY() >= 0 && !this.horzMove) {
			if (grid.canMove(this, 0, 1)) {
				if (shouldMove) {
					gridRow = this.getY() / 32;
					gridCol = this.getX() / 32;

					shouldMove = false;
					this.setHorzMove(false);
				} else {
					this.setY(this.currentY + (1 * this.getSpeed()));

					if (this.getY() % 32 == 0) {
						grid.removeFromGrid(this, this.gridRow, this.gridCol);
						gridRow = this.getY() / 32;
						gridCol = this.getX() / 32;
						grid.addToGrid(this, this.gridRow, this.gridCol);
						shouldMove = true;
					}
					this.setHorzMove(false);
				}
			}
		} else if (this.getY() < 0) {
			if (shouldMove) {
				shouldMove = false;
			} else {
				this.setY(this.currentY + (1 * this.getSpeed()));
				if (this.getY() % 32 == 0) {
					shouldMove = true;
				}
			}
		}
		this.setHorzMove(false);
		this.setXDir(0);
		this.setRotRight(false);
		this.setRotLeft(false);
	}

	public boolean drop;

	/**
	 * sets whether or not to drop piece
	 * 
	 * @param drop
	 *            boolean for whether to drop
	 */
	public void setDrop(boolean drop) {
		this.drop = drop;
	}

	public boolean changeSpeed;
	public int speedToChange;

	/**
	 * changes speed of piece during fall
	 * 
	 * @param change
	 *            whether or not to change
	 * @param speedToChange
	 *            speed value to change to
	 */
	public void setChangeSpeed(boolean change, int speedToChange) {
		this.changeSpeed = change;
		this.speedToChange = speedToChange;
	}

	@Override
	public void update() {
		if (this.getY() == 0) {
			this.gridRow = this.getY() / 32;
			this.gridCol = this.getX() / 32;
			grid.addToGrid(this, this.gridRow, this.gridCol);
		}

		if (this.getY() % 32 == 0) {
			this.gridRow = this.getY() / 32;
			if (this.drop) {
				this.setSpeed(32);
				this.drop = false;
			}
			if (this.changeSpeed) {
				this.setSpeed(this.speedToChange);
				this.changeSpeed = false;
			}
		}
		if (this.getX() % 32 == 0) {
			this.gridCol = this.getX() / 32;
		}

		this.move();
	}

}
