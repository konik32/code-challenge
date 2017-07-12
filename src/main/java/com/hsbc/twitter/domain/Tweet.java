package com.hsbc.twitter.domain;

import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import java.time.Clock;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Data
@NoArgsConstructor
public class Tweet {

    @NotBlank
    @Max(140)
    private String message;

    @Setter(PRIVATE)
    private LocalDateTime createDate = LocalDateTime.now(Clock.systemUTC());

    public Tweet(String message) {
        this.message = message;
    }


}
