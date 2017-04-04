package com.platformer.managers;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.platformer.util.box2d.LevelPhysicsGenerator;

/**
 *
 */
public class LevelManager {
  private static LevelManager instance = new LevelManager();

  public static LevelManager getInstance() {
    return instance;
  }

  private LevelManager() {
  }

  public OrthogonalTiledMapRenderer loadLevel(SpriteBatch batch, World world, RayHandler rayHandler, String name) {
    TiledMap tiledMap = new TmxMapLoader().load("maps/" + name + ".tmx");
    OrthogonalTiledMapRenderer tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, batch);

    LevelPhysicsGenerator lcg = new LevelPhysicsGenerator();
    lcg.createPhysics(tiledMap, world, rayHandler);

    return tiledMapRenderer;
  }

}