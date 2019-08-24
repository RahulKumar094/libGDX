package com.slamdunk.simplegame.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.slamdunk.simplegame.SpaceGame;
import com.slamdunk.simplegame.Utilities.BoxCollider;

import java.util.HashMap;
import java.util.Map;

public class PowerUps
{
    public enum PowerType
    {
        LASER(0),
        HOAMINGMISSILE(1),
        SHIELD(2);

        private int value;
        public static final Map<Integer, PowerType> map = new HashMap<>();

        private PowerType(int value) {
            this.value = value;
        }

        static {
            for (PowerType powerType : PowerType.values()) {
                map.put(powerType.value, powerType);
            }
        }

        public static PowerType valueOf(int powerType) {
            return (PowerType) map.get(powerType);
        }
    }


    private int scaleSize = 20;
    private int speed = 90;
    public PowerType type;
    private int x,y;
    private Color color;
    private Texture blank;
    private BoxCollider collider;
    public boolean destroy;

    public PowerUps(PowerType type, int x)
    {
        this.type = type;
        this.x = x;
        this.y = SpaceGame.HEIGHT + 100;

        blank = new Texture ("blank.png");

        if(type == PowerType.LASER)
        {
            color = Color.RED;
        }
        else if(type == PowerType.HOAMINGMISSILE)
        {
            color = Color.YELLOW;
        }
        else if(type == PowerType.SHIELD)
        {
            color = Color.GREEN;
        }

        collider = new BoxCollider(this.x,this.y, scaleSize, scaleSize, true);
    }

    public void renderAndUpdate(Batch batch, float delta)
    {
        batch.setColor(color);
        batch.draw(blank,x,y,scaleSize,scaleSize);
        batch.setColor(Color.WHITE);

        y -= speed * delta;
        collider.Move(x,y);

        if(y < -50)
            destroy = true;
    }

    public BoxCollider getCollider()
    {
        return collider;
    }
}
