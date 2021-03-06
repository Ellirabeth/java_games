package engine.graphics.shape;

import javax.media.opengl.GL2;

public class EmptyHalfCircle extends AbstractShape {
		
	private int radius;
	
	public EmptyHalfCircle(int radius) {
		this.radius = radius;
	}

	@Override
	public void draw(GL2 gl) {
		gl.glLoadIdentity();
		gl.glTranslated(x() + radius, y() + radius, 0);
		gl.glRotated(rot.x(), 1.0, 0, 0);
		gl.glRotated(rot.y(), 0, 1.0, 0);
		gl.glRotated(rot.z(), 0, 0, 1.0);
		gl.glColor3f(color.getRed(), color.getGreen(), color.getBlue());
		
		gl.glBegin(GL2.GL_LINE_LOOP); 
		   for (int i = 0; i <= 100; i++) {
		      float angle = (float) (i * Math.PI / 100.0);
		      gl.glVertex2d((Math.cos(angle) * radius), y() + (Math.sin(angle) * radius));
		   }
		gl.glEnd();
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

}
