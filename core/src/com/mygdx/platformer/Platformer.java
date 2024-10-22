package com.mygdx.platformer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.platformer.Screens.PlayScreen;

public class Platformer extends Game {

	// Scaling constants
	public static final int TILE_SIZE = 129;
	public static final int V_WIDTH = 13;
	public static final int V_HEIGHT = 10;
	public static final float PPM = 100;
	public static final float GRAVITY = -100f;

	// Collision bits
	public static final short NOTHING_BIT = 0;
	public static final short GROUND_BIT = 1;
	public static final short PLAYER_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short COIN_BIT = 8;
	public static final short DESTROYED_BIT = 16;
	public static final short OBJECT_BIT = 32;
	public static final short ENEMY_BIT = 64;
	public static final short ENEMY_HEAD_BIT = 128;
	public static final short ITEM_BIT = 256;
	public static final short PLAYER_HEAD_BIT = 512;
	public static final short PROJECTILE_BIT = 1024;
	public static final short CHEST_BIT = 2048;

	public SpriteBatch batch;

	public static AssetManager manager;

	public static float getvWidth() {
		return (V_WIDTH * TILE_SIZE) / PPM;
	}

	public static float getvHeight() {
		return (V_HEIGHT * TILE_SIZE) / PPM;
	}

	public static float getTileMultiplier(float multiplier) {
		return (multiplier * TILE_SIZE) / PPM;
	}
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		loadToAssetManager();

		setScreen(new PlayScreen(this));
	}

	public void loadToAssetManager() {
		manager = new AssetManager();
		manager.load("audio/music/mario_music.ogg", Music.class);
		manager.load("audio/sounds/coin.wav", Sound.class);
		manager.load("audio/sounds/bump.wav", Sound.class);
		manager.load("audio/sounds/breakblock.wav", Sound.class);
		manager.load("audio/sounds/powerup_spawn.wav", Sound.class);
		manager.load("audio/sounds/powerup.wav", Sound.class);
		manager.finishLoading();
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();
		manager.dispose();
	}
}
