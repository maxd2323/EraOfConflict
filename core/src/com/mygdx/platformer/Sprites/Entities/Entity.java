package com.mygdx.platformer.Sprites.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ArrayMap;
import com.mygdx.platformer.Screens.PlayScreen;

public abstract class Entity extends Sprite {
    public enum State { FALLING, JUMPING, STANDING, RUNNING, DEAD, THROWING, HURT, SHOOTING, SLASHING };

    // Box2D Physics
    protected World world;
    public Body b2body;
    protected PlayScreen screen;

    // Animation and Texture Management
    protected ArrayMap<String, Animation> animations;
    protected ArrayMap<String, TextureRegion> textures;

    // Entity State Management
    protected State currentState;
    protected State previousState;
    protected float stateTimer;

    // Flags for Entity Status
    public boolean isDead;
    protected boolean destroyed;
    protected boolean setToDestroy;
    protected boolean shouldMove;

    // Combat and Attack Management
    protected boolean inCombat;
    protected Entity opponent;
    protected float timeSinceLastAttack;

    // Animation management
    protected boolean runAttackAnimation;
    protected boolean runThrowAnimation;
    protected boolean runHurtAnimation;

    // Facing Direction
    protected boolean facingRight;

    // Stats
    protected EntityStats stats;
    protected float currentHealth;
    protected float currentMagicka;

    // Entity Identification
    protected String entityTag;

    public Entity(PlayScreen screen, EntityStats entityStats) {
        this(screen, 0f, 0f, entityStats, "entity");
    }

    public Entity(PlayScreen screen, float x, float y, EntityStats entityStats, String entityTag) {
        super();
        this.world = screen.getWorld();
        this.screen = screen;
        loadTexturesAndAnimations(screen);
        setPosition(x, y);

        stats = entityStats;
        currentHealth = stats.initialHealth;
        currentMagicka = stats.initialMagicka;

        destroyed = false;
        setToDestroy = false;
        timeSinceLastAttack = 0f;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        shouldMove = true;
        isDead = false;
        facingRight = true;
        this.entityTag = entityTag;

        runThrowAnimation = false;
        runAttackAnimation = false;
        runHurtAnimation = false;
        inCombat = false;

        defineEntity();
        logEntity();
    }

    private void logEntity() {
        Gdx.app.log(entityTag, "Entity instantiated with properties: " +
                "healthCapacity=" + stats.initialHealth +
                ", currentHealth=" + currentHealth +
                ", magickaCapacity=" + stats.initialMagicka +
                ", currentMagicka=" + currentMagicka +
                ", speed=" + stats.speed +
                ", destroyed=" + destroyed +
                ", setToDestroy=" + setToDestroy +
                ", timeSinceLastAttack=" + timeSinceLastAttack +
                ", currentState=" + currentState +
                ", previousState=" + previousState +
                ", stateTimer=" + stateTimer +
                ", shouldMove=" + shouldMove +
                ", isDead=" + isDead +
                ", baseDamage=" + stats.baseDamage +
                ", facingRight=" + facingRight +
                ", entityTag=" + entityTag
        );
    }

    public void move() {
        if (b2body != null && b2body.isActive()) {
            float multiple = facingRight ? 1 : -1;
            b2body.setLinearVelocity(new Vector2(stats.speed * multiple, 0));
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

        if(this.currentHealth > stats.initialHealth) {
            this.currentHealth = stats.initialHealth;
        }
    }

    protected void incrementMagicka(float magicka) {
        this.currentMagicka += magicka;

        if(this.currentMagicka > stats.initialMagicka) {
            this.currentMagicka = stats.initialMagicka;
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
        if (!runAttackAnimation) {
            runAttackAnimation = true; // Start animation
            stateTimer = 0; // Reset state timer for animation
        }
    }

    public TextureRegion getFrame(float deltaTime) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case DEAD:
                region = (TextureRegion) animations.get("dead").getKeyFrame(stateTimer, false);
                if(animations.get("dead").isAnimationFinished(stateTimer) && stateTimer > 1){
                    setToDestroy = true;
                }
                break;
            case SLASHING:
                region = (TextureRegion) animations.get("slash").getKeyFrame(stateTimer, false);
                if (animations.get("slash").isAnimationFinished(stateTimer)) {
                    // Apply damage at the end of animation
                    opponent.applyDamage(stats.baseDamage);
                    runAttackAnimation = false;  // Reset after animation finishes
                }
                break;
            case JUMPING:
                region = textures.get("jump");
                break;
            case RUNNING:
                region = (TextureRegion) animations.get("run").getKeyFrame(stateTimer, true);
                break;
            case THROWING:
                region = (TextureRegion) animations.get("throw").getKeyFrame(stateTimer);
                if(facingRight && !region.isFlipX()) {
                    region.flip(true, false);
                } else if(!facingRight && region.isFlipX()) {
                    region.flip(true, false);
                }
                if(animations.get("throw").isAnimationFinished(stateTimer)){
                    runThrowAnimation = false;
                }
                break;
            case HURT:
                region = (TextureRegion) animations.get("hurt").getKeyFrame(stateTimer);
                if(facingRight && !region.isFlipX()) {
                    region.flip(true, false);
                } else if(!facingRight && region.isFlipX()) {
                    region.flip(true, false);
                }
                if(animations.get("hurt").isAnimationFinished(stateTimer)){
                    runHurtAnimation = false;
                }
                break;
            case FALLING:
            case STANDING:
            default:
                region = textures.get("stand");;
                break;
        }
        if(!facingRight && !region.isFlipX()) {
            region.flip(true, false);
        }
        stateTimer = currentState == previousState ? stateTimer + deltaTime : 0;
        previousState = currentState;
        return region;
    }

    public State getState() {
        if(isDead) {
            return State.DEAD;
        } else if (runAttackAnimation) {
            return State.SLASHING;
        } else if(runThrowAnimation) {
            return State.THROWING;
        }
        else if(runHurtAnimation) {
            return State.HURT;
        }
        else if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)) {
            return State.JUMPING;
        }
        else if(b2body.getLinearVelocity().y < 0) {
            return State.FALLING;
        }
        else if(b2body.getLinearVelocity().x != 0) {
            return State.RUNNING;
        }
        else {
            return State.STANDING;
        }
    }

    protected void updateCombat(float deltaTime) {
        if (inCombat && !runAttackAnimation) {
            timeSinceLastAttack += deltaTime;
            if (timeSinceLastAttack >= stats.attackCooldown) {
                attack();
                timeSinceLastAttack = 0f;
            }
            if (opponent != null && opponent.isDead) {
                inCombat = false;
            }
        }
    }

    protected void updatePositionAndBounds(float deltaTime) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(deltaTime));
    }

    protected void handleDestruction() {
        if (setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
        }
    }

    protected void defineCommonBody(float radius, short categoryBits, short maskBits) {
        BodyDef bDef = new BodyDef();
        bDef.position.set(getX(), getY());
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        // Set restitution to zero to reduce bounce
        fDef.restitution = 0f;

        // Set friction to a higher value to help stickiness on collision
        fDef.friction = 0.9f;

        fDef.filter.categoryBits = categoryBits;
        fDef.filter.maskBits = maskBits;
        fDef.shape = shape;
        b2body.createFixture(fDef).setUserData(this);
    }


    public boolean isDestroyed() {
        return destroyed;
    }

    public abstract void loadTexturesAndAnimations(PlayScreen screen);
    protected abstract void defineEntity();
}
