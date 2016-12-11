package com.arsenarsen.userbot;

import java.awt.*;
import java.util.function.Predicate;

public enum Configuration {
    COLOR(s -> {
        if(s.equalsIgnoreCase("random"))
            return true;
        try {
            Color.decode(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }, "random"),
    PREFIX,
    EMBED_NAMED(s -> s.matches("(true)|(false)"), "true"),
    TOKEN {
        @Override
        public String get() {
            throw new IllegalArgumentException("Not today asshole!");
        }
    };

    private final Predicate<String> test;
    private final String def;

    Configuration() {
        test = s -> true;
        def = null;
    }

    Configuration(Predicate<String> test) {
        this.test = test;
        def = null;
    }

    Configuration(Predicate<String> test, String def){
        this.test = test;
        this.def = def;
    }

    public void set(String newValue) {
        if (test.test(newValue)) {
            UserBot.getInstance().getConfig().setProperty(name().toLowerCase(), newValue);
            UserBot.getInstance().saveConfig();
        } else {
            throw new IllegalArgumentException(String.format("The test for %s returned fasle for \"%s\"", this, newValue));
        }
    }

    public String get() {
        if(!isSet()){
            return def;
        }
        return UserBot.getInstance().getConfig().getProperty(name().toLowerCase());
    }

    public boolean isSet() {
        return UserBot.getInstance().getConfig().containsKey(name().toLowerCase());
    }

    public static Configuration get(String key) {
        try {
            return valueOf(key.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
