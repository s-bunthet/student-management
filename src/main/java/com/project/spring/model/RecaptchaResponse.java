package com.project.spring.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class RecaptchaResponse {
    private boolean success;

    @JsonProperty("error-codes")
    private List<String> errorCodes;
}
