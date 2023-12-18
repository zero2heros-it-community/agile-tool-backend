package org.zero2hero.applicationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zero2hero.applicationservice.entity.Workspace;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
}
