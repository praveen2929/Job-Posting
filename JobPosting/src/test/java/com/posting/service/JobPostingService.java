package com.posting.service;

import com.posting.entities.JobPosting;
import com.posting.exception.BadRequestException;
import com.posting.exception.ResourceNotFoundException;
import com.posting.repositories.JobPostingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class JobPostingServiceTest {

    @Mock
    private JobPostingRepository jobPostingRepository;

    @InjectMocks
    private JobPostingService jobPostingService;

    @Test
    void testCreateJobPosting() {
        JobPosting jobPosting = new JobPosting();
        jobPosting.setTitle("Java Developer");

        when(jobPostingRepository.save(any(JobPosting.class))).thenReturn(jobPosting);

        JobPosting createdJobPosting = jobPostingService.createJobPosting(jobPosting);

        assertEquals("Java Developer", createdJobPosting.getTitle());
        verify(jobPostingRepository, times(1)).save(any(JobPosting.class));
    }

    @Test
    void testGetAllJobPostings() {
        JobPosting jobPosting1 = new JobPosting();
        jobPosting1.setTitle("Java Developer");

        JobPosting jobPosting2 = new JobPosting();
        jobPosting2.setTitle("Python Developer");

        when(jobPostingRepository.findAll()).thenReturn(Arrays.asList(jobPosting1, jobPosting2));

        List<JobPosting> jobPostings = jobPostingService.getAllJobPostings();

        assertEquals(2, jobPostings.size());
        assertEquals("Java Developer", jobPostings.get(0).getTitle());
        assertEquals("Python Developer", jobPostings.get(1).getTitle());
        verify(jobPostingRepository, times(1)).findAll();
    }

    @Test
    void testGetJobPostingById() {
        JobPosting jobPosting = new JobPosting();
        jobPosting.setId(1L);
        jobPosting.setTitle("Java Developer");

        when(jobPostingRepository.findById(1L)).thenReturn(Optional.of(jobPosting));

        Optional<JobPosting> foundJobPosting = jobPostingService.getJobPostingById(1L);

        assertNotNull(foundJobPosting);
        assertEquals("Java Developer", foundJobPosting.get().getTitle());
        verify(jobPostingRepository, times(1)).findById(1L);
    }

    @Test
    void testGetJobPostingById_NotFound() {
        when(jobPostingRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            jobPostingService.getJobPostingById(1L);
        });

        assertEquals("Job Posting not found with id 1", exception.getMessage());
        verify(jobPostingRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateJobPosting() {
        JobPosting existingJobPosting = new JobPosting();
        existingJobPosting.setId(1L);
        existingJobPosting.setTitle("Java Developer");

        JobPosting updatedJobPosting = new JobPosting();
        updatedJobPosting.setTitle("Senior Java Developer");

        when(jobPostingRepository.findById(1L)).thenReturn(Optional.of(existingJobPosting));
        when(jobPostingRepository.save(any(JobPosting.class))).thenReturn(updatedJobPosting);

        JobPosting result = jobPostingService.updateJobPosting(1L, updatedJobPosting);

        assertEquals("Senior Java Developer", result.getTitle());
        verify(jobPostingRepository, times(1)).findById(1L);
        verify(jobPostingRepository, times(1)).save(any(JobPosting.class));
    }

    @Test
    void testUpdateJobPosting_NotFound() {
        JobPosting updatedJobPosting = new JobPosting();
        updatedJobPosting.setTitle("Senior Java Developer");

        when(jobPostingRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            jobPostingService.updateJobPosting(1L, updatedJobPosting);
        });

        assertEquals("Job Posting not found with id 1", exception.getMessage());
        verify(jobPostingRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteJobPosting() {
        when(jobPostingRepository.existsById(1L)).thenReturn(true);

        jobPostingService.deleteJobPosting(1L);

        verify(jobPostingRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteJobPosting_NotFound() {
        when(jobPostingRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            jobPostingService.deleteJobPosting(1L);
        });

        assertEquals("Job Posting not found with id 1", exception.getMessage());
        verify(jobPostingRepository, times(0)).deleteById(1L);
    }

    @Test
    void testSearchJobPostings() {
        JobPosting jobPosting = new JobPosting();
        jobPosting.setTitle("Java Developer");

        when(jobPostingRepository.findByTitleOrLocationOrRequiredSkills(
                "Java Developer", "Bangaluru", "Java"))
                .thenReturn(Arrays.asList(jobPosting));

        List<JobPosting> jobPostings = jobPostingService.searchJobPostings("Java");

        assertEquals(1, jobPostings.size());
        assertEquals("Java Developer", jobPostings.get(0).getTitle());
        verify(jobPostingRepository, times(1))
                .findByTitleOrLocationOrRequiredSkills("python", "Mumbai", "python");
    }

    @Test
    void testSearchJobPostings_EmptyKeyword() {
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            jobPostingService.searchJobPostings("");
        });

        assertEquals("Search keyword cannot be empty", exception.getMessage());
        verify(jobPostingRepository, times(0))
                .findByTitleOrLocationOrRequiredSkills(anyString(), anyString(), anyString());
    }
}