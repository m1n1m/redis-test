package ru.funbox.links.service;

import ru.funbox.links.service.dto.VisitedDomainsDTO;
import ru.funbox.links.service.dto.VisitedLinksDTO;

import javax.validation.constraints.NotNull;

public interface VisitedLinksService {

    /**
     * Save a VisitedLinks.
     *
     * @param visitedLinksDTO the entity to save
     * @return the persisted entity
     */
    @NotNull VisitedLinksDTO save(@NotNull VisitedLinksDTO visitedLinksDTO);

    @NotNull VisitedDomainsDTO findByDateBetween(@NotNull Long from, @NotNull Long to);
}
