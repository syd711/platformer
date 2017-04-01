package com.platformer.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;
import com.platformer.Game;
import com.platformer.util.box2d.Box2dUtil;

import static com.platformer.util.Settings.MPP;
import static com.platformer.util.Settings.PPM;

/**
 * Box2d support for entities
 */
public class BodyComponent implements Component, Pool.Poolable, Destroyable {

  public Body body;

  public float targetRadius;

  @Override
  public void reset() {
    this.body = null;
  }

  public Vector2 getWorldPosition() {
    Vector2 position = body.getPosition();
    return new Vector2(position.x * PPM, position.y * PPM);
  }

  public void setWorldPosition(Vector2 worldPosition) {
    Vector2 pos = Box2dUtil.toBox2Vector(worldPosition);
    body.setTransform(pos.x, pos.y, body.getAngle());
  }

  @Override
  public void destroy() {
    if(body != null) {
      Game.world.destroyBody(body);
      body = null;
    }
  }

  public void setTargetRadius(float f) {
    this.targetRadius = f * MPP;
  }

  public float distanceTo(Body body) {
    return body.getPosition().dst(body.getPosition());
  }
}
