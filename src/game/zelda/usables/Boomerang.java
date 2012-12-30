package game.zelda.usables;

import java.awt.Graphics2D;
import java.util.Iterator;

import engine.FaceDirection;
import engine.entity.enemy.AbstractEnemy;
import engine.entity.item.AbstractItem;
import engine.entity.weapon.AbstractWeapon;
import engine.math.Vector2D;
import engine.sound.LoopingSound;
import engine.sound.SoundBank;
import engine.sprite.AnimatedSprite;
import engine.sprite.SpriteBank;
import engine.sprite.SpriteSheet;
import engine.sprite.SpriteUtils;

public class Boomerang extends AbstractWeapon {
	
	private AnimatedSprite sprite;
	
	private boolean using;
	
	private Vector2D acceleration = new Vector2D();
	
	private double speed;
	
	private double distance;
	
	private Vector2D start;
	
	private boolean returning = false;
	
	private LoopingSound sound;
	
	public Boomerang() {
		super();
		SpriteSheet entities = (SpriteSheet) SpriteBank.getInstance().get("entities");
		
		SpriteSheet sheet = new SpriteSheet(8, 16, 16);
		sheet.set(0, entities.get(411));
		sheet.set(1, entities.get(412));
		sheet.set(2, entities.get(413));
		sheet.set(3, SpriteUtils.rotate(sheet.get(1), -Math.PI / 2));
		sheet.set(4, SpriteUtils.rotate(sheet.get(2), -Math.PI / 2));
		sheet.set(5, SpriteUtils.rotate(sheet.get(1), -Math.PI ));
		sheet.set(6, SpriteUtils.rotate(sheet.get(2), -Math.PI ));
		sheet.set(7, SpriteUtils.rotate(sheet.get(1), Math.PI / 2));
		sprite = new AnimatedSprite(sheet, 70);
		
		using = false;
		acceleration = new Vector2D();
		start = new Vector2D();
		speed = 8;
		distance = 16 * 5;
		damage = 1;
		collisionOffset = 4;
		sound = (LoopingSound) SoundBank.getInstance().get("boomerang");
	}
	
	public void draw(Graphics2D g) {
		if(!using) {
			return;
		}
		sprite.draw(g, renderX() , renderY());
	}
	
	public void use() {
		if(using) {
			return;
		}
		sound.play();
		using = true;
		returning = false;
		
		x = game.link().x();
		y = game.link().y();
		start.x(x);
		start.y(y);
		switch(game.link().face()) {
			case NORTH:
				acceleration.set(0, -speed);
				break;
			case EAST:
				acceleration.set(speed, 0);
				break;
			case SOUTH:
				acceleration.set(0, speed);
				break;
			case WEST:
				acceleration.set(-speed, 0);
				break;
			case NORTH_EAST:
				acceleration.set(speed - 1, -(speed - 1));
				acceleration = acceleration.unit().multiply(speed);
				break;
			case SOUTH_EAST:
				acceleration.set(speed - 1, speed - 1);
				acceleration = acceleration.unit().multiply(speed);
				break;
			case SOUTH_WEST:
				acceleration.set(-(speed - 1), speed - 1);
				acceleration = acceleration.unit().multiply(speed);
				break;
			case NORTH_WEST:
				acceleration.set(-(speed - 1), -(speed - 1));
				acceleration = acceleration.unit().multiply(speed);
				break;
		}
	}
	
	public boolean using() {
		return using;
	}
	
	public void handle() {
		if(!using) {
			return;
		}
		// items
		for(AbstractItem item : game.map().items()) {
			if(!item.mustTouch() && rectangleCollide(item)) {
				item.consume();
				returning = true;
			}
		}
		// enemies
		Iterator<AbstractEnemy> iter = game.map().enemies().iterator();
		while (iter.hasNext()) {
			AbstractEnemy enemy = iter.next();
			if (rectangleCollide(enemy)) {
				enemy.hit(damage());
				returning = true;
			}
		}
		
		if(!returning) {
			x += acceleration.x();
			y += acceleration.y();
			
			double dist = Math.sqrt(Math.pow(x - start.x(), 2) + Math.pow(y - start.y(), 2));
			if(dist > distance) {
				returning = true;
			}
		} else {
			double mX = 0, mY = 0;
			if(game.link().x() - x() < -4) {
				mX = -speed;
			} else if(game.link().x() - x() > 4) {
				mX = speed;
			}
			if(game.link().y() - y() < -4) {
				mY = -speed;
			} else if(game.link().y() - y() > 4) {
				mY = speed;
			} 
			acceleration.set(mX, mY);
			x += acceleration.x();
			y += acceleration.y();
			if(rectangleCollide(game.link())) {
				using = false;
				sound.stop();
			}
		}
	}
	
	public void menuDraw(Graphics2D g, int x, int y) {
		sprite.sprites().get(0).draw(g, x, y);
	}
	
	public String menuDisplayName() {
		return "";
	}
	
	public void face(FaceDirection face) {
		
	}

	@Override
	public int width() {
		return sprite.width();
	}

	@Override
	public int height() {
		return sprite.height();
	}
	
}