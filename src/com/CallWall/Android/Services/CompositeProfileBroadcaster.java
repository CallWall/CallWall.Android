package com.CallWall.Android.Services;

import com.CallWall.Android.Entities.Profile;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Lee
 * Date: 24/12/12
 * Time: 17:32
 * To change this template use File | Settings | File Templates.
 */
public final class CompositeProfileBroadcaster implements ProfileBroadcaster {

    private final ArrayList<ProfileBroadcaster> implementations = new ArrayList<ProfileBroadcaster>();

    public CompositeProfileBroadcaster()
    { }

    public CompositeProfileBroadcaster(Iterable<ProfileBroadcaster> implementations)
    {
        for(ProfileBroadcaster broadcaster : implementations)
        {
            this.implementations.add(broadcaster);
        }
    }

    @Override
    public void Broadcast(Profile profile) {
        for(ProfileBroadcaster broadcaster : this.implementations)
        {
            broadcaster.Broadcast(profile);
        }
    }

    public void Add(ProfileBroadcaster implementation)
    {
        this.implementations.add(implementation);
    }
}
