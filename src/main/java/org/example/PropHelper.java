package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropHelper {

    private final static String fileName = "config.properties";

    public final static String botToken = "botToken";
    public final static String binanceApiKey = "binanceApiKey";
    public final static String binanceSecretKey = "binanceSecretKey";

    public static AppProperties getProps(){

        FileInputStream fis;
        Properties property = new Properties();

        try {
            fis = new FileInputStream(fileName);
            property.load(fis);

            var props = new AppProperties();
            props.binanceApiKey = property.getProperty(binanceApiKey);
            props.binanceSecretKey = property.getProperty(binanceSecretKey);
            props.botToken = property.getProperty(botToken);

            return props;

        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл отсуствует");
        }

        return new AppProperties();
    }
}
