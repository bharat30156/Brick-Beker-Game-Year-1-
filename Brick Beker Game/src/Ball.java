import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Ellipse2D;

public class Ball extends Ellipse2D.Double {
	
	public double lastX;
	public double lastY;
	
	public double length;
	
	public Color color;
	
	// positive xSpeed = moving right, positive ySpeed = moving up
	public double xSpeed;
	public double ySpeed;
	
	Ball(double x, double y, double length, double xSpeed, double ySpeed, Color color)
	{
		super(x, y, length, length);
		
		this.length = length;
		this.color = color;
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
	}
	
	public void paintComponents(Graphics g)
	{
		g.setColor(this.color);
		g.fillOval((int)(x), (int)(y), (int)(this.length), (int)(this.length));
	}

}
