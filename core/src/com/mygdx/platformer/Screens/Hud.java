package com.mygdx.platformer.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.platformer.utils.DataStructures.Inventory;
import com.mygdx.platformer.utils.DataStructures.InventoryItem;

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;

    private static Integer currentHealth;
    private static Integer coins;

    private static Label scoreLabel;
    private static Label healthTracker;
    Label healthLabel;
    Label levelLabel;
    Label worldLabel;
    Label platformerLabel;

    static ProgressBar healthBar;
    static ProgressBar rechargeBar;  // Renamed to represent recharge points

    private float maxRechargePoints = 100;    // Max recharge points
    private float rechargePoints = 100;       // Current recharge points

    public Hud(SpriteBatch sb) {
        currentHealth = 99;
        coins = 0;

        viewport = new FitViewport(600, 400, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        drawAll(null);
    }

    public void drawAll(Inventory currentInventory) {
        stage.clear();
        createTable();
        healthBar = createProgressBar("ui/healthbar.png", 0, 100, 10, 10, currentHealth);
        rechargeBar = createProgressBar("ui/magickaBar.png", 0, maxRechargePoints, 350, 10, (int) rechargePoints);  // Updated to use recharge points
        if (currentInventory != null) {
            createInventoryBar(currentInventory);
        }
    }

    private void createInventoryBar(Inventory currentInventory) {
        int inventorySpaces = 9;
        for(int i = 0; i < inventorySpaces; i++) {
            Image inventoryBox = new Image();
            inventoryBox.setDrawable(new TextureRegionDrawable(new Texture(
                    i == currentInventory.selectedIndex ?
                            Gdx.files.local("ui/inventory_square2.png") :
                            Gdx.files.local("ui/inventory_square.png")
            )));
            inventoryBox.setScale(0.07f, 0.1f);
            inventoryBox.setFillParent(true);
            inventoryBox.setPosition((i*50)+50, 300);
            stage.addActor(inventoryBox);
        }
        for(int i = 0; i < currentInventory.size; i++) {
            if(currentInventory.get(i).isPresent()) {
                InventoryItem inventoryItem = currentInventory.get(i).get();
                Image inventoryBox = new Image();
                inventoryBox.setDrawable(new TextureRegionDrawable(inventoryItem.getTextureRegion()));
                inventoryBox.setScale(0.07f, 0.1f);
                inventoryBox.setFillParent(true);
                inventoryBox.setPosition((i * 50) + 50, 300);

                Label label = new Label(String.valueOf(inventoryItem.getSize()), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
                label.setBounds( inventoryBox.getX(), inventoryBox.getY(), inventoryBox.getWidth(), inventoryBox.getHeight() );
                label.setText(inventoryItem.getSize());
                label.setAlignment( Align.center );
                stage.addActor(inventoryBox);
                stage.addActor(label);
            }
        }
    }

    public void createTable() {
        Table table = new Table();
        table.top();
        table.setFillParent(true);

        scoreLabel = new Label(String.format("Coins %06d", coins), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(platformerLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(healthLabel).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(healthTracker).expandX();
        stage.addActor(table);
    }

    public ProgressBar createProgressBar(String path, float min, float max, float px, float py, int current) {
        Skin skin = new Skin();
        Pixmap pixmap = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        TextureRegionDrawable textureBar = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(path)), 1, 20));
        ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle(skin.newDrawable("white", Color.DARK_GRAY), textureBar);
        barStyle.knobBefore = barStyle.knob;
        ProgressBar bar = new ProgressBar(min, max, 0.5f, false, barStyle);
        bar.setPosition(px, py);
        bar.setSize(150, bar.getPrefHeight());
        bar.setValue(current);
        stage.addActor(bar);
        return bar;
    }

    public void update(float deltaTime) {
        rechargePoints = Math.min(rechargePoints + deltaTime * 10, maxRechargePoints);  // Simulate recharge rate
        rechargeBar.setValue(rechargePoints);  // Update recharge bar display
    }

    public static void setScore(int value) {
        coins = value;
        scoreLabel.setText(coins);
    }

    public static void setHealth(int health) {
        currentHealth = health;
        healthTracker.setText(currentHealth);
        healthBar.setValue((float) health);
    }

    public void setRechargePoints(float points) {
        rechargePoints = points;
        rechargeBar.setValue(rechargePoints);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
