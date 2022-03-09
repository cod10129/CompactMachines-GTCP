package dev.compactmods.machines.room.capability;

import dev.compactmods.machines.api.room.IMachineRoom;
import dev.compactmods.machines.api.room.IRoomCapabilities;
import dev.compactmods.machines.core.Capabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RoomChunkDataProvider implements ICapabilitySerializable<CompoundTag> {
    private final RoomChunkData room;
    private final RoomChunkCapabilities roomCaps;

    public RoomChunkDataProvider(LevelChunk chunk) {
        this.room = new RoomChunkData(chunk);
        this.roomCaps = new RoomChunkCapabilities(chunk);
    }

    /**
     * Retrieves the Optional handler for the capability requested on the specific side.
     * The return value <strong>CAN</strong> be the same for multiple faces.
     * Modders are encouraged to cache this value, using the listener capabilities of the Optional to
     * be notified if the requested capability get lost.
     *
     * @param cap  The capability to check
     * @param side The Side to check from,
     *             <strong>CAN BE NULL</strong>. Null is defined to represent 'internal' or 'self'
     * @return The requested an optional holding the requested capability.
     */
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == Capabilities.ROOM)
            return LazyOptional.of(this::room).cast();

        if(cap == Capabilities.ROOM_CAPS)
            return LazyOptional.of(this::caps).cast();

        return LazyOptional.empty();
    }

    @Nonnull
    private IRoomCapabilities caps() {
        return roomCaps;
    }

    @Nonnull
    private IMachineRoom room() {
        return this.room;
    }

    @Override
    public CompoundTag serializeNBT() {
        var tag = new CompoundTag();

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
    }
}