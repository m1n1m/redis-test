package ru.funbox.links.service.dto;

import ru.funbox.links.web.RestResponse;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class VisitedDomainsDTO extends RestResponse implements Serializable {

    private String[] domains;
    private long timestamp;

    public VisitedDomainsDTO() {
        super();
        this.domains = new String[]{};
        this.timestamp = new Date().getTime();
    }

    public VisitedDomainsDTO(String[] domains) {
        this.domains = domains;
        this.timestamp = new Date().getTime();
    }

    public VisitedDomainsDTO(String[] domains, long timestamp) {
        this.domains = domains;
        this.timestamp = timestamp;
    }

    public VisitedDomainsDTO(Set<String> domains) {
        String[] array = new String[domains.size()];
        domains.toArray(array);
        this.domains = array;
        this.timestamp = new Date().getTime();
    }

    public VisitedDomainsDTO(Set<String> domains, long timestamp) {
        String[] array = new String[domains.size()];
        domains.toArray(array);
        this.domains = array;
        this.timestamp = timestamp;
    }


    public String[] getDomains() {
        return domains;
    }

    public VisitedDomainsDTO setDomains(String[] domains) {
        this.domains = domains;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public VisitedDomainsDTO setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
