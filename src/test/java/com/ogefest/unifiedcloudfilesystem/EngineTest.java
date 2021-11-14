package com.ogefest.unifiedcloudfilesystem;

import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
abstract public class EngineTest {

    protected Engine fs;
    protected Properties props;

    protected void loadConfiguration() {
        try (InputStream input = new FileInputStream("./runtime.properties")) {

            props = new Properties();
            props.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
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
        assertTrue(fs.exists(ei));
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
    void list() throws IOException {
        ArrayList<EngineItem> lst = fs.list(new EngineRootItem());
        assertTrue(lst.get(0).getName().equals("abc.txt"));
    }

    @Test
    @Order(3)
    void exists() throws IOException {
        assertTrue(fs.exists(new EngineItem("abc.txt")));
    }

    @Test
    @Order(6)
    void delete() throws IOException {
        EngineItem toDelete = new EngineItem("cde.txt");
        assertTrue(fs.exists(toDelete));
        fs.delete(toDelete);
        assertFalse(fs.exists(toDelete));
    }

    @Test
    @Order(5)
    void move() throws IOException {
        EngineItem from = new EngineItem("abc.txt");
        EngineItem to = new EngineItem("cde.txt");

        assertTrue(fs.exists(from));
        assertFalse(fs.exists(to));
        fs.move(from, to);
        assertTrue(fs.exists(to));
        assertFalse(fs.exists(from));
    }
}