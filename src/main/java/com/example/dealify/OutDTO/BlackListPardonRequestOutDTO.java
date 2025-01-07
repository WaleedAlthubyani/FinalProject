package com.example.dealify.OutDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BlackListPardonRequestOutDTO {//Waleed

    private String customer;
    private String reason;
    private String status;
    private String vendor;
    private String response;

    private LocalDateTime requestDate;
    private LocalDateTime responseDate;
}
