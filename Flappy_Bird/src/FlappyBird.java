import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

public class FlappyBird implements ActionListener, KeyListener{

	private final int WIDTH = 800, HEIGHT = 800;

	public Render render;

	public Rectangle bird; //This is the bird

	public int ticks, yMotion, score;

	public ArrayList<Rectangle> columns; //These are the green pipes

	public Random rand;

	public boolean gameOver, started; //Started so we know that the game has started

	static final int DELAY = 20; //Speed of the game

	public FlappyBird() {

			JFrame jframe = new JFrame();
			Timer timer = new Timer(DELAY, this);

			render = new Render();
			rand = new Random();

			jframe.add(render);
			jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			jframe.setSize(WIDTH, HEIGHT);
			jframe.addKeyListener(this);
			jframe.setLocationRelativeTo(null);
			jframe.setResizable(false);
			jframe.setTitle("Flappy Bird Project");
			jframe.setVisible(true);

			bird = new Rectangle(WIDTH / 2 - 60, HEIGHT / 2 - 10, 20, 20); //coordinate of the Bird in the starting

			columns = new ArrayList<Rectangle>();
			//We add the first 4 pipes(2 up | 2 down)
			addColumn(true);
			addColumn(true);
			addColumn(true);
			addColumn(true);

			timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		ticks++;

		int speed = 10;

		if (started) {
			for (int i = 0; i < columns.size(); i++) {
				columns.get(i).x -= speed;
			}

			for (int i = 0; i < columns.size(); i++) {
				if (columns.get(i).x + columns.get(i).width < 0) {
					columns.remove(columns.get(i)); //If the pipe left the right of the screen then remove it

					//Add only one pipe as the top and the bottom pipes will be at the same columns place
					if (columns.get(i).y == 0) {
						addColumn(false);
					}
				}
			}
			if (ticks % 2 == 0 && yMotion < 15) {
				yMotion += 2;
			}

			bird.y += yMotion;

			for (Rectangle column : columns) {

				if (column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 - 10 &&
						bird.x + bird.width / 2 < column.x + column.width / 2 + 10) {
					score++;
				}

				if (column.intersects(bird)) { //if the bird hit a pipe
					gameOver = true;

					if (bird.x <= column.x) {
						bird.x = column.x - bird.width;
					} else {
						if (column.y != 0) {
							bird.y = column.y - bird.height;
						} else if (bird.y < column.height) {
							bird.y = column.height;
						}
					}

					bird.x = column.x - bird.width;
				}
			}


				if (bird.y > HEIGHT - 120 || bird.y < 0) {
					gameOver = true;
				}

				if (bird.y + yMotion >= HEIGHT - 120) {
					bird.y = HEIGHT - 120 - bird.height;
					gameOver = true;

				}
			}
			render.repaint();
		}


	public void addColumn(boolean start) {
		int space = 250;
		int width = 100;
		int height = 50 + rand.nextInt(300); //minimum height 50, Maximum 300


		if(start) {
			columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));

		}

		else {

			columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
		}
	}

	public void repaint(Graphics g) {
		g.setColor(Color.cyan); //Set sky color
		g.fillRect(0, 0, WIDTH, HEIGHT);

		g.setColor(Color.orange); //Set underground color
		g.fillRect(0, HEIGHT - 120, WIDTH, 120);

		g.setColor(Color.green); //Set ground color
		g.fillRect(0, HEIGHT - 120, WIDTH, 20);

		g.setColor(Color.red); //Set "bird" color
		g.fillRect(bird.x, bird.y, bird.width, bird.height);

		for(Rectangle column : columns) {
			paintColumn(g, column); //Set pipes color
		}

		g.setColor(Color.white); //Set starting message color
		g.setFont(new Font("SAN_SERIF", Font.BOLD, 50));

		if(!started) {
			g.drawString("Press [Space] to begin!", 120, HEIGHT / 2 - 50);
		}
		if(gameOver) { //Set game over messages
			if(bird.y < 0){
				bird.y = 0;
			}
			g.setFont(new Font("Arial", 1, 100));
			g.drawString("Game Over!", 110, HEIGHT / 2 - 50);
			g.setFont(new Font("Arial", 1, 50));
			g.drawString("Your score is: " + String.valueOf(score), 225, HEIGHT / 2);
		}
		if(!gameOver && started) { //Set score color
			g.setFont(new Font("Arial", 1, 100));
			g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
		}
	}

	public void paintColumn(Graphics g, Rectangle column) {
		g.setColor(Color.green.darker());
		g.fillRect(column.x, column.y, column.width, column.height);
	}

	public void jump() {
		if(gameOver) { //If the game is over then by pressing [Space] the game will restart
			bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
			columns.clear();
			yMotion = 0;
			score = 0;

			addColumn(true);
			addColumn(true);
			addColumn(true);
			addColumn(true);

			gameOver = false;
		}

		if(!started) { //Start the game
			started = true;
		}
		else if(!gameOver) { //Move the bird upwards (like it's jumping)
			if(yMotion > 0) { //If bird is outside the top of the screen
				yMotion = 0; //You have lost so make it go to the top and not outside the screen
			}
			yMotion -= 10; //Else move it 10 px upwards
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if(arg0.getKeyCode() == KeyEvent.VK_SPACE) {
			jump();
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {}
}

