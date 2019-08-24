package com.slamdunk.simplegame.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.slamdunk.simplegame.SpaceGame;
import com.slamdunk.simplegame.Utilities.BoxCollider;
import com.slamdunk.simplegame.screen.GameScreen;

public class HomingMissiles
{
    private int scaleX = 10;
    private int scaleY = 30;
    private static final float accl = 100;

    private int x,y;
    private Vector2 vel;

    private BoxCollider collider;
    private Texture blank;
    private Vector2 direction;
    public boolean destroy = false;
    private Enemy target;
    private Vector2 center;

    public HomingMissiles(int x, int y)
    {
        this.x = x;
        this.y = y;

        center = new Vector2(x + scaleX/2, y + scaleY/2);
        collider = new BoxCollider(x,y,scaleX,scaleY);
        blank = new Texture("blank.png");

        vel = new Vector2(0,0);
    }

    public void renderAndUpdate(Batch batch, float delta)
    {
        target = nearestTarget();

        center = new Vector2(x + scaleX/2, y + scaleY/2);
        direction = new Vector2(target.getCollider().centre.x - center.x, target.getCollider().centre.y - center.y);
        direction = direction.nor();

        vel.x += 3 * direction.x * accl * delta;
        vel.y += direction.y * accl * delta;

        x += vel.x * delta;
        y += vel.y * delta;

        batch.setColor(Color.YELLOW);
        batch.draw(blank,x + scaleX/2,y + scaleY/2,scaleX,scaleY);
        batch.setColor(Color.WHITE);
        collider.Move(x,y);

        if(x < -50 || x > SpaceGame.WIDTH + 50 || y < -50 || y > SpaceGame.HEIGHT + 50)
            destroy = true;
    }

    public BoxCollider getCollider()
    {
        return collider;
    }

    private Enemy nearestTarget()
    {
        Enemy target = new Enemy(SpaceGame.WIDTH/2, SpaceGame.HEIGHT + 100);
        float minDist = Vector2.dst(target.position().x,target.position().y, x, y);
        for(Enemy enemy : GameScreen.enemies)
        {
            if(enemy != null)
            {
                float dist = Vector2.dst(enemy.getCollider().centre.x, enemy.getCollider().centre.y, x, y);
                if(dist < minDist)
                {
                    target = enemy;
                    minDist = dist;
                }
            }
        }
        return target;
    }

}
