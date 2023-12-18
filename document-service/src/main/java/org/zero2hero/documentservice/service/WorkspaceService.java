package org.zero2hero.documentservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.zero2hero.documentservice.entity.Message;
import org.zero2hero.documentservice.repository.MessageRepository;

import java.util.UUID;

@Service
@EnableKafka
public class WorkspaceService {
@Autowired
    MessageRepository messageRepository;
    @KafkaListener(topics = "first_topic", groupId = "group_id")
    public void handleIncomingWorkspace(String workspace) {
        String id = UUID.randomUUID().toString();
        Message message = Message.builder()
                .id(id)
                .message(workspace)
                .build();
        messageRepository.save(message);
        //writes workspaces
        System.out.println("Workspace-->" + workspace);
    }
}
