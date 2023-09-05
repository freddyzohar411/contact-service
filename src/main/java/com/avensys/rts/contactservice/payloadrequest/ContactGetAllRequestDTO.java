package com.avensys.rts.contactservice.payloadrequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author: Koh He Xiang
 * This is the DTO class for the Contact request to get all contacts based on
 * entity id and entity type
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactGetAllRequestDTO {
    private int entityId;
    private String entityType;
}
