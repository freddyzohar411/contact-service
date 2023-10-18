package com.avensys.rts.contactservice.repository;

import com.avensys.rts.contactservice.entity.ContactNewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactNewRepository extends JpaRepository<ContactNewEntity, Integer> {
    List<ContactNewEntity> findByEntityTypeAndEntityId(String entityType, Integer entityId);
}
