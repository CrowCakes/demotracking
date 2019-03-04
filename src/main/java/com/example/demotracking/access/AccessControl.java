package com.example.demotracking.access;

import com.example.demotracking.classes.ConnectionManager;
import java.io.Serializable;

public interface AccessControl
extends Serializable {
    public String signIn(ConnectionManager manager, String var2, String var3);

    public boolean isUserSignedIn();

    public boolean isUserInRole(String var1);

    public boolean isRoleValid(String var1);

    public String getPrincipalName();
}

