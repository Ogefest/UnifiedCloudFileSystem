package com.ogefest.unifiedcloudfilesystem;

import java.util.HashMap;


public class EngineConfiguration {

    private HashMap<String, String> configuration;

//    public Configuration(String input) {
//
//    }

    public EngineConfiguration(HashMap<String, String> input) {
        this.configuration = input;
    }

    public String getStringValue(String key) throws MissingConfigurationKeyException {
        if (configuration.containsKey(key)) {
            return configuration.get(key);
        }
        throw new MissingConfigurationKeyException();
    }

    public int getIntValue(String key) throws MissingConfigurationKeyException {
        if (configuration.containsKey(key)) {
            return Integer.parseInt(configuration.get(key));
        }
        throw new MissingConfigurationKeyException();
    }

    public boolean getBooleanValue(String key) throws MissingConfigurationKeyException, InvalidConfigurationValueException {
        if (configuration.containsKey(key)) {
            if (configuration.get(key).equals("true")) {
                return true;
            }
            if (configuration.get(key).equals("false")) {
                return false;
            }
            throw new InvalidConfigurationValueException();
        }
        throw new MissingConfigurationKeyException();
    }

}
