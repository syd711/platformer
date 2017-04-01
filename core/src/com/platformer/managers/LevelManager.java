package com.platformer.managers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.platformer.Game;
import com.platformer.util.box2d.LevelCollisionGenerator;

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

  public OrthogonalTiledMapRenderer loadLevel(SpriteBatch batch, String name) {
    TiledMap tiledMap = new TmxMapLoader().load("maps/" + name + ".tmx");
    OrthogonalTiledMapRenderer tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, batch);

    LevelCollisionGenerator lcg = new LevelCollisionGenerator(Game.world);
    lcg.createPhysics(tiledMap);

    return tiledMapRenderer;
  }

}