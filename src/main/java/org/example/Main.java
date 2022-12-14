package org.example;

import com.binance4j.websocket.client.UserDataClient;
import com.binance4j.websocket.client.WebsocketUserDataClient;

public class Main {

    public static void main(String[] args) {
        try{
            var props = PropHelper.getProps();

            var bot = new TgBot(props.botToken);
            bot.start();

            var userDataClient = new UserDataClient(props.binanceApiKey, props.binanceSecretKey);
            var client = new WebsocketUserDataClient(userDataClient, bot);
            client.open();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}