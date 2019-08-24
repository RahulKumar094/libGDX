package com.slamdunk.simplegame.Utilities;

import com.badlogic.gdx.math.Vector2;

public class BoxCollider
{
	float x,y;
	int height,width;
	int tempHeight,tempWidth;
	public Vector2 centre;
	private boolean inverted = false;

	public BoxCollider(float x, float y, int width, int height)
	{
		SetInfo( x, y, width, height);
	}

	public BoxCollider(float x, float y, int width, int height, boolean inverted)
	{
		SetInfo(x, y, width, height);
		this.inverted = inverted;
	}

	private void SetInfo(float x, float y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;

		tempWidth = width;
		tempHeight = height;

		centre = getCentre(this.x, this.y);
	}

	public void Move(float x, float y)
	{
		this.x = x;
		this.y = y;

		centre = getCentre(this.x, this.y);
	}

	private Vector2 getCentre(float x, float y)
	{
		if(inverted)
			return new Vector2(x - width/2, y - height/2);
		else
			return new Vector2(x + width/2, y + height/2);
	}

	public boolean CollidesWith(BoxCollider col)
	{
		return Math.abs(col.centre.x - centre.x) < (col.width + width)/2 && Math.abs(col.centre.y - centre.y) < (col.height + height)/2;
	}

	public void SetActive(boolean active)
	{
		if(!active)
		{
			width = 0;
			height = 0;
		}
		else
		{
			width = tempWidth;
			height = tempHeight;
		}
	}

	public void setHeight(int value)
	{
		height = value;
	}
}
