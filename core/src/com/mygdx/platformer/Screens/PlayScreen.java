package com.mygdx.platformer.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.platformer.Platformer;
import com.mygdx.platformer.Sprites.Entities.DarkElf;
import com.mygdx.platformer.Sprites.Entities.Enemies.Enemy;
import com.mygdx.platformer.Sprites.Entities.Enemies.HellKnight;
import com.mygdx.platformer.Sprites.Entities.Player;
import com.mygdx.platformer.Sprites.Items.*;
import com.mygdx.platformer.Sprites.Projectiles.Projectile;
import com.mygdx.platformer.utils.B2WorldCreator;
import com.mygdx.platformer.utils.DataStructures.Inventory;
import com.mygdx.platformer.utils.WorldContactListener;

import java.util.concurrent.LinkedBlockingQueue;

public class PlayScreen implements Screen {

    public Platformer game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // Box2d
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    private HellKnight hellKnight;

    private Music music;

    private Array<Item> items;
    private Array<Projectile> projectiles;
    private Array<Player> playerEntities;
    private Array<Enemy> enemies;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;
    public Inventory inventory;

    private TextureAtlas darkElfAtlas;
    private TextureAtlas devilArcherAtlas;
    private TextureAtlas hellKnightAtlas;


    public PlayScreen(Platformer game) {
        darkElfAtlas = new TextureAtlas("sprites/dark_elf/dark_elf_merged.atlas");
        devilArcherAtlas = new TextureAtlas("sprites/devil_archer1/devil_archer1.atlas");
        hellKnightAtlas = new TextureAtlas("sprites/hell_knight/hell_knight.atlas");

        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(Platformer.getvWidth(), Platformer.getvHeight(), gameCam);
        hud = new Hud(game.batch);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("levels/level1/level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, (float) 1 / Platformer.PPM);

        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0,  Platformer.GRAVITY), true);
        b2dr = new Box2DDebugRenderer();
        creator = new B2WorldCreator(this);
        world.setContactListener(new WorldContactListener());

        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();
        items = new Array<Item>();
        projectiles = new Array<Projectile>();
        playerEntities = new Array<Player>();
        enemies = new Array<Enemy>();

        inventory = new Inventory(9);

        tempInitializeWorld();
    }

    private void tempInitializeWorld() {
        hellKnight = new HellKnight(this, 20, 3);
        enemies.add(hellKnight);
    }

    public void redrawHud(Inventory inventory) {
        hud.drawAll(inventory);
    }

    public void playMusic() {
        music = Platformer.manager.get("audio/music/mario_music.ogg", Music.class);
        music.setLooping(true);
        music.setVolume(0.1f);
        music.play();
    }

    public void spawnProjectile(Projectile projectile){
        projectiles.add(projectile);
    }

    public void spawnItem(ItemDef itemDef) {
        itemsToSpawn.add(itemDef);
    }

    public void handleSpawningItems() {
        if(!itemsToSpawn.isEmpty()) {
            ItemDef itemDef = itemsToSpawn.poll();
//            if(itemDef.type == Mushroom.class) {
//                items.add(new Mushroom(this, itemDef.position.x, itemDef.position.y));
//            }
        }
    }

    public TextureAtlas getAtlas(String atlasName) {
        switch (atlasName) {
            case "Devil Archer":
                return devilArcherAtlas;
            case "Hell Knight":
                return hellKnightAtlas;
            case "Dark Elf":
            default:
                return darkElfAtlas;
        }
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    public boolean gameOver() {
        return false;
    }

    public void handleInput(float dt) {
        if(!gameOver()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
                enemies.add(new HellKnight(this, 20, 3));
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                playerEntities.add(new DarkElf(this, 20, 3));
            }

            for(int i = 0; i < 9; i++) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.valueOf(String.valueOf(i+1)))) {
                    setSelectedInventorySpace(i);
                }
            }
        }
    }

    public void update(float dt) {
        handleInput(dt);
        handleSpawningItems();

        world.step(1/60f, 6, 4);

        for(Player player: playerEntities) {
            player.update(dt);
            if(player.isDestroyed())
                playerEntities.removeValue(player, true);
        }

        for(Enemy enemy : enemies) {
            enemy.update(dt);
            if(enemy.isDestroyed())
                enemies.removeValue(enemy, true);
        }

        for(Item item: items){
            item.update(dt);
        }

        for(Projectile projectile: projectiles) {
            projectile.update(dt);
            if(projectile.isDestroyed())
                projectiles.removeValue(projectile, true);
        }

        hud.update(dt);

        if(playerEntities.size > 0) {
            gameCam.position.x = playerEntities.get(playerEntities.size-1).b2body.getPosition().x;
        }
        gameCam.update();

        renderer.setView(gameCam);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // render game map
        renderer.render();

        // render our box2ddebuglines
        b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        update(delta);
        for(Player player : playerEntities) {
            player.draw(game.batch);
        }
        for(Enemy enemy : enemies) {
            enemy.draw(game.batch);
        }

        for(Projectile projectile : projectiles) {
            projectile.draw(game.batch);
        }

        for(Item item : items) {
            item.draw(game.batch);
        }
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        if(gameOver()) {
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
    }

    public void setSelectedInventorySpace(int i) {
        inventory.selectedIndex = i;
        redrawHud(inventory);
    }

    public boolean addItemToInventory(Item item, Class rootClass) {
        boolean addSuccess = inventory.addIfSpace(item, rootClass);
        if(addSuccess) {
            redrawHud(inventory);
        }
        return addSuccess;
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }

    @Override
    public void show() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
