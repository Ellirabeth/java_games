package game.zelda.enemy;

import engine.Game;
import engine.ai.RandomAIStrategy;
import engine.entity.enemy.AbstractEnemy;
import engine.graphics.sprite.AnimatedSprite;
import engine.graphics.sprite.SpriteSheet;
import game.zelda.item.RupeeBlue;
import game.zelda.player.Link;

public class LikeLike extends AbstractEnemy {

	public LikeLike(Link link, int x, int y) {
		setAIStrategy(new RandomAIStrategy(this, 2500));
		SpriteSheet sheet = (SpriteSheet) Game.sprites.get("entities");
		spriteCurrent = new AnimatedSprite(sheet.range(462,464), 230);
		spriteCurrent.reverseCycle(true);
		spriteE = spriteW = spriteN = spriteS = spriteCurrent;
		locate(x * game.map().tileWidth(), y * game.map().tileHeight());
		damage = 0.5;
		life = 2;
		maxLife = 2;
		dropItemProbability = 60;
		dropItems.add(new RupeeBlue(link));
	}

	@Override
	public void handle() {
		super.handle();
		strategy.handle();
		
	}

}
