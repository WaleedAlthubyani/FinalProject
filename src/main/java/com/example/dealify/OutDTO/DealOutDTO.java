package com.example.dealify.OutDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DealOutDTO { //Waleed
    private Integer currentParticipants;
    private Integer participantsLimit;
    private String status;
    private Integer quantity;
    private LocalDateTime startedAt;
    private LocalDateTime endsAt;
}