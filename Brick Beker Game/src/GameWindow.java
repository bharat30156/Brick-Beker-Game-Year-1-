import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.Timer;

public class GameWindow extends JPanel {
	
	public int height;
	public int width;
	
	public int frameRate;
	public int ballSpeed;
	
	public int highscore;
	
	public boolean gameScreen = false;
	
	GameLogic gameLogic;
	
	JRadioButton collisionCheatOn;
	JRadioButton collisionCheatOff;
	JRadioButton infiniteLivesOn;
	JRadioButton infiniteLivesOff;
	Button play;
	Button quit;
	
	Graphics g;
	
	Timer eventsTimer;
	public boolean paused = false;
	

	public GameWindow(int width, int height, int frameRate, int ballSpeed) {
		this.width = width;
		this.height = height;
		this.frameRate = frameRate;
		this.ballSpeed = ballSpeed;
		
		this.setBackground(Color.BLACK);
		this.gameLogic = new GameLogic(width, height, this);
		MouseAdapter mouseAdapter = new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				gameLogic.handleMouseMoveEvent(e);
			}
			public void mouseClicked(MouseEvent e) {
				gameLogic.handleMouseClickEvent(e);
			}
		};
		this.addMouseListener(mouseAdapter);
		this.addMouseMotionListener(mouseAdapter);
		
		this.setFocusable(true);
		this.requestFocus();
		this.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent ke) {
				if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
					gameLogic.endGame();
				}
				if (ke.getKeyCode() == KeyEvent.VK_P) {
					if (!paused) {
						stopEvents();
					}
					else {
						runEvents();
					}
					paused = !paused;
				}
				if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
					gameLogic.ball.xSpeed -= 1;
				}
				if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
					gameLogic.ball.xSpeed += 1;
				}
			}
		});
		
		this.setLayout(null);
		
		collisionCheatOn = new JRadioButton("On");
	    collisionCheatOff = new JRadioButton("Off");
	    collisionCheatOn.setBounds(width/2 + 30, 311, 50, 30);
	    collisionCheatOn.setBackground(Color.BLACK);
	    collisionCheatOn.setForeground(Color.WHITE);
	    collisionCheatOn.addMouseListener(new MouseAdapter() {
	    	public void mouseClicked(MouseEvent e) {
	    		gameLogic.bottomCollision = true;
	    	}
	    });
	    collisionCheatOff.setBounds(width/2 + 80, 311, 50, 30);
	    collisionCheatOff.setBackground(Color.BLACK);
	    collisionCheatOff.setForeground(Color.WHITE);
	    collisionCheatOff.addMouseListener(new MouseAdapter() {
	    	public void mouseClicked(MouseEvent e) {
	    		gameLogic.bottomCollision = false;
	    	}
	    });
	    
	    ButtonGroup buttonGroup = new ButtonGroup();
	    buttonGroup.add(collisionCheatOn);
	    buttonGroup.add(collisionCheatOff);
	    this.add(collisionCheatOn);
	    this.add(collisionCheatOff);
	    collisionCheatOff.setSelected(true);
	    
	    infiniteLivesOn = new JRadioButton("On");
	    infiniteLivesOff = new JRadioButton("Off");
	    infiniteLivesOn.setBounds(width/2 + 30, 342, 50, 30);
	    infiniteLivesOn.setBackground(Color.BLACK);
	    infiniteLivesOn.setForeground(Color.WHITE);
	    infiniteLivesOn.addMouseListener(new MouseAdapter() {
	    	public void mouseClicked(MouseEvent e) {
	    		gameLogic.infiniteLives = true;
	    	}
	    });
	    infiniteLivesOff.setBounds(width/2 + 80, 342, 50, 30);
	    infiniteLivesOff.setBackground(Color.BLACK);
	    infiniteLivesOff.setForeground(Color.WHITE);
	    infiniteLivesOff.addMouseListener(new MouseAdapter() {
	    	public void mouseClicked(MouseEvent e) {
	    		gameLogic.infiniteLives = false;
	    	}
	    });
	    
	    ButtonGroup buttonGroup2 = new ButtonGroup();
	    buttonGroup2.add(infiniteLivesOn);
	    buttonGroup2.add(infiniteLivesOff);
	    this.add(infiniteLivesOn);
	    this.add(infiniteLivesOff);
	    infiniteLivesOff.setSelected(true);
	    
	    play = new Button("Play");
	    play.setFont(new Font("TimesRoman", Font.PLAIN, 20));
	    play.setBounds(width/2 - 110, 400, 70, 50);
	    play.setBackground(Color.BLACK);
	    play.setForeground(Color.WHITE);
	    play.addMouseListener(new MouseAdapter() {
	    	public void mouseClicked(MouseEvent e) {
	    		setSplashButtons(false);
	    		gameScreen = true;
	    		if (paused) {
	    			runEvents();
	    			paused = false;
	    		}
	    	}
	    });
	    this.add(play);
	    
	    quit = new Button("Quit");
	    quit.setFont(new Font("TimesRoman", Font.PLAIN, 20));
	    quit.setBounds(width/2 + 40, 400, 70, 50);
	    quit.setBackground(Color.BLACK);
	    quit.setForeground(Color.WHITE);
	    quit.addMouseListener(new MouseAdapter() {
	    	public void mouseClicked(MouseEvent e) {
	    		System.exit(0);
	    	}
	    });
	    this.add(quit);
	    
	    this.setVisible(true);
	}
	
	public void paintComponent(Graphics g) {
		this.g = g;
		super.paintComponent(g); // clear the previous paint
		if (gameScreen) {
			gameLogic.paint(g);
		}
		else {
			paintSplashScreen(g);
		}
	}
	
	public void paintSplashScreen(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("TimesRoman", Font.BOLD, 50));
		drawCenterString(g, "Jennifer", 50, 0);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		drawCenterString(g, "Created By Bharat", 80, 0);
		
		g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
		drawCenterString(g, "Use the mouse to aim where you want the ball to shoot.", 130, 0);
		drawCenterString(g, "Once you're ready, simply left click and start playing!", 150, 0);
		drawCenterString(g, "Break the brick for a special powerup!", 170, 0);
		drawCenterString(g, "Use the left and right arrow keys to 'tilt' the screen.", 190, 0);
		drawCenterString(g, "Press esc to exit back to this menu and P to pause the game.", 230, 0);
		
		drawCenterString(g, "You have 3 lives to try and get the highest score possible.", 250, 0);

		g.setFont(new Font("TimesRoman", Font.PLAIN, 35));
		drawCenterString(g, "Cheats", 300, 0);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
		drawCenterString(g, "Bounce on bottom screen:", 330, -60);
		drawCenterString(g, "Infinite lives:", 360, -20);
		
		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		String scoreString = "Highscore: " + this.highscore;
		g.drawString(scoreString, 30, height - 25);
	}
	
	public void setSplashButtons(boolean on) {
		this.collisionCheatOn.setVisible(on);
		this.collisionCheatOff.setVisible(on);
		this.infiniteLivesOn.setVisible(on);
		this.infiniteLivesOff.setVisible(on);
		this.play.setVisible(on);
		this.quit.setVisible(on);
	}
	
	public void drawCenterString(Graphics g, String myString, int y, int xOffset) {
		FontMetrics fm = g.getFontMetrics();
		Rectangle2D rect2D = fm.getStringBounds(myString, g);
		int x = (width - (int)(rect2D.getWidth())) / 2;
		x += xOffset;
		g.drawString(myString, x, y);
	}
	
	public void repaintWithFrameRate(int frameRate) {
		int delay = (int)(1000 / frameRate); //milliseconds
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				repaint();
			}
		};
		new Timer(delay, taskPerformer).start();
	}
	
	public void runEvents() {
		int delay = this.ballSpeed; //milliseconds
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				gameLogic.runGame();
			}
		};
		eventsTimer = new Timer(delay, taskPerformer);
		eventsTimer.start();
	}
	
	public void stopEvents() {
		eventsTimer.stop();
	}
	
	public void resizeWindow() {
		Rectangle bounds = this.getBounds();
		this.width = (int)bounds.getWidth();
		this.height = (int)bounds.getHeight();
		collisionCheatOn.setLocation(width/2 + 30, 311);
		collisionCheatOff.setLocation(width/2 + 80, 311);
		infiniteLivesOn.setLocation(width/2 + 30, 342);
		infiniteLivesOff.setLocation(width/2 + 80, 342);
		play.setLocation(width/2 - 110, 400);
		quit.setLocation(width/2 + 40, 400);
		
		gameLogic.resizeGame(width, height);
		
		repaint();
	}
}

