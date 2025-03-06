package com.example.tradeversioning.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.Map;

@Document(collection = "trades")
@Builder
public record Trade(
        @Id String id, 
        String tradeId, 
        int version, 
        String status, 
        Instant timestamp, 
        Map<String, Object> tradeData) {}
