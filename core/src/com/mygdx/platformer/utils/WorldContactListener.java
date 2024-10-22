package com.mygdx.platformer.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.platformer.Platformer;
import com.mygdx.platformer.Sprites.Entities.Enemies.Enemy;
import com.mygdx.platformer.Sprites.Entities.Entity;
import com.mygdx.platformer.Sprites.Items.Coin;
import com.mygdx.platformer.Sprites.Projectiles.Projectile;
import com.mygdx.platformer.Sprites.Tiles.Chest;
import com.mygdx.platformer.Sprites.Tiles.InteractiveTileObject;
import com.mygdx.platformer.Sprites.Items.Item;
import com.mygdx.platformer.Sprites.Entities.Player;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;


        switch (cDef) {
            case Platformer.PLAYER_BIT | Platformer.CHEST_BIT:
                if(fixA.getFilterData().categoryBits == Platformer.PLAYER_BIT)
                    ((Chest) fixB.getUserData()).collideWithPlayer((Player) fixA.getUserData());
                else
                    ((Chest) fixA.getUserData()).collideWithPlayer((Player) fixB.getUserData());
                break;
            case Platformer.COIN_BIT | Platformer.PLAYER_BIT:
                if(fixA.getFilterData().categoryBits == Platformer.COIN_BIT)
                    ((Coin) fixA.getUserData()).use((Player) fixB.getUserData());
                else
                    ((Coin) fixB.getUserData()).use((Player) fixA.getUserData());
                break;
            case Platformer.ENEMY_HEAD_BIT | Platformer.PLAYER_BIT:
                if(fixA.getFilterData().categoryBits == Platformer.ENEMY_HEAD_BIT)
                    ((Enemy) fixA.getUserData()).hitOnHead((Player) fixB.getUserData());
                else
                    ((Enemy) fixB.getUserData()).hitOnHead((Player) fixA.getUserData());
                break;
            case Platformer.ENEMY_BIT | Platformer.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == Platformer.ENEMY_BIT)
                    ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                break;
            case Platformer.ENEMY_BIT | Platformer.ENEMY_BIT:
                ((Enemy) fixA.getUserData()).onEnemyHit((Enemy) fixB.getUserData());
                ((Enemy) fixB.getUserData()).onEnemyHit((Enemy) fixB.getUserData());
                break;
            case Platformer.ITEM_BIT | Platformer.PLAYER_BIT:
                if(fixA.getFilterData().categoryBits == Platformer.ITEM_BIT)
                    ((Item) fixA.getUserData()).use((Player) fixB.getUserData());
                else
                    ((Item) fixB.getUserData()).use((Player) fixA.getUserData());
                break;
            case Platformer.PLAYER_BIT | Platformer.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == Platformer.PLAYER_BIT) {
                    ((Player) fixA.getUserData()).startCombat((Enemy) fixB.getUserData());
                    ((Enemy) fixB.getUserData()).startCombat((Player) fixA.getUserData());
                }
                else {
                    ((Player) fixB.getUserData()).startCombat((Enemy) fixA.getUserData());
                    ((Enemy) fixA.getUserData()).startCombat((Player) fixB.getUserData());
                }
                break;
            case Platformer.PROJECTILE_BIT | Platformer.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == Platformer.PROJECTILE_BIT)
                    ((Projectile) fixA.getUserData()).collideWithEnemy((Enemy) fixB.getUserData());
                else
                    ((Projectile) fixB.getUserData()).collideWithEnemy((Enemy) fixA.getUserData());
                break;
            case Platformer.PROJECTILE_BIT | Platformer.PLAYER_BIT:
                if(fixA.getFilterData().categoryBits == Platformer.PROJECTILE_BIT)
                    ((Projectile) fixA.getUserData()).collideWithPlayer((Player) fixB.getUserData());
                else
                    ((Projectile) fixB.getUserData()).collideWithPlayer((Player) fixA.getUserData());
                break;
            case Platformer.PROJECTILE_BIT | Platformer.GROUND_BIT:
                if(fixA.getFilterData().categoryBits == Platformer.PROJECTILE_BIT)
                    ((Projectile) fixA.getUserData()).setToDestroy();
                else
                    ((Projectile) fixB.getUserData()).setToDestroy();
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
