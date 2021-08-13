package dev.compactmods.machines.client.machine;

import dev.compactmods.machines.block.tiles.CompactMachineTile;
import dev.compactmods.machines.network.MachinePlayersChangedPacket;
import dev.compactmods.machines.teleportation.DimensionalPosition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;

import java.util.UUID;

public class MachinePlayerEventHandler {

    public static void handlePlayerMachineChanged(UUID playerID,
                                                  MachinePlayersChangedPacket.EnumPlayerChangeType changeType,
                                                  DimensionalPosition pos) {
        ClientWorld w = Minecraft.getInstance().level;
        if(w == null)
            return;

        // Early exit if machine in another dimension
        if (w.dimension() != pos.getDimension())
            return;

        // Early exit if machine isn't in range of the player - the block sync will handle those
        if(!w.isLoaded(pos.getBlockPosition()))
            return;

        CompactMachineTile tile = (CompactMachineTile) w.getBlockEntity(pos.getBlockPosition());
        if (tile == null)
            return;

        switch (changeType) {
            case EXITED:
                tile.handlePlayerLeft(playerID);
                break;

            case ENTERED:
                tile.handlePlayerEntered(playerID);
                break;
        }
    }
}