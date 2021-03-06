package game.cubes;

import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;

import engine.Game;
import engine.GameFactory;
import engine.GameStateEnum;
import game.cubes.gamestates.MainGameLoop;

public class Cubes extends Game {
	
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		GameFactory.set(new Cubes());
		Game game = GameFactory.get();
		game.setTitle("Cubes");
		game.start();
		game.run();
	}
	
	private Cubes() {
		super(460, 350, 2);
		
	}
	
	/**
	 * load global resources
	 */
	@Override
	public void start() {
		super.start();
		// load globals
		
		// load sprites
		// Game.sprites.set("entities", new SpriteSheet("sprites/entity/zelda/entities.png", 16, 16));

		// load fonts
		Game.fonts.set("small", new Font("Serif", Font.BOLD, 10));
		Game.fonts.set("large", new Font("Serif", Font.BOLD, 40));
		
		// Game.sounds.set("title_screen", new LoopingSound("sounds/bg/LoZ Oracle of Seasons Intro + Title Screen.WAV"));

		// Game.usables.set("sword1", new SwordLevel1());

		gameLoops.put(GameStateEnum.MAIN, new MainGameLoop());

		gameState = GameStateEnum.MAIN;
		gameLoops.get(GameStateEnum.MAIN).start();
		
	}
	
	public void run() {
		while(true) {
			switch(gameState) {
				case TITLE_SCREEN:
					break;
				case MAIN:
					gameLoops.get(GameStateEnum.MAIN).run();
					break;
				case ITEM_SCREEN:
					break;
				case PAUSED:
					break;		
				case DEAD:
					break;
				case END:
					System.exit(0);
					break;
			}
		}
	}
		
}
