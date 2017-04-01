package com.platformer.entities;

import com.badlogic.gdx.math.Vector2;
import com.platformer.components.ComponentFactory;
import com.platformer.components.PositionComponent;
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
    createComponents();
  }

  public void createComponents() {
    PositionComponent positionComponent = ComponentFactory.addPositionComponent(this);
    positionComponent.setPosition(500, 500);
    add(new ScreenPositionComponent(500, 500));
  }

  public Vector2 getCenter() {
    return null;
  }
}
