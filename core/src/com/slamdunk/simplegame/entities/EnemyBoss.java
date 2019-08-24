package com.slamdunk.simplegame.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.slamdunk.simplegame.SpaceGame;
import com.slamdunk.simplegame.screen.GameScreen;

public class EnemyBoss extends Enemy
{
	private static int SHIELD_HEALTH = 200;
	private static final float TIME_TO_SHIELD = 5;
	private float speedX = 50f;
	private boolean moveRight = false;
	private float lifeTime = 0;
	private Shield shield;
	private Vector2 center;

	public EnemyBoss(int x)
	{
		super(x);
		scale = 4;
		bulletSpawnTime = 5f;
		damage = 30f;
		fullHealth = 300f;
		
		setEnemyData();
	}
	
	@Override
	protected void move(Batch batch,float delta)
	{
		if(y > SpaceGame.HEIGHT - 10)
			y -= speedMultiplier * speed * delta;
		
		if(x <= width + 10)
			moveRight = true;
		else if(x >= SpaceGame.WIDTH)
			moveRight = false;
		
		if(moveRight)
			x += speedMultiplier * speedX * delta;
		else
			x -= speedMultiplier * speedX * delta;

		//Shield
		lifeTime += delta;
		if(lifeTime >= TIME_TO_SHIELD)
		{
			createShield();
			lifeTime = -120;
		}

		center = new Vector2(x - SHIP_PIXEL_WIDTH * scale/2, y - SHIP_PIXEL_HEIGHT * scale/2);

		if(shield != null)
		{
			if(!shield.destroy)
			{
				shield.renderAndUpdate(batch, center);
			}
			else
				shield = null;
		}
	}

	private void createShield()
	{
		if(shield == null)
		{
			shield = new Shield(center, 80, SHIELD_HEALTH,false);
		}
		else
			shield.renewShield();
	}
	
	@Override
	protected void createBullets()
	{
		Bullet b1 = new Bullet(x - (width/2 + bulletXOffset), y - height, 285);
		Bullet b2 = new Bullet(x - (width/2 + bulletXOffset), y - height, 270);
		Bullet b3 = new Bullet(x - (width/2 + bulletXOffset), y - height, 255);
		
		GameScreen.instance.enemyBullets.add(b1);
		GameScreen.instance.enemyBullets.add(b2);
		GameScreen.instance.enemyBullets.add(b3);
	}

	public Shield getShield()
	{
		return shield;
	}
	
}
