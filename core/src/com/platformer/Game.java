package com.platformer;

import box2dLight.RayHandler;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.platformer.managers.CameraManager;
import com.platformer.managers.EntityManager;

public class Game extends ApplicationAdapter {

  private OrthographicCamera camera;
  public static World world;
  private Box2DDebugRenderer box2DDebugRenderer;
  private RayHandler rayHandler;

  @Override
  public void create() {

    //camera
    camera = new OrthographicCamera();
    camera.setToOrtho(false);
    camera.update();

    //box2d
    world = new World(new Vector2(0, 0), false);
//    world.setContactListener(new GameContactListener());
    box2DDebugRenderer = new Box2DDebugRenderer();

    EntityManager.create();


    //init camera manager
    CameraManager.getInstance().init(camera);

    //light
    rayHandler = new RayHandler(world);
    RayHandler.useDiffuseLight(true);
    rayHandler.setCulling(true);
    rayHandler.setCombinedMatrix(camera);
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

    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    rayHandler.setCombinedMatrix(camera);
    rayHandler.updateAndRender();

    CameraManager.getInstance().update(deltaTime);
    EntityManager.getInstance().update();
    GdxAI.getTimepiece().update(deltaTime);//TODO not sure if required

    world.step(deltaTime, 6, 2);
  }

  @Override
  public void dispose() {
    box2DDebugRenderer.dispose();
    world.dispose();
    super.dispose();
  }
}