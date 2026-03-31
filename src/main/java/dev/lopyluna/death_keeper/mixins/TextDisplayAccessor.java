package dev.lopyluna.death_keeper.mixins;

import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Display.TextDisplay.class)
public interface TextDisplayAccessor {
    @Accessor("DATA_TEXT_ID")
    static EntityDataAccessor<Component> getDataText() {
        throw new AssertionError();
    }
    @Accessor("DATA_STYLE_FLAGS_ID")
    static EntityDataAccessor<Byte> getDataStyle() {
        throw new AssertionError();
    }
}
