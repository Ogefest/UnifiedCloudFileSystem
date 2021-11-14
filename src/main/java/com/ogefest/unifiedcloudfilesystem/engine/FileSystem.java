package com.ogefest.unifiedcloudfilesystem.engine;

import com.ogefest.unifiedcloudfilesystem.Engine;
import com.ogefest.unifiedcloudfilesystem.EngineConfiguration;
import com.ogefest.unifiedcloudfilesystem.EngineItem;
import com.ogefest.unifiedcloudfilesystem.MissingConfigurationKeyException;

import java.io.*;
import java.util.ArrayList;

public class FileSystem extends Engine {

    public FileSystem(EngineConfiguration c) {
        super(c);
    }

    @Override
    public EngineItem set(EngineItem engineItem, InputStream input) {

        FileOutputStream fout = null;
        try {
            String outputPath = getFullPath(engineItem);
            fout = new FileOutputStream(outputPath);

            byte[] buffer = new byte[10 * 1024 * 1024];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                fout.write(buffer, 0, bytesRead);
            }
            fout.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new EngineItem(engineItem.getPath());
    }

    @Override
    public InputStream get(EngineItem engineItem) {
        try {
            return new FileInputStream(getFullPath(engineItem));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<EngineItem> list(EngineItem engineItem) {
        File d = new File(getFullPath(engineItem));

        File[] files = d.listFiles();
        ArrayList<EngineItem> result = new ArrayList<>();
        for (File f : files) {
            EngineItem tmp = new EngineItem(f.getPath());
            result.add(tmp);
        }

        return result;
    }

//    @Override
//    public ArrayList<EngineItem> root() {
//        try {
//            EngineItem rootItem = new EngineItem(getConfiguration().getStringValue("path"), "root", 0);
//            return list(rootItem);
//        } catch (MissingConfigurationKey e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    @Override
    public boolean exists(EngineItem engineItem) {
        File f = new File(getFullPath(engineItem));

        return f.exists();
    }

    @Override
    public void delete(EngineItem engineItem) {
        File f = new File(getFullPath(engineItem));
        f.delete();
    }

    @Override
    public void move(EngineItem from, EngineItem to) {
        File f = new File(getFullPath(from));
        f.renameTo(new File(getFullPath(to)));
    }

    private String getFullPath(EngineItem item) {
        try {
            return getConfiguration().getStringValue("path") + File.separator + item.getPath();
        } catch (MissingConfigurationKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

}
