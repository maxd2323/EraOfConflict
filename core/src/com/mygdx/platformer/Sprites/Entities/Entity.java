package com.mygdx.platformer.Sprites.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.mygdx.platformer.Screens.Hud;
import com.mygdx.platformer.Screens.PlayScreen;
import com.mygdx.platformer.Sprites.Items.Item;

public abstract class Entity extends Sprite {
    public enum State { FALLING, JUMPING, STANDING, RUNNING, DEAD, THROWING, HURT, SHOOTING, SLASHING };

    // Box2D Physics
    public World world;
    public Body b2body;

    // States
    public State currentState;
    public State previousState;
    public float stateTimer;

    public boolean isDead;

    protected boolean shouldMove;
    protected boolean destroyed;
    protected boolean setToDestroy;
    protected float healthCapacity;
    protected float currentHealth;
    protected float magickaCapacity;
    protected float currentMagicka;
    protected float speed;
    protected boolean inCombat = false;
    protected float timeSinceLastAttack;
    protected Entity opponent;
    protected ArrayMap<String, Animation> animations;
    protected ArrayMap<String, TextureRegion> textures;
    protected String entityTag;
    public Entity(PlayScreen screen, float health, float magicka, float speed) {
        super();
        loadTexturesAndAnimations(screen);
        healthCapacity = health;
        currentHealth = health;
        magickaCapacity = magicka;
        currentMagicka = magicka;
        this.speed = speed;
        destroyed = false;
        setToDestroy = false;
        timeSinceLastAttack = 0f;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        shouldMove = true;
        isDead = false;
        entityTag = "entity";
    }

    public Entity(PlayScreen screen, float health, float magicka, float speed, String entityTag) {
        super();
        loadTexturesAndAnimations(screen);
        healthCapacity = health;
        currentHealth = health;
        magickaCapacity = magicka;
        currentMagicka = magicka;
        this.speed = speed;
        destroyed = false;
        setToDestroy = false;
        timeSinceLastAttack = 0f;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        shouldMove = true;
        isDead = false;
        this.entityTag = entityTag;
    }

    public void die() {
        isDead = true;
    }

    public void applyDamage(float damage) {
        currentHealth -= damage;
        Gdx.app.log(entityTag, "currentHealth: " + String.valueOf(currentHealth) + " damage " + String.valueOf(damage));
        if (currentHealth <= 0) {
            die();
        }
    }

    protected void incrementHealth(int health) {
        this.currentHealth += health;

        if(this.currentHealth > healthCapacity) {
            this.currentHealth = healthCapacity;
        }
        Hud.setHealth((int) currentHealth);
    }

    protected void incrementMagicka(float magicka) {
        this.currentMagicka += magicka;

        if(this.currentMagicka > magickaCapacity) {
            this.currentMagicka = magickaCapacity;
        }
        if(this.currentMagicka < 0) {
            this.currentMagicka = 0;
        }
        Hud.setMagicka((int) currentMagicka);
    }

    public void startCombat(Entity opponent) {
        Gdx.app.log(entityTag, "start combat with " + opponent.entityTag);
        this.opponent = opponent;
        inCombat = true;
        timeSinceLastAttack = 0f;  // reset the timer when combat starts
        shouldMove = false;
    }

    public void stopCombat() {
        inCombat = false;
        opponent = null;
        shouldMove = true;
    }

    private void swingSword() {
        if (opponent != null) {
            opponent.applyDamage(5f);
        }
    }


    public boolean isDestroyed() {
        return destroyed;
    }

    public abstract void loadTexturesAndAnimations(PlayScreen screen);
}
