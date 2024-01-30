package com.codigo.msregister.controller;

import com.codigo.msregister.aggregates.request.RequestPersons;
import com.codigo.msregister.aggregates.response.ResponseBase;
import com.codigo.msregister.service.PersonsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/persons")
public class PersonsController {
    private final PersonsService personsService;

    public PersonsController(PersonsService personsService) {
        this.personsService = personsService;
    }

    @PostMapping
    public ResponseBase createPerson(@RequestBody RequestPersons requestPersons) {
        return personsService.createPersons(requestPersons);
    }

    @GetMapping("{id}")
    public ResponseBase findOnePersonById(@PathVariable int id) {
        return personsService.findOnePersonById(id);
    }

    @GetMapping("/doc/{document}")
    public ResponseBase findOnePersonByDocument(@PathVariable String document) {
        return personsService.findOnePersonByDocument(document);
    }

    @GetMapping
    public ResponseBase findAll() {
        return personsService.findAllPersons();
    }

    @PatchMapping("{id}")
    public ResponseBase updatePerson(@PathVariable int id, @RequestBody RequestPersons requestPersons) {
        return personsService.updatePerson(id, requestPersons);
    }

    @DeleteMapping("{id}")
    public ResponseBase deletePerson(@PathVariable int id){
        return personsService.deletePerson(id);
    }
}
