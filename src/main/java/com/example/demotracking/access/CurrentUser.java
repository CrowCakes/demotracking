package com.example.demotracking.access;

import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;

public final class CurrentUser {
    public static final String CURRENT_USER_SESSION_ATTRIBUTE_KEY = CurrentUser.class.getCanonicalName();
    public static final String CURRENT_USER_SESSION_ATTRIBUTE_KEY_INT = String.valueOf(CurrentUser.class.getCanonicalName()) + "Int";
    public static final String CURRENT_USER_SESSION_ATTRIBUTE_KEY_NAME = String.valueOf(CurrentUser.class.getCanonicalName()) + "Name";

    static {
    }

    private CurrentUser() {
    }

    public static String get() {
        String currentUser = (String)CurrentUser.getCurrentRequest().getWrappedSession().getAttribute(CURRENT_USER_SESSION_ATTRIBUTE_KEY);
        if (currentUser == null) {
            return "";
        }
        return currentUser;
    }

    public static String getName() {
        String currentUser = (String)CurrentUser.getCurrentRequest().getWrappedSession().getAttribute(CURRENT_USER_SESSION_ATTRIBUTE_KEY_NAME);
        if (currentUser == null) {
            return "";
        }
        return currentUser;
    }

    public static String getId() {
        String currentUser = (String)CurrentUser.getCurrentRequest().getWrappedSession().getAttribute(CURRENT_USER_SESSION_ATTRIBUTE_KEY_INT);
        if (currentUser == null) {
            return "";
        }
        return currentUser;
    }

    public static void set(String currentUser) {
        if (currentUser == null) {
            CurrentUser.getCurrentRequest().getWrappedSession().removeAttribute(CURRENT_USER_SESSION_ATTRIBUTE_KEY);
        } else {
            CurrentUser.getCurrentRequest().getWrappedSession().setAttribute(CURRENT_USER_SESSION_ATTRIBUTE_KEY, (Object)currentUser);
        }
    }

    public static void set(String currentUser, String string) {
        if (currentUser == null) {
            CurrentUser.getCurrentRequest().getWrappedSession().removeAttribute(CURRENT_USER_SESSION_ATTRIBUTE_KEY);
            CurrentUser.getCurrentRequest().getWrappedSession().removeAttribute(CURRENT_USER_SESSION_ATTRIBUTE_KEY_INT);
        } else {
            CurrentUser.getCurrentRequest().getWrappedSession().setAttribute(CURRENT_USER_SESSION_ATTRIBUTE_KEY, (Object)currentUser);
            CurrentUser.getCurrentRequest().getWrappedSession().setAttribute(CURRENT_USER_SESSION_ATTRIBUTE_KEY_INT, (Object)string);
        }
    }

    public static void set(String currentUser, String string, String name) {
        if (currentUser == null) {
            CurrentUser.getCurrentRequest().getWrappedSession().removeAttribute(CURRENT_USER_SESSION_ATTRIBUTE_KEY);
            CurrentUser.getCurrentRequest().getWrappedSession().removeAttribute(CURRENT_USER_SESSION_ATTRIBUTE_KEY_INT);
            CurrentUser.getCurrentRequest().getWrappedSession().removeAttribute(CURRENT_USER_SESSION_ATTRIBUTE_KEY_NAME);
        } else {
            CurrentUser.getCurrentRequest().getWrappedSession().setAttribute(CURRENT_USER_SESSION_ATTRIBUTE_KEY, (Object)currentUser);
            CurrentUser.getCurrentRequest().getWrappedSession().setAttribute(CURRENT_USER_SESSION_ATTRIBUTE_KEY_INT, (Object)string);
            CurrentUser.getCurrentRequest().getWrappedSession().setAttribute(CURRENT_USER_SESSION_ATTRIBUTE_KEY_NAME, (Object)name);
        }
    }

    private static VaadinRequest getCurrentRequest() {
        VaadinRequest request = VaadinService.getCurrentRequest();
        if (request == null) {
            throw new IllegalStateException("No request bound to current thread");
        }
        return request;
    }
}

