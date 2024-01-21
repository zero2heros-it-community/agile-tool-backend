package org.zero2hero.documentservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zero2hero.documentservice.entity.Message;
import org.zero2hero.documentservice.repository.MessageRepository;

import java.util.UUID;

@RestController()
@RequestMapping("/test")
public class TestController {
    @Autowired
    MessageRepository messageRepository;
    @GetMapping
    ResponseEntity<?>testWrite(){
        Message message= Message.builder()
                .id(UUID.randomUUID().toString())
                .message("test message")
                .build();
        messageRepository.save(message);
        return ResponseEntity.ok("OK");
    }
}
