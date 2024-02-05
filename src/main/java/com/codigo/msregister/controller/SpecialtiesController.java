package com.codigo.msregister.controller;

import com.codigo.appointmentslibrary.response.ResponseBase;
import com.codigo.msregister.aggregates.request.RequestSpecialties;
import com.codigo.msregister.service.SpecialtiesService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/specialties")
public class SpecialtiesController {
    private final SpecialtiesService specialtiesService;

    public SpecialtiesController(SpecialtiesService specialtiesService) {
        this.specialtiesService = specialtiesService;
    }

    @PostMapping
    public ResponseBase createSpecialty(@RequestBody RequestSpecialties requestSpecialties) {
        return specialtiesService.createSpecialties(requestSpecialties);
    }

    @GetMapping("{id}")
    public ResponseBase findOneSpecialtyById(@PathVariable int id) {
        return specialtiesService.findOneSpecialtyById(id);
    }

    @GetMapping
    public ResponseBase findAllSpecialties() {
        return specialtiesService.findAllSpecialties();
    }

    @PatchMapping("{id}")
    public ResponseBase updateSpecialty(@PathVariable int id, @RequestBody RequestSpecialties requestSpecialties) {
        return specialtiesService.updateSpecialty(id, requestSpecialties);
    }

    @DeleteMapping("{id}")
    public ResponseBase deleteSpecialty(@PathVariable int id) {
        return specialtiesService.deleteSpecialty(id);
    }
}
