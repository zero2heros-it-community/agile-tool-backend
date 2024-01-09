package org.zero2hero.applicationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created By Hasan-Murat Kücüközdemir
 * Date : 29.12.2023
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardUpdateDto {
    private String name;
    private String workSpaceId;
}
