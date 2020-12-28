import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class BrickBreaker extends JFrame {
	
	public static GameWindow gameWindow;
	public static int width = 800;
	public static int height = 600;
	
	public static int frameRate = 30;
	public static int ballSpeed = 5;
	
	
	public BrickBreaker() {
		super();
		this.setTitle("Jennifer Brick Breaker");
		this.setSize(width, height);
		this.getContentPane().setLayout(new GridLayout());
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
	}

	public static void main(String[] args) {
		if (args.length > 1) {
			ballSpeed = Integer.parseInt(args[1]);
			if (ballSpeed > 10 || ballSpeed < 1) {
				ballSpeed = 5;
			}
			else {
				ballSpeed = 11 - ballSpeed;
			}
		}
		if (args.length > 0) {
			frameRate = Integer.parseInt(args[0]);
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				BrickBreaker frame = new BrickBreaker();
				
				gameWindow = new GameWindow(width, height, frameRate, ballSpeed);
				frame.add(gameWindow);
				gameWindow.runEvents();
				gameWindow.repaintWithFrameRate(frameRate);
				frame.setMinimumSize(new Dimension(500, 550));
				frame.addComponentListener(new ComponentAdapter() {
					public void componentResized(ComponentEvent e) {
						gameWindow.resizeWindow();
					}
				});
			}
		});
	}
}