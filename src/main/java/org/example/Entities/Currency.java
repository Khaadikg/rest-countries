package org.example.Entities;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class Currency {
    private String code;
    private String name;
    private String symbol;
}
