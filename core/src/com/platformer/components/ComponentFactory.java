package com.platformer.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

/**
 * All component creations should be here.
 */
public class ComponentFactory {
  public static PooledEngine engine;

  private static <T extends Component> T createComponent(Class<T> componentType) {
    return engine.createComponent(componentType);
  }

  public static PositionComponent addPositionComponent(Entity entity) {
    PositionComponent component = createComponent(PositionComponent.class);
    entity.add(component);
    return component;
  }

}
