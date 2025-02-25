package potionstudios.byg.datagen.providers.tag;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.PoiTypeTagsProvider;
import net.minecraft.tags.PoiTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import potionstudios.byg.BYG;
import potionstudios.byg.common.entity.ai.village.poi.BYGPOITypeTags;
import potionstudios.byg.common.entity.ai.village.poi.BYGPoiTypes;

public class BYGPoiTypeTagsProvider extends PoiTypeTagsProvider {
    public BYGPoiTypeTagsProvider(DataGenerator gen, ExistingFileHelper existingFileHelper) {
        super(gen, BYG.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(BYGPOITypeTags.ACQUIRABLE_JOB_SITE).add(BYGPoiTypes.FORAGER.getResourceKey());
        this.tag(PoiTypeTags.ACQUIRABLE_JOB_SITE).addTag(BYGPOITypeTags.ACQUIRABLE_JOB_SITE);
    }
}
