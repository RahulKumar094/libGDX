package com.slamdunk.simplegame.screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.slamdunk.simplegame.GameCamera;
import com.slamdunk.simplegame.SpaceGame;

public class MainMenuScreen implements Screen
{
	private static final int BTN_EXIT_HEIGHT = 120;
	private static final int BTN_EXIT_WIDTH = 200;
	private static final int BTN_PLAY_HEIGHT = 120;
	private static final int BTN_PLAY_WIDTH = 200;
	private static final int BTN_EXIT_YPOS = 150;
	private static final int BTN_PLAY_YPOS = 300;

	private static final int BTN_TOGGLE_HEIGHT = 50;
	private static final int BTN_TOGGLE_WIDTH = 50;
	private static final int BTN_TOGGLE_YPOS = 450;
	
	Texture btnExitActive;
	Texture btnExitInactive;
	Texture btnPlayActive;
	Texture btnPlayInactive;
	Texture blank;
	SpaceGame game;
	boolean enableYAxis = false;
	
	public MainMenuScreen(SpaceGame game)
	{
		this.game = game;
		btnExitActive = new Texture("Exit_Active.png");
		btnExitInactive = new Texture("Exit_Inactive.png");
		btnPlayActive = new Texture("Play_Active.png");
		btnPlayInactive = new Texture("Play_Inactive.png");
		blank = new Texture("blank.png");
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0.2f, 0.3f, 0.5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.batch.begin();
		
		buttonInput();
		toggleInput();
		
		game.batch.end();
	}

	private void toggleInput()
	{
		int actY = (int)SpaceGame.camera.getWorldPoints().y;

		if(SpaceGame.camera.getWorldPoints().x > SpaceGame.WIDTH/2 - BTN_TOGGLE_WIDTH/2  && SpaceGame.camera.getWorldPoints().x < SpaceGame.WIDTH/2 - BTN_TOGGLE_WIDTH/2 + BTN_TOGGLE_WIDTH
			&& actY > BTN_TOGGLE_YPOS && actY < BTN_TOGGLE_YPOS + BTN_TOGGLE_HEIGHT && Gdx.input.justTouched())
		{
			enableYAxis = !enableYAxis;
		}

		if(enableYAxis)
			game.batch.setColor(Color.GREEN);
		else
			game.batch.setColor(Color.RED);

		game.batch.draw(blank, SpaceGame.WIDTH/2 - BTN_TOGGLE_WIDTH/2, BTN_TOGGLE_YPOS, BTN_TOGGLE_WIDTH, BTN_TOGGLE_HEIGHT);
		game.batch.setColor(Color.WHITE);
	}
	
	private void buttonInput()
	{
		int exitX = SpaceGame.WIDTH/2 - BTN_EXIT_WIDTH/2;
		int actY = (int)SpaceGame.camera.getWorldPoints().y;
		
		if(SpaceGame.camera.getWorldPoints().x > exitX  && SpaceGame.camera.getWorldPoints().x < exitX + BTN_EXIT_WIDTH
				&& actY > BTN_EXIT_YPOS && actY < BTN_EXIT_YPOS + BTN_EXIT_HEIGHT && Gdx.input.isTouched())
		{
			game.batch.draw(btnExitActive, exitX, BTN_EXIT_YPOS, BTN_EXIT_WIDTH, BTN_EXIT_HEIGHT);
			if(Gdx.input.justTouched())
				Gdx.app.exit();
		}
		else
			game.batch.draw(btnExitInactive, exitX, BTN_EXIT_YPOS, BTN_EXIT_WIDTH, BTN_EXIT_HEIGHT);
		
		exitX = SpaceGame.WIDTH/2 - BTN_PLAY_WIDTH/2;
		if(SpaceGame.camera.getWorldPoints().x > exitX && SpaceGame.camera.getWorldPoints().x < exitX + BTN_PLAY_WIDTH
				&& actY > BTN_PLAY_YPOS && actY < BTN_PLAY_YPOS + BTN_PLAY_HEIGHT && Gdx.input.isTouched())
		{
			game.batch.draw(btnPlayActive, exitX, BTN_PLAY_YPOS, BTN_PLAY_WIDTH, BTN_PLAY_HEIGHT);
			if(Gdx.input.justTouched())
			{
				this.dispose();
				game.setScreen(new GameScreen(game, enableYAxis));
			}
		}
		else
			game.batch.draw(btnPlayInactive, exitX, BTN_PLAY_YPOS, BTN_PLAY_WIDTH, BTN_PLAY_HEIGHT);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
