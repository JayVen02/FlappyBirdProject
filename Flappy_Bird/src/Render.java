import java.awt.Graphics;

import javax.swing.JPanel;

public class Render extends JPanel{

	private static final long serialVersionUID = 1L;

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		launcher.flappyBird.repaint(g);
	}
}
