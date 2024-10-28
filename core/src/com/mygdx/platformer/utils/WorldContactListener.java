package com.mygdx.platformer.utils;

import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.platformer.Platformer;
import com.mygdx.platformer.Sprites.Entities.Enemies.Enemy;
import com.mygdx.platformer.Sprites.Projectiles.Projectile;
import com.mygdx.platformer.Sprites.Items.Item;
import com.mygdx.platformer.Sprites.Entities.Player;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;


        switch (cDef) {
            case Platformer.ENEMY_HEAD_BIT | Platformer.PLAYER_BIT:
                if(fixA.getFilterData().categoryBits == Platformer.ENEMY_HEAD_BIT)
                    ((Enemy) fixA.getUserData()).hitOnHead((Player) fixB.getUserData());
                else
                    ((Enemy) fixB.getUserData()).hitOnHead((Player) fixA.getUserData());
                break;
            case Platformer.ENEMY_BIT | Platformer.ENEMY_BIT:
                if (((Enemy) fixA.getUserData()).getX() < ((Enemy) fixB.getUserData()).getX()) {
                    ((Enemy) fixB.getUserData()).startFriendlyCollision();
                } else {
                    ((Enemy) fixA.getUserData()).startFriendlyCollision();
                }
                break;
            case Platformer.ITEM_BIT | Platformer.PLAYER_BIT:
                if(fixA.getFilterData().categoryBits == Platformer.ITEM_BIT)
                    ((Item) fixA.getUserData()).use((Player) fixB.getUserData());
                else
                    ((Item) fixB.getUserData()).use((Player) fixA.getUserData());
                break;
            case Platformer.PLAYER_BIT | Platformer.PLAYER_BIT:
                if (((Player) fixA.getUserData()).getX() > ((Player) fixB.getUserData()).getX()) {
                    ((Player) fixB.getUserData()).startFriendlyCollision();
                } else {
                    ((Player) fixA.getUserData()).startFriendlyCollision();
                }
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
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;


        switch (cDef) {
            case Platformer.PLAYER_BIT | Platformer.ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == Platformer.PLAYER_BIT) {
                    ((Player) fixA.getUserData()).stopCombat();
                    ((Enemy) fixB.getUserData()).stopCombat();
                } else {
                    ((Player) fixB.getUserData()).stopCombat();
                    ((Enemy) fixA.getUserData()).stopCombat();
                }
                break;
            case Platformer.PLAYER_BIT | Platformer.PLAYER_BIT:
                ((Player) fixA.getUserData()).endFriendlyCollision();
                ((Player) fixB.getUserData()).endFriendlyCollision();
                break;
            case Platformer.ENEMY_BIT | Platformer.ENEMY_BIT:
                ((Enemy) fixA.getUserData()).endFriendlyCollision();
                ((Enemy) fixB.getUserData()).endFriendlyCollision();
                break;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
