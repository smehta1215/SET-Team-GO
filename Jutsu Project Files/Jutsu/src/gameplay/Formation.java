package gameplay;

public abstract class Formation {

	public boolean pieceActive;

	protected int highestY;

	/**
	 * sets all included pieces to inactive
	 */
	public abstract void disableAll();

	/**
	 * sets the active state of the whole piece
	 * 
	 * @param set
	 *            boolean for active state
	 */
	public void setPieceActive(boolean set) {
		this.pieceActive = set;
	}

	/**
	 * @return current active state of puzzle piece
	 */
	public boolean getPieceActive() {
		return this.pieceActive;
	}

	/**
	 * changes the horizontal direction
	 * 
	 * @param xDir
	 *            left or right x direction as 1 or -1
	 */
	public abstract void changeXLocation(int xDir);

	/**
	 * rotates puzzle right
	 */
	public abstract void rotateRight();

	/**
	 * rotates puzzle left
	 */
	public abstract void rotateLeft();

	/**
	 * resets all pieces in grid for stability
	 */
	public abstract void lockInAll();

}
