package com.avensys.rts.contactservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avensys.rts.contactservice.entity.ContactNewEntity;

public interface ContactNewRepository extends JpaRepository<ContactNewEntity, Long> {
	List<ContactNewEntity> findByEntityTypeAndEntityId(String entityType, Integer entityId);
}
