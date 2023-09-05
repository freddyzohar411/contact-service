package com.avensys.rts.contactservice.repository;

import com.avensys.rts.contactservice.entity.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * author: Koh He Xiang
 * This is the repository class for the contact table in the database
 */
public interface ContactRepository extends JpaRepository<ContactEntity, Integer> {

    @Query("select c from ContactEntity c where c.entityId = ?1 and c.entityType = ?2")
    List<ContactEntity> findByEntityIdAndEntityType(int entityId, String entityType);
}
