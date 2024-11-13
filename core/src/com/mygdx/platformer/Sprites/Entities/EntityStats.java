package com.mygdx.platformer.Sprites.Entities;

public class EntityStats {
    public float initialHealth;
    public float initialMagicka;
    public float speed;
    public float baseDamage;
    public float attackCooldown;
    public float spawnCost;

    public EntityStats(EntityStats other) {
        this.initialHealth = other.initialHealth;
        this.initialMagicka = other.initialMagicka;
        this.speed = other.speed;
        this.baseDamage = other.baseDamage;
        this.attackCooldown = other.attackCooldown;
        this.spawnCost = other.spawnCost;
    }

    public EntityStats(float initialHealth, float initialMagicka, float speed, float baseDamage, float attackCooldown, float spawnCost) {
        this.initialHealth = initialHealth;
        this.initialMagicka = initialMagicka;
        this.speed = speed;
        this.baseDamage = baseDamage;
        this.attackCooldown = attackCooldown;
        this.spawnCost = spawnCost;
    }

    public void applyUpgrade(float healthIncrease, float magickaIncrease, float speedIncrease, float damageIncrease, float attackCooldown, float spawnCost) {
        this.initialHealth += healthIncrease;
        this.initialMagicka += magickaIncrease;
        this.speed += speedIncrease;
        this.baseDamage += damageIncrease;
        this.attackCooldown += attackCooldown;
        this.spawnCost += spawnCost;
    }
}
