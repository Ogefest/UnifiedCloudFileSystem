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

    private FTPClient client;
    private String hostname;
    private int port;
    private String login;
    private String password;
    private String rootPath;

    public Ftp(EngineConfiguration conf) throws IOException {
        super(conf);

        try {
            client = new FTPClient();
            hostname = getConfiguration().getStringValue("host");
            port = getConfiguration().getIntValue("port");
            login = getConfiguration().getStringValue("username");
            password = getConfiguration().getStringValue("password");
            rootPath = getConfiguration().getStringValue("path");

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

//        client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        client.setBufferSize(1024 * 1024);

        client.enterLocalPassiveMode();
        client.setAutodetectUTF8(true);

        client.login(login, password);
        client.changeWorkingDirectory(rootPath);
    }

    @Override
    public void finish() throws IOException {
        client.disconnect();
    }

    @Override
    public EngineItem set(EngineItem engineItem, InputStream input) throws IOException {
        client.storeFile(getFullPath(engineItem), input);

        return new EngineItem(engineItem.getPath());
    }

    @Override
    public InputStream get(EngineItem engineItem) throws IOException {
        return client.retrieveFileStream(getFullPath(engineItem));
    }

    @Override
    public ArrayList<EngineItem> list(EngineItem engineItem) throws IOException {
        String pathToList = getFullPath(engineItem);
        FTPFile[] files = client.listFiles(pathToList);

        ArrayList<EngineItem> items = new ArrayList<>();
        for (FTPFile f : files) {
            if (f.getName().equals(".") || f.getName().equals("..")) {
                continue;
            }
            items.add(new EngineItem(pathToList + "/" + f.getName()));
        }

        return items;
    }

    @Override
    public boolean exists(EngineItem engineItem) throws IOException {
        FTPFile[] files = client.listFiles(getFullPath(engineItem));
        return files.length == 1;
    }

    @Override
    public void delete(EngineItem engineItem) throws IOException {
        client.deleteFile(getFullPath(engineItem));
    }

    @Override
    public void move(EngineItem from, EngineItem to) throws IOException {
        client.rename(getFullPath(from), getFullPath(to));
    }

    private String getFullPath(EngineItem item) {

        String rawPath = item.getPath();
        if (item.getPath().substring(0, 1).equals("/")) {
            rawPath = item.getPath().substring(1);
        }
        String toReturn = rawPath;

        return toReturn;
    }
}
