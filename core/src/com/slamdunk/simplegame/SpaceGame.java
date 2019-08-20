package com.slamdunk.simplegame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.slamdunk.simplegame.screen.MainMenuScreen;

public class SpaceGame extends Game
{
	public SpriteBatch batch;
	public static final int WIDTH = 480;
	public static final int HEIGHT = 720;
	public static GameCamera camera;
	
	@Override
	public void create () 
	{
		camera = new GameCamera(WIDTH, HEIGHT);
		batch = new SpriteBatch();
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render ()
	{
		batch.setProjectionMatrix(camera.combined());
		super.render();
	}
	
	@Override
	public void resize(int width, int height)
	{
		camera.update(width, height);
		super.resize(width, height);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		super.dispose();
	}
	
}
