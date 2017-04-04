package com.platformer.util.box2d;


import box2dLight.RayHandler;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.platformer.components.ComponentFactory;
import com.platformer.components.LightComponent;
import com.platformer.entities.Collidable;
import com.platformer.entities.Light;
import com.platformer.managers.EntityManager;

import java.util.Iterator;

import static com.platformer.util.Settings.MPP;


/**
 *
 */

public class LevelPhysicsGenerator {
  public static final String COLLISION_LAYER = "CollisionLayer";
  public static final String LIGHTS_LAYER = "LightsLayer";

  //light properties
  public static final String TYPE_CONE_LIGHT = "ConeLight";
  public static final String TYPE_POINT_LIGHT = "PointLight";

  public static final String PROPERTY_LIGHT_DISTANCE = "lightDistance";
  public static final String PROPERTY_LIGHT_DEGREE = "lightDegree";
  public static final String PROPERTY_CONE_DEGREE = "coneDegree";

  public static final String PROPERTY_OBJECT_TYPE = "type";



  public void createPhysics(TiledMap map, World world, RayHandler rayHandler) {
    createCollidables(map, world, COLLISION_LAYER);
    createLights(map, rayHandler, LIGHTS_LAYER);
  }

  private void createLights(TiledMap map, RayHandler rayHandler, String lightsLayer) {
    MapLayer layer = map.getLayers().get(lightsLayer);

    MapObjects objects = layer.getObjects();
    Iterator<MapObject> objectIt = objects.iterator();

    while(objectIt.hasNext()) {
      MapObject object = objectIt.next();

      if(object instanceof RectangleMapObject) {

      }
      else if(object instanceof EllipseMapObject) {
        EllipseMapObject ellipseMapObject = (EllipseMapObject) object;
        Ellipse ellipse = ellipseMapObject.getEllipse();
        String type = (String) object.getProperties().get(PROPERTY_OBJECT_TYPE);

        Vector2 centeredPosition = new Vector2(ellipse.x + ellipse.width / 2, ellipse.y + ellipse.height / 2).scl(MPP);
        float distance = ellipse.width * MPP;
        float degree = object.getProperties().get(PROPERTY_LIGHT_DEGREE, 360f, Float.class);
        float coneDegree = object.getProperties().get(PROPERTY_CONE_DEGREE, 30f, Float.class);

        Entity entity = new Light();
        LightComponent component =  ComponentFactory.addLightComponent(entity);

        if(type.equals(TYPE_POINT_LIGHT)) {
          component.init(rayHandler, distance, centeredPosition.x, centeredPosition.y, false);
        }
        else if(type.equals(TYPE_CONE_LIGHT)) {
          component.init(rayHandler, distance, centeredPosition.x, centeredPosition.y, degree, coneDegree, false);
        }

        EntityManager.getInstance().add(entity);
      }
      else {
        throw new UnsupportedOperationException("No valid object type for a light " + object);
      }
    }
  }

  public static final short BIT_WALL = 1;
  public static final short BIT_PLAYER = 2;
  public static final short BIT_SENSOR = 4;

  public void createCollidables(TiledMap map, World world, String layerName) {
    MapLayer layer = map.getLayers().get(layerName);

    MapObjects objects = layer.getObjects();
    Iterator<MapObject> objectIt = objects.iterator();

    while(objectIt.hasNext()) {
      LevelGeometry geometry = null;
      MapObject object = objectIt.next();

      if(object instanceof TextureMapObject) {
        continue;
      }

      Shape shape;
      BodyDef bodyDef = new BodyDef();
      bodyDef.type = BodyDef.BodyType.StaticBody;

      if(object instanceof RectangleMapObject) {
        geometry = getRectangle((RectangleMapObject) object);
        shape = geometry.getShape();
      }
      else if(object instanceof PolygonMapObject) {
        geometry = getPolygon((PolygonMapObject) object);
        shape = geometry.getShape();
      }
      else if(object instanceof PolylineMapObject) {
        geometry = getPolyline((PolylineMapObject) object);
        shape = geometry.getShape();
      }
      else if(object instanceof EllipseMapObject) {
        geometry = getCircle((EllipseMapObject) object);
        shape = geometry.getShape();
      }
      else {
        Gdx.app.error("Unrecognized shape", "" + object.toString());
        continue;
      }

      Body body = world.createBody(bodyDef);

      FixtureDef fixtureDef = new FixtureDef();
      fixtureDef.shape = shape;
      fixtureDef.density = 1.0f;
      fixtureDef.filter.categoryBits = BIT_WALL;
      fixtureDef.filter.maskBits = BIT_PLAYER | BIT_WALL | BIT_SENSOR;
      fixtureDef.filter.groupIndex = 0;

      body.createFixture(fixtureDef);

      // All collisions need an entity, and all entities need a type to handle collisions
      Entity levelEntity = new Collidable(body);
      body.setUserData(levelEntity);
      EntityManager.getInstance().add(levelEntity);

      fixtureDef.shape = null;
      shape.dispose();
    }
  }

  private LevelGeometry getRectangle(RectangleMapObject rectangleObject) {
    Rectangle rectangle = rectangleObject.getRectangle();
    PolygonShape polygon = new PolygonShape();
    Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) * MPP,
        (rectangle.y + rectangle.height * 0.5f) * MPP);
    polygon.setAsBox(rectangle.width * 0.5f * MPP,
        rectangle.height * 0.5f * MPP,
        size,
        0.0f);
    return new LevelGeometry(polygon);
  }

  private LevelGeometry getCircle(EllipseMapObject circleObject) {
    Ellipse ellipse = circleObject.getEllipse();
    CircleShape circleShape = new CircleShape();
    circleShape.setRadius(ellipse.width * MPP);
    circleShape.setPosition(new Vector2(ellipse.x * MPP, ellipse.y * MPP));
    return new LevelGeometry(circleShape);
  }

  private LevelGeometry getPolygon(PolygonMapObject polygonObject) {
    PolygonShape polygon = new PolygonShape();
    float[] vertices = polygonObject.getPolygon().getTransformedVertices();

    float[] worldVertices = new float[vertices.length];

    for(int i = 0; i < vertices.length; ++i) {
      worldVertices[i] = vertices[i] * MPP;
    }

    polygon.set(worldVertices);
    return new LevelGeometry(polygon);
  }

  private LevelGeometry getPolyline(PolylineMapObject polylineObject) {
    float[] vertices = polylineObject.getPolyline().getTransformedVertices();
    Vector2[] worldVertices = new Vector2[vertices.length / 2];

    for(int i = 0; i < vertices.length / 2; ++i) {
      worldVertices[i] = new Vector2();
      worldVertices[i].x = vertices[i * 2] * MPP;
      worldVertices[i].y = vertices[i * 2 + 1] * MPP;
    }

    ChainShape chain = new ChainShape();
    chain.createChain(worldVertices);
    return new LevelGeometry(chain);
  }

  public static class LevelGeometry /*implements Collidable*/ {
    private Shape shape;

    public LevelGeometry(Shape shape) {
      this.shape = shape;
    }

    public Shape getShape() {
      return shape;
    }
  }
}
