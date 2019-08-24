package com.slamdunk.simplegame.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.slamdunk.simplegame.Utilities.CircleCollider;

import static com.slamdunk.simplegame.screen.GameScreen.SHIP_HEIGHT;

/**
 * Created by Rahul_K on 8/22/2019.
 */

public class Shield
{
    private static int FULLHEALTH = 100;

    private int health;
    private float radius;
    private Vector2 center;
    private Texture blank;
    private CircleCollider collider;
    private Color color;
    private boolean barAtLeft;
    public boolean destroy = false;

    public Shield(Vector2 center, float radius, int health, boolean barAtLeft)
    {
        FULLHEALTH = health;
        this.health = FULLHEALTH;
        this.center = center;
        this.radius = radius;
        this.barAtLeft = barAtLeft;

        blank = new Texture("blank.png");
        collider = new CircleCollider(center, radius);
        color = Color.GREEN;
    }

    public void renderAndUpdate(Batch batch, Vector2 center)
    {
        batch.setColor(color);
        for(int i = 0; i <= 360; i++)
        {
            float x = (float)Math.cos(i) * radius + center.x;
            float y = (float)Math.sin(i) * radius + center.y;
            batch.draw(blank, x, y);
        }
        batch.setColor(Color.WHITE);
        collider.Move(center);

        if(barAtLeft)
            batch.draw(blank, center.x - radius - 5, center.y - radius/2, 5, SHIP_HEIGHT * 0.8f * health / FULLHEALTH);
        else
            batch.draw(blank, center.x + radius + 5, center.y - radius/2, 5, SHIP_HEIGHT * 0.8f * health / FULLHEALTH);

        if(health <= 0)
        {
            destroy = true;
        }
    }

    public CircleCollider getCollider()
    {
        return collider;
    }

    public void takeDamage(float damage)
    {
        health -= damage;
    }

    public void renewShield()
    {
        health = FULLHEALTH;
    }

    public void takeContinuousDamage(float damageRate)
    {
        health -= damageRate;
    }
}
