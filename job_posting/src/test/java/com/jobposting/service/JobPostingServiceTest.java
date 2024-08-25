package com.jobposting.service;

import com.jobposting.entities.JobPosting;
import com.jobposting.exception.ResourceNotFoundException;
import com.jobposting.repositories.JobPostingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class JobPostingServiceTest {

    @Mock
    private JobPostingRepository repository;

    @InjectMocks
    private JobPostingService service;

    private JobPosting jobPosting;

    @BeforeEach
    public void setUp() {
        jobPosting = new JobPosting();
        jobPosting.setId(1L);
        jobPosting.setTitle("Software Engineer");
        jobPosting.setDescription("Maintain software applications.");
        jobPosting.setLocation("India");
        jobPosting.setCompany("Google");
        jobPosting.setSalaryRange("70,000 - 90,000");
        jobPosting.setRequiredSkills(List.of("Java", "Spring Boot", "SQL"));
    }

    @Test
    public void testCreateJobPosting() {
        when(repository.save(any(JobPosting.class))).thenReturn(jobPosting);

        JobPosting createdJobPosting = service.createJobPosting(jobPosting);

        assertNotNull(createdJobPosting);
        assertEquals(jobPosting.getTitle(), createdJobPosting.getTitle());
        verify(repository).save(jobPosting);
    }

    @Test
    public void testGetJobPostingById() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(jobPosting));

        JobPosting foundJobPosting = service.getJobPostingById(1L);

        assertNotNull(foundJobPosting);
        assertEquals(jobPosting.getTitle(), foundJobPosting.getTitle());
        verify(repository).findById(1L);
    }

    @Test
    public void testUpdateJobPosting() {
        when(repository.findById(1L)).thenReturn(Optional.of(jobPosting));
        when(repository.save(any(JobPosting.class))).thenReturn(jobPosting);

        jobPosting.setTitle("Senior Software Engineer");
        JobPosting updatedJobPosting = service.updateJobPosting(1L, jobPosting);

        assertNotNull(updatedJobPosting);
        assertEquals("Senior Software Engineer", updatedJobPosting.getTitle());
        verify(repository).save(jobPosting);
    }

    @Test
    public void testDeleteJobPosting() {
        when(repository.findById(1L)).thenReturn(Optional.of(jobPosting));

        service.deleteJobPosting(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    public void testGetJobPostingById_NotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.getJobPostingById(1L);
        });

        assertEquals("Job posting not found with id: 1", exception.getMessage());
    }
}
