package com.slamdunk.simplegame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;
import com.slamdunk.simplegame.SpaceGame;

public class EndGameScreen implements Screen 
{
	private BitmapFont fontGameover;
	private BitmapFont fontOther;
	private GlyphLayout layoutGameOver;
	private GlyphLayout layoutScore;
	private GlyphLayout layoutHighScore;
	private int score = 0;
	private int highScore = 0;
	
	private SpaceGame game;
	
	private boolean justTouched = false;
	private float doubleTouchSpeed = 0.5f;
	private float timer = 0;
	
	private float scale = 0.5f;
	
	public EndGameScreen(SpaceGame game , int score)
	{
		this.game = game;
		this.score = score;
		
		fontGameover = new BitmapFont(Gdx.files.internal("fonts/score.fnt"));
		fontOther = new BitmapFont(Gdx.files.internal("fonts/score.fnt"));
		
		layoutGameOver = new GlyphLayout(fontGameover, "GAMEOVER");
		layoutHighScore = new GlyphLayout();
		layoutScore = new GlyphLayout();
	}
	
	@Override
	public void show()
	{
		Preferences prefs = Gdx.app.getPreferences("spacegame");
		highScore = prefs.getInteger("highscore", 0);
		
		if(highScore < score)
		{
			prefs.putInteger("highscore", score);
			prefs.flush();
		}
		
		layoutScore.setText(fontOther, "" + score);
		layoutHighScore.setText(fontOther, "" + highScore);
		
		fontOther.getData().setScale(scale);
	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0.2f, 0.3f, 0.5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.batch.begin();
		
		renderObjects();
		
		if(Gdx.input.justTouched())
		{
			if(!justTouched)
				justTouched = true;
			else if(timer < doubleTouchSpeed)
				game.setScreen(new MainMenuScreen(game));
		}
		
		if(justTouched)
		{
			timer += delta;
		}
		
		if(timer > doubleTouchSpeed)
		{
			timer = 0;
			justTouched = false;
		}			
		
		game.batch.end();
	}
	
	private void renderObjects()
	{
		fontGameover.draw(game.batch, layoutGameOver, SpaceGame.WIDTH /2 - layoutGameOver.width/2, SpaceGame.HEIGHT - 30);
		fontOther.draw(game.batch, "HIGHSCORE: ", 100, SpaceGame.HEIGHT - 160, SpaceGame.WIDTH /2, 0, false);
		fontOther.draw(game.batch, layoutHighScore, SpaceGame.WIDTH /2 - layoutHighScore.width/2, SpaceGame.HEIGHT - 190);
		fontOther.draw(game.batch, "SCORE:", 40, SpaceGame.HEIGHT - 240, SpaceGame.WIDTH /2, 0, false);
		fontOther.draw(game.batch, layoutScore, SpaceGame.WIDTH /2 - layoutScore.width/2, SpaceGame.HEIGHT - 270);
		fontOther.draw(game.batch, "double tap to go to MainMenu", SpaceGame.WIDTH /2, 50, SpaceGame.WIDTH /2, 0, false);
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
