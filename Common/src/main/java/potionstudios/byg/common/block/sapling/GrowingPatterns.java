package potionstudios.byg.common.block.sapling;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.corgilib.serialization.codec.CodecUtil;
import corgitaco.corgilib.serialization.codec.CommentedCodec;
import corgitaco.corgilib.serialization.jankson.JanksonJsonOps;
import corgitaco.corgilib.serialization.jankson.JanksonUtil;
import corgitaco.corgilib.shadow.blue.endless.jankson.api.SyntaxError;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import potionstudios.byg.BYG;
import potionstudios.byg.common.block.BYGBlocks;
import potionstudios.byg.common.block.BYGWoodTypes;
import potionstudios.byg.common.world.feature.features.end.BYGEndVegetationFeatures;
import potionstudios.byg.common.world.feature.features.nether.BYGNetherVegetationFeatures;
import potionstudios.byg.common.world.feature.features.overworld.BYGOverworldTreeFeatures;
import potionstudios.byg.common.world.feature.features.overworld.BYGOverworldVegetationFeatures;
import potionstudios.byg.config.BYGConfigHandler;
import potionstudios.byg.util.ModPlatform;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;

public record GrowingPatterns(boolean logGrowth,
                              Supplier<Map<Block, List<GrowingPatternEntry>>> patternsForBlock) {


    public static final int MAX_PATTERN_SIZE = 5;

    public Optional<List<GrowingPatternEntry>> getPatterns(Block key) {
        Map<Block, List<GrowingPatternEntry>> resourceLocationListMap = this.patternsForBlock.get();
        List<GrowingPatternEntry> entry = resourceLocationListMap.get(key);
        if (entry != null) {
            return Optional.of(entry);
        }
        return Optional.empty();
    }

    public static final Supplier<GrowingPatterns> DEFAULT = Suppliers.memoize(() -> {
        return new GrowingPatterns(false, Suppliers.memoize(() -> {
            Map<Block, List<GrowingPatternEntry>> map = new TreeMap<>(Comparator.comparing(s -> Registry.BLOCK.getKey(s)));

            map.put(BYGBlocks.ARAUCARIA_SAPLING.get(), List.of(
                    new GrowingPatternEntry(List.of("x"),
                            SimpleWeightedRandomList.<FeatureSpawner>builder()
                                    .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.ARAUCARIA_TREE1)), 1)
                                    .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.ARAUCARIA_TREE2)), 1)
                                    .build())
            ));
            map.put(BYGWoodTypes.ASPEN.growerItem().get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.ASPEN_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.ASPEN_TREE3)), 1)
                            .build()),
                    new GrowingPatternEntry(List.of(
                            " x ",
                            "xxx",
                            " x "), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.ASPEN_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.ASPEN_TREE4)), 1)
                            .build()),
                    new GrowingPatternEntry(List.of(
                            "x x",
                            " x ",
                            "x x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.ASPEN_TREE5)), 1)
                            .build())
            ));
            map.put(BYGWoodTypes.BAOBAB.growerItem().get(), List.of(
                    new GrowingPatternEntry(List.of(
                            "xxx",
                            "xxx",
                            "xxx"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.BAOBAB_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.BAOBAB_TREE2)), 1)
                            .build())
            ));
            map.put(BYGWoodTypes.BLUE_ENCHANTED.growerItem().get(), List.of(
                    new GrowingPatternEntry(List.of(
                            " x ",
                            "xxx",
                            " x "), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.BLUE_ENCHANTED_TREE1)), 2)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.BLUE_ENCHANTED_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.BLUE_ENCHANTED_TREE3)), 1)
                            .build())
            ));
            map.put(BYGBlocks.BLUE_SPRUCE_SAPLING.get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_BLUE_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_BLUE_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_BLUE_TREE3)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_BLUE_TREE4)), 1)
                            .build()),
                    new GrowingPatternEntry(List.of(
                            "xx",
                            "xx"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_BLUE_TREE_MEDIUM1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_BLUE_TREE_MEDIUM2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_BLUE_TREE_MEDIUM3)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_BLUE_TREE_MEDIUM4)), 1)
                            .build()),
                    new GrowingPatternEntry(List.of(
                            "xxx",
                            "xxx",
                            "xxx"), SimpleWeightedRandomList.single(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_BLUE_TREE_LARGE1))))
            ));
            map.put(BYGBlocks.BROWN_BIRCH_SAPLING.get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.BROWN_BIRCH_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.BROWN_BIRCH_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.BROWN_BIRCH_TREE3)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.BROWN_BIRCH_TREE4)), 1)
                            .build())
            ));
            map.put(BYGBlocks.BROWN_OAK_SAPLING.get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.BROWN_OAK_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.BROWN_OAK_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.BROWN_OAK_TREE3)), 1)
                            .build()),
                    new GrowingPatternEntry(List.of(
                            "xx",
                            "xx"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.BROWN_OAK_TREE_LARGE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.BROWN_OAK_TREE_LARGE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.BROWN_OAK_TREE_LARGE3)), 1)
                            .build())
            ));
            map.put(BYGBlocks.BROWN_ZELKOVA_SAPLING.get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.ZELKOVA_BROWN_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.ZELKOVA_BROWN_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.ZELKOVA_BROWN_TREE3)), 1)
                            .build())
            ));
            map.put(BYGWoodTypes.CIKA.growerItem().get(), List.of(
                    new GrowingPatternEntry(List.of(
                            " x ",
                            "xxx",
                            " x "), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.CIKA_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.CIKA_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.CIKA_TREE3)), 1)
                            .build())
            ));
            map.put(BYGWoodTypes.CYPRESS.growerItem().get(), List.of(
                    new GrowingPatternEntry(List.of(
                            "  x  ",
                            "  x  ",
                            "xxxxx",
                            "  x  ",
                            "  x  "), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.CYPRESS_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.CYPRESS_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.CYPRESS_TREE3)), 1)
                            .build())
            ));
            map.put(BYGWoodTypes.EBONY.growerItem().get(), List.of(
                    new GrowingPatternEntry(List.of(
                            "xx",
                            "xx"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.EBONY_BUSH1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.EBONY_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.EBONY_TREE2)), 1)
                            .build())
            ));
            map.put(BYGWoodTypes.FIR.growerItem().get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.CONIFER_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.CONIFER_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.CONIFER_TREE3)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.CONIFER_TREE4)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.CONIFER_TREE5)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.CONIFER_TREE6)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.CONIFER_TREE7)), 1)
                            .build()),
                    new GrowingPatternEntry(List.of(
                            "xx",
                            "xx"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.CONIFER_TREE8)), 1)
                            .build())

            ));
            map.put(BYGWoodTypes.GREEN_ENCHANTED.growerItem().get(), List.of(
                    new GrowingPatternEntry(List.of(
                            " x ",
                            "xxx",
                            " x "), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.GREEN_ENCHANTED_TREE1)), 2)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.GREEN_ENCHANTED_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.GREEN_ENCHANTED_TREE3)), 1).build())
            ));
            map.put(BYGWoodTypes.HOLLY.growerItem().get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.HOLLY_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.HOLLY_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.HOLLY_TREE4)), 1)
                            .build()),
                    new GrowingPatternEntry(List.of(
                            "xx",
                            "xx"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.HOLLY_TREE3)), 1)
                            .build())
            ));
            map.put(BYGWoodTypes.JACARANDA.growerItem().get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.JACARANDA_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.JACARANDA_TREE2)), 1)
                            .build())
            ));
            map.put(BYGBlocks.INDIGO_JACARANDA_SAPLING.get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.INDIGO_JACARANDA_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.INDIGO_JACARANDA_TREE2)), 1)
                            .build())
            ));
            map.put(BYGBlocks.JOSHUA_SAPLING.get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.JOSHUA_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.JOSHUA_TREE2)), 1)
                            .build())
            ));
            map.put(BYGWoodTypes.MAHOGANY.growerItem().get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.MAHOGANY_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.MAHOGANY_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.MAHOGANY_TREE3)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.MAHOGANY_TREE4)), 1)
                            .build())
            ));
            map.put(BYGWoodTypes.WHITE_MANGROVE.growerItem().get(), List.of(
                    new GrowingPatternEntry(List.of(
                            " x ",
                            "x x",
                            " x "), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.WHITE_MANGROVE_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.WHITE_MANGROVE_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.WHITE_MANGROVE_TREE3)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.WHITE_MANGROVE_TREE4)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.WHITE_MANGROVE_TREE5)), 1)
                            .build())
            ));

            map.put(BYGWoodTypes.MAPLE.growerItem().get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>single(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.MAPLE_TREE1))))
            ));
            map.put(BYGBlocks.ORANGE_BIRCH_SAPLING.get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.BIRCH_ORANGE_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.BIRCH_ORANGE_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.BIRCH_ORANGE_TREE3)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.BIRCH_ORANGE_TREE4)), 1)
                            .build())
            ));
            map.put(BYGBlocks.ORANGE_OAK_SAPLING.get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.ORANGE_OAK_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.ORANGE_OAK_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.ORANGE_OAK_TREE3)), 1)
                            .build()),
                    new GrowingPatternEntry(List.of(
                            "xx",
                            "xx"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.ORANGE_OAK_TREE_LARGE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.ORANGE_OAK_TREE_LARGE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.ORANGE_OAK_TREE_LARGE3)), 1)
                            .build())
            ));
            map.put(BYGBlocks.ORANGE_SPRUCE_SAPLING.get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_ORANGE_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_ORANGE_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_ORANGE_TREE3)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_ORANGE_TREE4)), 1)
                            .build()),
                    new GrowingPatternEntry(List.of(
                            "xx",
                            "xx"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_ORANGE_TREE_MEDIUM1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_ORANGE_TREE_MEDIUM2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_ORANGE_TREE_MEDIUM3)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_ORANGE_TREE_MEDIUM4)), 1)
                            .build()),
                    new GrowingPatternEntry(List.of(
                            "xxx",
                            "xxx",
                            "xxx"), SimpleWeightedRandomList.single(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_ORANGE_TREE_LARGE1))))
            ));
            map.put(BYGBlocks.ORCHARD_SAPLING.get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.ORCHARD_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.ORCHARD_TREE3)), 1)
                            .build()),
                    new GrowingPatternEntry(List.of(
                            "xxx",
                            "xxx",
                            "xxx"), SimpleWeightedRandomList.single(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.ORCHARD_TREE2))))
            ));
            map.put(BYGBlocks.PALO_VERDE_SAPLING.get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.PALO_VERDE_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.PALO_VERDE_TREE2)), 1)
                            .build())
            ));
            map.put(BYGBlocks.PALO_VERDE_SAPLING.get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.PALO_VERDE_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.PALO_VERDE_TREE2)), 1)
                            .build())
            ));
            map.put(BYGWoodTypes.PINE.growerItem().get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.PINE_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.PINE_TREE2)), 1)
                            .build())
            ));
            map.put(BYGBlocks.PINK_CHERRY_SAPLING.get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>single(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.CHERRY_PINK_TREE2)))),
                    new GrowingPatternEntry(List.of(
                            "xx",
                            "xx"), SimpleWeightedRandomList.<FeatureSpawner>single(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.CHERRY_PINK_TREE1))))
            ));
            map.put(BYGWoodTypes.RAINBOW_EUCALYPTUS.growerItem().get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>single(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.RAINBOW_EUCALYPTUS_TREE1)))),
                    new GrowingPatternEntry(List.of(
                            " x ",
                            "xxx",
                            " x "), SimpleWeightedRandomList.<FeatureSpawner>single(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.RAINBOW_EUCALYPTUS_TREE1))))
            ));
            map.put(BYGBlocks.RED_BIRCH_SAPLING.get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.BIRCH_RED_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.BIRCH_RED_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.BIRCH_RED_TREE3)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.BIRCH_RED_TREE4)), 1)
                            .build())
            ));
            map.put(BYGBlocks.RED_MAPLE_SAPLING.get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.RED_MAPLE_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.RED_MAPLE_TREE2)), 1)
                            .build())
            ));
            map.put(BYGBlocks.RED_OAK_SAPLING.get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.RED_OAK_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.RED_OAK_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.RED_OAK_TREE3)), 1)
                            .build()),
                    new GrowingPatternEntry(List.of(
                            "xx",
                            "xx"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.RED_OAK_TREE_LARGE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.RED_OAK_TREE_LARGE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.RED_OAK_TREE_LARGE3)), 1)
                            .build())
            ));
            map.put(BYGBlocks.RED_SPRUCE_SAPLING.get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_RED_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_RED_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_RED_TREE3)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_RED_TREE4)), 1)
                            .build()),
                    new GrowingPatternEntry(List.of(
                            "xx",
                            "xx"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_RED_TREE_MEDIUM1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_RED_TREE_MEDIUM2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_RED_TREE_MEDIUM3)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_RED_TREE_MEDIUM4)), 1)
                            .build()),
                    new GrowingPatternEntry(List.of(
                            "xxx",
                            "xxx",
                            "xxx"), SimpleWeightedRandomList.single(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_RED_TREE_LARGE1))))
            ));
            map.put(BYGWoodTypes.REDWOOD.growerItem().get(), List.of(
                    new GrowingPatternEntry(List.of(
                            " x ",
                            "xxx",
                            " x "), SimpleWeightedRandomList.single(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.REDWOOD_TREE3)))),
                    new GrowingPatternEntry(List.of(
                            "xxx",
                            "xxx",
                            "xxx"
                    ), SimpleWeightedRandomList.single(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.REDWOOD_TREE1)))),
                    new GrowingPatternEntry(List.of(
                            "  x  ",
                            " xxx ",
                            "xxxxx",
                            " xxx ",
                            "  x  "
                    ), SimpleWeightedRandomList.single(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.REDWOOD_TREE2))))
            ));
            map.put(BYGBlocks.SILVER_MAPLE_SAPLING.get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SILVER_MAPLE_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SILVER_MAPLE_TREE2)), 1)
                            .build())
            ));
            map.put(BYGWoodTypes.SKYRIS.growerItem().get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SKYRIS_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SKYRIS_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SKYRIS_TREE3)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SKYRIS_TREE4)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SKYRIS_TREE5)), 1)
                            .build())
            ));
            map.put(BYGBlocks.WHITE_CHERRY_SAPLING.get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>single(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.CHERRY_WHITE_TREE2)))),
                    new GrowingPatternEntry(List.of(
                            "xx",
                            "xx"), SimpleWeightedRandomList.<FeatureSpawner>single(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.CHERRY_WHITE_TREE1))))
            ));
            map.put(BYGWoodTypes.WILLOW.growerItem().get(), List.of(
                    new GrowingPatternEntry(List.of(
                            "  x  ",
                            "     ",
                            "x   x",
                            "     ",
                            "  x  "), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.WILLOW_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.WILLOW_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.WILLOW_TREE3)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.WILLOW_TREE4)), 1)
                            .build())
            ));
            map.put(BYGWoodTypes.WITCH_HAZEL.growerItem().get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.WITCH_HAZEL1)), 2)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.WITCH_HAZEL2)), 2)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.WITCH_HAZEL3)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.WITCH_HAZEL4)), 3)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.WITCH_HAZEL5)), 1)
                            .build())
            ));
            map.put(BYGBlocks.YELLOW_BIRCH_SAPLING.get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.BIRCH_YELLOW_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.BIRCH_YELLOW_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.BIRCH_YELLOW_TREE3)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.BIRCH_YELLOW_TREE4)), 1)
                            .build())
            ));
            map.put(BYGBlocks.YELLOW_SPRUCE_SAPLING.get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_YELLOW_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_YELLOW_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_YELLOW_TREE3)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_YELLOW_TREE4)), 1)
                            .build()),
                    new GrowingPatternEntry(List.of(
                            "xx",
                            "xx"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_YELLOW_TREE_MEDIUM1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_YELLOW_TREE_MEDIUM2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_YELLOW_TREE_MEDIUM3)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_YELLOW_TREE_MEDIUM4)), 1)
                            .build()),
                    new GrowingPatternEntry(List.of(
                            "xxx",
                            "xxx",
                            "xxx"), SimpleWeightedRandomList.single(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.SPRUCE_YELLOW_TREE_LARGE1))))
            ));
            map.put(BYGWoodTypes.ZELKOVA.growerItem().get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.ZELKOVA_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.ZELKOVA_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.ZELKOVA_TREE3)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.ZELKOVA_BROWN_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.ZELKOVA_BROWN_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.ZELKOVA_BROWN_TREE3)), 1)
                            .build())
            ));
            map.put(BYGWoodTypes.PALM.growerItem().get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.PALM_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.PALM_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.PALM_TREE3)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldTreeFeatures.PALM_TREE4)), 1)
                            .build())
            ));
            map.put(BYGWoodTypes.LAMENT.growerItem().get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGNetherVegetationFeatures.LAMENT_TWISTY_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGNetherVegetationFeatures.LAMENT_TWISTY_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGNetherVegetationFeatures.LAMENT_TWISTY_TREE3)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGNetherVegetationFeatures.LAMENT_WEEPING_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGNetherVegetationFeatures.LAMENT_WEEPING_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGNetherVegetationFeatures.LAMENT_WEEPING_TREE3)), 1)
                            .build())
            ));
            map.put(BYGBlocks.WITHERING_OAK_SAPLING.get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGNetherVegetationFeatures.WITHERING_OAK_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGNetherVegetationFeatures.WITHERING_OAK_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGNetherVegetationFeatures.WITHERING_OAK_TREE3)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGNetherVegetationFeatures.WITHERING_OAK_TREE4)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGNetherVegetationFeatures.WITHERING_OAK_TREE5)), 1)
                            .build())
            ));
            map.put(BYGWoodTypes.ETHER.growerItem().get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.ETHER_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.ETHER_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.ETHER_TREE3)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.ETHER_TREE4)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.ETHER_TREE5)), 1)
                            .build())
            ));
            map.put(BYGWoodTypes.NIGHTSHADE.growerItem().get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.NIGHTSHADE_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.NIGHTSHADE_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.NIGHTSHADE_TREE3)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.NIGHTSHADE_TREE4)), 1)
                            .build())
            ));
            map.put(BYGWoodTypes.EMBUR.growerItem().get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGNetherVegetationFeatures.EMBUR_MUSHROOM)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGNetherVegetationFeatures.EMBUR_MUSHROOM2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGNetherVegetationFeatures.EMBUR_MUSHROOM3)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGNetherVegetationFeatures.EMBUR_MUSHROOM4)), 1)
                            .build())
            ));

            map.put(BYGBlocks.WEEPING_MILKCAP.get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldVegetationFeatures.WEEPING_MILKCAP_HUGE)), 1)
                            .build())
            ));

            map.put(BYGBlocks.WOOD_BLEWIT.get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldVegetationFeatures.WOOD_BLEWIT_HUGE)), 1)
                            .build())
            ));

            map.put(BYGBlocks.GREEN_MUSHROOM.get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGOverworldVegetationFeatures.GREEN_MUSHROOM_HUGE)), 1)
                            .build())
            ));

            map.put(BYGWoodTypes.SYTHIAN.growerItem().get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGNetherVegetationFeatures.SYTHIAN_FUNGUS_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGNetherVegetationFeatures.SYTHIAN_FUNGUS_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGNetherVegetationFeatures.SYTHIAN_FUNGUS_TREE3)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGNetherVegetationFeatures.SYTHIAN_FUNGUS_TREE4)), 1)
                            .build())
            ));

            map.put(BYGBlocks.SOUL_SHROOM.get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGNetherVegetationFeatures.SOUL_SHROOM_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGNetherVegetationFeatures.SOUL_SHROOM_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGNetherVegetationFeatures.SOUL_SHROOM_TREE3)), 1)
                            .build())
            ));

            map.put(BYGBlocks.DEATH_CAP.get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGNetherVegetationFeatures.DEATH_CAP_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGNetherVegetationFeatures.DEATH_CAP_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGNetherVegetationFeatures.DEATH_CAP_TREE3)), 1)
                            .build())
            ));

            map.put(BYGWoodTypes.BULBIS.growerItem().get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.BULBIS_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.BULBIS_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.BULBIS_TREE3)), 1)
                            .build()),
                    new GrowingPatternEntry(List.of(
                            " x ",
                            "xxx",
                            " x "), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.BULBIS_TREE4)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.BULBIS_TREE5)), 1)
                            .build()),
                    new GrowingPatternEntry(List.of(
                            "xxx",
                            "xxx",
                            "xxx"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.BULBIS_TREE6), new Vec3i(-1, 0, -1)), 1)
                            .build()),
                    new GrowingPatternEntry(List.of(
                            "  x  ",
                            " xxx ",
                            "xxxxx",
                            " xxx ",
                            "  x  "), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.BULBIS_TREE7)), 1)
                            .build())
            ));

            map.put(BYGBlocks.PURPLE_BULBIS_ODDITY.get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.PURPLE_BULBIS_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.PURPLE_BULBIS_TREE2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.PURPLE_BULBIS_TREE3)), 1)
                            .build()),
                    new GrowingPatternEntry(List.of(
                            " x ",
                            "xxx",
                            " x "), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.PURPLE_BULBIS_TREE4)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.PURPLE_BULBIS_TREE5)), 1)
                            .build()),
                    new GrowingPatternEntry(List.of(
                            "xxx",
                            "xxx",
                            "xxx"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.PURPLE_BULBIS_TREE6), new Vec3i(-1, 0, -1)), 1)
                            .build()),
                    new GrowingPatternEntry(List.of(
                            "  x  ",
                            " xxx ",
                            "xxxxx",
                            " xxx ",
                            "  x  "), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.BULBIS_TREE7)), 1)
                            .build())
            ));

            map.put(BYGBlocks.SHULKREN_FUNGUS.get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.SHULKREN_TREE1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.SHULKREN_TREE2)), 1)
                            .build())
            ));
            map.put(BYGWoodTypes.IMPARIUS.growerItem().get(), List.of(
                    new GrowingPatternEntry(List.of(
                            " x ",
                            "x x",
                            " x "
                    ), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.IMPARIUS_MUSHROOM1), Vec3i.ZERO.offset(0, 0, 1)), 1)
                            .build()),
                    new GrowingPatternEntry(List.of(
                            "  x  ",
                            "x   x",
                            "  x  "
                    ), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.IMPARIUS_MUSHROOM2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.IMPARIUS_MUSHROOM3)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.IMPARIUS_MUSHROOM4)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.IMPARIUS_MUSHROOM5)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.IMPARIUS_MUSHROOM6)), 1)
                            .build())
            ));
            map.put(BYGBlocks.FUNGAL_IMPARIUS.get(), List.of(
                    new GrowingPatternEntry(List.of("x"), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.FUNGAL_IMPARIUS1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.FUNGAL_IMPARIUS2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.FUNGAL_IMPARIUS3)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.FUNGAL_IMPARIUS4)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.FUNGAL_IMPARIUS5)), 1)
                            .build()),
                    new GrowingPatternEntry(List.of(
                            " x ",
                            "x x",
                            " x "), SimpleWeightedRandomList.<FeatureSpawner>builder()
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.FUNGAL_IMPARIUS1)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.FUNGAL_IMPARIUS2)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.FUNGAL_IMPARIUS3)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.FUNGAL_IMPARIUS4)), 1)
                            .add(new FeatureSpawner(Holder.hackyErase(BYGEndVegetationFeatures.FUNGAL_IMPARIUS5)), 1)
                            .build())
            ));
            return map;
        }));
    });

    public static GrowingPatterns INSTANCE = null;

    public static final Codec<GrowingPatterns> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    CommentedCodec.optionalOf(Codec.BOOL, "logGrowth", "Print growable related actions in the latest.log?", false).forGetter(saplingPatterns -> saplingPatterns.logGrowth),
                    CommentedCodec.of(Codec.unboundedMap(CodecUtil.BLOCK_CODEC, GrowingPatternEntry.CODEC.listOf()), "sapling_patterns", "Sapling patterns for a given block.\nNot all blocks work, only blocks using \"FeatureGrowerFromBlockPattern\"").forGetter(saplingPatterns -> saplingPatterns.patternsForBlock.get())
            ).apply(builder, (Boolean logGrowth1, Map<Block, List<GrowingPatternEntry>> patternsForBlock1) -> new GrowingPatterns(logGrowth1, Suppliers.memoize(() -> patternsForBlock1)))
    );

    private static final Path PATH = ModPlatform.INSTANCE.configPath().resolve("growing-patterns.json5");
    private static final Path OLD_PATH = ModPlatform.INSTANCE.configPath().resolve(BYG.MOD_ID + "-sapling-patterns.json");


    public static GrowingPatterns getConfig() {
        return getConfig(false, false);
    }

    public static GrowingPatterns getConfig(boolean serialize, boolean recreate) {
        if (INSTANCE == null || serialize || recreate) {
            INSTANCE = readConfig(recreate);
        }
        return INSTANCE;
    }

    public static void setInstance(GrowingPatterns instance) {
        GrowingPatterns.INSTANCE = instance;
    }

    private static GrowingPatterns readConfig(boolean recreate) {

        GrowingPatterns from = DEFAULT.get();
        if (OLD_PATH.toFile().exists()) {
            try {
                try {
                    from = JanksonUtil.readConfig(OLD_PATH, CODEC, JanksonJsonOps.INSTANCE);
                    Files.delete(OLD_PATH);
                } catch (SyntaxError | IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!PATH.toFile().exists() || recreate) {
            JanksonUtil.createConfig(PATH, CODEC, JanksonUtil.HEADER_CLOSED, new HashMap<>(), JanksonJsonOps.INSTANCE, from);
        }
        BYG.logInfo(String.format("\"%s\" was read.", PATH.toString()));

        try {
            from = JanksonUtil.readConfig(PATH, CODEC, JanksonJsonOps.INSTANCE);
        } catch (Exception e) {
            e.printStackTrace();
            BYGConfigHandler.CONFIG_EXCEPTIONS.add(e);
        }
        return from;
    }

    public record GrowingPatternEntry(List<String> pattern, SimpleWeightedRandomList<FeatureSpawner> spawners) {
        public static final Codec<GrowingPatternEntry> CODEC = RecordCodecBuilder.create(builder ->
                builder.group(
                        CommentedCodec.of(Codec.STRING.listOf(), "pattern", "Pattern required for this block to spawn a configured feature.\n\"x\" represents a sapling position.\" \" represents air.\nMax size is 5x5.").forGetter(GrowingPatternEntry::pattern),
                        CommentedCodec.of(SimpleWeightedRandomList.wrappedCodec(FeatureSpawner.CODEC), "spawners", "Configured Feature spawner.").forGetter(GrowingPatternEntry::spawners)
                ).apply(builder, GrowingPatternEntry::new)
        );
    }


    public record FeatureSpawner(ResourceKey<ConfiguredFeature<?, ?>> spawnerID, Vec3i spawnOffset) {
        public static final Codec<FeatureSpawner> CODEC = RecordCodecBuilder.create(builder ->
                builder.group(
                        CommentedCodec.of(ResourceKey.codec(Registry.CONFIGURED_FEATURE_REGISTRY), "featureID", "Registry ID of the configured feature.").forGetter(FeatureSpawner::spawnerID),
                        CommentedCodec.optionalOf(Vec3i.CODEC, "spawnOffset", "Some features don't spawn centered, this lets us offset the feature to center it.", Vec3i.ZERO).forGetter(FeatureSpawner::spawnOffset)
                ).apply(builder, FeatureSpawner::new)
        );

        public FeatureSpawner(Holder<ConfiguredFeature<?, ?>> spawnerID) {
            this(spawnerID, Vec3i.ZERO);
        }

        public FeatureSpawner(Holder<ConfiguredFeature<?, ?>> spawnerID, Vec3i spawnOffset) {
            this(spawnerID.unwrapKey().orElseThrow(), spawnOffset);
        }

        public FeatureSpawner(ResourceKey<ConfiguredFeature<?, ?>> spawnerID) {
            this(spawnerID, Vec3i.ZERO);
        }
    }
}