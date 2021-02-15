package com.example.unsplashphotoapp.api.unsplash;

import java.util.List;

public class SearchResponse {

    private List<UnsplashResponse> results;

    public SearchResponse(List<UnsplashResponse> results) {
        this.results = results;
    }

    public List<UnsplashResponse> getResults() {
        return results;
    }

    public void setResults(List<UnsplashResponse> results) {
        this.results = results;
    }
}
