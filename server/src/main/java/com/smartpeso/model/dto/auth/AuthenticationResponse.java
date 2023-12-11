package com.smartpeso.model.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartpeso.model.User;

public record AuthenticationResponse(String accessToken) {
}
