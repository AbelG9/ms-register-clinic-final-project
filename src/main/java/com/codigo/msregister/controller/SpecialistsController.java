package com.codigo.msregister.controller;

import com.codigo.msregister.aggregates.request.RequestSpecialists;
import com.codigo.msregister.aggregates.response.ResponseBase;
import com.codigo.msregister.service.SpecialistsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/specialists")
public class SpecialistsController {
    private final SpecialistsService specialistsService;

    public SpecialistsController(SpecialistsService specialistsService) {
        this.specialistsService = specialistsService;
    }

    @PostMapping
    public ResponseBase createSpecialist(@RequestBody RequestSpecialists requestSpecialists) {
        return specialistsService.createSpecialists(requestSpecialists);
    }

    @GetMapping("{id}")
    public ResponseBase findOneSpecialistById(@PathVariable int id) {
        return specialistsService.findOneSpecialistById(id);
    }
}
