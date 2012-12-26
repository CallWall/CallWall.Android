package com.CallWall.Android.Services;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Lee
 * Date: 24/12/12
 * Time: 17:32
 * To change this template use File | Settings | File Templates.
 */
public final class CompositeIdentityBroadcaster implements IdentityBroadcaster {

    private final ArrayList<IdentityBroadcaster> implementations = new ArrayList<IdentityBroadcaster>();

    public CompositeIdentityBroadcaster()
    { }

    public CompositeIdentityBroadcaster(Iterable<IdentityBroadcaster> implementations)
    {
        for(IdentityBroadcaster broadcaster : implementations)
        {
            this.implementations.add(broadcaster);
        }
    }

    @Override
    public void Broadcast(String identity) {
        for(IdentityBroadcaster broadcaster : this.implementations)
        {
            broadcaster.Broadcast(identity);
        }
    }

    public void Add(IdentityBroadcaster implementation)
    {
        this.implementations.add(implementation);
    }
}
