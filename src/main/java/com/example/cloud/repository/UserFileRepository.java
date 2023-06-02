package com.example.cloud.repository;

import com.example.cloud.model.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFileRepository extends JpaRepository<UserFile, Long> {

    @Query(value = "SELECT * FROM FILES WHERE USER = ?2 LIMIT ?1", nativeQuery = true)
    List<UserFile> findAllWithLimit(int limit, String user);

    UserFile findByFilenameAndUser(String filename, String user);

    void deleteByFilenameAndUser(String filename, String user);
}