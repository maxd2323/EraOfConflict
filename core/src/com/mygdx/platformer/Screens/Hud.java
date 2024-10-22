package com.mygdx.platformer.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.platformer.Platformer;
import com.mygdx.platformer.Sprites.Items.Item;
import com.mygdx.platformer.Sprites.Items.ItemDef;
import com.mygdx.platformer.Sprites.UI.HealthBar;
import com.mygdx.platformer.utils.DataStructures.Inventory;
import com.mygdx.platformer.utils.DataStructures.InventoryItem;

import java.util.Optional;

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;
    private static Integer currentHealth;
    private static Integer currentMagicka;
    private static Integer coins;

    private static Label scoreLabel;
    private static Label healthTracker;
    private static Inventory currentInventory;
    private DialogBox dialogBox;
    Label healthLabel;
    Label levelLabel;
    Label worldLabel;
    Label platformerLabel;

    static ProgressBar healthBar;
    static ProgressBar magickaBar;

    public Hud(SpriteBatch sb) {
        currentInventory = new Inventory(9);
        currentHealth = 99;
        currentMagicka = 99;
        coins = 0;

        viewport = new FitViewport(600, 400, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        drawAll(currentInventory);
    }

    public void setDialogBoxText(String text) {
        this.dialogBox.setText(text);
    }

    public void appendDialogBoxText(String text) {
        this.dialogBox.appendText(text);
    }

    public void drawAll(Inventory currentInventory) {
        stage.clear();
        createTable();
        healthBar = createProgressBar("ui/healthbar.png", 0, 100, 10, 10, currentHealth);
        magickaBar = createProgressBar("ui/magickaBar.png", 0, 100, 350, 10, currentMagicka);
        createInventoryBar(currentInventory);
        dialogBox = new DialogBox(stage);
    }

    private void createInventoryBar(Inventory currentInventory) {
        int inventorySpaces = 9;
        for(int i = 0; i<inventorySpaces; i++) {
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
        for(int i = 0; i<currentInventory.size; i++) {
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

        healthTracker = new Label(String.format("%03d", currentHealth), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("Coins %06d", coins), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        healthLabel = new Label("Heatlh", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new Label("1-L", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel = new Label("Location", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        platformerLabel = new Label("PLATFORMER", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

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

    public static void setMagicka(int magicka) {
        currentMagicka = magicka;
        magickaBar.setValue((float) magicka);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
