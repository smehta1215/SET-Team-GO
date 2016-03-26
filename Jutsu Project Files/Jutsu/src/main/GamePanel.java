package main;

import gameplay.Grid;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import states.GameStateManager;

public class GamePanel extends JPanel implements Runnable, KeyListener,
		MouseListener {

	private static final long serialVersionUID = 1L;

	private Thread thread;
	private boolean running = false;
	private int FPS = 60;
	private long targetTime = 1000 / FPS;

	public static final int TILESIZE = 32;
	public static final int TILESACROSS = 17;
	public static final int TILESDOWN = 21;
	public static final int SCALE = 1;
	public static final int WIDTH = TILESIZE * TILESACROSS;
	public static final int HEIGHT = TILESIZE * TILESDOWN;

	private BufferedImage image;
	private Grid grid;
	private int[] pixels;

	private Graphics2D g;
	private Graphics gScreen;

	private boolean paused;
	private JFrame frame;

	private GameStateManager gsm;

	/**
	 * Controls GamePanel object that controls thread and game ticks
	 * 
	 * @param frame
	 *            JFrame that holds the game panel
	 */
	public GamePanel(JFrame frame) {
		super();
		this.frame = frame;
		setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		addMouseListener(this);
		setFocusable(true);
		requestFocus();

		initialize();
	}

	/**
	 * starts thread
	 */
	public synchronized void start() {
		running = true;
		paused = false;
	}

	/**
	 * resumes thread
	 */
	public synchronized void resume() {
		this.paused = false;
	}

	/**
	 * stops thread
	 */
	public synchronized void stop() {
		paused = true;
	}

	public void addNotify() {
		super.addNotify();
		if (thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}

	@Override
	public void keyPressed(KeyEvent key) {
		gsm.keyPressed(key.getKeyCode());

	}

	@Override
	public void keyReleased(KeyEvent key) {
		gsm.keyReleased(key.getKeyCode());

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	public int toDraw = 0;

	@Override
	public void run() {

		long start;
		long elapsed;
		long wait;

		while (running) {

			if (!paused) {
				start = System.nanoTime();

				update();
				render();
				drawToScreen();

				if (toDraw == 5) {
					toDraw = 0;
					draw();
				}

				elapsed = System.nanoTime() - start;
				wait = targetTime - elapsed / 1000000;

				if (wait < 0) {
					wait = 5;
				}

				try {
					Thread.sleep(wait);
				} catch (Exception e) {
					e.printStackTrace();
				}

				toDraw++;
			}
		}
	}

	/**
	 * updates the GameStateManager and corresponding states and mouse location
	 * relative to screen
	 */
	private void update() {
		if ((gsm.currentState == gsm.MENUSTATE
				|| gsm.currentState == gsm.CREDITSSTATE
				|| gsm.currentState == gsm.SCORESSTATE || gsm.currentState == gsm.STORYTWOSTATE)
				&& this.getMousePosition() != null) {
			try {
				gsm.setMouseLocs(this.getMousePosition().x,
						this.getMousePosition().y);
			} catch (Exception e) {

			}
		}
		gsm.update();
	}

	/**
	 * draws 2D images off the main image
	 */
	private void draw() {
		g = (Graphics2D) getGraphics();
		gsm.draw(g);
	}

	/**
	 * renders the pixel array
	 */
	private void render() {
		for (int i = 0; i < pixels.length; i++) {
			this.pixels[i] = gsm.getPixels()[i];
		}
	}

	private int drawHeight = 608;

	/**
	 * sets height for the image on screen
	 * 
	 * @param height
	 *            desired image height
	 */
	public void setDraw(int height) {
		this.drawHeight = height;
	}

	/**
	 * draws main pixel image on screen
	 */
	private void drawToScreen() {
		Graphics gScreen = getGraphics();
		gScreen.drawImage(image, 0, 0, WIDTH * SCALE, drawHeight, null);

	}

	/**
	 * initializes main game and GameStateManager
	 */
	private void initialize() {
		image = new BufferedImage(WIDTH, 608, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		grid = new Grid(TILESIZE, TILESACROSS, TILESDOWN);
		gsm = new GameStateManager(this, this.thread, g, HEIGHT, WIDTH, pixels,
				grid);

		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {

				try {
					gsm.writeToFile();
					gsm.getWriter().flush();
					gsm.getWriter().close();
				} catch (IOException e) {
					e.printStackTrace();
				}

				System.exit(0);
			}
		});

		this.start();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent m) {
	}

	@Override
	public void mouseExited(MouseEvent m) {

	}

	@Override
	public void mousePressed(MouseEvent m) {
		gsm.mousePressed(m);
	}

	@Override
	public void mouseReleased(MouseEvent m) {
		gsm.mouseReleased(m);

	}
}
