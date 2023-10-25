package io.project.SpringTelegramBot.service;

import io.project.SpringTelegramBot.config.BotConfig;
import io.project.SpringTelegramBot.config.LogType;
import io.project.SpringTelegramBot.config.Logs;
import io.project.SpringTelegramBot.model.*;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;


@Component
public class TelegramBot extends TelegramLongPollingBot
{
    BotConfig config;

    @Autowired
    User_Repository user_repository;
    @Autowired
    Message_Repository message_repository;

    @Autowired
    Domains_Repository domains_repository;

    FileDownloader fileDownloader;

    public TelegramBot(BotConfig config)
    {
        this.config = config;

        Logs.setLog(LogType.Debug, "ON STARTED");

        List<BotCommand> botCommandList = new ArrayList<>();
        botCommandList.add(new BotCommand("/start","Welcome"));
        botCommandList.add(new BotCommand("/about","Description"));
        try
        {
            this.execute(new SetMyCommands(botCommandList, new BotCommandScopeDefault(), null));
        }
        catch (TelegramApiException e)
        {
            Logs.setLog(LogType.Error, e.getMessage());
        }
        fileDownloader = new FileDownloader();//автоматическая загрузка файла
    }

    @Override
    public void onUpdateReceived(Update update)
    {
        if (update.hasMessage() && update.getMessage().hasText())
        {
            String messageText = update.getMessage().getText();
            long chatID = update.getMessage().getChatId();
            String name = update.getMessage().getChat().getFirstName();
            String answer = null;

            if (messageText.contains("/send") && (chatID == Integer.parseInt("5010164097")))
            {//ручная рассылка всем пользователям
                var textToSend = messageText.substring(messageText.indexOf(" "));
                var users = user_repository.findAll();
                for (SQL_user user : users)
                {
                    sendMessage(chatID, textToSend);
                }
            }
            else
            {
                switch (messageText)
                {
                    case "/start":
                        answer = "Hi, " + name + ", nice to meet you!";
                        break;
                    case "/about":
                        answer = "Test task for the company \"M-Social\"";
                        break;
                    default:
                        answer = "Sorry, command was not recognized";
                        break;
                }
            }

            sendMessage(chatID, answer);
            GetMessage(update.getMessage(), answer);
        }
    }

    private void sendMessage(long chatID, String textToSend)
    {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatID));
        message.setText(textToSend);

        try
        {
            execute(message);
        }
        catch (TelegramApiException e)
        {
            Logs.setLog(LogType.Error, "(ERROR)" + e.getMessage());
        }
    }

    private void GetMessage(Message message, String answer)
    {
        SQL_user user = user_repository.findById(message.getChatId()).orElse(new SQL_user());


        Logs.setLog(LogType.Info, "START MESSAGE" + user.toString());

        if (user.getChatID() == null)
            user=RegisterUser(message, user);//регистрация нового пользователя в базу бота
        else
            UpdateTimeOfLastMessage(user);//обновление даты последнего сообщения

        SaveHistory(message, user, answer);//сохранение истории сообщений
    }
    private SQL_user RegisterUser (Message message, SQL_user user)
    {
        Logs.setLog(LogType.Info, "REGISTER " + user);
        //регистрация нового пользователя в базу бота
        user.setChatID(message.getChatId());
        user.setUserName(message.getChat().getUserName());
        user.setUserFirstName(message.getChat().getFirstName());
        user.setUserSecondName(message.getChat().getLastName());
        user.setLast_message_at(new Timestamp(System.currentTimeMillis()));
        Logs.setLog(LogType.Info, "USER: " + user.toString());
        user_repository.save(user);
        Logs.setLog(LogType.Info, "User saved" + user);
        return user;
    }

    private void UpdateTimeOfLastMessage(SQL_user user)
    {//обновление даты последнего сообщения
        Logs.setLog(LogType.Info, "UPDATE MESSAGE" + user);
        user.setLast_message_at(new Timestamp(System.currentTimeMillis()));
        user_repository.save(user);
        Logs.setLog(LogType.Info, "Time of last message update:" + user);
    }

    private void SaveHistory(Message message, SQL_user user, String answer)
    {//сохранение истории сообщений
        Logs.setLog(LogType.Info, "Add to history " + user);

        SQL_messages msg = new SQL_messages();
        msg.setUser(user);
        msg.setUserText(message.getText());
        msg.setBotText(answer);

        message_repository.save(msg);

        Logs.setLog(LogType.Info, "User saved " + user);
    }


    @Scheduled(cron = "0 55 14 * * *")//автоматический запуск метода каждый день в 14.55
    private void Get_Daily_Domains()
    {//удаление доменов из БД, скачивание файла, обновление доменов в БД
        domains_repository.deleteAll();
        fileDownloader.downloadFile();
        Parser();
    }

    private void Parser()
    {//вывод данных из Json файла и добавление в БД
        try
        {
            JSONParser jsonParser = new JSONParser(new FileReader("/var/files/backorder.json"));
            Object obj = jsonParser.parse();

            if (obj instanceof ArrayList)
            {
                ArrayList domains = (ArrayList) obj;
                for (Object domain:domains)
                {
                    var linkedHashMap = (LinkedHashMap) domain;
                    boolean block = (boolean) linkedHashMap.get("block");
                    String domainName = linkedHashMap.get("domainname").toString();
                    SQL_daily_domains daily_domains = new SQL_daily_domains();
                    daily_domains.setDomains(domainName);
                    domains_repository.save(daily_domains);
                    //Logs.setLog(LogType.Info, "(Info) " + domainName+ " - " + block);
                }
            }
        } catch (FileNotFoundException e)
        {
            Logs.setLog(LogType.Error, "(Error) " + e.getMessage());
        } catch (ParseException e)
        {
            Logs.setLog(LogType.Error, "(Error) " + e.getMessage());
        }

    }

    @Scheduled(cron = "0 0 15 * * *")//автоматический запуск метода каждый день в 15.00
    private void Send_Daily_Domains()
    {//автоматическая отправка сообщений всем пользователям (авто-рассылка)
        var user = user_repository.findAll();
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        var dateStr = format.format(date);

            for(SQL_user users: user)
            {
                SendMessage message = new SendMessage();
                message.setChatId(String.valueOf(users.getChatID()));
                message.setText(dateStr + ". Собрано " + (domains_repository.count() + " доменов"));
                try
                {
                    execute(message);
                }
                catch (TelegramApiException e)
                {
                    Logs.setLog(LogType.Error, "(ERROR)" + e.getMessage());
                }

            }
        }


    @Override
    public String getBotUsername() {return config.getBotName();}
    @Override
    public String getBotToken() {return config.getBotToken();}
}
