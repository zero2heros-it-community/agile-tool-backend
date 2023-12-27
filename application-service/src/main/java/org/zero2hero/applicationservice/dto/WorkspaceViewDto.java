package org.zero2hero.applicationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zero2hero.applicationservice.entity.Workspace;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceViewDto {

    private String id;
    private String name;

    public static WorkspaceViewDto of(Workspace workspace) {
        return new WorkspaceViewDto(workspace.getId().toString(), workspace.getName());
    }
}