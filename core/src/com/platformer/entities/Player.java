package com.platformer.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.platformer.components.ComponentFactory;
import com.platformer.components.PositionComponent;
import com.platformer.components.ScreenPositionComponent;
import com.platformer.util.box2d.BodyGenerator;
import com.platformer.util.box2d.Box2dUtil;

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

    Vector2 position = Box2dUtil.toBox2Vector(new Vector2(500, 700));
    Body body = BodyGenerator.createPlayer();
    body.setTransform(position, 0);
    ComponentFactory.addBodyComponent(this, body);
    positionComponent.setPosition(position);
    add(new ScreenPositionComponent(500, 500));
  }

  public Vector2 getCenter() {
    return null;
  }
}
