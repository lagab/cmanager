package com.lagab.cmanager.web.rest;

import com.lagab.cmanager.CmanagerApp;

import com.lagab.cmanager.domain.ContactList;
import com.lagab.cmanager.domain.Contact;
import com.lagab.cmanager.domain.ContactFolder;
import com.lagab.cmanager.repository.ContactListRepository;
import com.lagab.cmanager.service.ContactListService;
import com.lagab.cmanager.web.rest.errors.ExceptionTranslator;
import com.lagab.cmanager.service.dto.ContactListCriteria;
import com.lagab.cmanager.service.ContactListQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static com.lagab.cmanager.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ContactListResource REST controller.
 *
 * @see ContactListResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CmanagerApp.class)
public class ContactListResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private ContactListRepository contactListRepository;

    

    @Autowired
    private ContactListService contactListService;

    @Autowired
    private ContactListQueryService contactListQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restContactListMockMvc;

    private ContactList contactList;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ContactListResource contactListResource = new ContactListResource(contactListService, contactListQueryService);
        this.restContactListMockMvc = MockMvcBuilders.standaloneSetup(contactListResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactList createEntity(EntityManager em) {
        ContactList contactList = new ContactList()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return contactList;
    }

    @Before
    public void initTest() {
        contactList = createEntity(em);
    }

    @Test
    @Transactional
    public void createContactList() throws Exception {
        int databaseSizeBeforeCreate = contactListRepository.findAll().size();

        // Create the ContactList
        restContactListMockMvc.perform(post("/api/contact-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactList)))
            .andExpect(status().isCreated());

        // Validate the ContactList in the database
        List<ContactList> contactListList = contactListRepository.findAll();
        assertThat(contactListList).hasSize(databaseSizeBeforeCreate + 1);
        ContactList testContactList = contactListList.get(contactListList.size() - 1);
        assertThat(testContactList.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContactList.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createContactListWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contactListRepository.findAll().size();

        // Create the ContactList with an existing ID
        contactList.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactListMockMvc.perform(post("/api/contact-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactList)))
            .andExpect(status().isBadRequest());

        // Validate the ContactList in the database
        List<ContactList> contactListList = contactListRepository.findAll();
        assertThat(contactListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactListRepository.findAll().size();
        // set the field null
        contactList.setName(null);

        // Create the ContactList, which fails.

        restContactListMockMvc.perform(post("/api/contact-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactList)))
            .andExpect(status().isBadRequest());

        List<ContactList> contactListList = contactListRepository.findAll();
        assertThat(contactListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllContactLists() throws Exception {
        // Initialize the database
        contactListRepository.saveAndFlush(contactList);

        // Get all the contactListList
        restContactListMockMvc.perform(get("/api/contact-lists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactList.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    

    @Test
    @Transactional
    public void getContactList() throws Exception {
        // Initialize the database
        contactListRepository.saveAndFlush(contactList);

        // Get the contactList
        restContactListMockMvc.perform(get("/api/contact-lists/{id}", contactList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contactList.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getAllContactListsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        contactListRepository.saveAndFlush(contactList);

        // Get all the contactListList where name equals to DEFAULT_NAME
        defaultContactListShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the contactListList where name equals to UPDATED_NAME
        defaultContactListShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllContactListsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        contactListRepository.saveAndFlush(contactList);

        // Get all the contactListList where name in DEFAULT_NAME or UPDATED_NAME
        defaultContactListShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the contactListList where name equals to UPDATED_NAME
        defaultContactListShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllContactListsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactListRepository.saveAndFlush(contactList);

        // Get all the contactListList where name is not null
        defaultContactListShouldBeFound("name.specified=true");

        // Get all the contactListList where name is null
        defaultContactListShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllContactListsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        contactListRepository.saveAndFlush(contactList);

        // Get all the contactListList where description equals to DEFAULT_DESCRIPTION
        defaultContactListShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the contactListList where description equals to UPDATED_DESCRIPTION
        defaultContactListShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllContactListsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        contactListRepository.saveAndFlush(contactList);

        // Get all the contactListList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultContactListShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the contactListList where description equals to UPDATED_DESCRIPTION
        defaultContactListShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllContactListsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactListRepository.saveAndFlush(contactList);

        // Get all the contactListList where description is not null
        defaultContactListShouldBeFound("description.specified=true");

        // Get all the contactListList where description is null
        defaultContactListShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllContactListsByContactsIsEqualToSomething() throws Exception {
        // Initialize the database
        Contact contacts = ContactResourceIntTest.createEntity(em);
        em.persist(contacts);
        em.flush();
        contactList.addContacts(contacts);
        contactListRepository.saveAndFlush(contactList);
        Long contactsId = contacts.getId();

        // Get all the contactListList where contacts equals to contactsId
        defaultContactListShouldBeFound("contactsId.equals=" + contactsId);

        // Get all the contactListList where contacts equals to contactsId + 1
        defaultContactListShouldNotBeFound("contactsId.equals=" + (contactsId + 1));
    }


    @Test
    @Transactional
    public void getAllContactListsByFolderIsEqualToSomething() throws Exception {
        // Initialize the database
        ContactFolder folder = ContactFolderResourceIntTest.createEntity(em);
        em.persist(folder);
        em.flush();
        contactList.setFolder(folder);
        contactListRepository.saveAndFlush(contactList);
        Long folderId = folder.getId();

        // Get all the contactListList where folder equals to folderId
        defaultContactListShouldBeFound("folderId.equals=" + folderId);

        // Get all the contactListList where folder equals to folderId + 1
        defaultContactListShouldNotBeFound("folderId.equals=" + (folderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultContactListShouldBeFound(String filter) throws Exception {
        restContactListMockMvc.perform(get("/api/contact-lists?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactList.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultContactListShouldNotBeFound(String filter) throws Exception {
        restContactListMockMvc.perform(get("/api/contact-lists?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingContactList() throws Exception {
        // Get the contactList
        restContactListMockMvc.perform(get("/api/contact-lists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContactList() throws Exception {
        // Initialize the database
        contactListService.save(contactList);

        int databaseSizeBeforeUpdate = contactListRepository.findAll().size();

        // Update the contactList
        ContactList updatedContactList = contactListRepository.findById(contactList.getId()).get();
        // Disconnect from session so that the updates on updatedContactList are not directly saved in db
        em.detach(updatedContactList);
        updatedContactList
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restContactListMockMvc.perform(put("/api/contact-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedContactList)))
            .andExpect(status().isOk());

        // Validate the ContactList in the database
        List<ContactList> contactListList = contactListRepository.findAll();
        assertThat(contactListList).hasSize(databaseSizeBeforeUpdate);
        ContactList testContactList = contactListList.get(contactListList.size() - 1);
        assertThat(testContactList.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContactList.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingContactList() throws Exception {
        int databaseSizeBeforeUpdate = contactListRepository.findAll().size();

        // Create the ContactList

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restContactListMockMvc.perform(put("/api/contact-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactList)))
            .andExpect(status().isBadRequest());

        // Validate the ContactList in the database
        List<ContactList> contactListList = contactListRepository.findAll();
        assertThat(contactListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteContactList() throws Exception {
        // Initialize the database
        contactListService.save(contactList);

        int databaseSizeBeforeDelete = contactListRepository.findAll().size();

        // Get the contactList
        restContactListMockMvc.perform(delete("/api/contact-lists/{id}", contactList.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ContactList> contactListList = contactListRepository.findAll();
        assertThat(contactListList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactList.class);
        ContactList contactList1 = new ContactList();
        contactList1.setId(1L);
        ContactList contactList2 = new ContactList();
        contactList2.setId(contactList1.getId());
        assertThat(contactList1).isEqualTo(contactList2);
        contactList2.setId(2L);
        assertThat(contactList1).isNotEqualTo(contactList2);
        contactList1.setId(null);
        assertThat(contactList1).isNotEqualTo(contactList2);
    }
}
