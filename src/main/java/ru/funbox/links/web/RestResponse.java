package ru.funbox.links.web;

import com.fasterxml.jackson.annotation.JsonInclude;

public class RestResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String status;

    public RestResponse() {
    }

    public RestResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public RestResponse setStatus(String status) {
        this.status = status;
        return this;
    }
}
