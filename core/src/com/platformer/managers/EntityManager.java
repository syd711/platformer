package com.platformer.managers;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.platformer.Game;
import com.platformer.components.ComponentFactory;
import com.platformer.components.Destroyable;
import com.platformer.entities.GameEntity;
import com.platformer.entities.Player;
import com.platformer.util.Box2dUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Central Ashley initialization of galaxy systems.
 */
public class EntityManager {
  private PooledEngine engine;
  private List<GameEntity> destroyEntities = new ArrayList<>();

  private static EntityManager INSTANCE;

  private EntityManager() {
    this.engine = new PooledEngine();
    ComponentFactory.engine = engine;
  }

  private void init() {
    engine.addEntity(Player.getInstance());
  }

  public static EntityManager create() {
    INSTANCE = new EntityManager();
    INSTANCE.init();
    return INSTANCE;
  }

  public static EntityManager getInstance() {
    return INSTANCE;
  }

  /**
   * Registers an galaxy click listener
   */
  public void addEntityListener(EntityListener listener) {
    this.engine.addEntityListener(listener);
  }

  /**
   * Adds the given galaxy to the Ashley engine.
   *
   * @param entity the galaxy to add
   */
  public void add(Entity entity) {
    engine.addEntity(entity);
  }

  /**
   * Registers entities to be destroyed
   * for the next render interval.
   *
   * @param toDestroy the list of entities to be destroyed
   */
  public void destroy(GameEntity toDestroy) {
    destroyEntities.add(toDestroy);
  }

  /**
   * Pauses all ashley systems
   */
  public void pauseSystems(boolean pause) {
    ImmutableArray<EntitySystem> systems = engine.getSystems();
    for(EntitySystem system : systems) {
      system.setProcessing(!pause);
    }
  }

  /**
   * Uses the Ashley engine to update
   * all classes implementing the Updateable interface.
   * This can be used for non-component based refreshes.
   */
  public void update() {
    engine.update(Gdx.graphics.getDeltaTime());

    for(GameEntity entity : destroyEntities) {
      ImmutableArray<Component> components = entity.getComponents();
      for(Component component : components) {
        if(component instanceof Destroyable) {
          ((Destroyable) component).destroy();
        }
      }

      if(entity instanceof EntityListener) {
        engine.removeEntityListener((EntityListener) entity);
      }

      engine.removeEntity(entity);
      entity.destroyed();
//        Gdx.app.log(this.toString(), "Destroyed " + galaxy);
    }
    destroyEntities.clear();
  }

  //-------------- Entity Helper -----------------------------------------------------------------------

  public Entity getEntityAt(float x, float y) {
    Vector2 clickPoint = new Vector2(x, y);
    return Box2dUtil.getEntityAtClickPoint(Game.world, clickPoint);
  }

  public Entity getEntityAt(Vector2 pos) {
    return Box2dUtil.getEntityAtClickPoint(Game.world, pos);
  }

  public ImmutableArray<Entity> getEntitiesFor(Class<? extends Component> componentClass) {
    return engine.getEntitiesFor(Family.all(componentClass).get());
  }

  public <T> List<T> getEntities(Class<T> clazz) {
    List<T> result = new ArrayList<T>();
    for(Entity entity : engine.getEntities()) {
      if(clazz.isInstance(entity)) {
        result.add((T) entity);
      }
    }
    return result;
  }
}
