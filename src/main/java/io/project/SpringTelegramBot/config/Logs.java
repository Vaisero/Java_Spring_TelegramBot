package io.project.SpringTelegramBot.config;

import org.slf4j.LoggerFactory;

public class Logs
{

    static final org.slf4j.Logger log = LoggerFactory.getLogger(BotInitializer.class);

    public static void setLog(LogType logtype, String string)
    {
        switch (logtype)
        {
            case Info:
                log.info("(Info) " + string);
                break;
            case Error:
                log.error("(Error) " + string);
                break;
            case Debug:
                log.debug("(Debug) " + string);
                break;
            default:
                break;
        }

    }
}
