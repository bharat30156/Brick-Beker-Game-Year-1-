import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class paddle extends Rectangle {
	
	public Color color; 
	
	public paddle(int x, int y, int width, int height, Color color) {
		super(x, y, width, height);
		this.color = color;
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(this.color);
		g.fillRect(this.x, this.y, this.width, this.height);
	}
}

