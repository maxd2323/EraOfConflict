package com.mygdx.platformer.Sprites.Entities;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.mygdx.platformer.Platformer;
import com.mygdx.platformer.Screens.Hud;
import com.mygdx.platformer.Screens.PlayScreen;
import com.mygdx.platformer.Sprites.Entities.Enemies.Enemy;
import com.mygdx.platformer.Sprites.Entities.Enemies.HellKnight;
import com.mygdx.platformer.Sprites.Items.Coin;
import com.mygdx.platformer.Sprites.Items.Food;
import com.mygdx.platformer.Sprites.Items.Item;
import com.mygdx.platformer.Sprites.Items.Potion;
import com.mygdx.platformer.Sprites.Projectiles.Projectile;
import com.mygdx.platformer.Sprites.Projectiles.RockProjectile;
import com.mygdx.platformer.utils.Constants;
import com.mygdx.platformer.utils.DataStructures.Inventory;
import com.mygdx.platformer.utils.DataStructures.InventoryItem;

import java.util.Map;
import java.util.Optional;

public class Player extends Entity {

    // States
    public State currentState;
    public State previousState;

    // Box2D Physics
    public World world;
    public Body b2body;

    // Boolean Flags
    private boolean runningRight;
    private boolean runThrowAnimation;
    private boolean runHurtAnimation;
    private boolean isDead;

    // Other
    public float stateTimer;

    public int jumpCount;
    public Inventory inventory;
    private int coinCount;
    private PlayScreen screen;
    private float swingCooldown = 1.0f;  // 1 second between swings
    private Entity opponent;


    public Player(PlayScreen screen) {
        super(
                screen,
                Constants.PLAYER_INITIAL_HEALTH,
                Constants.PLAYER_INITIAL_MAGICKA,
                Constants.PLAYER_INITIAL_SPEED
        );
        this.world = screen.getWorld();
        this.screen = screen;

        jumpCount = 0;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        definePlayer();

        setBounds(0, 0, Platformer.getTileMultiplier(1.5f), Platformer.getTileMultiplier(1.5f));
        setRegion(textures.get("stand"));

        Hud.setHealth((int) currentHealth);
        Hud.setMagicka((int) currentMagicka);

        inventory = new Inventory(9);
        coinCount = 0;
    }

    // Getters

