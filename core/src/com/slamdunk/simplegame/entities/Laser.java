package com.slamdunk.simplegame.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.slamdunk.simplegame.SpaceGame;
import com.slamdunk.simplegame.Utilities.BoxCollider;

public class Laser
{
    private static final float LIFETIME = 15;

    private int scaleX = 20;
    private int defaultY = SpaceGame.HEIGHT;
    private float scaleY;
    private float x,y;
    private Color color;
    private Texture blank;
    private BoxCollider collider;
    private boolean incSize;
    private float laserTime = 0;
    public boolean destroy = false;

    public Laser(float x, float y)
    {
        this.x = x;
        this.y = y;
        blank = new Texture("blank.png");
        scaleY = defaultY;
        collider = new BoxCollider(x - scaleX/2, y, scaleX, (int)scaleY);
    }

    public void renderAndUpdate(Batch batch, float delta, float x, float y)
    {
        laserTime += delta;
        batch.setColor(Color.RED);
        batch.draw(blank, x - scaleX/2, y, scaleX, scaleY);
        batch.setColor(Color.WHITE);
        collider.Move(x- scaleX/2, y);
        flicker(delta);

        if(laserTime >= LIFETIME)
            destroy = true;
    }

    private void flicker(float delta)
    {
        if(scaleX <= 3)
        {
            incSize = true;
        }
        else if(scaleX >= 6)
        {
            incSize = false;
        }

        if(incSize)
        {
            scaleX += 100 * delta;
        }
        else
        {
            scaleX -= 100 * delta;
        }
    }

    private void setScaleY(float value)
    {
        scaleY = value;
        collider.setHeight((int)value);

        if(value == 0)
        {
            scaleY = 0;
            collider.setHeight(0);
        }
    }

    public void collidingAtPosition(float posY, float objHeight)
    {
        setScaleY((int)(Math.abs(posY - y) - objHeight) + 15);//buffer
    }

    public void setDefault()
    {
        setScaleY(defaultY);
    }

    public BoxCollider getCollider()
    {
        return collider;
    }

    public void renewLaser()
    {
        laserTime = 0;
    }
}
