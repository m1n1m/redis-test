package ru.funbox.links.service.dto;

import java.io.Serializable;

public class VisitedLinksDTO implements Serializable {

    private String[] links;

    public VisitedLinksDTO() {
        this.links = new String[0];
    }

    public VisitedLinksDTO(String[] links) {
        this.links = links;
    }

    public String[] getLinks() {
        return links;
    }

    public VisitedLinksDTO setLinks(String[] links) {
        this.links = links;
        return this;
    }
}
