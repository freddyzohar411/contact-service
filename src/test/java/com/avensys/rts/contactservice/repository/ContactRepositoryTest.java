package com.avensys.rts.contactservice.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.avensys.rts.contactservice.entity.ContactEntity;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContactRepositoryTest {

	@Autowired
	ContactRepository contactRepository;

	ContactEntity contactEntity;

	@BeforeEach
	void setUp() {
		contactEntity = new ContactEntity(1L, "title", "firstName", "lastName", 1L, "entityType", 1L, 1L);
	}

	@AfterEach
	void tearDown() throws Exception {
		contactRepository.deleteAll();
		contactEntity = null;
	}

	@Test
	void testFindByEntityTypeAndEntityId() {
		List<ContactEntity> contactList = contactRepository.findByEntityTypeAndEntityId("entityType", 1);
		assertNotNull(contactList);
	}

}
