package com.mygdx.platformer.utils;

import com.badlogic.gdx.Gdx;
import com.mygdx.platformer.Screens.PlayScreen;
import com.mygdx.platformer.Sprites.Entities.Enemies.Enemy;
import com.mygdx.platformer.utils.DataStructures.EntityFactory;

public class EnemySpawner {
    private PlayScreen screen;
    private float spawnInterval;
    private float timer;
    private float spawnX, spawnY;
    private Class<? extends Enemy> enemyClass;

    public EnemySpawner(PlayScreen screen, float spawnX, float spawnY, float spawnInterval, Class<? extends Enemy> enemyClass) {
        this.screen = screen;
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.spawnInterval = spawnInterval;
        this.enemyClass = enemyClass;
        this.timer = 0;
    }

    public void update(float deltaTime) {
        timer += deltaTime;

        // Check if it's time to spawn a new enemy
        if (timer >= spawnInterval) {
            spawnEnemy();
            timer = 0;  // Reset timer after spawning
        }
    }

    private void spawnEnemy() {
        try {
            // Instantiate the enemy using reflection, passing in the PlayScreen, spawnX, and spawnY
            Enemy enemy = (Enemy) EntityFactory.createEntity(enemyClass, screen, spawnX, spawnY);
            screen.spawnEnemy(enemy);  // Add the enemy to PlayScreen’s enemy list
        } catch (Exception e) {
            e.printStackTrace();
            Gdx.app.log("EnemySpawner", "Error spawning enemy of type: " + enemyClass.getName());
        }
    }
}
