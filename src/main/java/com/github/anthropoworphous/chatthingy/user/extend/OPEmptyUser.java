package com.github.anthropoworphous.chatthingy.user.extend;

import com.github.anthropoworphous.chatthingy.user.impl.EmptyUser;

public class OPEmptyUser extends EmptyUser {
    @Override
    public boolean checkPermission(String node) {
        return true;
    }
}
