package com.airtribe.ridewise.util;

import java.util.UUID;

public final class IdGenerator{

    private IdGenerator(){

    }

    public static UUID generateId() {
        return UUID.randomUUID();
    }

}