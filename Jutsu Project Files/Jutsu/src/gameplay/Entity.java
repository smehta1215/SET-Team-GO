package gameplay;

import java.util.ArrayList;

public abstract class Entity {

	protected int startingX;
	protected int startingY;
	protected int currentX;
	protected int currentY;

	public int id;

	protected ArrayList<int[]> animations;

	protected int speed;
	protected long loopTime;
	protected long elapsedTime;

	/**
	 * true if enemy is controllable, false if piece has set
	 */
	protected boolean isActive;
	protected boolean drop;

	protected int gridRow;
	protected int gridCol;

	/**
	 * @return ID of each individual enemy
	 */
	public int getID() {
		return id;
	}

	/**
	 * initializes starting positions, speed, images, and other beginning values
	 * for the enemy
	 */
	protected abstract void initialize();

	/**
	 * updates position, rotation, and other changed values for enemy
	 */
	public abstract void update();

	/**
	 * @return row of enemy in relation to grid
	 */
	public int getRow() {
		return gridRow;
	}

	/**
	 * @return row of enemy in relation to grid
	 */
	public int getCol() {
		return gridCol;
	}

	/**
	 * sets the enemy active state
	 * 
	 * @param active
	 *            desired active state
	 */
	public void setActive(boolean active) {
		isActive = active;
	}

	/**
	 * @return active state of the enemy
	 */
	public boolean checkActive() {
		return isActive;
	}

	/**
	 * sets the x coordinate location of enemy
	 * 
	 * @param x
	 *            desired x coordinate
	 */
	public void setX(int x) {
		this.currentX = x;
	}

	/**
	 * sets the y coordinate location of enemy
	 * 
	 * @param y
	 *            desired y coordinate
	 */
	public void setY(int y) {
		this.currentY = y;
	}

	/**
	 * @return current x coordinate
	 */
	public int getX() {
		return currentX;
	}

	/**
	 * @return current y coordinate
	 */
	public int getY() {
		return currentY;
	}

	/**
	 * @return current speed
	 */
	protected int getSpeed() {
		return speed;
	}

	/**
	 * changes the speed when the enemy reaches a new grid location
	 * 
	 * @param speed
	 *            speed value to change too at next grid location
	 */
	public void setSpeed(int speed) {
		if (this.getY() % 32 == 0) {
			this.speed = speed;
		}
	}

	/**
	 * sets the animation int arrays for the enemy
	 * 
	 * @param int array for animation
	 */
	protected void addAnimationTiles(int[] tile) {
		this.animations.add(tile);
	}

	/**
	 * @param index
	 *            specific animation tile index
	 * @return animation int array
	 */
	protected int[] returnTile(int index) {
		return this.animations.get(index);
	}

	/**
	 * @return necessary animation int array
	 */
	public int[] returnActiveTile() {
		if (isActive) {
			elapsedTime = System.currentTimeMillis() - loopTime;
			int tempSpeed = this.speed;
			if (this.speed == 32) {
				tempSpeed = 16;
			}
			if (elapsedTime > (int) (((260 - ((tempSpeed * tempSpeed))) * 1) / 2.0)) {
				loopTime = System.currentTimeMillis();
				elapsedTime = 0;
				this.switchActiveTile();
			}
			return this.animations.get(activeTile);
		} else
			return this.animations.get(0);
	}

	public int activeTile = 0;

	/**
	 * switches between the animation arrays
	 */
	public void switchActiveTile() {
		if (activeTile < this.animations.size() - 1) {
			activeTile++;
		} else {
			activeTile = 0;
		}
	}
}
