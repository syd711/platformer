package com.platformer;

import box2dLight.RayHandler;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.platformer.entities.Player;
import com.platformer.managers.CameraManager;
import com.platformer.managers.EntityManager;
import com.platformer.managers.InputManager;
import com.platformer.managers.LevelManager;

import static com.platformer.util.Settings.PPM;


public class Game extends InputAdapter implements ApplicationListener {
  public static World world;
  public static OrthographicCamera camera;
  public static RayHandler rayHandler;

  private SpriteBatch batch;
  private Box2DDebugRenderer b2dr;
  private OrthogonalTiledMapRenderer tiledMapRenderer;

  @Override
  public void create() {
    camera = new OrthographicCamera();
    camera.setToOrtho(false);
    camera.update();


    batch = new SpriteBatch();
    this.world = new World(new Vector2(0, -9.8f), false);
    this.b2dr = new Box2DDebugRenderer();

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
    world.step(delta, 6, 2);
    rayHandler.update();

    Matrix4 debugMatrix = batch.getProjectionMatrix().cpy().scale(PPM, PPM, 0);

    batch.setProjectionMatrix(camera.combined);
    rayHandler.setCombinedMatrix(camera.combined.cpy().scl(PPM));


    tiledMapRenderer.setView(camera);
    tiledMapRenderer.render();

    b2dr.render(world, debugMatrix);
    rayHandler.render();

    CameraManager.getInstance().update(delta);

    EntityManager.getInstance().update();
    InputManager.getInstance().update(delta);

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



}
