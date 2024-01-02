package com.avensys.rts.contactservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avensys.rts.contactservice.entity.ContactEntity;

public interface ContactRepository extends JpaRepository<ContactEntity, Long> {
	List<ContactEntity> findByEntityTypeAndEntityId(String entityType, Integer entityId);
}
