package dev.lopyluna.death_keeper;

import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
@EventBusSubscriber(modid = DeathKeeper.MOD_ID)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue GRAVESTONE_ON_DEATH = BUILDER
            .comment("Whether to where if Player/Mob Drops will be put into the Gravestone")
            .define("GravestoneOnDeath", true);

    private static final ModConfigSpec.DoubleValue ITEM_LOST_PERCENTAGE = BUILDER
            .comment("Percentage of amount that items are lost")
            .defineInRange("ItemLostPercentage", 100.0, 0, 100);
    private static final ModConfigSpec.IntValue GRAVE_DISTANCE = BUILDER
            .comment("Distance to merge graves instead of placing a new grave")
            .defineInRange("GraveMaxDistance", 5, 1, 256);
    private static final ModConfigSpec.IntValue MAX_GRAVE_LIFESPAN = BUILDER
            .comment("Time till the grave despawns in Ticks")
            .defineInRange("MaxGraveLifespan", 20 * 60 * 60, 1, 20 * 60 * 60 * 12);
    private static final ModConfigSpec.BooleanValue GRAVE_MERGE = BUILDER
            .comment("Whether to allow grave merging")
            .define("GraveMerging", true);
    private static final ModConfigSpec.BooleanValue EMPTY_GRAVES = BUILDER
            .comment("Whether to allow grave to spawn with no drops")
            .define("EmptyGraves", false);
    private static final ModConfigSpec.BooleanValue COARSE_DIRT_BELOW = BUILDER
            .comment("Whether to allow coarse to spawn under graves")
            .define("CoarseDirtBelowGraves", false);
    private static final ModConfigSpec.BooleanValue NON_PLAYERS_GRAVE_TITLES = BUILDER
            .comment("Whether to text to be displayed above the gravestone for a non player entity")
            .define("NonPlayerGraveTitles", false);
    private static final ModConfigSpec.ConfigValue<List<? extends String>> ENTITIES_GRAVES = BUILDER
            .comment("A whitelist of entities to be able to drop graves.")
            .defineListAllowEmpty("EntitiesGraves", List.of("minecraft:player", "minecraft:villager", "minecraft:wandering_trader"), Config::validateEntity);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean gravestoneOnDeath;

    public static double itemLostPercentage;
    public static int graveMaxDistance;
    public static int maxGraveLifespan;
    public static boolean graveMerging;
    public static boolean emptyGraves;
    public static boolean nonPlayerGraveTitles;
    public static boolean coarseDirtBelowGraves;
    public static Set<EntityType<?>> entities;

    private static boolean validateEntity(final Object obj) {
        return obj instanceof String name && name.contains(":") && EntityType.byString(name).isPresent();
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (event.getConfig().getSpec() != SPEC) return;
        gravestoneOnDeath = GRAVESTONE_ON_DEATH.get();
        itemLostPercentage = ITEM_LOST_PERCENTAGE.get();
        graveMaxDistance = GRAVE_DISTANCE.get();
        maxGraveLifespan = MAX_GRAVE_LIFESPAN.get();
        graveMerging = GRAVE_MERGE.get();
        emptyGraves = EMPTY_GRAVES.get();
        nonPlayerGraveTitles = NON_PLAYERS_GRAVE_TITLES.get();
        coarseDirtBelowGraves = COARSE_DIRT_BELOW.get();
        entities = ENTITIES_GRAVES.get().stream()
                .map(type -> EntityType.byString(type).orElse(null))
                .filter(Objects::nonNull).collect(Collectors.toSet());
    }
}
