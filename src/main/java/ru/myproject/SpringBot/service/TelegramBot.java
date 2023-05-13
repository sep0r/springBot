package ru.myproject.SpringBot.service;

import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import ru.myproject.SpringBot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;

    public TelegramBot(BotConfig config) {
        this.config = config;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Получить приветственное сообщение"));
        listOfCommands.add(new BotCommand("/mydata", "Получить историю запросов"));
        listOfCommands.add(new BotCommand("/deletedata", "Удалить все мои запросы"));
        listOfCommands.add(new BotCommand("/help", "Получить подробное описание команд"));
        listOfCommands.add(new BotCommand("/settings", "Установить настройки"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/help":
                    helpCommandReceived(chatId);
                    break;
                default:
                    sendMessage(chatId, "Извини, но такой команды пока нет");
            }
        }
    }

    private void startCommandReceived(long chatId, String name) {
        String answer = "Привет " + name + ", рад познакомиться с тобой!";
        log.info("Replied to user " + name);

        sendMessage(chatId, answer);
    }

    private void helpCommandReceived(long chatId) {
        String answer = "Я бот, над котором проводит опыты один бестолковый программмист, но я не жалуюсь, ведь я страдаю ради науки и это греет мне душу.\n" +
                "\n" +
                "❗️Список команд\n" +
                "/start - получить приветственное сообщение\n" +
                "/mydata - получить историю запросов\n" +
                "/deletedata - удалить все мои запросы\n" +
                "/help - получить подробное описание команд\n" +
                "/settings - просмотреть текущие настройки\n";
        log.info("Test message");

        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }
}
