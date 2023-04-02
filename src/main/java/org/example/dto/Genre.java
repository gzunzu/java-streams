package org.example.dto;

import org.apache.commons.lang3.StringUtils;

public enum Genre {
    ACTION,
    ADVENTURE,
    ARCADE,
    CASINO,
    EDUCATIONAL,
    FIGHTING,
    HACK_AND_SLASH,
    HORROR,
    INDIE,
    LOCATION_BASED,
    PLATFORM,
    PUZZLE,
    RACING,
    ROLE_PLAYING,
    SANDBOX,
    SHOOTER,
    SIMULATION,
    SPORTS,
    STEALTH,
    STRATEGY,
    SURVIVAL,
    TRIVIA;

    @Override
    public String toString() {
        return StringUtils.capitalize(StringUtils.replace(this.name(), "_", " ").toLowerCase());
    }
}
