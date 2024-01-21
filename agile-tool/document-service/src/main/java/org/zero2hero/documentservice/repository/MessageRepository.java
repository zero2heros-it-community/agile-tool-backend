package org.zero2hero.documentservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.zero2hero.documentservice.entity.Message;

public interface MessageRepository extends MongoRepository<Message,String> {
}
