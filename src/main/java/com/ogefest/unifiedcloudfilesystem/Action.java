package com.ogefest.unifiedcloudfilesystem;

import java.util.HashMap;

abstract public class Action {

    private HashMap<String, Engine> engines = new HashMap<>();

    public void setEngines(HashMap<String, Engine> engines) {
        this.engines = engines;
    }

    protected Engine getEngine(String name) {
        return engines.get(name);
    }

    abstract public void run();
}
