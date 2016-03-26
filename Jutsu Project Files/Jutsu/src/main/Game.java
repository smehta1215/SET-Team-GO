package main;

import javax.swing.JFrame;

public class Game {

	public static void main(String[] args) {

		Logging logger = new Logging();

		JFrame window = new JFrame("Jutsu");
		window.setContentPane(new GamePanel(window));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
	}
}
