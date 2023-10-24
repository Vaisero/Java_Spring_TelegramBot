package io.project.SpringTelegramBot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "daily_domains")
public class SQL_daily_domains
{

    @Id
    @GeneratedValue
    private Long Id;

    private String domains;

    public String getDomains() {
        return domains;
    }

    public void setDomains(String domains) {
        this.domains = domains;
    }
}
