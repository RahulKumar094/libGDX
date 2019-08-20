package com.slamdunk.simplegame.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Explosion
{
	private static final int WIDTH = 32;
	private static final int HEIGHT = 32;
	
	private Animation<TextureRegion> anim;
	private Texture texture;
	private float x,y;
	private float frameSpeed = 0.15f;
	private float stateTime = 0f;
	private float scale = 1;
	
	public boolean destroy = false;
	
	public Explosion(float x, float y)
	{
		this.x = x - WIDTH/2;
		this.y = y - HEIGHT/2;
		
		if(texture == null)
		{
			texture = new Texture("explosion.png");
		}
		
		TextureRegion[][] region = TextureRegion.split(texture, WIDTH, HEIGHT);
		anim = new Animation<TextureRegion>(frameSpeed, region[0]);
	}
	
	public void render(Batch batch)
	{
		batch.draw((TextureRegion)anim.getKeyFrame(stateTime), x , y, scale * WIDTH, scale * HEIGHT);
	}
	
	public void update(float delta)
	{
		stateTime += delta;
		
		if(stateTime > frameSpeed * 4)
		{
			anim.setPlayMode(PlayMode.REVERSED);
			anim.setFrameDuration(frameSpeed * 2.5f);
		}
		
		if(anim.getPlayMode() == PlayMode.REVERSED && anim.getKeyFrameIndex(delta) == 2)
			destroy = true;
	}
	
	public void setScale(float scale)
	{
		this.scale = scale;
	}
	
	public void destroy()
	{
		texture.dispose();
	}
}
