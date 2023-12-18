package org.zero2hero.applicationservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Workspaces")
public class Workspace {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "workspace_id")
    private Long id;

    @Column(name = "workspace_name")
    private String name;


}
