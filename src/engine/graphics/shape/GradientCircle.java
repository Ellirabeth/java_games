package engine.graphics.shape;

import java.awt.Color;

import javax.media.opengl.GL2;

public class GradientCircle extends AbstractShape {

	private static double increment = 2 * Math.PI / 50;

	private int radius;
	
	private Color innerColor;

	public GradientCircle(int radius, Color outerColor, Color innerColor) {
		this.radius = radius;
		color = outerColor;
		this.innerColor = innerColor;
	}

	@Override
	public void draw(GL2 gl) {
		gl.glLoadIdentity();
		gl.glTranslated(x() + radius, y() + radius, 0);
		gl.glRotated(rot.x(), 1.0, 0, 0);
		gl.glRotated(rot.y(), 0, 1.0, 0);
		gl.glRotated(rot.z(), 0, 0, 1.0);
		gl.glColor3f(color.getRed(), color.getGreen(), color.getBlue());
		
		
		// Starting loop for drawing triangles
		for (double angle = 0; angle < 2 * Math.PI; angle += increment) {
			gl.glBegin(GL2.GL_POLYGON);
				// One vertex of each triangle is at center of circle
				gl.glColor3f(innerColor.getRed(), innerColor.getGreen(), innerColor.getBlue());
				gl.glVertex2d(0, 0);
				gl.glColor3f(color.getRed(), color.getGreen(), color.getBlue());
				// Other two vertices form the periphery of the circle
				gl.glVertex2d(Math.cos(angle) * radius, Math.sin(angle) * radius);
				gl.glVertex2d(Math.cos(angle + increment) * radius, Math.sin(angle + increment) * radius);
			gl.glEnd();
		}
	}

	@Override
	public int mapX() {
		return 0;
	}

	@Override
	public int mapY() {
		return 0;
	}

	@Override
	public int width() {
		return radius * 2;
	}

	@Override
	public int height() {
		return radius * 2;
	}

	public int radius() {
		return radius;
	}
	
	public void innerColor(Color color) {
		innerColor = color;
	}

	public void outerColor(Color color) {
		this.color = color;
	}
	
}
