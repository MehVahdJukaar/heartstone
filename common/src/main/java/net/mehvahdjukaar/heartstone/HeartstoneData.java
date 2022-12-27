package net.mehvahdjukaar.heartstone;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;


public class HeartstoneData extends SavedData {

    public static final String DATA_NAME = "heartstone_data";
    public final long currentIndex;

    public HeartstoneData(long nextIndex) {
        this.currentIndex = nextIndex;
        this.setDirty();
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        nbt.putLong("index", this.currentIndex);
        return nbt;
    }

    public HeartstoneData(CompoundTag tag) {
        this.currentIndex = tag.getLong("index");
    }

    public static HeartstoneData get(ServerLevel world) {
        return world.getServer().overworld().getDataStorage().computeIfAbsent(HeartstoneData::new,
                () -> new HeartstoneData(0L),
                DATA_NAME);
    }

    public static long getNewId(ServerLevel level) {
        long i = get(level).currentIndex + 1L;
        level.getServer().overworld().getDataStorage().set(DATA_NAME, new HeartstoneData(i));
        return i;
    }

}



