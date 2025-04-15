package com.project.spring.model;

import lombok.Getter;

@Getter
public enum Major {
    SOFTWARE_ENGINEERING("Software Engineering"),
    ARTIFICIAL_INTELLIGENCE("Artificial Intelligence"),
    CYBERSECURITY("Cybersecurity");

    private final String displayName;

    Major(String displayName) {
        this.displayName = displayName;
    }

}
