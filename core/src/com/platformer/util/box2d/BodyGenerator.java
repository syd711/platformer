package com.platformer.util.box2d;

import com.badlogic.gdx.physics.box2d.*;
import com.platformer.Game;

import static com.platformer.util.Settings.MPP;

public class BodyGenerator {
  private static World world = Game.world;

  public static Body createPlayer() {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.DynamicBody;
    Body body = world.createBody(bodyDef);

    FixtureDef fdef = new FixtureDef();

    PolygonShape shape = new PolygonShape();
    shape.setAsBox(40 * MPP, 40* MPP);

    fdef.shape = shape;
    fdef.isSensor = false;
    fdef.friction = 0.5f;
    fdef.filter.categoryBits = LevelPhysicsGenerator.BIT_WALL;
    fdef.filter.maskBits = LevelPhysicsGenerator.BIT_PLAYER | LevelPhysicsGenerator.BIT_WALL | LevelPhysicsGenerator.BIT_SENSOR;
    fdef.filter.groupIndex = 0;
    body.createFixture(fdef);
    body.setLinearDamping(2f);

    shape.dispose();
    return body;
  }
}
