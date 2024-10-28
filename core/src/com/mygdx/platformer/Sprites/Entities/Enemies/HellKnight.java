package com.mygdx.platformer.Sprites.Entities.Enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.ArrayMap;
import com.mygdx.platformer.Platformer;
import com.mygdx.platformer.Screens.PlayScreen;
import com.mygdx.platformer.Sprites.Entities.Player;
import com.mygdx.platformer.Sprites.Projectiles.Projectile;
import com.mygdx.platformer.utils.Constants;

public class HellKnight extends Enemy {

    // Boolean flags
    private boolean runHurtAnimation;
    public boolean facingRight;

    private float stateTimer;

    public HellKnight(PlayScreen screen, float x, float y) {
        super(
                screen,
                x,
                y,
                Constants.HELL_KNIGHT_INITIAL_HEALTH,
                Constants.HELL_KNIGHT_INITIAL_MAGICKA,
                Constants.HELL_KNIGHT_INITIAL_SPEED,
                Constants.HELL_KNIGHT_BASE_DAMAGE,
                "HellKnight"
        );
        facingRight = true;

        attackCooldown = 5f;

        setRegion((TextureRegion) animations.get("run").getKeyFrames()[0]);
        setBounds(getX(), getY(), Platformer.getTileMultiplier(1.5f), Platformer.getTileMultiplier(1.5f));
    }

    public TextureRegion getFrame(float deltaTime) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case DEAD:
                region = (TextureRegion) animations.get("dead").getKeyFrame(stateTimer, false);
                if(animations.get("dead").isAnimationFinished(stateTimer) && stateTimer > 5){
                    setToDestroy = true;
                }
                break;
            case SLASHING:
                region = (TextureRegion) animations.get("slash").getKeyFrame(stateTimer, false);
                if(animations.get("slash").isAnimationFinished(stateTimer) && stateTimer > 5){
                    currentState = State.STANDING;
                }
                break;
            case RUNNING:
                region = (TextureRegion) animations.get("run").getKeyFrame(stateTimer, true);
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
                region = (TextureRegion) animations.get("run").getKeyFrame(stateTimer, true);
                break;
        }
        if((b2body.getLinearVelocity().x < 0 || !facingRight) && !region.isFlipX()) {
            region.flip(true, false);
            facingRight = false;
        }
        else if((b2body.getLinearVelocity().x > 0 || facingRight) && region.isFlipX()) {
            region.flip(true, false);
            facingRight = true;
        }
        stateTimer = (currentState == previousState) ? stateTimer + deltaTime : 0;
        previousState = currentState;
        return region;
    }

    public State getState() {
        if(isDead) {
            return State.DEAD;
        } else if(runAttackAnimation) {
            return State.SLASHING;
        }
        else if(runHurtAnimation) {
            return State.HURT;
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

    @Override
    public void hit(Projectile projectile) {
        runHurtAnimation = true;
        applyDamage(projectile.damage);
    }

    @Override
    public void hitOnHead(Player player) {

    }

    @Override
    public void onEnemyHit(Enemy enemy) {

    }

    @Override
    public void loadTexturesAndAnimations(PlayScreen screen) {
        TextureRegion playerStand = new TextureRegion(new Texture("sprites/dark_elf/raw/Idle/0_Dark_Elves_Idle_000.png"));

        textures = new ArrayMap<>();
        textures.put("jump", playerStand);
        textures.put("stand", playerStand);

        Animation walk = new Animation(0.1f, screen.getAtlas("Hell Knight").findRegions("0_Hell_Knight_Walking"));
        Animation dead = new Animation(0.1f, screen.getAtlas("Hell Knight").findRegions("0_Hell_Knight_Dying"));
        Animation slash = new Animation(0.3f, screen.getAtlas("Hell Knight").findRegions("0_Hell_Knight_Slashing"));
        // Animation idle = new Animation(0.1f, screen.getAtlas("Hell Knight").findRegions("0_Hell_Knight_Idle"));

        animations = new ArrayMap<>();
        animations.put("run", walk);
        animations.put("dead", dead);
        animations.put("slash", slash);
        // animations.put("idle", idle);
    }
}
