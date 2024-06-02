package org.zero2hero.applicationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zero2hero.applicationservice.entity.Workspace;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    Optional<Workspace> findByName(String name);
    List<Workspace> findByUsername(String username);
}
