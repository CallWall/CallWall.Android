package com.CallWall.Android.Entities;

/**
 * Created with IntelliJ IDEA.
 * User: Lee
 * Date: 12/02/13
 * Time: 09:16
 * To change this template use File | Settings | File Templates.
 */
public final class PersonalIdentifier {
    private final String _identifierType;
    private final String _provider;
    private final String _value;

    public PersonalIdentifier(String identifierType, String provider, String value)
    {
        _identifierType = identifierType;
        _provider = provider;
        _value = value;
    }

    public String get_IdentifierType() {
        return _identifierType;
    }

    public String get_Provider() {
        return _provider;
    }

    public String get_Value() {
        return _value;
    }
}
