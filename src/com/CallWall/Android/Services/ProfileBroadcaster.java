package com.CallWall.Android.Services;

/**
 * Created with IntelliJ IDEA.
 * User: Lee
 * Date: 09/11/12
 * Time: 08:30
 * To change this template use File | Settings | File Templates.
 */

import com.CallWall.Android.Entities.Profile;

public interface ProfileBroadcaster
{
    void Broadcast(Profile profile);
}