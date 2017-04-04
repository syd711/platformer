package com.platformer.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.platformer.components.ComponentFactory;

/**
 *
 */
public class Collidable extends GameEntity {

  public Collidable(Body body) {
    ComponentFactory.addBodyComponent(this, body);
  }
}
