package com.platformer.components;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.PositionalLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.platformer.util.Settings;

/**
 * Component implementation for map objects
 */
public class LightComponent extends LimitingComponent implements Destroyable {
  private PositionalLight light;
  private final int MAX_RAYS = 2000;
  private boolean moveable;

  public void init(RayHandler rayHandler, float distance, float x, float y, boolean moveable) {
    super.init(1, 1, 1, Settings.getInstance().fade_in_offset+0.01f, Settings.getInstance().fade_out_offset+0.01f);
    light = new PointLight(rayHandler, MAX_RAYS, Color.WHITE, distance, x, y);
    light.setSoftnessLength(0f);
    light.setSoft(true);
    light.setXray(false);
    this.moveable = moveable;
  }

  public void init(RayHandler rayHandler, float distance, float x, float y, float degree, float coneDegree, boolean moveable) {
    super.init(1, 1, 1, Settings.getInstance().fade_in_offset+0.01f, Settings.getInstance().fade_out_offset+0.01f);
    light = new ConeLight(rayHandler, MAX_RAYS, Color.WHITE, distance, x, y, degree, coneDegree);
    light.setSoftnessLength(0f);
    light.setSoft(true);
    light.setXray(false);
    this.moveable = moveable;
  }

  @Override
  public void updateValue() {
//    super.updateValue();
//    if(getCurrentValue() != getTargetValue()) {
//      light.setColor(getCurrentValue(), getCurrentValue(), getCurrentValue(), getCurrentValue());
//    }
  }

  public void setPosition(float x, float y) {
    light.setPosition(x, y);
  }

  public boolean isMoveable() {
    return moveable;
  }


  @Override
  public void reset() {
    super.reset();
    this.light = null;
  }

  @Override
  public void destroy() {
    light.dispose();
  }
}
