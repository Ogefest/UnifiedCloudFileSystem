package com.ogefest.unifiedcloudfilesystem.engine;

import com.ogefest.unifiedcloudfilesystem.Engine;
import com.ogefest.unifiedcloudfilesystem.EngineConfiguration;
import com.ogefest.unifiedcloudfilesystem.EngineItem;
import com.ogefest.unifiedcloudfilesystem.MissingConfigurationKeyException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.ArrayList;

public class Ftp extends Engine {

    FTPClient client;
    String hostname;
    int port;
    String login;
    String password;

    public Ftp(EngineConfiguration conf) throws IOException {
        super(conf);

        try {
            hostname = getConfiguration().getStringValue("host");
            port = getConfiguration().getIntValue("port");
            login = getConfiguration().getStringValue("login");
            password = getConfiguration().getStringValue("password");

            client.connect(hostname, port);
        } catch (MissingConfigurationKeyException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int replyCode = client.getReplyCode();
        if (!FTPReply.isPositiveCompletion(replyCode)) {
            client.disconnect();
            throw new IOException("Exception in connecting to FTP Server");
        }

        client.login(login, password);
    }

    @Override
    public void finish() throws IOException {
        client.disconnect();
    }

    @Override
    public EngineItem set(EngineItem engineItem, InputStream input) throws IOException {
        client.storeFile(engineItem.getPath(), input);

        return new EngineItem(engineItem.getPath());
    }

    @Override
    public InputStream get(EngineItem engineItem) throws IOException {
        return client.retrieveFileStream(engineItem.getPath());
    }

    @Override
    public ArrayList<EngineItem> list(EngineItem engineItem) throws IOException {
        FTPFile[] files = client.listFiles(engineItem.getPath());

        ArrayList<EngineItem> items = new ArrayList<>();
        for (FTPFile f : files) {
            items.add(new EngineItem(f.getLink()));
        }

        return items;
    }

    @Override
    public boolean exists(EngineItem engineItem) throws IOException {
        FTPFile[] files = client.listFiles(engineItem.getPath());
        return files.length == 1;
    }

    @Override
    public void delete(EngineItem engineItem) throws IOException {
        client.deleteFile(engineItem.getPath());
    }

    @Override
    public void move(EngineItem from, EngineItem to) throws IOException {
        client.rename(from.getPath(), to.getPath());
    }


}
