package com.testing_exam_webapp.controller;

import com.testing_exam_webapp.dto.NurseRequest;
import com.testing_exam_webapp.model.mysql.Nurse;
import com.testing_exam_webapp.service.NurseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/nurses")
public class NurseController {

    private final NurseService nurseService;

    public NurseController(NurseService nurseService) {
        this.nurseService = nurseService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<Nurse>> getNurses() {
        List<Nurse> nurses = nurseService.getNurses();
        if(nurses.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(nurses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Nurse> getNurseById(@PathVariable UUID id) {
        Nurse nurse = nurseService.getNurseById(id);
        return new ResponseEntity<>(nurse, HttpStatus.OK);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Nurse> createNurse(@Valid @RequestBody NurseRequest request) {
        Nurse nurse = nurseService.createNurse(request);
        return new ResponseEntity<>(nurse, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Nurse> updateNurse(@PathVariable UUID id, @Valid @RequestBody NurseRequest request) {
        Nurse nurse = nurseService.updateNurse(id, request);
        return new ResponseEntity<>(nurse, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteNurse(@PathVariable UUID id) {
        nurseService.deleteNurse(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

