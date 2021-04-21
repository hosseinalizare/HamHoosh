package com.example.koohestantest1.constants;

public class CurrentCartId {
    private static long id;

    public static long getId() {
        return id;
    }

    public static void setId(long id) {
        CurrentCartId.id = id;
    }
}
