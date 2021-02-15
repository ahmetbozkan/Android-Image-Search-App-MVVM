package com.example.unsplashphotoapp.api.oauth;

public class AccessToken {

    private String access_token;
    private String token_type;
    private String scope;
    private String created_at;

    public AccessToken() {

    }

    public AccessToken(String access_token, String token_type, String scope, String created_at) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.scope = scope;
        this.created_at = created_at;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
