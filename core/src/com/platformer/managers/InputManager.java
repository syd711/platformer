package com.platformer.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.platformer.entities.Player;

/**
 * Handles all kind of user input.
 */
public class InputManager implements InputProcessor {
  private static InputManager instance = new InputManager();

  private InputMultiplexer inputMultiplexer = new InputMultiplexer();

  private InputManager() {
  }

  public void init() {
    //we add this instance as last instance for the multiplexer to gui the UI elements the priority
    InputManager.getInstance().getInputMultiplexer().addProcessor(this);
    Gdx.input.setInputProcessor(InputManager.getInstance().getInputMultiplexer());
  }

  public static InputManager getInstance() {
    return instance;
  }

  public InputMultiplexer getInputMultiplexer() {
    return inputMultiplexer;
  }

  public void addInputProcessor(InputProcessor inputProcessor) {
    inputMultiplexer.addProcessor(inputProcessor);
  }

  @Override
  public boolean keyDown(int keycode) {
    return false;
  }

  @Override
  public boolean keyUp(int keycode) {
    if(keycode == Input.Keys.ESCAPE) {
      System.exit(0);
    }
    else if(keycode == Input.Keys.F12) {
      System.exit(0);
    }
    else if(keycode == Input.Keys.C) {
      CameraManager.getInstance().reset();
      return true;
    }
    else if(keycode == Input.Keys.PLUS) {
      CameraManager.getInstance().updateTargetZoom(-0.2f);
      return true;
    }
    else if(keycode == Input.Keys.MINUS) {
      CameraManager.getInstance().updateTargetZoom(0.2f);
      return true;
    }

    return false;
  }

  public void update(float delta) {
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

    Player.getInstance().moveBy(x, y, delta);
  }


  @Override
  public boolean keyTyped(char character) {
    return false;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    return false;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
//    if(!navigationEnabled) {
//      return false;
//    }
//
//    float targetX = screenX;
//    float targetY = Gdx.graphics.getHeight() - screenY;
//
//    lastClickLocation = new Vector2(targetX, targetY);
//    Vector2 worldCoordinates = GraphicsUtil.transform2WorldCoordinates(camera, targetX, targetY);
//
//    float dst = worldCoordinates.dst(Player.getInstance().getCenter());
//    if(dst < 80) {
//      return false; //TODO
//    }
//
//    if(button == Input.Buttons.RIGHT) {
//      Player.getInstance().moveTo(worldCoordinates);
//      return true;
//    }
//
//    if(button == Input.Buttons.LEFT) {
//      return SelectionManager.getInstance().selectAt(targetX, targetY, true);
//    }
    return false;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    return false;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY) {
    return false;
  }

  @Override
  public boolean scrolled(int amount) {
    return false;
  }
}
