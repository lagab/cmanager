package com.lagab.cmanager.web.rest;

import com.lagab.cmanager.CmanagerApp;

import com.lagab.cmanager.domain.ContactFolder;
import com.lagab.cmanager.domain.ContactList;
import com.lagab.cmanager.domain.Project;
import com.lagab.cmanager.repository.ContactFolderRepository;
import com.lagab.cmanager.service.ContactFolderService;
import com.lagab.cmanager.web.rest.errors.ExceptionTranslator;
import com.lagab.cmanager.service.dto.ContactFolderCriteria;
import com.lagab.cmanager.service.ContactFolderQueryService;

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
 * Test class for the ContactFolderResource REST controller.
 *
 * @see ContactFolderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CmanagerApp.class)
public class ContactFolderResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private ContactFolderRepository contactFolderRepository;

    

    @Autowired
    private ContactFolderService contactFolderService;

    @Autowired
    private ContactFolderQueryService contactFolderQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restContactFolderMockMvc;

    private ContactFolder contactFolder;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ContactFolderResource contactFolderResource = new ContactFolderResource(contactFolderService, contactFolderQueryService);
        this.restContactFolderMockMvc = MockMvcBuilders.standaloneSetup(contactFolderResource)
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
    public static ContactFolder createEntity(EntityManager em) {
        ContactFolder contactFolder = new ContactFolder()
            .name(DEFAULT_NAME);
        return contactFolder;
    }

    @Before
    public void initTest() {
        contactFolder = createEntity(em);
    }

    @Test
    @Transactional
    public void createContactFolder() throws Exception {
        int databaseSizeBeforeCreate = contactFolderRepository.findAll().size();

        // Create the ContactFolder
        restContactFolderMockMvc.perform(post("/api/contact-folders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactFolder)))
            .andExpect(status().isCreated());

        // Validate the ContactFolder in the database
        List<ContactFolder> contactFolderList = contactFolderRepository.findAll();
        assertThat(contactFolderList).hasSize(databaseSizeBeforeCreate + 1);
        ContactFolder testContactFolder = contactFolderList.get(contactFolderList.size() - 1);
        assertThat(testContactFolder.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createContactFolderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contactFolderRepository.findAll().size();

        // Create the ContactFolder with an existing ID
        contactFolder.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactFolderMockMvc.perform(post("/api/contact-folders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactFolder)))
            .andExpect(status().isBadRequest());

        // Validate the ContactFolder in the database
        List<ContactFolder> contactFolderList = contactFolderRepository.findAll();
        assertThat(contactFolderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactFolderRepository.findAll().size();
        // set the field null
        contactFolder.setName(null);

        // Create the ContactFolder, which fails.

        restContactFolderMockMvc.perform(post("/api/contact-folders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactFolder)))
            .andExpect(status().isBadRequest());

        List<ContactFolder> contactFolderList = contactFolderRepository.findAll();
        assertThat(contactFolderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllContactFolders() throws Exception {
        // Initialize the database
        contactFolderRepository.saveAndFlush(contactFolder);

        // Get all the contactFolderList
        restContactFolderMockMvc.perform(get("/api/contact-folders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactFolder.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    

    @Test
    @Transactional
    public void getContactFolder() throws Exception {
        // Initialize the database
        contactFolderRepository.saveAndFlush(contactFolder);

        // Get the contactFolder
        restContactFolderMockMvc.perform(get("/api/contact-folders/{id}", contactFolder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contactFolder.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllContactFoldersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        contactFolderRepository.saveAndFlush(contactFolder);

        // Get all the contactFolderList where name equals to DEFAULT_NAME
        defaultContactFolderShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the contactFolderList where name equals to UPDATED_NAME
        defaultContactFolderShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllContactFoldersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        contactFolderRepository.saveAndFlush(contactFolder);

        // Get all the contactFolderList where name in DEFAULT_NAME or UPDATED_NAME
        defaultContactFolderShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the contactFolderList where name equals to UPDATED_NAME
        defaultContactFolderShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllContactFoldersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactFolderRepository.saveAndFlush(contactFolder);

        // Get all the contactFolderList where name is not null
        defaultContactFolderShouldBeFound("name.specified=true");

        // Get all the contactFolderList where name is null
        defaultContactFolderShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllContactFoldersByListsIsEqualToSomething() throws Exception {
        // Initialize the database
        ContactList lists = ContactListResourceIntTest.createEntity(em);
        em.persist(lists);
        em.flush();
        contactFolder.addLists(lists);
        contactFolderRepository.saveAndFlush(contactFolder);
        Long listsId = lists.getId();

        // Get all the contactFolderList where lists equals to listsId
        defaultContactFolderShouldBeFound("listsId.equals=" + listsId);

        // Get all the contactFolderList where lists equals to listsId + 1
        defaultContactFolderShouldNotBeFound("listsId.equals=" + (listsId + 1));
    }


    @Test
    @Transactional
    public void getAllContactFoldersByProjectIsEqualToSomething() throws Exception {
        // Initialize the database
        Project project = ProjectResourceIntTest.createEntity(em);
        em.persist(project);
        em.flush();
        contactFolder.setProject(project);
        contactFolderRepository.saveAndFlush(contactFolder);
        Long projectId = project.getId();

        // Get all the contactFolderList where project equals to projectId
        defaultContactFolderShouldBeFound("projectId.equals=" + projectId);

        // Get all the contactFolderList where project equals to projectId + 1
        defaultContactFolderShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultContactFolderShouldBeFound(String filter) throws Exception {
        restContactFolderMockMvc.perform(get("/api/contact-folders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactFolder.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultContactFolderShouldNotBeFound(String filter) throws Exception {
        restContactFolderMockMvc.perform(get("/api/contact-folders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingContactFolder() throws Exception {
        // Get the contactFolder
        restContactFolderMockMvc.perform(get("/api/contact-folders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContactFolder() throws Exception {
        // Initialize the database
        contactFolderService.save(contactFolder);

        int databaseSizeBeforeUpdate = contactFolderRepository.findAll().size();

        // Update the contactFolder
        ContactFolder updatedContactFolder = contactFolderRepository.findById(contactFolder.getId()).get();
        // Disconnect from session so that the updates on updatedContactFolder are not directly saved in db
        em.detach(updatedContactFolder);
        updatedContactFolder
            .name(UPDATED_NAME);

        restContactFolderMockMvc.perform(put("/api/contact-folders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedContactFolder)))
            .andExpect(status().isOk());

        // Validate the ContactFolder in the database
        List<ContactFolder> contactFolderList = contactFolderRepository.findAll();
        assertThat(contactFolderList).hasSize(databaseSizeBeforeUpdate);
        ContactFolder testContactFolder = contactFolderList.get(contactFolderList.size() - 1);
        assertThat(testContactFolder.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingContactFolder() throws Exception {
        int databaseSizeBeforeUpdate = contactFolderRepository.findAll().size();

        // Create the ContactFolder

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restContactFolderMockMvc.perform(put("/api/contact-folders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactFolder)))
            .andExpect(status().isBadRequest());

        // Validate the ContactFolder in the database
        List<ContactFolder> contactFolderList = contactFolderRepository.findAll();
        assertThat(contactFolderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteContactFolder() throws Exception {
        // Initialize the database
        contactFolderService.save(contactFolder);

        int databaseSizeBeforeDelete = contactFolderRepository.findAll().size();

        // Get the contactFolder
        restContactFolderMockMvc.perform(delete("/api/contact-folders/{id}", contactFolder.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ContactFolder> contactFolderList = contactFolderRepository.findAll();
        assertThat(contactFolderList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactFolder.class);
        ContactFolder contactFolder1 = new ContactFolder();
        contactFolder1.setId(1L);
        ContactFolder contactFolder2 = new ContactFolder();
        contactFolder2.setId(contactFolder1.getId());
        assertThat(contactFolder1).isEqualTo(contactFolder2);
        contactFolder2.setId(2L);
        assertThat(contactFolder1).isNotEqualTo(contactFolder2);
        contactFolder1.setId(null);
        assertThat(contactFolder1).isNotEqualTo(contactFolder2);
    }
}
