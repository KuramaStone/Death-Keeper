package dev.lopyluna.death_keeper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NullableProblems")
public class DeathSnapshotData extends SavedData {
    public static final String NAME = "death_snapshots";
    public final List<DeathSnapshot> snapshots = new ArrayList<>();

    public static DeathSnapshotData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(new Factory<>(DeathSnapshotData::new, DeathSnapshotData::load), NAME);
    }

    public static List<DeathSnapshot> getSnapshots(ServerLevel level) {
        var data = DeathSnapshotData.get(level);
        return data.snapshots;
    }

    public static List<DeathSnapshot> getSnapshots(ServerLevel level, BlockPos pos) {
        var snaps = new ArrayList<DeathSnapshot>();
        var dim = level.dimension().location();
        for (var snap : getSnapshots(level)) {
            if (!snap.dim.equals(dim)) continue;
            if (!snap.pos.equals(pos)) continue;
            snaps.add(snap);
        }
        return snaps;
    }

    public static void add(ServerLevel level, DeathSnapshot snap) {
        var data = DeathSnapshotData.get(level);
        data.snapshots.add(snap);
        data.setDirty();
    }

    public static void remove(ServerLevel level, DeathSnapshot snap) {
        var data = DeathSnapshotData.get(level);
        data.snapshots.remove(snap);
        data.setDirty();
    }

    public static DeathSnapshotData load(CompoundTag nbt, HolderLookup.Provider provider) {
        var data = new DeathSnapshotData();
        var list = nbt.getList("Snapshots", 10);
        for (int i = 0; list.size() > i; i++) {
            var snapNbt = list.getCompound(i);
            data.snapshots.add(DeathSnapshot.readSnapshot(snapNbt, provider));
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag nbt, HolderLookup.Provider provider) {
        ListTag list = new ListTag();
        for (var snap : snapshots) list.add(DeathSnapshot.writeSnapshot(snap, provider));
        nbt.put("Snapshots", list);
        return nbt;
    }
}
