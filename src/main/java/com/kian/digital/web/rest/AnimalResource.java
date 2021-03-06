package com.kian.digital.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kian.digital.service.AnimalService;
import com.kian.digital.web.rest.errors.BadRequestAlertException;
import com.kian.digital.web.rest.util.HeaderUtil;
import com.kian.digital.service.dto.AnimalDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Animal.
 */
@RestController
@RequestMapping("/api")
public class AnimalResource {

    private final Logger log = LoggerFactory.getLogger(AnimalResource.class);

    private static final String ENTITY_NAME = "animal";

    private AnimalService animalService;

    public AnimalResource(AnimalService animalService) {
        this.animalService = animalService;
    }

    /**
     * POST  /animals : Create a new animal.
     *
     * @param animalDTO the animalDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new animalDTO, or with status 400 (Bad Request) if the animal has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/animals")
    @Timed
    public ResponseEntity<AnimalDTO> createAnimal(@RequestBody AnimalDTO animalDTO) throws URISyntaxException {
        log.debug("REST request to save Animal : {}", animalDTO);
        if (animalDTO.getId() != null) {
            throw new BadRequestAlertException("A new animal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnimalDTO result = animalService.save(animalDTO);
        return ResponseEntity.created(new URI("/api/animals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /animals : Updates an existing animal.
     *
     * @param animalDTO the animalDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated animalDTO,
     * or with status 400 (Bad Request) if the animalDTO is not valid,
     * or with status 500 (Internal Server Error) if the animalDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/animals")
    @Timed
    public ResponseEntity<AnimalDTO> updateAnimal(@RequestBody AnimalDTO animalDTO) throws URISyntaxException {
        log.debug("REST request to update Animal : {}", animalDTO);
        if (animalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AnimalDTO result = animalService.save(animalDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, animalDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /animals : get all the animals.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of animals in body
     */
    @GetMapping("/animals")
    @Timed
    public List<AnimalDTO> getAllAnimals() {
        log.debug("REST request to get all Animals");
        return animalService.findAll();
    }

    /**
     * GET  /animals/:id : get the "id" animal.
     *
     * @param id the id of the animalDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the animalDTO, or with status 404 (Not Found)
     */
    @GetMapping("/animals/{id}")
    @Timed
    public ResponseEntity<AnimalDTO> getAnimal(@PathVariable Long id) {
        log.debug("REST request to get Animal : {}", id);
        Optional<AnimalDTO> animalDTO = animalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(animalDTO);
    }

    /**
     * DELETE  /animals/:id : delete the "id" animal.
     *
     * @param id the id of the animalDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/animals/{id}")
    @Timed
    public ResponseEntity<Void> deleteAnimal(@PathVariable Long id) {
        log.debug("REST request to delete Animal : {}", id);
        animalService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
