package com.ogefest.unifiedcloudfilesystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EngineItemTest {

    EngineItem engineItem;

    @BeforeEach
    void setUp() {
        engineItem = new EngineItem("path1/name1");
    }

    @Test
    void getName() {
        assertEquals(engineItem.getName(), "name1");
    }

    @Test
    void getPath() {
        assertEquals(engineItem.getPath(), "path1/name1");
    }

    @Test
    void setPath() {
        engineItem.setPath("path2");
        assertEquals(engineItem.getPath(), "path2");
    }

    @Test
    void setName() {
        engineItem.setPath("path2/name2");
        assertEquals(engineItem.getName(), "name2");
    }
}