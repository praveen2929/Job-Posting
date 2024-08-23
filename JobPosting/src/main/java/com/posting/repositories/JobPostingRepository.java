package com.posting.repositories;

import com.posting.entities.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    List<JobPosting> findByTitleOrLocationOrRequiredSkills(String title, String location, String skill);
}
