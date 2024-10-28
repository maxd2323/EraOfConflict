package com.mygdx.platformer.utils;

import com.mygdx.platformer.Sprites.Entities.EntityStats;

import java.util.Map;

public class Constants {
    private Constants() {}
    public static final EntityStats DARK_ELF_STATS = new EntityStats(100f, 100f, 2f, 5f, 0.5f);
    public static final EntityStats DEVIL_ARCHER_STATS = new EntityStats(50f, 10f, 2f, 5f, 2f);
    public static final EntityStats HELL_KNIGHT_STATS = new EntityStats(50f, 10f, 2f, 5f, 1.3f);
}
