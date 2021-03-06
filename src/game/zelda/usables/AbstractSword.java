package game.zelda.usables;

import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.Random;

import engine.Game;
import engine.entity.enemy.AbstractEnemy;
import engine.entity.item.AbstractItem;
import engine.entity.usable.AbstractWeapon;
import engine.graphics.sprite.SimpleSprite;
import engine.graphics.sprite.SpriteSheet;
import engine.graphics.sprite.SpriteUtils;
import engine.map.tiled.MetaTilesNumber;
import engine.sound.AbstractSound;
import game.zelda.item.Heart;
import game.zelda.item.RupeeGreen;
import game.zelda.player.Link;

public abstract class AbstractSword extends AbstractWeapon {
	
	private int phase = 0;
	
	protected SimpleSprite spriteN;
	protected SimpleSprite spriteE;
	protected SimpleSprite spriteS;
	protected SimpleSprite spriteW;
	protected SimpleSprite spriteN2;
	protected SimpleSprite spriteE2;
	protected SimpleSprite spriteS2;
	protected SimpleSprite spriteW2;
	
	protected SimpleSprite sprite;
	
	protected AbstractSound swingSound;
	
	protected AbstractSound cutSound;
	
	protected Random r = new Random();
	
	protected Link link;

	protected AbstractSword(Link link, int entityNumber, int damage) {
		super(link);
		this.link = link;
		this.damage = damage;
		SpriteSheet entities = (SpriteSheet) Game.sprites.get("entities");
		
		spriteN = new SimpleSprite(entities.get(entityNumber));
		spriteN2 = SpriteUtils.rotate(spriteN, Math.PI / 4);
		spriteS = SpriteUtils.flipVertical(spriteN);
		spriteS2 = SpriteUtils.rotate(spriteS, Math.PI / 4);
		spriteE = SpriteUtils.rotate(spriteN, Math.PI / 2);
		spriteE2 = SpriteUtils.rotate(spriteE, Math.PI / 4);
		spriteW = SpriteUtils.flipHorizontal(spriteE);
		spriteW2 = SpriteUtils.rotate(spriteW, Math.PI / 4);	
		sprite = spriteN;
		swingSound = Game.sounds.get("sword_slash1");
		cutSound = Game.sounds.get("bush_cut");
	}
	
	@Override
	public void draw(Graphics2D g) {
		if(phase > 0) {
			sprite.draw(g, 
					x() + game.map().offset().x(), 
					y() + game.map().offset().y());
			// collide with cuttable
			int offX = link.mapX();
			int offY = link.mapY();
			cut(offX - 1, offY - 1); 
			cut(offX, offY - 1); 
			cut(offX + 1, offY - 1);
			cut(offX - 1, offY); 
			cut(offX, offY); 
			cut(offX + 1, offY);
			cut(offX - 1, offY + 1); 
			cut(offX, offY + 1); 
			cut(offX + 1, offY + 1);
		}
	}

	@Override
	public void handle() {
		if(phase > 0) {
			
			// items
			for(AbstractItem item : game.map().items()) {
				if(!item.mustTouch() && rectangleCollide(item)) {
					item.consume();
				}
			}
			
			// enemies
			Iterator<AbstractEnemy> iter = game.map().enemies().iterator();
			while (iter.hasNext()) {
				AbstractEnemy enemy = iter.next();
				if (rectangleCollide(enemy)) {
					enemy.hit(damage());
				}
			}
			
			link.attackFace(link.face());
			if(phase == 1) {
				switch(link.face()) {
					case NORTH:
						sprite = spriteN2;
						locate(link.x() + 11, link.y() - 7);	
						break;
					case EAST:
						sprite = spriteE2;
						locate(link.x() + 11, link.y() + 9);
						break;
					case SOUTH:
						sprite = spriteS2;
						locate(link.x() - 5, link.y() + 11);
						break;
					case WEST:
						sprite = spriteW2;
						locate(link.x() - 11, link.y() - 5);
						break;
				}
				phase++;
			} else if(phase >= 2) {
				switch(link.face()) {
					case NORTH:
						sprite = spriteN;
						locate(link.x() -5, link.y() - 9);	
						break;
					case EAST:
						sprite = spriteE;
						locate(link.x() + 13, link.y() - 3);
						break;
					case SOUTH:
						sprite = spriteS;
						locate(link.x() + 6, link.y() + 11);
						break;
					case WEST:
						sprite = spriteW;
						locate(link.x() + -12, link.y() + 5);
						break;
				}
				phase++;
				if(phase >= 6) {
					phase = 0;
					link.face(link.face());
				}
			}
		}
	}
	
	private void cut(int x, int y) {
		if(x < 0 || y < 0 || x >= game.map().width() || y >= game.map().height()) {
			return;
		}
		if(game.map().metaLayer()[x][y].value() == MetaTilesNumber.CUTTABLE) {
			if(game.map().renderLayers()[1][x][y].rectangleCollide(sprite)) {
				cutGrass(x, y);
			}
		}
	}
	
	private void cutGrass(int x, int y) {
		int val = r.nextInt(100);
		AbstractItem item = null;
		if(val >= 90) {
			if(link.life() < link.maxLife()) {
				item = new Heart(link);
			} else {
				item = new RupeeGreen(link);
			}
		} else if(val >= 80) {
			item = new RupeeGreen(link);
		}
		if(item != null) {
			item.locate(
					x * game.map().renderLayers()[1][x][y].width() + (game.map().renderLayers()[1][x][y].width() - width()) / 2, 
					y * game.map().renderLayers()[1][x][y].height() + (game.map().renderLayers()[1][x][y].height() - height()) / 2);
			item.justDropped();
			game.map().items().add(item);
		}
		game.map().renderLayers()[1][x][y].value(0);
		game.map().metaLayer()[x][y].value(0);
		cutSound.play();
	}

	@Override
	public void use() {
		phase = 1;
		swingSound.play();
	}
	
	@Override
	public boolean using() {
		return phase > 0;
	}

	@Override
	public int width() {
		return sprite.width();
	}

	@Override
	public int height() {
		return sprite.height();
	}
	
	@Override
	public void menuDraw(Graphics2D g, int x, int y) {
		spriteN.draw(g, x, y);
	}
	
}
