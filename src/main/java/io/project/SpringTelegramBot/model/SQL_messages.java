package io.project.SpringTelegramBot.model;

import javax.persistence.*;

@Entity(name = "messages")
public class SQL_messages
{

    @Id
    @GeneratedValue
    private Long Id;


    //private Long chatID;
    @ManyToOne
    @JoinColumn(name = "chatID")

    private SQL_user user;

    public SQL_user getUser() {
        return user;
    }

    public void setUser(SQL_user user) {
        this.user = user;
    }

    private String userText;

    private String botText;

    public Long getChatID() {
        return user.getChatID();
    }

//    public void setChatID(Long chatID) {
//        this.user.setChatID(chatID);
//    }

    public String getUserText() {
        return userText;
    }

    public void setUserText(String userText) {
        this.userText = userText;
    }

    public String getBotText() {
        return botText;
    }

    public void setBotText(String botText) {
        this.botText = botText;
    }

    @Override
    public String toString() {
        return "SQL_messages{" +
                "chatID=" + user.getChatID() +
                ", userText='" + userText + '\'' +
                ", botText='" + botText + '\'' +
                '}';
    }
}
