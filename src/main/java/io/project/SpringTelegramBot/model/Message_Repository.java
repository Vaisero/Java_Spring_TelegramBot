package io.project.SpringTelegramBot.model;

import org.springframework.data.repository.CrudRepository;

public interface Message_Repository extends CrudRepository<SQL_messages, Long>
{
}
