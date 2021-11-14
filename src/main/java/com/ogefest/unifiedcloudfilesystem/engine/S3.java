package com.ogefest.unifiedcloudfilesystem.engine;

import com.ogefest.unifiedcloudfilesystem.Engine;
import com.ogefest.unifiedcloudfilesystem.EngineConfiguration;
import com.ogefest.unifiedcloudfilesystem.EngineItem;

import java.io.InputStream;
import java.util.ArrayList;

public class S3 extends Engine {

    public S3(EngineConfiguration c) {
        super(c);
    }

    @Override
    public EngineItem set(EngineItem engineItem, InputStream input) {
        return null;
    }

    @Override
    public InputStream get(EngineItem engineItem) {
        return null;
    }

    @Override
    public ArrayList<EngineItem> list(EngineItem engineItem) {
        return null;
    }

    @Override
    public boolean exists(EngineItem engineItem) {
        return false;
    }

    @Override
    public void delete(EngineItem engineItem) {

    }

    @Override
    public void move(EngineItem from, EngineItem to) {

    }


}
