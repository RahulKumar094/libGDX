package com.slamdunk.simplegame.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class Star
{
    private static final int maxSize = 12;
    private static final int twinkleSpeed = 25;
    private static final int moveSpeed = 105;

    private int layer;
    private int x;
    private int y;
    private float sizeMax;
    private float sizeMin;
    private float size;
    private boolean incSize = false;
    private float speed;

    private Vector2 startPos;

    private Texture texture;

    public Star(int layer, int x, int y)
    {
        this.layer = layer;
        this.x = x;
        this.y = y;

        texture = new Texture("star.png");

        sizeMax = maxSize;
        speed = moveSpeed;

        if(layer != 0)
        {
            sizeMax = 0.7f * maxSize;
            speed = 0.5f * moveSpeed;
        }

        sizeMin = sizeMax/2;
        size = sizeMin;

        startPos = new Vector2(x,y);
    }

    public void renderAndUpdate(Batch batch, float delta)
    {
        batch.draw(texture,x - size/2,y - size/2, size, size);

        if(y >= 20)
            y -= speed * delta;
        else
            {
                x = (int)startPos.x;
                y = (int)startPos .y;
            }

        twinkle(delta);
    }

    private void twinkle(float delta)
    {
        if(size <= sizeMin)
        {
            incSize = true;
        }
        else if(size >= sizeMax)
        {
            incSize = false;
        }

        if(incSize)
        {
            size += twinkleSpeed * delta;
        }
        else
        {
            size -= twinkleSpeed * delta;
        }
    }
}
