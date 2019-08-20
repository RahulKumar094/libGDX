package com.slamdunk.simplegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * Created by Rahul_K on 8/17/2019.
 */

public class GameCamera
{
    public OrthographicCamera cam;
    public StretchViewport viewport;

    public GameCamera(int width, int height)
    {
        cam = new OrthographicCamera();
        viewport = new StretchViewport(width, height, cam);
        viewport.apply();
        cam.position.set(width/2, height/2, 0);
        cam.update();
    }

    public Matrix4 combined()
    {
        return cam.combined;
    }

    public void update(int width, int height)
    {
        viewport.update(width, height);
    }

    public Vector3 getWorldPoints()
    {
        Vector3 touchPoints = new Vector3(Gdx.input.getX(0), Gdx.input.getY(0), 0);
        Vector3 unProjected = cam.unproject(touchPoints);
        return new Vector3(unProjected.x, unProjected.y, 0);
    }
}
