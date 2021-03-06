package com.eu.habbo.habbohotel.guilds;

public enum GuildState
{
    OPEN(0),
    LOCKED(1),
    CLOSED(2);

    public final int state;

    GuildState(int state)
    {
        this.state = state;
    }

    public static GuildState valueOf(int state)
    {
        try
        {
            return values()[state];
        }
        catch (Exception e)
        {
            return OPEN;
        }
    }
}
