package com.company.inventory.desktop.util;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

public class UserSession {
    @Getter @Setter private static String token;
    @Getter @Setter private static String username;
    @Getter @Setter private static List<String> roles;

    public static void cleanSession() {
        token = null;
        username = null;
        roles = null;
    }
}