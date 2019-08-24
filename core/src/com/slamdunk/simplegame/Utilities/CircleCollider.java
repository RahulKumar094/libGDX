package com.slamdunk.simplegame.Utilities;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Rahul_K on 8/22/2019.
 */

public class CircleCollider
{
    public Vector2 center;
    public float radius;

    private float tempRadius;
    private Vector2 pointOfCollision = Vector2.Zero;
    private float sloap = 0;
    private boolean colliding = false;

    public CircleCollider(Vector2 center, float radius)
    {
        this.center = center;
        this.radius = radius;

        tempRadius = radius;
    }

    public void Move(Vector2 center)
    {
        this.center = center;
    }

    public boolean CollideWith(CircleCollider col)
    {
        return Math.sqrt(Math.pow(col.center.x - center.x,2) + Math.pow(col.center.y - center.y,2)) <= col.radius + radius;
    }

    public boolean CollideWith(BoxCollider col)
    {
        if(Math.abs(col.centre.x - center.x) < (col.width + 2*radius)/2 && Math.abs(col.centre.y - center.y) < (col.height + 2*radius)/2)
        {
            if(!colliding)
            {
                colliding = true;
                sloap = (col.centre.y - center.y)/(col.centre.x - center.x);
            }
            return true;
        }
        else
        {
            colliding = false;
            return false;
        }
    }

    private void getCollisionPoint()
    {
    }

    public void SetActive(boolean active)
    {
        if(!active)
            radius = 0;
        else
            radius = tempRadius;
    }

}
