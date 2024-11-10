package com.mygdx.platformer.Sprites.Entities;

public class EntityStats {
    public float initialHealth;
    public float initialMagicka;
    public float speed;
    public float baseDamage;
    public float attackCooldown;

    public EntityStats(EntityStats other) {
        this.initialHealth = other.initialHealth;
        this.initialMagicka = other.initialMagicka;
        this.speed = other.speed;
        this.baseDamage = other.baseDamage;
        this.attackCooldown = other.attackCooldown;
    }

    public EntityStats(float initialHealth, float initialMagicka, float speed, float baseDamage, float attackCooldown) {
        this.initialHealth = initialHealth;
        this.initialMagicka = initialMagicka;
        this.speed = speed;
        this.baseDamage = baseDamage;
        this.attackCooldown = attackCooldown;
    }

    public void applyUpgrade(float healthIncrease, float magickaIncrease, float speedIncrease, float damageIncrease, float attackCooldown) {
        this.initialHealth += healthIncrease;
        this.initialMagicka += magickaIncrease;
        this.speed += speedIncrease;
        this.baseDamage += damageIncrease;
        this.attackCooldown += attackCooldown;
    }
}