    public TextureRegion getFrame(float deltaTime) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case DEAD:
                region = (TextureRegion) animations.get("dead").getKeyFrame(stateTimer, false);
                break;
            case JUMPING:
                region = textures.get("jump");
                break;
            case RUNNING:
                region = (TextureRegion) animations.get("run").getKeyFrame(stateTimer, true);
                break;
            case THROWING:
                region = (TextureRegion) animations.get("throw").getKeyFrame(stateTimer);
                if(runningRight && !region.isFlipX()) {
                    region.flip(true, false);
                } else if(!runningRight && region.isFlipX()) {
                    region.flip(true, false);
                }
                if(animations.get("throw").isAnimationFinished(stateTimer)){
                    runThrowAnimation = false;
                }
                break;
            case HURT:
                region = (TextureRegion) animations.get("hurt").getKeyFrame(stateTimer);
                if(runningRight && !region.isFlipX()) {
                    region.flip(true, false);
                } else if(!runningRight && region.isFlipX()) {
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
        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        }
        else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }
        stateTimer = currentState == previousState ? stateTimer + deltaTime : 0;
        previousState = currentState;
        return region;
    }

    public State getState() {
        if(isDead) {
            return State.DEAD;
        }
        else if(runThrowAnimation) {
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
    public float getStateTimer() {
        return stateTimer;
    }

    public void moveRight() {
        this.b2body.setLinearVelocity(new Vector2(1.5f, 0));
    }

    public void update(float deltaTime) {
        Hud.setHealth((int) currentHealth);
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 + 8 / Platformer.PPM);
        setRegion(getFrame(deltaTime));
        incrementMagicka(1f);

        if(b2body.getLinearVelocity().y == 0) {
            this.moveRight();
        }

        if(b2body.getPosition().y < -10) {
            isDead = true;
        }
        if(currentState == State.STANDING || currentState == State.RUNNING) {
            jumpCount = 0;
        }
        if(currentHealth <= 0) {
            isDead = true;
        }
    }

    private void shoot(Boolean shootRight) {
        RockProjectile rock = new RockProjectile(
                screen,
                b2body.getPosition().x,
                b2body.getPosition().y,
                runningRight
        );
        screen.spawnProjectile(rock);
        runThrowAnimation = true;
        runningRight = shootRight;
    }

    public void hit(Enemy enemy) {
        if(currentState != State.HURT) {
            if (enemy instanceof HellKnight) {
                if ((((HellKnight) enemy).currentState.equals(State.SLASHING))) {
                    applyDamage(enemy.collisionDamage);
                    Hud.setHealth((int) currentHealth);
                    b2body.applyLinearImpulse(-5f, 2f, getX(), getY(), true);
                    runHurtAnimation = true;
                }
            } else {
                applyDamage(enemy.collisionDamage);
                Hud.setHealth((int) currentHealth);
                b2body.applyLinearImpulse(-5f, 2f, getX(), getY(), true);
                runHurtAnimation = true;
            }
        }
    }

    public void definePlayer() {
        BodyDef bDef = new BodyDef();
        bDef.position.set(Platformer.getTileMultiplier(2f), Platformer.getTileMultiplier(2f));
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(Platformer.getTileMultiplier(0.375f));
        fDef.filter.categoryBits = Platformer.PLAYER_BIT;
        fDef.filter.maskBits = Platformer.GROUND_BIT
                | Platformer.COIN_BIT
                | Platformer.BRICK_BIT
                | Platformer.ENEMY_BIT
                | Platformer.OBJECT_BIT
                | Platformer.ENEMY_HEAD_BIT
                | Platformer.ITEM_BIT
                | Platformer.CHEST_BIT
                | Platformer.PROJECTILE_BIT;

        fDef.shape = shape;
        b2body.createFixture(fDef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(Platformer.getTileMultiplier(-.2f), Platformer.getTileMultiplier(.7f)), new Vector2(Platformer.getTileMultiplier(.2f), Platformer.getTileMultiplier(.7f)));
        fDef.filter.categoryBits = Platformer.PLAYER_HEAD_BIT;
        fDef.filter.maskBits = Platformer.GROUND_BIT
                | Platformer.COIN_BIT
                | Platformer.BRICK_BIT
                | Platformer.ENEMY_BIT
                | Platformer.OBJECT_BIT
                | Platformer.ENEMY_HEAD_BIT
                | Platformer.ITEM_BIT
                | Platformer.CHEST_BIT;
        fDef.shape = head;
        fDef.isSensor = true;

        b2body.createFixture(fDef).setUserData(this);
    }

    public boolean addItemToInventory(Item item, Class rootClass) {
        boolean addSuccess = inventory.addIfSpace(item, rootClass);
        if(addSuccess) {
            screen.redrawHud(inventory);
        }
        return addSuccess;
    }
    public void setSelectedInventorySpace(int i) {
        inventory.selectedIndex = i;
        screen.redrawHud(inventory);
    }
    public void incrementCoins(int value) {
        this.coinCount += value;
        Hud.setScore(this.coinCount);
    }

    @Override
    public void loadTexturesAndAnimations(PlayScreen screen) {
        TextureRegion playerStand = new TextureRegion(new Texture("sprites/dark_elf/raw/Idle/0_Dark_Elves_Idle_000.png"));

        textures = new ArrayMap<>();
        textures.put("jump", playerStand);
        textures.put("stand", playerStand);

        Animation playerRun = new Animation(0.1f, screen.getAtlas("Dark Elf").findRegions("0_Dark_Elves_Running"));
        Animation playerDead = new Animation(0.1f, screen.getAtlas("Dark Elf").findRegions("0_Dark_Elves_Dying"));
        Animation playerThrow = new Animation(0.05f, screen.getAtlas("Dark Elf").findRegions("0_Dark_Elves_Throwing"));
        Animation playerHurt = new Animation(0.05f, screen.getAtlas("Dark Elf").findRegions("0_Dark_Elves_Hurt"));

        animations = new ArrayMap<>();
        animations.put("run", playerRun);
        animations.put("dead", playerDead);
        animations.put("throw", playerThrow);
        animations.put("hurt", playerHurt);
    }
}
