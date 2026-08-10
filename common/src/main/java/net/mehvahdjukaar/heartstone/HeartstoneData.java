package net.mehvahdjukaar.heartstone;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mehvahdjukaar.moonlight.api.misc.WorldSavedData;
import net.mehvahdjukaar.moonlight.api.misc.WorldSavedDataType;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class HeartstoneData extends WorldSavedData {

    public static final Codec<HeartstoneData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.LONG.fieldOf("index").forGetter(d -> d.lastId)
    ).apply(i, HeartstoneData::new));

    private long lastId;

    private HeartstoneData(long lastId) {
        this.lastId = lastId;
    }

    @Override
    public WorldSavedDataType<HeartstoneData> getType() {
        return Heartstone.HEARTSTONE_DATA;
    }

    public static long getNewId(ServerLevel level) {
        HeartstoneData data = Heartstone.HEARTSTONE_DATA.getData(level);
        data.lastId++;
        data.setDirty();
        return data.lastId;
    }

    /**
     * Ids handed out before the switch to moonlight's saved data live in their own file. Carrying that
     * counter over matters: restarting from zero would hand out ids that existing heartstones already use,
     * silently pairing up unrelated players.
     */
    static HeartstoneData create(ServerLevel overworld) {
        LegacyData legacy = overworld.getDataStorage().get(LegacyData.FACTORY, LegacyData.NAME);
        return new HeartstoneData(legacy == null ? 0 : legacy.lastId);
    }

    private static class LegacyData extends SavedData {

        private static final String NAME = "heartstone_data";
        private static final Factory<LegacyData> FACTORY = new Factory<>(
                () -> new LegacyData(0),
                (tag, registries) -> new LegacyData(tag.getLong("index")),
                null);

        private final long lastId;

        private LegacyData(long lastId) {
            this.lastId = lastId;
        }

        @Override
        public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
            tag.putLong("index", this.lastId);
            return tag;
        }
    }
}
