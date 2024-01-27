package com.codigo.msregister.controller;

import com.codigo.msregister.aggregates.request.RequestPersons;
import com.codigo.msregister.aggregates.response.ResponseBase;
import com.codigo.msregister.service.PersonsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
