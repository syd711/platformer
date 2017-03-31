package com.platformer.entities;

import com.badlogic.gdx.math.Vector2;
import com.platformer.components.ComponentFactory;
import com.platformer.components.ScreenPositionComponent;

/**
 * The player with all ashley components.
 */
public class Player extends GameEntity {
  private static Player instance = new Player();


  public static Player getInstance() {
    return instance;
  }

  public Player() {
    instance = this;
    createComponents();
  }

  public void createComponents() {
    ComponentFactory.addPositionComponent(this);
    add(new ScreenPositionComponent(0, 0));
  }

  public Vector2 getCenter() {
    return null;
  }
}
