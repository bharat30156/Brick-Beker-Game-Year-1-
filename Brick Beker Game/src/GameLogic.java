import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class GameLogic {
	
	public int windowWidth;
	public int windowHeight;
	public GameWindow gameWindow;
	
	Ball ball;
	public int ballLength = 10;
	public double ballXSpeed;
	public double ballYSpeed;
	
	paddle paddle;
	public int paddleYOffset = 100;
	public int paddleWidth = 80;
	public int paddleHeight = 10;
	public Color paddleColor = Color.GRAY;
	public boolean paddlePowerup = false;
	
	ArrayList<Brick> bricks; // assume bricks are placed in sequence
	public int brickWidth;
	public int brickHeight;
	public ArrayList<Color> colorArray;
	public int numBricks = 75;
	
	public boolean aimPhase = true;
	public Point aimPoint;
	double dirX;
	double dirY;
	double aimLineX;
	double aimLineY;
	
	boolean bottomCollision = false;
	int lives;
	boolean infiniteLives = false;
	
	int score;
	
	
	GameLogic(int windowWidth, int windowHeight, GameWindow gameWindow) {
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		this.gameWindow = gameWindow;
		
		this.paddleWidth = this.windowWidth / 10;
		this.paddleYOffset = this.windowHeight / 6;
		
		this.paddle = new paddle(windowWidth/2 - paddleWidth/2, windowHeight - paddleYOffset, paddleWidth, paddleHeight, paddleColor);
		this.ball = new Ball(paddle.getCenterX() - ballLength/2, paddle.getCenterY() - paddle.height/2 - ballLength, ballLength, ballXSpeed, ballYSpeed, Color.WHITE);
		
		brickWidth = windowWidth / 15;
		brickHeight = brickWidth / 2;
		
		colorArray = new ArrayList<Color>();
		colorArray.add(Color.RED);
		colorArray.add(Color.ORANGE);
		colorArray.add(Color.YELLOW); 
		colorArray.add(Color.GREEN);
		colorArray.add(Color.MAGENTA);
		
		this.aimPoint = new Point(0, 0);
		setDefaults();
	}
	
	public void setDefaults() {
		this.lives = 3;
		this.score = 0;
		this.paddlePowerup = false;
		this.paddle.color = Color.GRAY;
		this.paddle.width = this.windowWidth / 10;
		paddle.width = (int)paddleWidth;
		createBricks(this.numBricks);
	}
	
	public void runGame() {
		if (!aimPhase) {
			int collisionInt = hasCollision();
			moveBall(collisionInt);
		}
	}
	
	public void paint(Graphics g) {
		paddle.paintComponent(g);
		paintBricks(g);
		ball.paintComponents(g);
		paintAimPoint(g);
		paintLives(g);
		paintScore(g);
	}
	
	public void paintBricks(Graphics g) {
		for (int i = 0; i < bricks.size(); i++) {
			Brick brick = bricks.get(i);
			brick.paintComponents(g, colorArray);
		}
	}
	
	public void handleMouseMoveEvent(MouseEvent e) {
		if (aimPhase) {
			aimPoint.setLocation(e.getX(), e.getY());
		}
		else {
			int x = e.getX();
			if (x > windowWidth - paddle.width/2)
				x = windowWidth - paddle.width/2;
			if (x < paddle.width/2)
				x = paddle.width/2;
			x -= paddle.width/2;
			paddle.setLocation(x, paddle.y);
		}
	}
	
	public void handleMouseClickEvent(MouseEvent e) {
		if (aimPhase && gameWindow.gameScreen) {
			aimPhase = false;
			ball.xSpeed = this.dirX*2;
			ball.ySpeed = this.dirY*2;
		}
	}
	
	public void paintAimPoint(Graphics g) {
		if (aimPhase) {
			this.dirX = aimPoint.x - ball.getCenterX();
			this.dirY = aimPoint.y - ball.getCenterY();
			double dirLen = Math.sqrt(dirX*dirX + dirY*dirY);
			this.dirX = dirX / dirLen;
			this.dirY = dirY / dirLen;
			this.aimLineX = this.dirX * 90;
			this.aimLineY = this.dirY * 90;
			g.drawLine((int)(ball.getCenterX()), (int)(ball.getCenterY()), (int)(ball.getCenterX() + this.aimLineX), (int)(ball.getCenterY() + this.aimLineY));
		}
	}
	
	public void paintLives(Graphics g) {
		int xOffset = 30;
		int yOffset = windowHeight - 70;
		g.setColor(Color.WHITE);
		for (int i = 0; i < this.lives; i++) {
			g.fillOval(xOffset, yOffset, ballLength, ballLength);
			xOffset += ballLength * 2;
		}
	}
	
	public void paintScore(Graphics g) {
		String scoreString = "Score: " + this.score;
		g.setColor(Color.WHITE);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		g.drawString(scoreString, 110, windowHeight - 58);
	}
	
	public Brick buildBrick(ArrayList<Brick> bricks) {
		int topX;
		int topY;
		boolean broken = false;
		Color color;
		if (bricks.size() == 0) {
			topX = 0;
			topY = 0;
			color = colorArray.get(0);
			colorArray.add(colorArray.remove(0));
		}
		else {
			Brick lastBrick = bricks.get(bricks.size() - 1);
			
			if (lastBrick.x + 2*(lastBrick.width) >= this.windowWidth) {
				topX = 0;
				topY = lastBrick.y + this.brickHeight;
				color = colorArray.get(0);
				colorArray.add(colorArray.remove(0));
			}
			else {
				topX = lastBrick.x + this.brickWidth;
				topY = lastBrick.y;
				color = lastBrick.color;
			}
		}
		if ((int)Math.floor(Math.random() * 5) == 1) {
			broken = true;
		}
		return new Brick(topX, topY, brickWidth, brickHeight, color, broken);
	}
	
	public void createBricks(int n) {
		bricks = new ArrayList<Brick>();
		for (int i = 0; i < n; i++) {
			bricks.add(buildBrick(bricks));
		}
		setSpecialBrick();
	}
	
	public void setSpecialBrick() {
		int specialInt;
		while (true) {
			specialInt = (int)(Math.floor(Math.random() * 75));
			if (bricks.get(specialInt).broken) {
				continue;
			}
			else {
				bricks.get(specialInt).color = null;
				break;
			}
		}
	}
	
	public void moveBall(int collisionInt) {
		if (collisionInt == 1) {
			ball.ySpeed = -ball.ySpeed;
		}
		if (collisionInt == 3) {
			if (this.bottomCollision) {
				ball.ySpeed = -ball.ySpeed;
			}
			else {
				if (this.infiniteLives) {
					this.aimPhase = true;
					setDefaultLocation();
				}
				else {
					lives -= 1;
					if (lives == 0) {
						endGame();
					}
					else {
						this.aimPhase = true;
						setDefaultLocation();
					}
				}
			}
		}
		else if (collisionInt == 2) {
			ball.xSpeed = -ball.xSpeed;
		}
		ball.lastX = ball.x;
		ball.lastY = ball.y;
		
		ball.setFrame(ball.x + ball.xSpeed, ball.y - ball.ySpeed, ball.length, ball.length);
	}
	
	
	public int hasCollision() {
		// returns 0 if no collision, 1 if horizontal collision, 2 if vertical collision,
		// 3 if bottom y collision (game over)
		int brickCollisionInt = brickCollision(this.bricks);
		if (brickCollisionInt != 0) {
			return brickCollisionInt;
		}	
		if (paddleCollision() != 0) {
			return 1;
		}
		return windowCollision();	
	}
	
	public int paddleCollision() {
		if (ball.intersects(paddle)) {
			ball.y = paddle.y - paddle.height;
			return 1;
		}
		return 0;
	}
	
	public int brickCollision(ArrayList<Brick> bricks) {
		for (int i = 0; i < bricks.size(); i++) {
			Brick brick = bricks.get(i);
			if (!brick.broken && ball.intersects(brick)) {
				if (brick.color == null) {
					getPowerup();
				}
				brick.broken = true;
				this.score += 10;
				checkGameOver();
				int myint = brickCollisionType(brick);
				return myint;
			}
		}
		return 0;
	}
	
	public int brickCollisionType(Brick brick) {
		// assuming collision exists
		// utilizing the Minkowski sum
		// referencing: http://gamedev.stackexchange.com/questions/29786/a-simple-2d-rectangle-collision-algorithm-that-also-determines-which-sides-that
		double width = 0.5 * (ball.length + brick.width);
		double height = 0.5 * (ball.length + brick.height);
		double xDistance = ball.getCenterX() - brick.getCenterX();
		double yDistance = ball.getCenterY() - brick.getCenterY();
		
		double widthY = width * yDistance;
		double heightX = height * xDistance;
		
		if (widthY > heightX) {
			if (widthY > -heightX) { //collision at the top
				return 1;
			}
			else { // collision on the left
				return 2;
			}
		}
		else {
			if (widthY > -heightX) { // collision on the right
				return 2;
			}
			else { // collision at the bottom
				return 1;
			}
		}
	}
	
	public int windowCollision() {
		if (ball.x < 0) {
			ball.x = 0;
			return 2;
		}
		else if (ball.x + ball.length > this.windowWidth) {
			ball.x = this.windowWidth - ball.length;
			return 2;
		}
		else if (ball.y < 0) {
			ball.y = 0;
			return 1;
		}
		else if (ball.y + ball.length > this.windowHeight) {
			ball.y = this.windowHeight - ball.length;
			return 3;
		}
		else
			return 0;
	}
	
	public void checkGameOver() {
		for (int i = 0; i < bricks.size(); i++) {
			if (!bricks.get(i).broken)
				return;
		}
		endGame();
	}
	
	public void setDefaultLocation() {
		this.paddle.setLocation(windowWidth/2 - paddleWidth/2, windowHeight - paddleYOffset);
		this.ball.setFrame(paddle.getCenterX() - ballLength/2, paddle.getCenterY() - paddle.height/2 - ballLength, ball.length, ball.length);
	}
	
	public void endGame() {
		if (this.gameWindow.highscore < this.score) {
			this.gameWindow.highscore = this.score;
		}
		this.gameWindow.gameScreen = false;
		this.gameWindow.setSplashButtons(true);
		this.aimPhase = true;
		setDefaults();
		setDefaultLocation();
	}
	
	public void resizeGame(int x, int y) {
		this.windowWidth = x;
		this.windowHeight = y;
		this.brickWidth = windowWidth / 15;
		this.brickHeight = this.brickWidth / 2;
		resizeBricks(this.bricks);
		resizePaddle();
		resizeBall();
	}
	
	public void resizeBricks(ArrayList<Brick> bricks) {
		int width = this.windowWidth / 15;
		int height = width / 2;
		int topX;
		int topY;
		for (int i = 0; i < bricks.size(); i++) {
			Brick brick = bricks.get(i);
			if (i == 0) {
				topX = 0;
				topY = 0;
			}
			else {
				Brick lastBrick = bricks.get(i - 1);
				
				if (lastBrick.x + 2*(lastBrick.width) >= this.windowWidth) {
					topX = 0;
					topY = lastBrick.y + height;
				}
				else {
					topX = lastBrick.x + width;
					topY = lastBrick.y;
				}
			}
			brick.x = topX;
			brick.y = topY;
			brick.width = width;
			brick.height = height;
		}
	}
	
	public void resizePaddle() {
		double paddleWidth = this.windowWidth / 10;
		if (paddlePowerup) {
			paddleWidth = this.windowWidth / 5;
		}
		double paddleYOffset = this.windowHeight / 6;
		
		paddle.width = (int)paddleWidth;
		paddle.height = (int)paddleHeight;
		paddle.x = (int)(windowWidth/2 - paddleWidth/2);
		paddle.y = (int)(this.windowHeight - paddleYOffset);
	}
	
	public void resizeBall() {
		ball.x = paddle.getCenterX() - ballLength/2;
		ball.y = paddle.getCenterY() - paddle.height/2 - ballLength;
	}
	
	public void getPowerup() {
		this.lives += 1;
		this.paddlePowerup = true;
		this.paddle.color = Color.RED;
		this.paddle.width = this.windowWidth / 5;
	}
}

