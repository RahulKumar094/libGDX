package com.slamdunk.simplegame.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.slamdunk.simplegame.SpaceGame;
import com.slamdunk.simplegame.screen.GameScreen;
import com.slamdunk.simplegame.Utilities.BoxCollider;

public class Enemy
{
	private static final float ANIMATION_SPEED = 0.5f;
	protected static final int SHIP_PIXEL_WIDTH = 17;
	protected static final int SHIP_PIXEL_HEIGHT = 32;
	private static final int SPAWN_POSITION_Y = 100;
	
	protected float fullHealth = 100f;
	protected float bulletSpawnTime = 3f;
	protected int scale = 2;
	protected int width;
	protected int height;
	
	private Texture tex;
	private Texture blank;
	private Animation<TextureRegion> anim;
	protected float x;
	protected float y = SpaceGame.HEIGHT + SPAWN_POSITION_Y;
	protected BoxCollider collider;
	protected float speed = 50;
	private float stateTime = 0;
	private float bulletTime = 0f;
	protected float bulletXOffset = 2f;
	private float health;
	protected float damage = 50f;
	public boolean destroy = false;
	
	public static float fireRateMultiplier = 1f;
	public static float speedMultiplier = 1f;
	
	public Enemy(int x)
	{
		this.x = x;
		tex = new Texture("SpriteSheet_Ship.png");
		blank = new Texture("blank.png");
		TextureRegion[][] region = TextureRegion.split(tex, SHIP_PIXEL_WIDTH,SHIP_PIXEL_HEIGHT);
		anim = new Animation<TextureRegion>(ANIMATION_SPEED, region[0]);
		
		setEnemyData();
	}

	public Enemy(int x,int y)
	{
		this.x = x;
		this.y = y;
	}
	
	protected void setEnemyData()
	{
		this.health = fullHealth;
		
		width = SHIP_PIXEL_WIDTH * scale;
		height = SHIP_PIXEL_HEIGHT * scale;
		
		collider = new BoxCollider(x, y, width, height, true);
	}
	
	public void renderAndUpdate(Batch batch, float delta)
	{
		batch.draw((TextureRegion)anim.getKeyFrame(stateTime,true), x, y, 0, 0, SHIP_PIXEL_WIDTH, 
				SHIP_PIXEL_HEIGHT, scale, scale, 180);
		
		batch.setColor(Color.GREEN);
		batch.draw(blank, x - (width + 10), y - (height - 5), 5, height * 0.8f * health/ fullHealth);
		batch.setColor(Color.WHITE);

		collider.Move(x, y);
		move(batch,delta);

		if(y < -50)
			destroy = true;

		stateTime += delta;

		shootBullets(delta);

		if(health <= 0)
		{
			health = 0;
			destroy = true;
		}

		if(destroy)
			destroy();
	}
	
	private void shootBullets(float delta)
	{
		bulletTime += delta;
		
		if(bulletTime >= (bulletSpawnTime/fireRateMultiplier))
		{
			createBullets();	
			bulletTime = 0;
		}
	}
	
	public float getHealth()
	{
		return health;
	}
	
	public Vector2 position()
	{
		return new Vector2(x,y);
	}
	
	public void takeDamage()
	{
		health -= damage;
	}
	
	public void forceDestroy()
	{
		health = 0;
	}
	
	protected void createBullets()
	{
		Bullet b = new Bullet(x - (width/2 + bulletXOffset), y - height, 270);
		GameScreen.instance.enemyBullets.add(b);
	}
	
	protected void move(Batch batch,float delta)
	{
		y -= speedMultiplier * speed * delta;
	}
	
	public BoxCollider getCollider()
	{
		return collider;
	}
	
	private void destroy()
	{
		tex.dispose();
	}

	public void takeContinuousDamage(float damageRate)
	{
		health -= damageRate;
	}

	public float getHeight()
	{
		return height;
	}
}
