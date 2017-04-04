package com.platformer.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.platformer.components.BodyComponent;
import com.platformer.components.ComponentFactory;
import com.platformer.components.PositionComponent;
import com.platformer.components.ScreenPositionComponent;
import com.platformer.managers.InputManager;
import com.platformer.util.box2d.BodyGenerator;
import com.platformer.util.box2d.Box2dUtil;

/**
 * The player with all ashley components.
 */
public class Player extends GameEntity {
  private static Player instance = new Player();
  private BodyComponent bodyComponent;

  public static Player getInstance() {
    return instance;
  }

  public Player() {
    createComponents();
  }

  public void createComponents() {
    PositionComponent positionComponent = ComponentFactory.addPositionComponent(this);

    Vector2 position = Box2dUtil.toBox2Vector(new Vector2(200, 200));
    Body body = BodyGenerator.createPlayer();
    body.setTransform(position, 0);
    bodyComponent = ComponentFactory.addBodyComponent(this, body);
    positionComponent.setPosition(position);
    add(new ScreenPositionComponent(100, 100));
  }

  public Vector2 getBox2dCenter() {
    return bodyComponent.body.getPosition();
  }

  public Vector2 getCenter() {
    return bodyComponent.getWorldPosition();
  }

  public void moveBy(float x, float y, float delta) {
    // Dampening check
    if(x != 0) {
      bodyComponent.body.setLinearVelocity(x * 350 * delta, bodyComponent.body.getLinearVelocity().y);
    }
    if(y != 0) {
      bodyComponent.body.setLinearVelocity(bodyComponent.body.getLinearVelocity().x, y * 350 * delta);
    }
  }
}
