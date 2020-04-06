package com.hanu.util.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.io.File;

import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuration class
 * @see {@link #org.apache.commons.configuration2}
 */
public class Configuration {

    private static Configuration instance;

    private static final String CONFIGURATION_PATH = "./src/main/resources/configurations";
    private static final Pattern EXTENSION_PATTERN = Pattern.compile("^(.+)\\.([A-Za-z0-9]+)$");
    private static final Logger logger = LoggerFactory.getLogger(Configuration.class);

    private List<FileBasedConfiguration> configurations;

    private Configuration() {}

    public void loadConfigurations() throws ConfigurationException {
        configurations = new ArrayList<>();
        File configurationFolder = new File(CONFIGURATION_PATH);
        Configurations configurations = new Configurations();
        for (File configurationFile : configurationFolder.listFiles()) {
            String fileName = configurationFile.getName();
            Matcher extMatcher = EXTENSION_PATTERN.matcher(fileName);
            extMatcher.find();
            switch (extMatcher.group(2)) {
                case "xml":
                    this.configurations.add(configurations.xml(configurationFile));
                    break;
                case "properties":
                    this.configurations.add(configurations.properties(configurationFile));
                    break;
                case "ini":
                    this.configurations.add(configurations.ini(configurationFile));
                    break;
                default:
                    break;
            }
            logger.info("Added configuration file: " + fileName);
        }
    }

    public String get(String property) {
        return configurations.stream().filter(c -> c.containsKey(property))
                                    .map(c -> c.getString(property))
                                    .collect(Collectors.joining(""));
    }

    @Override
    public String toString() {
        return configurations.toString();
    }

    public static Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }
}