package org.example;

import com.binance4j.websocket.callback.WebsocketCallback;
import com.binance4j.websocket.dto.UserDataUpdate;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.HashSet;

public class TgBot extends Thread implements WebsocketCallback<UserDataUpdate> {

    private final String[] coins = {"BNB", "USDT", "ETH", "BTC", "APE", "BUSD", ""};

    private final HashSet<Long> chats = new HashSet<>();

    private final TelegramBot bot;

    public TgBot(String token){
        bot = new TelegramBot(token);
    }

    @Override
    public void run() {
        bot.setUpdatesListener(list -> {
            for (var update: list) {
                if(update.message() != null){
                    chats.add(update.message().chat().id());
                }
            }

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

        super.run();
    }

    private void sendMessagesForAll(String message){
        for (var id: chats) {
            SendMessage msg = new SendMessage(id, message);
            bot.execute(msg);
        }
    }

    @Override
    public void onMessage(UserDataUpdate userDataUpdate) {
        switch (userDataUpdate.eventType()){
            case ACCOUNT_POSITION_UPDATE -> {
                var event = userDataUpdate.balanceUpdateEvent();
            }
            case BALANCE_UPDATE -> {
                var event = userDataUpdate.balanceUpdateEvent();
            }
            case ORDER_TRADE_UPDATE -> {
                StringBuilder strBuilder = new StringBuilder();
                var event = userDataUpdate.orderTradeUpdateEvent();

                var from = "";
                var price = "";

                for (var coin: coins) {
                    if (event.symbol().toUpperCase().startsWith(coin.toUpperCase())){
                        from = Double.parseDouble(event.originalQuantity()) + " " + coin.toUpperCase();
                        price = Double.parseDouble(event.price()) + " " + event.symbol().toUpperCase().replace(coin.toUpperCase(), "");
                        break;
                    }
                }

                strBuilder.append(event.executionType()).append(" ")
                        .append(event.orderId()).append("\n")
                        .append(event.side()).append(" ").append(from).append(" for ").append(price);

                sendMessagesForAll(strBuilder.toString());
            }
        }
    }
}
