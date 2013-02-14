package com.CallWall.Android.Entities;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Lee
 * Date: 12/02/13
 * Time: 09:16
 * To change this template use File | Settings | File Templates.
 */
public final class Profile {
    private final ArrayList<PersonalIdentifier> _ids = new ArrayList<PersonalIdentifier>();

    public Profile(Iterable<PersonalIdentifier> personalIdentifiers)
    {
        for(PersonalIdentifier id : personalIdentifiers)
        {
            this._ids.add(id);
        }
    }

    public Iterable<PersonalIdentifier> get_PersonalIdentifiers()
    {
        return this._ids;
    }


    public String ToString()
    {
        String result = "Profile{";
        Boolean isFirst = true;
        for(PersonalIdentifier id : _ids)
        {
            if(!isFirst) result+=", ";
            result+= id.get_Value();
        }
        result+="}";
        return result;
    }
}
