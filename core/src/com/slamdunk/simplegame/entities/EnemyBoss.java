package com.slamdunk.simplegame.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.slamdunk.simplegame.SpaceGame;
import com.slamdunk.simplegame.screen.GameScreen;
import com.slamdunk.simplegame.tools.BoxCollider;

public class EnemyBoss extends Enemy
{
	private float speedX = 50f;
	private boolean moveRight = false;
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
	protected void move(float delta)
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
	
}
