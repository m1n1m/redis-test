package ru.funbox.links.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.funbox.links.service.VisitedLinksService;
import ru.funbox.links.service.dto.VisitedDomainsDTO;
import ru.funbox.links.service.dto.VisitedLinksDTO;
import ru.funbox.links.web.RestResponse;
import ru.funbox.links.web.rest.errors.BadRequestAlertException;

import javax.validation.constraints.NotNull;

@RestController
public class VisitedLinksResource {

    private final Logger log = LoggerFactory.getLogger(VisitedLinksResource.class);

    private final VisitedLinksService visitedLinksService;

    public VisitedLinksResource(VisitedLinksService visitedLinksService) {
        this.visitedLinksService = visitedLinksService;
    }

    /**
     * POST  /visited_links : Save information about visited links
     *
     * request body is json contains "links" - array of strings
     *
     * @return the json with status: ok
     */
    @PostMapping("/visited_links")
    public ResponseEntity<RestResponse> saveVisitedLinks(@RequestBody VisitedLinksDTO visitedLinksDTO) {
        log.debug("REST request to save visited links : {}", visitedLinksDTO);

        if (visitedLinksDTO.getLinks() == null || visitedLinksDTO.getLinks().length == 0) {
            throw new BadRequestAlertException("Field links is empty", "VisitedLinks", "field.links.empty");
        }

        visitedLinksService.save(visitedLinksDTO);
        return ResponseEntity.ok().body(new RestResponse("ok"));
    }

    /**
     * GET  /visited_domains : Get visited domains between two date
     *
     * @param from - Long timestamp - begin date
     * @param to - Long timestamp - end date
     *
     * @return VisitedDomainsDTO
     */
    @GetMapping("/visited_domains")
    public ResponseEntity<VisitedDomainsDTO> getVisitedDomain(
            @RequestParam @NotNull Long from, @RequestParam @NotNull Long to
    ) {

        log.debug("REST request to get visited domains : from {} to {}", from, to);

        VisitedDomainsDTO visitedDomainsDTO = visitedLinksService.findByDateBetween(from, to);
        visitedDomainsDTO.setStatus("ok");

        return ResponseEntity.ok().body(visitedDomainsDTO);
    }
}
