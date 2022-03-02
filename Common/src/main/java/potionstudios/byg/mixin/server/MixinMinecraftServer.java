package potionstudios.byg.mixin.server;


import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.datafixers.DataFixer;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerResources;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.WorldData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import potionstudios.byg.BYG;
import potionstudios.byg.common.world.biome.BYGBiomes;
import potionstudios.byg.common.world.surfacerules.BYGSurfaceRules;
import potionstudios.byg.mixin.access.NoiseBasedChunkGeneratorAccess;
import potionstudios.byg.mixin.access.NoiseGeneratorSettingsAccess;

import java.net.Proxy;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("deprecation")
@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

    @Shadow
    @Final
    protected RegistryAccess.RegistryHolder registryHolder;

    @Shadow
    @Final
    protected WorldData worldData;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void addBYGFeatures(Thread thread, RegistryAccess.RegistryHolder impl, LevelStorageSource.LevelStorageAccess session, WorldData saveProperties, PackRepository resourcePackManager, Proxy proxy, DataFixer dataFixer, ServerResources serverResourceManager, MinecraftSessionService minecraftSessionService, GameProfileRepository gameProfileRepository, GameProfileCache userCache, ChunkProgressListenerFactory worldGenerationProgressListenerFactory, CallbackInfo ci) {
        Optional<? extends Registry<Biome>> biomeRegistry = this.registryHolder.registry(Registry.BIOME_REGISTRY);
        if (biomeRegistry.isPresent()) {
            for (Map.Entry<ResourceKey<Biome>, Biome> biomeEntry : biomeRegistry.get().entrySet()) {
                BYGBiomes.addBYGFeaturesToBiomes(biomeEntry.getValue(), biomeEntry.getKey());
            }
        }
    }

    @Inject(method = "createLevels", at = @At("RETURN"))
    private void hackyAddNetherAndEndSurfaceRules(ChunkProgressListener $$0, CallbackInfo ci) {
        LevelStem levelStem = this.worldData.worldGenSettings().dimensions().get(LevelStem.NETHER);

        ChunkGenerator generator = levelStem.generator();
        if (generator != null && generator instanceof NoiseBasedChunkGenerator) {
            Object noiseGeneratorSettings = ((NoiseBasedChunkGeneratorAccess) generator).getSettings().get();
            ((NoiseGeneratorSettingsAccess) noiseGeneratorSettings).setSurfaceRule(SurfaceRules.sequence(BYGSurfaceRules.NETHER_SURFACE_RULES, BYG.MODLOADER_DATA.netherRuleSource().get(), ((NoiseGeneratorSettings) noiseGeneratorSettings).surfaceRule()));
        }

        LevelStem endLevelStem = this.worldData.worldGenSettings().dimensions().get(LevelStem.END);

        ChunkGenerator endGenerator = endLevelStem.generator();
        if (generator != null && generator instanceof NoiseBasedChunkGenerator) {
            Object noiseGeneratorSettings = ((NoiseBasedChunkGeneratorAccess) endGenerator).getSettings().get();
            ((NoiseGeneratorSettingsAccess) noiseGeneratorSettings).setSurfaceRule(SurfaceRules.sequence(BYGSurfaceRules.END_SURFACE_RULES, ((NoiseGeneratorSettings) noiseGeneratorSettings).surfaceRule()));
        }
    }

//    @ModifyConstant(method = "prepareLevels", constant = @Constant(intValue = 11, ordinal = 0))
//    private static int fastSpawn(int constant) {
//        return 0;
//    }
//
//    @ModifyConstant(method = "prepareLevels", constant = @Constant(intValue = 441, ordinal = 0))
//    private static int fastSpawn2(int constant) {
//        return 0;
//    }
}
