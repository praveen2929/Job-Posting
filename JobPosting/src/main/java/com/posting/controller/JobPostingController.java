package com.posting.controller;


import com.posting.entities.JobPosting;
import com.posting.service.JobPostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-postings")
public class JobPostingController {

    @Autowired
    private JobPostingService jobPostingService;

    @PostMapping
    public ResponseEntity<JobPosting> createJobPosting(@RequestBody JobPosting jobPosting) {
        JobPosting createdJobPosting = jobPostingService.createJobPosting(jobPosting);
        return ResponseEntity.ok(createdJobPosting);
    }

    @GetMapping
    public ResponseEntity<List<JobPosting>> getAllJobPostings() {
        return ResponseEntity.ok(jobPostingService.getAllJobPostings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobPosting> getJobPostingById(@PathVariable Long id) {
        return jobPostingService.getJobPostingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobPosting> updateJobPosting(@PathVariable Long id, @RequestBody JobPosting jobPostingDetails) {
        return ResponseEntity.ok(jobPostingService.updateJobPosting(id, jobPostingDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobPosting(@PathVariable Long id) {
        jobPostingService.deleteJobPosting(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<JobPosting>> searchJobPostings(@RequestParam String keyword) {
        List<JobPosting> jobPostings = jobPostingService.searchJobPostings(keyword);
        return ResponseEntity.ok(jobPostings);
    }
}