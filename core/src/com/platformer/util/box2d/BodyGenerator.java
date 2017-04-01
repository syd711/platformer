package com.platformer.util.box2d;

import com.badlogic.gdx.physics.box2d.*;
import com.platformer.Game;

import static com.platformer.util.Settings.MPP;
import static com.platformer.util.box2d.PhysicsManager.MASK_PLAYER;
import static com.platformer.util.box2d.PhysicsManager.PLAYER_BITS;

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
    fdef.filter.groupIndex = 0;
    fdef.filter.categoryBits = PLAYER_BITS;
    fdef.filter.maskBits = MASK_PLAYER;
    body.createFixture(fdef);
    body.setLinearDamping(3f);

    shape.dispose();
    return body;
  }
}
