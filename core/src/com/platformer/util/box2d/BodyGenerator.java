package com.platformer.util.box2d;

import com.badlogic.gdx.physics.box2d.*;
import com.platformer.Game;

public class BodyGenerator {
  private final static short PLAYER_BITS = 0x0001;
  private final static short NPC_BITS = 0x0002;
  private final static short WORLD_BITS = 0x0004;
  private final static short BULLET_BITS = 0x0008;
//  public final static short BITS = 0x0032;

  private final static short MASK_PLAYER = NPC_BITS | WORLD_BITS | BULLET_BITS;
  private final static short MASK_NPC = PLAYER_BITS | NPC_BITS | BULLET_BITS;
  private final static short MASK_WORLD = PLAYER_BITS;
  private final static short MASK_BULLET_BITS = BULLET_BITS | PLAYER_BITS | NPC_BITS;

  private static World world = Game.world;

  public static Body createMapObjectBody(Shape shape) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.StaticBody;
    Body body = world.createBody(bodyDef);

    FixtureDef fdef = new FixtureDef();
    fdef.shape = shape;
    fdef.isSensor = true;
    fdef.filter.groupIndex = 0;
    fdef.filter.categoryBits = WORLD_BITS;
    fdef.filter.maskBits = MASK_WORLD;
    body.createFixture(fdef);

    shape.dispose();
    return body;
  }
}
