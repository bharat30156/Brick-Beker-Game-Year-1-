import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.TimerTask;

import javax.swing.Timer;

public class Brick extends Rectangle {
	
	public boolean broken;
	public Color color;
	public int colorNum;
	
	
	Brick(int x, int y, int width, int height, Color color, boolean broken)
	{
		super(x, y, width, height);
		
		this.color = color;
		this.broken = broken;
		
		int delay = 200;  // milliseconds
		ActionListener taskPerformer = new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				colorNum = (int)(Math.floor(Math.random() * 5));
			}
		};
		new Timer(delay, taskPerformer).start();
	}
	
	public void paintComponents(Graphics g , ArrayList<Color> colors)
	{
		if(!this.broken)
		{
			if(this.color == null)
			{
				g.setColor(colors.get(colorNum));
			}
			else
			{
				g.setColor(this.color);
			}
			g.fillRect(x, y, width - 1, height - 1 );
		}
	}
	
}
