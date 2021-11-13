package com.ogefest.unifiedcloudfilesystem.engine;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import com.ogefest.unifiedcloudfilesystem.Engine;
import com.ogefest.unifiedcloudfilesystem.EngineConfiguration;
import com.ogefest.unifiedcloudfilesystem.EngineItem;
import com.ogefest.unifiedcloudfilesystem.MissingConfigurationKeyException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WebDav extends Engine {

    private Sardine webdavClient;

    private String url;
    private String username;
    private String password;

    public WebDav(EngineConfiguration conf) {
        super(conf);

        try {
            url = getConfiguration().getStringValue("url");
            username = getConfiguration().getStringValue("username");
            password = getConfiguration().getStringValue("password");
        } catch (MissingConfigurationKeyException e) {
            e.printStackTrace();
        }

        webdavClient = SardineFactory.begin(username, password);
    }

    private String getFullUrl(EngineItem item) {
        return url + item.getPath();
    }

    @Override
    public EngineItem set(EngineItem engineItem, InputStream input) throws IOException {
        webdavClient.put(getFullUrl(engineItem), input);

        return engineItem;
    }

    @Override
    public InputStream get(EngineItem engineItem) throws IOException {
        return webdavClient.get(getFullUrl(engineItem));
    }

    @Override
    public ArrayList<EngineItem> list(EngineItem engineItem) throws IOException {
        ArrayList<EngineItem> result = new ArrayList<>();

        List<DavResource> davList = webdavClient.list(getFullUrl(engineItem), 1);
        URL baseUrl = new URL(url);

        for (DavResource res : davList) {

            String path = baseUrl.getPath().substring(0, baseUrl.getPath().length()-1);
            String filePath = res.getPath().replace(path, "");
            if (filePath.equals("/")) {
                continue;
            }

            result.add(new EngineItem(filePath));
        }

        return result;
    }

    @Override
    public boolean exists(EngineItem engineItem) throws IOException {
        return webdavClient.exists(getFullUrl(engineItem));
    }

    @Override
    public void delete(EngineItem engineItem) throws IOException {
        webdavClient.delete(getFullUrl(engineItem));
    }

    @Override
    public void move(EngineItem from, EngineItem to) throws IOException {
        webdavClient.move(getFullUrl(from), getFullUrl(to));
    }
}
