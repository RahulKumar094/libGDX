package com.slamdunk.simplegame.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.slamdunk.simplegame.SpaceGame;
import com.slamdunk.simplegame.screen.GameScreen;
import com.slamdunk.simplegame.Utilities.BoxCollider;

public class Asteroid
{
	private static final int WIDTH = 32;
	private static final int HEIGHT = 32;
	private static final int SPEED = 150;
	public float x;
	public float y = SpaceGame.HEIGHT + GameScreen.SHIP_HEIGHT;
	private Texture texture;
	private BoxCollider collider;
	public boolean destroy = false;
	public Vector2 center;
	public static float speedMultiplier = 1f;
	private float rotationInRadian;

	public Asteroid(float x, float rotation)
	{
		this.x = x;
		collider = new BoxCollider(x, y, WIDTH, HEIGHT);

		if(texture == null)
			texture = new Texture("asteroid.png");

		rotationInRadian = (float)(rotation * (Math.PI/180));
	}

	public void render(Batch batch)
	{
		batch.draw(texture, x , y, WIDTH, HEIGHT);
	}

	public void update(float deltaTime)
	{
		y -= speedMultiplier * SPEED * deltaTime;
		x += speedMultiplier * Math.cos(rotationInRadian) * SPEED * deltaTime;

		collider.Move(x, y);
		center = new Vector2(x + WIDTH/2, y + HEIGHT/2);

		if(y < -50)
			destroy = true;
	}

	public BoxCollider getCollider()
	{
		return collider;
	}

	public void destroy()
	{
		texture.dispose();
	}
}
