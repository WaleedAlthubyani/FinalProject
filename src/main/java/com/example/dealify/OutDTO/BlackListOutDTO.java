package com.example.dealify.OutDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class BlackListOutDTO {

    private String customer;

    private String reason;

    private LocalDate addDate;
}
