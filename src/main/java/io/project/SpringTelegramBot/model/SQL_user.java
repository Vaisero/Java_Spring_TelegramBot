package io.project.SpringTelegramBot.model;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity(name = "users")
public class SQL_user
{
    @Id
    private Long chatID;

    private String userName;

    private String userFirstName;

    private String userSecondName;

    private Timestamp last_message_at;


    public Long getChatID() {
        return chatID;
    }

    public void setChatID(Long chatID) {
        this.chatID = chatID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String getUserFirstName) {
        this.userFirstName = getUserFirstName;
    }

    public String getUserSecondName() {
        return userSecondName;
    }

    public void setUserSecondName(String userSecondName) {
        this.userSecondName = userSecondName;
    }

    public Timestamp getLast_message_at() {
        return last_message_at;
    }

    public void setLast_message_at(Timestamp last_message_at) {
        this.last_message_at = last_message_at;
    }

    @Override
    public String toString() {
        return "SQL_user{" +
                "chatID=" + chatID +
                ", userName='" + userName + '\'' +
                ", userFirstName='" + userFirstName + '\'' +
                ", userSecondName='" + userSecondName + '\'' +
                ", last_message_at=" + last_message_at +
                '}';
    }
}
