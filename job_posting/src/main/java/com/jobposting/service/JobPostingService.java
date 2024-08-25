package com.jobposting.service;

import com.jobposting.entities.JobPosting;
import com.jobposting.exception.BadInputException;
import com.jobposting.exception.ResourceNotFoundException;
import com.jobposting.repositories.JobPostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class JobPostingService {

    @Autowired
    private JobPostingRepository repository;

    public JobPosting createJobPosting(JobPosting jobPosting) {
        if (jobPosting == null || jobPosting.getTitle() == null || jobPosting.getTitle().isEmpty()) {
            throw new BadInputException("Job posting or title cannot be null or empty");
        }
        return repository.save(jobPosting);
    }
    public JobPosting getJobPostingById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job posting not found with id: " + id));
    }
    public List<JobPosting> getAllJobPostings() {
        return repository.findAll();
    }

    public void deleteJobPosting(Long id) {
        JobPosting jobPosting = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job posting not found with id: " + id));
        repository.deleteById(id);
    }

    public JobPosting updateJobPosting(Long id, JobPosting jobPosting) {
        JobPosting existingJobPosting = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job posting not found with id: " + id));
        existingJobPosting.setTitle(jobPosting.getTitle());
        // Set other properties as needed
        return repository.save(existingJobPosting);
    }
    public List<JobPosting> searchJobPostings(String title, String location, String requiredSkills) {
        // Additional validation can be added here if necessary
        return repository.findByTitleContainingIgnoreCaseOrLocationContainingIgnoreCaseOrRequiredSkillsContainingIgnoreCase(
                title, location, requiredSkills);
    }
}