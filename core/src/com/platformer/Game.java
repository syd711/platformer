package com.platformer;

import box2dLight.RayHandler;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.platformer.managers.CameraManager;
import com.platformer.managers.EntityManager;
import com.platformer.managers.InputManager;
import com.platformer.managers.LevelManager;
import com.platformer.util.Settings;

import static com.platformer.util.Settings.PPM;

public class Game extends ApplicationAdapter {

  public static OrthographicCamera camera;
  public static World world;
  private Box2DDebugRenderer box2DDebugRenderer;
  private RayHandler rayHandler;
  private SpriteBatch batch;
  private OrthogonalTiledMapRenderer tiledMapRenderer;

  @Override
  public void create() {

    //camera
    camera = new OrthographicCamera();
    camera.setToOrtho(false);
    camera.update();

    //box2d
    world = new World(new Vector2(0f, -9.8f), false);
//    world.setContactListener(new GameContactListener());
    box2DDebugRenderer = new Box2DDebugRenderer();

    EntityManager.create();

    batch = new SpriteBatch();

    tiledMapRenderer = LevelManager.getInstance().loadLevel(batch, "map_1");

    //init camera manager
    CameraManager.getInstance().init(camera);

    //light
    rayHandler = new RayHandler(world);
    RayHandler.useDiffuseLight(true);
    rayHandler.setCulling(true);
    rayHandler.setCombinedMatrix(camera);

    InputManager.getInstance().init();
  }

  @Override
  public void pause() {
  }

  @Override
  public void resume() {

  }

  @Override
  public void render() {
    float deltaTime = Gdx.graphics.getDeltaTime();

    world.step(deltaTime, 6, 2);

    Matrix4 debugMatrix = batch.getProjectionMatrix().cpy().scale(PPM, PPM, 0);

    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    CameraManager.getInstance().update(deltaTime);

    batch.setProjectionMatrix(camera.combined);

    rayHandler.setCombinedMatrix(camera);
    rayHandler.updateAndRender();

    tiledMapRenderer.setView(camera);
    tiledMapRenderer.render();

    if(Settings.getInstance().debug) {
      box2DDebugRenderer.render(Game.world, debugMatrix);
    }

    EntityManager.getInstance().update();
    GdxAI.getTimepiece().update(deltaTime);//TODO not sure if required
  }

  @Override
  public void dispose() {
    box2DDebugRenderer.dispose();
    world.dispose();
    super.dispose();
  }
}