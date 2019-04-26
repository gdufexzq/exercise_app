package com.gdufe.exercise_app.entity;

import lombok.Data;

@Data
public class TokenInfo {

    private String token;
    private String open_id;
    private String session_key;

}
