package com.jobposting.controller;

import com.jobposting.entities.JobPosting;
import com.jobposting.exception.ResourceNotFoundException;
import com.jobposting.service.JobPostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/jobpostings")
public class JobPostingController {

    @Autowired
    private JobPostingService service;

    @PostMapping
    public ResponseEntity<JobPosting> createJobPosting(@RequestBody JobPosting jobPosting) {
        JobPosting createdJobPosting = service.createJobPosting(jobPosting);
        return new ResponseEntity<>(createdJobPosting, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobPosting> getJobPostingById(@PathVariable Long id) {
        try {
            JobPosting jobPosting = service.getJobPostingById(id);
            return ResponseEntity.ok(jobPosting);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping
    public ResponseEntity<List<JobPosting>> getAllJobPostings() {
        List<JobPosting> jobPostings = service.getAllJobPostings();
        return ResponseEntity.ok(jobPostings);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobPosting> updateJobPosting(@PathVariable Long id, @RequestBody JobPosting jobPosting) {
        JobPosting updatedJobPosting = service.updateJobPosting(id, jobPosting);
        return ResponseEntity.ok(updatedJobPosting);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobPosting(@PathVariable Long id) {
        service.deleteJobPosting(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<JobPosting>> searchJobPostings(
            @RequestParam String title,
            @RequestParam String location,
            @RequestParam String requiredSkills) {
        List<JobPosting> jobPostings = service.searchJobPostings(title, location, requiredSkills);
        return ResponseEntity.ok(jobPostings);
    }
}