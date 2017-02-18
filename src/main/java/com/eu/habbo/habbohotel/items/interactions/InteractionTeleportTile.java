package com.eu.habbo.habbohotel.items.interactions;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.gameclients.GameClient;
import com.eu.habbo.habbohotel.items.Item;
import com.eu.habbo.habbohotel.rooms.Room;
import com.eu.habbo.habbohotel.rooms.RoomUnit;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.rooms.users.RoomUserStatusComposer;
import com.eu.habbo.threading.runnables.teleport.TeleportActionOne;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Murdock on 14/11/2016.
 */
public class InteractionTeleportTile extends InteractionTeleport
{
    public InteractionTeleportTile(ResultSet set, Item baseItem) throws SQLException
    {
        super(set, baseItem);
    }

    public InteractionTeleportTile(int id, int userId, Item item, String extradata, int limitedStack, int limitedSells)
    {
        super(id, userId, item, extradata, limitedStack, limitedSells);
    }

    @Override
    public void onWalkOn(RoomUnit roomUnit, Room room, Object[] objects) throws Exception
    {
        super.onWalkOn(roomUnit, room, objects);

        Habbo habbo = room.getHabbo(roomUnit);

        if (habbo != null && roomUnit.getGoal().x == this.getX() && roomUnit.getGoal().y == this.getY() && canUseTeleport(habbo.getClient(), room))
        {
            this.setExtradata("1");
            room.updateItem(this);
            roomUnit.getStatus().remove("mv");
            room.sendComposer(new RoomUserStatusComposer(roomUnit).compose());
            Emulator.getThreading().run(new TeleportActionOne(this, room, habbo.getClient()), 0);
            roomUnit.isTeleporting = true;
        }
    }

    @Override
    public void onClick(GameClient client, Room room, Object[] objects) throws Exception
    {
        client.getHabbo().getRoomUnit().setGoalLocation(room.getLayout().getTile(this.getX(), this.getY()));
    }

    private boolean canUseTeleport(GameClient client, Room room)
    {
        if(client.getHabbo().getRoomUnit().isTeleporting)
            return false;

        if(!room.getHabbosAt(getX(), getY()).isEmpty())
            return false;

        if(!this.getExtradata().equals("0"))
            return false;

        return true;
    }
}
