package com.ogefest.unifiedcloudfilesystem.engine;

import com.ogefest.unifiedcloudfilesystem.EngineConfiguration;
import com.ogefest.unifiedcloudfilesystem.EngineItem;
import com.ogefest.unifiedcloudfilesystem.EngineRootItem;
import org.junit.jupiter.api.*;

import java.awt.event.ItemEvent;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FileSystemTest {

    FileSystem fs;
    File temporaryDirectory;

    @BeforeAll
    void setUp() throws IOException {

        Path tempDirWithPrefix = Files.createTempDirectory("ucfstestdir");
        temporaryDirectory = tempDirWithPrefix.toFile();
        if (!temporaryDirectory.exists()) {
            temporaryDirectory.mkdirs();
        }

        HashMap<String, String> hs = new HashMap<>();
        hs.put("path", temporaryDirectory.getAbsolutePath());

        fs = new FileSystem(new EngineConfiguration(hs));
    }

    @AfterAll
    void cleanup() {
        Path pathToBeDeleted = Paths.get(temporaryDirectory.getPath());

        try {
            Files.walk(pathToBeDeleted)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(1)
    void set() throws IOException {

        EngineItem ei = new EngineItem("abc.txt");
        byte[] initialArray = { '1', '2', '3' };
        InputStream input = new ByteArrayInputStream(initialArray);
        fs.set(ei, input);
        input.close();

        File check = new File(temporaryDirectory + File.separator + "abc.txt");

        assertTrue(check.exists());
        assertTrue(check.length() == initialArray.length);
    }

    @Test
    @Order(2)
    void get() throws IOException {
        EngineItem ei = new EngineItem("abc.txt");
        InputStream is = fs.get(ei);
        byte[] fdata = is.readAllBytes();

        byte[] initialArray = { '1', '2', '3' };

        assertArrayEquals(fdata, initialArray);
    }

    @Test
    @Order(4)
    void list() {
        ArrayList<EngineItem> lst = fs.list(new EngineRootItem());
        assertTrue(lst.get(0).getName().equals("abc.txt"));
    }

    @Test
    @Order(3)
    void exists() {
        assertTrue(new File(temporaryDirectory.getAbsolutePath() + File.separator + "abc.txt").exists());
        assertTrue(fs.exists(new EngineItem("abc.txt")));
    }

    @Test
    @Order(6)
    void delete() {
        EngineItem toDelete = new EngineItem("cde.txt");
        assertTrue(fs.exists(toDelete));
        fs.delete(toDelete);
        assertFalse(fs.exists(toDelete));
    }

    @Test
    @Order(5)
    void move() {
        EngineItem from = new EngineItem("abc.txt");
        EngineItem to = new EngineItem("cde.txt");

        assertTrue(fs.exists(from));
        assertFalse(fs.exists(to));
        fs.move(from, to);
        assertTrue(fs.exists(to));
        assertFalse(fs.exists(from));
    }
}