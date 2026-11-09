package com.tontoque28.rpgsubterra.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class SubterraCommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue ENABLE_DIRECTIONAL_LIGHT;
    public static final ForgeConfigSpec.BooleanValue ENABLE_RELIQUARY_CHARM_UNLOCK;
    public static final ForgeConfigSpec.DoubleValue GLOBAL_MINING_SPEED_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue CHEST_LOOT_CHANCE_MULTIPLIER;

    static {
        BUILDER.push("Configuracion General de RPGCore: Subterra");

        ENABLE_DIRECTIONAL_LIGHT = BUILDER
                .comment("Activa el haz de luz direccional en los cascos de minero y linternas de Curios.")
                .define("enableDirectionalLight", true);

        ENABLE_RELIQUARY_CHARM_UNLOCK = BUILDER
                .comment("Permite usar el Relicario del Gambusino para desbloquear +1 slot de Charm (Ponlo en false si el modpack ya tiene demasiados slots).")
                .define("enableReliquaryCharmUnlock", true);

        GLOBAL_MINING_SPEED_MULTIPLIER = BUILDER
                .comment("Multiplicador global para ajustar todos los bonos de velocidad de mineria del mod (1.0 = valores por defecto: +5% a +45%).")
                .defineInRange("globalMiningSpeedMultiplier", 1.0, 0.1, 5.0);

        CHEST_LOOT_CHANCE_MULTIPLIER = BUILDER
                .comment("Multiplicador de probabilidad de encontrar reliquias de mineria en cofres de Mineshafts y Dungeons (1.0 = normal, 0.0 = desactivado).")
                .defineInRange("chestLootChanceMultiplier", 1.0, 0.0, 5.0);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
