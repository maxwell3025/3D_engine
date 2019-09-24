package main;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import static main.IntMath.*;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import vectors.Matrix2D;
import vectors.Point2D;
import vectors.Triangle2D;

public class MainPanel extends JPanel implements Runnable, MouseMotionListener, MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7330391458428729272L;
	Container parent;
	BufferedImage raster;
	int width;
	int height;
	boolean added = false;
	int mx = 0;
	int my = 0;
	int smx = 0;
	int smy = 0;
	Triangle2D[][] tex = new Triangle2D[40][20];
	Triangle2D[][] texcoords = new Triangle2D[40][20];
	Point2D[][] nodes = new Point2D[21][21];
	BufferedImage subject;

	public MainPanel(int width, int height) {
		setPreferredSize(new Dimension(width, height));
		addMouseListener(this);
		addMouseMotionListener(this);
		raster = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		this.width = width;
		this.height = height;
		for (int x = 0; x < 21; x++) {
			for (int y = 0; y < 21; y++) {
				nodes[x][y] = new Point2D(x * width / 20, y * height / 20);
			}
		}
		for (int x = 0; x < 20; x++) {
			for (int y = 0; y < 20; y++) {
				texcoords[2 * x][y] = new Triangle2D(new Point2D(x * 620 / 20, y * 620 / 20),
						new Point2D((x + 1) * 620 / 20, y * 620 / 20), new Point2D(x * 620 / 20, (y + 1) * 620 / 20));
				texcoords[2 * x + 1][y] = new Triangle2D(new Point2D((x + 1) * 620 / 20, (y + 1) * 620 / 20),
						new Point2D(x * 620 / 20, (y + 1) * 620 / 20), new Point2D((x + 1) * 620 / 20, y * 620 / 20));
			}
		}
		try {
			subject = ImageIO.read(new File("C:/title.png"));
		} catch (IOException e) {
		}
		new Thread(this).start();
	}

	@Override
	public void run() {
		while (getParent() == null) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
			}
		}
		parent = getParent();
		parent.addMouseListener(this);
		parent.addMouseMotionListener(this);

	}

	public void paint(Graphics g) {
		g.drawImage(raster, 0, 0, width, height, null);

	}

	public void update() {
		int[] data = new int[width * height];
		Arrays.fill(data, -1);
		raster.setRGB(0, 0, width, height, data, 0, width);
		for (int x = 1; x < 20; x++) {
			for (int y = 1; y < 20; y++) {
				nodes[x][y] = new Point2D(x * width / 20 + 20, y * height / 20 + 20);
			}
		}
		for (int x = 0; x < 20; x++) {
			for (int y = 0; y < 20; y++) {
				tex[2 * x][y] = new Triangle2D(nodes[x][y], nodes[x + 1][y], nodes[x][y + 1]);
				tex[2 * x + 1][y] = new Triangle2D(nodes[x + 1][y + 1], nodes[x][y + 1], nodes[x + 1][y]);
			}
		}
		for (int x = 0; x < 40; x++) {
			for (int y = 0; y < 20; y++) {
				drawtex(tex[x][y], 0xff000000);
			}
		}
	}

	public void drawLine(int x1, int y1, int x2, int y2, int color) {
		if (abs(x1 - x2) < abs(y1 - y2)) {
			drawLineY(x1, y1, x2, y2, color);
		} else {
			drawLineX(x1, y1, x2, y2, color);
		}

	}

	public void drawLineX(int x1, int y1, int x2, int y2, int color) {
		int minx = min(x1, x2);
		int maxx = max(x1, x2);
		int miny = x1 < x2 ? y1 : y2;
		int maxy = x1 > x2 ? y1 : y2;
		int difx = maxx - minx;
		for (int x = minx; !(x > maxx); x++) {
			float shiftx = ((float) x - minx) / difx;
			int y = (int) (maxy * shiftx + miny * (1.0f - shiftx) + 0.5f);
			if ((x < 0 != x < width) && (y < 0 != y < height)) {
				raster.setRGB(x, y, color);
			}
		}
	}

	public void drawLineY(int x1, int y1, int x2, int y2, int color) {
		int miny = min(y1, y2);
		int maxy = max(y1, y2);
		int minx = y1 < y2 ? x1 : x2;
		int maxx = y1 > y2 ? x1 : x2;
		int dify = maxy - miny;
		for (int y = miny; !(y > maxy); y++) {
			float shifty = ((float) y - miny) / dify;
			int x = (int) (maxx * shifty + minx * (1.0f - shifty) + 0.5f);
			if ((x < 0 != x < width) && (y < 0 != y < height)) {
				raster.setRGB(x, y, color);
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mx = e.getX() * width / width;
		my = e.getY() * height / height;
		update();
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		smx = e.getX() * width / width;
		smy = e.getY() * height / height;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
