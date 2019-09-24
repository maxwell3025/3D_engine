package main;

import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		JFrame main = new JFrame();
		main.add(new MainPanel(1080,720));
		main.setResizable(false);
		main.pack();
		main.setDefaultCloseOperation(3);
		main.setLocationRelativeTo(null);
		main.setVisible(true);
	}
}
