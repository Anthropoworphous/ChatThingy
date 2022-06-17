package com.github.anthropoworphous.chatthingy.user.extend;

import com.github.anthropoworphous.chatthingy.user.impl.EmptyUser;

public class OPEmptyUser<T> extends EmptyUser<T> {
    @Override
    public boolean checkPermission(String node) { return true; }
}
