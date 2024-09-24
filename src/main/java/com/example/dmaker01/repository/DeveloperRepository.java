package com.example.dmaker01.repository;

import com.example.dmaker01.code.DeveloperStatusCode;
import com.example.dmaker01.entity.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeveloperRepository 
        extends JpaRepository<Developer, Long> {

    Optional<Developer> findByMemberId(String memberId);
    List<Developer> findByDeveloperStatusCodeEquals(DeveloperStatusCode developerStatusCode);
}