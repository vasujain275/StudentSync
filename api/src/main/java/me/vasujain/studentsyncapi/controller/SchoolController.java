package me.vasujain.studentsyncapi.controller;

import me.vasujain.studentsyncapi.dto.CreateSchoolDTO;
import me.vasujain.studentsyncapi.model.School;
import me.vasujain.studentsyncapi.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/school")
public class SchoolController {

    @Autowired
    private final SchoolService schoolService;

    public SchoolController(SchoolService schoolService){
        this.schoolService = schoolService;
    }

    @GetMapping
    public ResponseEntity<List<School>> getAllSchools() {
        List<School> schools = schoolService.getAllSchools();
        return ResponseEntity.ok(schools);
    }

    @PostMapping
    public ResponseEntity<School> createSchool(@RequestBody CreateSchoolDTO dto){
        School school= schoolService.createSchool(dto);
        return ResponseEntity.ok(school);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSchool(@RequestParam("id") UUID uuid) {
        schoolService.deleteSchool(uuid);
        return ResponseEntity.noContent().build();
    }


}
