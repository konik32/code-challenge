package com.hsbc.twitter.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.time.Clock;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Data
@NoArgsConstructor
public class Tweet {

    @NotBlank
    @Length(max = 140)
    private String message;

    @Setter(PRIVATE)
    @ApiModelProperty(hidden = true)
    private LocalDateTime createDate = LocalDateTime.now(Clock.systemUTC());

    public Tweet(String message) {
        this.message = message;
    }


}
