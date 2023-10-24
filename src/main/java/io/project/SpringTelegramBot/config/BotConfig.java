package io.project.SpringTelegramBot.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ComponentScan
@PropertySource("application.properties")

public class BotConfig
{
    @Value("${bot.name}")
    private String botName;
    public String getBotName()
    {
        return this.botName;
    }

    @Value("${bot.token}")
    private String botToken;
    public String getBotToken()
    {
        return botToken;
    }
}
