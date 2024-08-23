package com.posting.service;


import com.posting.entities.JobPosting;
import com.posting.exception.BadRequestException;
import com.posting.exception.ResourceNotFoundException;
import com.posting.repositories.JobPostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobPostingService {

    @Autowired
    private JobPostingRepository jobPostingRepository;

    public JobPosting createJobPosting(JobPosting jobPosting) {
        if (jobPosting.getTitle() == null || jobPosting.getTitle().isEmpty()) {
            throw new BadRequestException("Job title cannot be empty");
        }
        return jobPostingRepository.save(jobPosting);
    }

    public List<JobPosting> getAllJobPostings() {
        return jobPostingRepository.findAll();
    }

    public Optional<JobPosting> getJobPostingById(Long id) {
        return Optional.ofNullable(jobPostingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job Posting not found with id " + id)));
    }

    public JobPosting updateJobPosting(Long id, JobPosting jobPostingDetails) {
        JobPosting jobPosting = jobPostingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job Posting not found with id " + id));

        jobPosting.setTitle(jobPostingDetails.getTitle());
        jobPosting.setDescription(jobPostingDetails.getDescription());
        jobPosting.setLocation(jobPostingDetails.getLocation());
        jobPosting.setCompany(jobPostingDetails.getCompany());
        jobPosting.setSalaryRange(jobPostingDetails.getSalaryRange());
        jobPosting.setApplicationDeadline(jobPostingDetails.getApplicationDeadline());
        jobPosting.setRequiredSkills(jobPostingDetails.getRequiredSkills());

        return jobPostingRepository.save(jobPosting);
    }

    public void deleteJobPosting(Long id) {
        if (!jobPostingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Job Posting not found with id " + id);
        }
        jobPostingRepository.deleteById(id);
    }

    public List<JobPosting> searchJobPostings(String searchKeyword) {
        if (searchKeyword == null || searchKeyword.trim().isEmpty()) {
            throw new BadRequestException("Search keyword cannot be empty");
        }
        return jobPostingRepository.findByTitleOrLocationOrRequiredSkills(searchKeyword, searchKeyword, searchKeyword);
    }
}