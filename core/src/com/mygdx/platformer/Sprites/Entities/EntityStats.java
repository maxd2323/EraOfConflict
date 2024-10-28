package com.mygdx.platformer.Sprites.Entities;

public class EntityStats {
    public final float initialHealth;
    public final float initialMagicka;
    public final float speed;
    public final float baseDamage;
    public final float attackCooldown;

    public EntityStats(float initialHealth, float initialMagicka, float speed, float baseDamage, float attackCooldown) {
        this.initialHealth = initialHealth;
        this.initialMagicka = initialMagicka;
        this.speed = speed;
        this.baseDamage = baseDamage;
        this.attackCooldown = attackCooldown;
    }
}
