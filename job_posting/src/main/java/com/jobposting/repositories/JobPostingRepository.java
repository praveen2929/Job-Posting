package com.jobposting.repositories;

;
import com.jobposting.entities.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    List<JobPosting> findByTitleContainingIgnoreCaseOrLocationContainingIgnoreCaseOrRequiredSkillsContainingIgnoreCase(
            String title, String location, String requiredSkills);
}