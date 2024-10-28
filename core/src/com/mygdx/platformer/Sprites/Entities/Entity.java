package com.mygdx.platformer.Sprites.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
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

    protected PlayScreen screen;

    // States
    public State currentState;
    public State previousState;
    public float stateTimer;

    public boolean isDead;
    public boolean facingRight;

    protected boolean runAttackAnimation;

    protected boolean shouldMove;
    protected boolean destroyed;
    protected boolean setToDestroy;
    protected float healthCapacity;
    protected float currentHealth;
    protected float magickaCapacity;
    protected float currentMagicka;
    protected float speed;
    protected float baseDamage;
    protected float attackCooldown;
    protected boolean inCombat = false;
    protected float timeSinceLastAttack;
    protected Entity opponent;
    protected ArrayMap<String, Animation> animations;
    protected ArrayMap<String, TextureRegion> textures;
    protected String entityTag;
    public Entity(PlayScreen screen, float health, float magicka, float baseDamage, float speed) {
        this(screen, 0f, 0f, health, magicka, speed, baseDamage, "entity");
    }

    public Entity(PlayScreen screen, float x, float y, float health, float magicka, float speed, float baseDamage, String entityTag) {
        super();
        this.world = screen.getWorld();
        this.screen = screen;
        loadTexturesAndAnimations(screen);
        setPosition(x, y);
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
        attackCooldown = 5f;
        this.baseDamage = baseDamage;
        facingRight = true;
        this.entityTag = entityTag;
        defineEntity();
        //logEntity();
    }

    private void logEntity() {
        Gdx.app.log(entityTag, "Entity instantiated with properties: " +
                "healthCapacity=" + healthCapacity +
                ", currentHealth=" + currentHealth +
                ", magickaCapacity=" + magickaCapacity +
                ", currentMagicka=" + currentMagicka +
                ", speed=" + speed +
                ", destroyed=" + destroyed +
                ", setToDestroy=" + setToDestroy +
                ", timeSinceLastAttack=" + timeSinceLastAttack +
                ", currentState=" + currentState +
                ", previousState=" + previousState +
                ", stateTimer=" + stateTimer +
                ", shouldMove=" + shouldMove +
                ", isDead=" + isDead +
                ", baseDamage=" + baseDamage +
                ", facingRight=" + facingRight +
                ", entityTag=" + entityTag
        );
    }

    public void move() {
        if (b2body != null && b2body.isActive()) {
            float multiple = facingRight ? 1 : -1;
            b2body.setLinearVelocity(new Vector2(speed * multiple, 0));
        }
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
    }

    protected void incrementMagicka(float magicka) {
        this.currentMagicka += magicka;

        if(this.currentMagicka > magickaCapacity) {
            this.currentMagicka = magickaCapacity;
        }
        if(this.currentMagicka < 0) {
            this.currentMagicka = 0;
        }
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
        runAttackAnimation = false;
    }

    public void startFriendlyCollision () {
        shouldMove = false;
    }

    public void endFriendlyCollision () {
        shouldMove = true;
    }

    protected void attack() {
        opponent.applyDamage(baseDamage);
        runAttackAnimation = true;
    }


    public boolean isDestroyed() {
        return destroyed;
    }

    public abstract void loadTexturesAndAnimations(PlayScreen screen);
    protected abstract void defineEntity();
}
