package com.dynview.mixin;

import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Comparator;

@Mixin(ChunkTicketType.class)
public class ChunkTypeMixin
{
    @Shadow
    @Final
    @Mutable
    public static ChunkTicketType<ChunkPos> UNKNOWN = ChunkTicketType.create("unknown", Comparator.comparingLong(ChunkPos::toLong), 1201);
}
