package com.mygdx.platformer.utils;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.platformer.Platformer;
import com.mygdx.platformer.Screens.PlayScreen;
import com.badlogic.gdx.utils.Array;
import com.mygdx.platformer.Sprites.Entities.Enemies.Enemy;
import com.mygdx.platformer.Sprites.Tiles.Brick;

public class B2WorldCreator {

    public Array<Enemy> getEnemies() {
        return enemies;
    }

    private PlayScreen screen;
    private World world;
    private TiledMap map;
    private Array<Enemy> enemies;
    public B2WorldCreator(PlayScreen screen) {
        this.screen = screen;
        world = screen.getWorld();
        map = screen.getMap();

        createFixtures();
        //createInteractiveTiles();
        //createEnemies();
    }

    public void createFixtures() {
        BodyDef bDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fDef = new FixtureDef();
        Body body;

        // create ground bodies / fixtures
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bDef.type = BodyDef.BodyType.StaticBody;
            bDef.position.set((rect.getX() + rect.getWidth()/2) / Platformer.PPM, (rect.getY() + rect.getHeight()/2) / Platformer.PPM);

            body = world.createBody(bDef);

            shape.setAsBox((rect.getWidth() / 2) / Platformer.PPM, (rect.getHeight() / 2) / Platformer.PPM);
            fDef.shape = shape;
            body.createFixture(fDef);
        }

        // create pipe bodies / fixtures
        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bDef.type = BodyDef.BodyType.StaticBody;
            bDef.position.set((rect.getX() + rect.getWidth()/2) / Platformer.PPM, (rect.getY() + rect.getHeight()/2) / Platformer.PPM);

            body = world.createBody(bDef);

            shape.setAsBox((rect.getWidth() / 2) / Platformer.PPM, (rect.getHeight() / 2) / Platformer.PPM);
            fDef.shape = shape;
            fDef.filter.categoryBits = Platformer.OBJECT_BIT;
            body.createFixture(fDef);
        }
    }

    public void createInteractiveTiles() {
        // create brick bodies / fixtures
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            new Brick(screen, object);
        }

        // create coin bodies / fixtures
//        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
//            new Coin(screen, object);
//        }
    }

    public void createEnemies() {

    }
}
