package io.project.SpringTelegramBot.config;

import io.project.SpringTelegramBot.service.TelegramBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.starter.AfterBotRegistration;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class BotInitializer
{
    @Autowired
    TelegramBot bot;



    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException
    {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try
        {
            var session = telegramBotsApi.registerBot(bot);
            session.stop();
        }
        catch (TelegramApiException e)
        {
            Logs.setLog(LogType.Error, e.getMessage());
        }

    }

}
