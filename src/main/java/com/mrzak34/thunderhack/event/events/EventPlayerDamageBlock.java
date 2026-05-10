package com.mrzak34.thunderhack.event.events;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class EventPlayerDamageBlock extends Event
{
    private BlockPos BlockPos;
    private EnumFacing Direction;


    public EventPlayerDamageBlock(BlockPos posBlock, EnumFacing directionFacing)
    {
        BlockPos = posBlock;
        setDirection(directionFacing);
    }

    public BlockPos getPos()
    {
        return BlockPos;
    }

    public EnumFacing getDirection()
    {
        return Direction;
    }

    public void setDirection(EnumFacing direction)
    {
        Direction = direction;
    }


    public Object getPacket() {
        return null;
    }
}

