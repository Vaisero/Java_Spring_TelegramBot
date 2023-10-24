package io.project.SpringTelegramBot.model;

import io.project.SpringTelegramBot.config.LogType;
import io.project.SpringTelegramBot.config.Logs;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class FileDownloader
{
    public void downloadFile() {
        Logs.setLog(LogType.Info, "(INFO) Started download");
        String fileUrl = "https://backorder.ru/json/?order=desc&expired=1&by=hotness&page=1&items=50";
        String savePath = "/var/files/backorder.json";

        URL url = null;
        try
        {
            url = new URL(fileUrl);
        } catch (MalformedURLException e)
        {
            throw new RuntimeException(e);
        }
        try (BufferedInputStream inputStream = new BufferedInputStream(url.openStream());
             FileOutputStream outputStream = new FileOutputStream(savePath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer, 0, 1024)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        catch (Exception e)
        {
            Logs.setLog(LogType.Error, "(Error) " + e.getMessage());
        }
        Logs.setLog(LogType.Info, "(INFO) Download Ended");
    }
}