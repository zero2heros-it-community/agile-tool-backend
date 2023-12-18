package org.zero2hero.documentservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document( "message")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Message {
    @Id
    String id;
    String message;
}
