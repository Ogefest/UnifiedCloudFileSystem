package com.ogefest.unifiedcloudfilesystem;

import java.io.File;

public class EngineItem {

    protected String name;
    protected long size;
    protected String path;

    public EngineItem(String path) {
        this.path = path;
        updateName();
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public String getPath() {
        return path;
    }

//    public void setName(String name) {
//        this.name = name;
//    }

    public void setPath(String path) {
        this.path = path;
        updateName();
    }

    private void updateName() {

        File f =  new File(path);
        name = f.getName();

    }
}
