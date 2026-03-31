package dev.lopyluna.death_keeper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeathSnapshot {
    public ResourceLocation dim;
    public int lifespan = Config.maxGraveLifespan;
    public BlockPos pos;
    public Direction dir;
    public int exp;
    public List<ItemStack> inv;
    @Nullable public UUID uuidText;
    public UUID uuid;
    public EntityType<?> entityType;

    public static CompoundTag writeSnapshot(DeathSnapshot snap, HolderLookup.Provider provider) {
        var nbt = new CompoundTag();
        nbt.putString("dim", snap.dim.toString());
        nbt.putUUID("uuid", snap.uuid);
        if (snap.uuidText != null) nbt.putUUID("uuidText", snap.uuidText);
        nbt.putString("type", EntityType.getKey(snap.entityType).toString());
        nbt.putInt("lifespan", snap.lifespan);
        nbt.putInt("x", snap.pos.getX());
        nbt.putInt("y", snap.pos.getY());
        nbt.putInt("z", snap.pos.getZ());
        nbt.putInt("dir", snap.dir.ordinal());
        nbt.putInt("exp", snap.exp);
        var invList = new ListTag();
        for (var stack : snap.inv) invList.add(stack.save(provider, new CompoundTag()));
        nbt.put("inv", invList);
        return nbt;
    }
    public static DeathSnapshot readSnapshot(CompoundTag nbt, HolderLookup.Provider provider) {
        var snap = new DeathSnapshot();
        snap.dim = ResourceLocation.parse(nbt.getString("dim"));
        snap.uuid = nbt.getUUID("uuid");
        if (nbt.contains("uuidText")) snap.uuidText = nbt.getUUID("uuidText");
        snap.entityType = EntityType.byString(nbt.getString("type")).orElse(EntityType.PIG);
        snap.lifespan = nbt.getInt("lifespan");
        snap.pos = new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
        snap.dir = Direction.values()[nbt.getInt("dir")];
        snap.exp = nbt.getInt("exp");
        var invList = nbt.getList("inv", 10);
        snap.inv = new ArrayList<>();
        for (int i = 0; invList.size() > i; i++) snap.inv.add(ItemStack.parse(provider, invList.getCompound(i)).orElse(ItemStack.EMPTY));
        return snap;
    }
}
