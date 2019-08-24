package com.slamdunk.simplegame.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.slamdunk.simplegame.SpaceGame;
import com.slamdunk.simplegame.Utilities.BoxCollider;

public class Bullet 
{
	private static final int WIDTH = 3;
	private static final int HEIGHT = 10;
	private static final int SPEED = 250;
	private float x;
	private float y;
	private Texture texture;
	private BoxCollider collider;
	
	public float rotation;
	public float rotationInRadian;
	public boolean destroy = false;
	
	public Bullet(float x, float y, float rotation)
	{
		this.x = x;
		this.y = y;
		this.rotation = rotation;
		
		rotationInRadian = (float)(rotation * (Math.PI/180));
		collider = new BoxCollider(x, y, WIDTH, HEIGHT);
		
		if(texture == null)
			texture = new Texture("bullet.png");
	}
	
	public Vector2 position()
	{
		return new Vector2(x,y);
	}
	
	public void render(Batch batch)
	{
		batch.draw(texture, x, y, WIDTH, HEIGHT);
	}
	
	public void update(float deltaTime)
	{
		y += Math.sin(rotationInRadian) * SPEED * deltaTime;
		x += Math.cos(rotationInRadian) * SPEED * deltaTime;
		
		collider.Move(x, y);
		
		if(y > SpaceGame.HEIGHT || x > SpaceGame.WIDTH || y < 0 || x < 0)
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