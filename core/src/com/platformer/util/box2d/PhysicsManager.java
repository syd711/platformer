package com.platformer.util.box2d;


public class PhysicsManager {
    public final static short PLAYER_BITS = 0x0001;
    public final static short ENEMY_BITS = 0x0002;
    public final static short WORLD_BITS = 0x0004;
    public final static short BULLET_BITS = 0x0008;

    public final static short MASK_PLAYER = ENEMY_BITS | WORLD_BITS | BULLET_BITS;
    public final static short MASK_NPC = PLAYER_BITS | ENEMY_BITS | BULLET_BITS;
    public final static short MASK_WORLD = PLAYER_BITS;
    public final static short MASK_BULLET_BITS = BULLET_BITS | PLAYER_BITS | ENEMY_BITS;
}

