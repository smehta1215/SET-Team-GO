package states;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

public abstract class GameState {

	protected GameStateManager gsm;

	/**
	 * gives starting variables the values for each GameState in
	 * GameStateManager
	 */
	public abstract void initialize();

	/**
	 * updates changed values in state with the thread
	 */
	public abstract void update();

	/**
	 * draws graphics that pertain to each specific state
	 * 
	 * @param g
	 *            2D Graphics from panel
	 */
	public abstract void draw(Graphics2D g);

	/**
	 * allows for unique input in each state when key pressed
	 * 
	 * @param k
	 *            key code
	 */
	public abstract void keyPressed(int k);

	/**
	 * allows for unique input in each state when key released
	 * 
	 * @param k
	 *            key code
	 */
	public abstract void keyReleased(int k);

	/**
	 * performs action when mouse pressed
	 * 
	 * @param m
	 *            MouseEvent
	 */
	public abstract void mousePressed(MouseEvent m);

	/**
	 * performs action when mouse released
	 * 
	 * @param m
	 *            MouseEvent
	 */
	public abstract void mouseReleased(MouseEvent m);
}
