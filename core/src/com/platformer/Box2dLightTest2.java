package com.platformer;


import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.platformer.entities.Player;
import com.platformer.managers.CameraManager;
import com.platformer.managers.EntityManager;
import com.platformer.managers.InputManager;
import com.platformer.managers.LevelManager;


public class Box2dLightTest2 extends InputAdapter implements ApplicationListener {

  // Filters
  public static final short BIT_WALL = 1;
  public static final short BIT_PLAYER = 2;
  public static final short BIT_SENSOR = 4;
  public static final short BIT_NOLIGHT = 8;
  public static final short BIT_BREAKABLE = 16;
  public static final float PPM = 100;


  public static World world;
  private Box2DDebugRenderer b2dr;
  private Body player;

  private RayHandler rayHandler;
  private PointLight pl;
  private ConeLight cl;
  private SpriteBatch batch;
  private OrthographicCamera camera;
  private OrthogonalTiledMapRenderer tiledMapRenderer;

  @Override
  public void create() {

    //camera
    camera = new OrthographicCamera();
    camera.setToOrtho(false);
    camera.update();


    batch = new SpriteBatch();
    this.world = new World(new Vector2(0, 0), false);
    this.b2dr = new Box2DDebugRenderer();

    this.player = createBox(world, 0, 0, 32, 32, false, true);
    this.player.setLinearDamping(20f);

    rayHandler = new RayHandler(world);
    rayHandler.setAmbientLight(.5f);

    EntityManager.create();

    batch = new SpriteBatch();

    tiledMapRenderer = LevelManager.getInstance().loadLevel(batch, world, rayHandler, "map_1");

    //init camera manager
    CameraManager.getInstance().init(camera);

    InputManager.getInstance().init();
  }

  @Override
  public void resize(int width, int height) {

  }

  @Override
  public void render() {
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    float delta = Gdx.graphics.getDeltaTime();
    world.step(1 / 60f, 6, 2);
    rayHandler.update();

    Matrix4 debugMatrix = batch.getProjectionMatrix().cpy().scale(PPM, PPM, 0);

    inputUpdate(delta);
    cameraUpdate();
    batch.setProjectionMatrix(camera.combined);
    rayHandler.setCombinedMatrix(camera.combined.cpy().scl(PPM));


    tiledMapRenderer.setView(camera);
    tiledMapRenderer.render();

    b2dr.render(world, debugMatrix);
    rayHandler.render();

//    CameraManager.getInstance().update(delta);
    inputUpdate(delta);

    EntityManager.getInstance().update();
    InputManager.getInstance().update();
    Player.getInstance().update();

    GdxAI.getTimepiece().update(delta);//TODO not sure if required
  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void dispose() {
    rayHandler.dispose();
    b2dr.dispose();
    world.dispose();
  }


  private void cameraUpdate() {
    lerpToTarget(camera, player.getPosition().scl(PPM));
  }

  private void inputUpdate(float delta) {
    float x = 0, y = 0;
    if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
      y += 1;
    }
    if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
      y -= 1;
    }
    if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
      x -= 1;
    }
    if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
      x += 1;
    }

    // Dampening check
    if(x != 0) {
      player.setLinearVelocity(x * 350 * delta, player.getLinearVelocity().y);
    }
    if(y != 0) {
      player.setLinearVelocity(player.getLinearVelocity().x, y * 350 * delta);
    }
  }

  public static Body createBox(World world, float x, float y, int width, int height, boolean isStatic, boolean fixedRotation) {
    Body pBody;
    BodyDef def = new BodyDef();

    if(isStatic)
      def.type = BodyDef.BodyType.StaticBody;
    else
      def.type = BodyDef.BodyType.DynamicBody;

    def.position.set(x / PPM, y / PPM);
    def.fixedRotation = fixedRotation;
    pBody = world.createBody(def);
    pBody.setUserData("wall");

    PolygonShape shape = new PolygonShape();
    shape.setAsBox(width / 2 / PPM, height / 2 / PPM);

    FixtureDef fd = new FixtureDef();
    fd.shape = shape;
    fd.density = 1.0f;
    fd.filter.categoryBits = BIT_WALL;
    fd.filter.maskBits = BIT_PLAYER | BIT_WALL | BIT_SENSOR;
    fd.filter.groupIndex = 0;
    pBody.createFixture(fd);
    shape.dispose();
    return pBody;
  }


  /* Cone Lights */
  public static ConeLight createConeLight(RayHandler rayHandler, Body body, Color c, float dist, float dir, float cone) {
    ConeLight cl = new ConeLight(rayHandler, 120, c, dist, 0, 0, dir, cone);
    cl.setSoftnessLength(0f);
    cl.attachToBody(body);
    cl.setXray(false);
    return cl;
  }

  public static PointLight createPointLight(RayHandler rayHandler, Body body, Color c, float dist) {
    PointLight pl = new PointLight(rayHandler, 120, c, dist, 0, 0);
    pl.setSoftnessLength(0f);
//    pl.attachToBody(body);
    pl.setPosition(body.getPosition());
    pl.setXray(false);
    return pl;
  }

  public static void lerpToTarget(Camera camera, Vector2 target) {
    // a + (b - a) * lerp factor
    Vector3 position = camera.position;
    position.x = camera.position.x + (target.x - camera.position.x) * .1f;
    position.y = camera.position.y + (target.y - camera.position.y) * .1f;
    camera.position.set(position);
    camera.update();
  }
}
