package com.platformer.managers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.platformer.Game;
import com.platformer.util.Settings;
import com.platformer.util.box2d.LevelCollisionGenerator;

import static com.platformer.util.Settings.PPM;

/**
 *
 */
public class LevelManager {
  private static LevelManager instance = new LevelManager();

  private SpriteBatch batch;
  private OrthogonalTiledMapRenderer tiledMapRenderer;
  private Box2DDebugRenderer box2DDebugRenderer;

  public static LevelManager getInstance() {
    return instance;
  }

  private LevelManager() {
    batch = new SpriteBatch();
    box2DDebugRenderer = new Box2DDebugRenderer();
  }

  public void loadLevel(String name) {
    TiledMap tiledMap = new TmxMapLoader().load("maps/" + name + ".tmx");
    tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, batch);

    LevelCollisionGenerator lcg = new LevelCollisionGenerator(Game.world);
    lcg.createPhysics(tiledMap);
  }

  public void render() {

    Matrix4 debugMatrix = batch.getProjectionMatrix().cpy().scale(PPM, PPM, 0);

    batch.setProjectionMatrix(Game.camera.combined);

    tiledMapRenderer.setView(Game.camera);
    tiledMapRenderer.render();


    if(Settings.getInstance().debug) {
      box2DDebugRenderer.render(Game.world, debugMatrix);
    }
  }

  public void dispose() {
    box2DDebugRenderer.dispose();
  }
}
