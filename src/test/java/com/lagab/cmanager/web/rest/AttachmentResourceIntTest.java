package com.lagab.cmanager.web.rest;

import com.lagab.cmanager.CmanagerApp;

import com.lagab.cmanager.domain.Attachment;
import com.lagab.cmanager.domain.Contract;
import com.lagab.cmanager.repository.AttachmentRepository;
import com.lagab.cmanager.service.AttachmentService;
import com.lagab.cmanager.web.rest.errors.ExceptionTranslator;
import com.lagab.cmanager.service.dto.AttachmentCriteria;
import com.lagab.cmanager.service.AttachmentQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static com.lagab.cmanager.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AttachmentResource REST controller.
 *
 * @see AttachmentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CmanagerApp.class)
public class AttachmentResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_FILENAME = "AAAAAAAAAA";
    private static final String UPDATED_FILENAME = "BBBBBBBBBB";

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final Integer DEFAULT_DOWNLOADS = 1;
    private static final Integer UPDATED_DOWNLOADS = 2;

    private static final Integer DEFAULT_SIZE = 1;
    private static final Integer UPDATED_SIZE = 2;

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    @Autowired
    private AttachmentRepository attachmentRepository;

    

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private AttachmentQueryService attachmentQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAttachmentMockMvc;

    private Attachment attachment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AttachmentResource attachmentResource = new AttachmentResource(attachmentService, attachmentQueryService);
        this.restAttachmentMockMvc = MockMvcBuilders.standaloneSetup(attachmentResource)
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
    public static Attachment createEntity(EntityManager em) {
        Attachment attachment = new Attachment()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .contentType(DEFAULT_CONTENT_TYPE)
            .filename(DEFAULT_FILENAME)
            .path(DEFAULT_PATH)
            .downloads(DEFAULT_DOWNLOADS)
            .size(DEFAULT_SIZE)
            .createdAt(DEFAULT_CREATED_AT)
            .createdBy(DEFAULT_CREATED_BY);
        return attachment;
    }

    @Before
    public void initTest() {
        attachment = createEntity(em);
    }

    @Test
    @Transactional
    public void createAttachment() throws Exception {
        int databaseSizeBeforeCreate = attachmentRepository.findAll().size();

        // Create the Attachment
        restAttachmentMockMvc.perform(post("/api/attachments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attachment)))
            .andExpect(status().isCreated());

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeCreate + 1);
        Attachment testAttachment = attachmentList.get(attachmentList.size() - 1);
        assertThat(testAttachment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAttachment.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAttachment.getContentType()).isEqualTo(DEFAULT_CONTENT_TYPE);
        assertThat(testAttachment.getFilename()).isEqualTo(DEFAULT_FILENAME);
        assertThat(testAttachment.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testAttachment.getDownloads()).isEqualTo(DEFAULT_DOWNLOADS);
        assertThat(testAttachment.getSize()).isEqualTo(DEFAULT_SIZE);
        assertThat(testAttachment.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testAttachment.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    public void createAttachmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = attachmentRepository.findAll().size();

        // Create the Attachment with an existing ID
        attachment.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttachmentMockMvc.perform(post("/api/attachments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attachment)))
            .andExpect(status().isBadRequest());

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = attachmentRepository.findAll().size();
        // set the field null
        attachment.setName(null);

        // Create the Attachment, which fails.

        restAttachmentMockMvc.perform(post("/api/attachments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attachment)))
            .andExpect(status().isBadRequest());

        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAttachments() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList
        restAttachmentMockMvc.perform(get("/api/attachments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].contentType").value(hasItem(DEFAULT_CONTENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH.toString())))
            .andExpect(jsonPath("$.[*].downloads").value(hasItem(DEFAULT_DOWNLOADS)))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())));
    }
    

    @Test
    @Transactional
    public void getAttachment() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get the attachment
        restAttachmentMockMvc.perform(get("/api/attachments/{id}", attachment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(attachment.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.contentType").value(DEFAULT_CONTENT_TYPE.toString()))
            .andExpect(jsonPath("$.filename").value(DEFAULT_FILENAME.toString()))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH.toString()))
            .andExpect(jsonPath("$.downloads").value(DEFAULT_DOWNLOADS))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()));
    }

    @Test
    @Transactional
    public void getAllAttachmentsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where name equals to DEFAULT_NAME
        defaultAttachmentShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the attachmentList where name equals to UPDATED_NAME
        defaultAttachmentShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where name in DEFAULT_NAME or UPDATED_NAME
        defaultAttachmentShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the attachmentList where name equals to UPDATED_NAME
        defaultAttachmentShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where name is not null
        defaultAttachmentShouldBeFound("name.specified=true");

        // Get all the attachmentList where name is null
        defaultAttachmentShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttachmentsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where description equals to DEFAULT_DESCRIPTION
        defaultAttachmentShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the attachmentList where description equals to UPDATED_DESCRIPTION
        defaultAttachmentShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultAttachmentShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the attachmentList where description equals to UPDATED_DESCRIPTION
        defaultAttachmentShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where description is not null
        defaultAttachmentShouldBeFound("description.specified=true");

        // Get all the attachmentList where description is null
        defaultAttachmentShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttachmentsByContentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where contentType equals to DEFAULT_CONTENT_TYPE
        defaultAttachmentShouldBeFound("contentType.equals=" + DEFAULT_CONTENT_TYPE);

        // Get all the attachmentList where contentType equals to UPDATED_CONTENT_TYPE
        defaultAttachmentShouldNotBeFound("contentType.equals=" + UPDATED_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByContentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where contentType in DEFAULT_CONTENT_TYPE or UPDATED_CONTENT_TYPE
        defaultAttachmentShouldBeFound("contentType.in=" + DEFAULT_CONTENT_TYPE + "," + UPDATED_CONTENT_TYPE);

        // Get all the attachmentList where contentType equals to UPDATED_CONTENT_TYPE
        defaultAttachmentShouldNotBeFound("contentType.in=" + UPDATED_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByContentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where contentType is not null
        defaultAttachmentShouldBeFound("contentType.specified=true");

        // Get all the attachmentList where contentType is null
        defaultAttachmentShouldNotBeFound("contentType.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttachmentsByFilenameIsEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where filename equals to DEFAULT_FILENAME
        defaultAttachmentShouldBeFound("filename.equals=" + DEFAULT_FILENAME);

        // Get all the attachmentList where filename equals to UPDATED_FILENAME
        defaultAttachmentShouldNotBeFound("filename.equals=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByFilenameIsInShouldWork() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where filename in DEFAULT_FILENAME or UPDATED_FILENAME
        defaultAttachmentShouldBeFound("filename.in=" + DEFAULT_FILENAME + "," + UPDATED_FILENAME);

        // Get all the attachmentList where filename equals to UPDATED_FILENAME
        defaultAttachmentShouldNotBeFound("filename.in=" + UPDATED_FILENAME);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByFilenameIsNullOrNotNull() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where filename is not null
        defaultAttachmentShouldBeFound("filename.specified=true");

        // Get all the attachmentList where filename is null
        defaultAttachmentShouldNotBeFound("filename.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttachmentsByPathIsEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where path equals to DEFAULT_PATH
        defaultAttachmentShouldBeFound("path.equals=" + DEFAULT_PATH);

        // Get all the attachmentList where path equals to UPDATED_PATH
        defaultAttachmentShouldNotBeFound("path.equals=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByPathIsInShouldWork() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where path in DEFAULT_PATH or UPDATED_PATH
        defaultAttachmentShouldBeFound("path.in=" + DEFAULT_PATH + "," + UPDATED_PATH);

        // Get all the attachmentList where path equals to UPDATED_PATH
        defaultAttachmentShouldNotBeFound("path.in=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where path is not null
        defaultAttachmentShouldBeFound("path.specified=true");

        // Get all the attachmentList where path is null
        defaultAttachmentShouldNotBeFound("path.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttachmentsByDownloadsIsEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where downloads equals to DEFAULT_DOWNLOADS
        defaultAttachmentShouldBeFound("downloads.equals=" + DEFAULT_DOWNLOADS);

        // Get all the attachmentList where downloads equals to UPDATED_DOWNLOADS
        defaultAttachmentShouldNotBeFound("downloads.equals=" + UPDATED_DOWNLOADS);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByDownloadsIsInShouldWork() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where downloads in DEFAULT_DOWNLOADS or UPDATED_DOWNLOADS
        defaultAttachmentShouldBeFound("downloads.in=" + DEFAULT_DOWNLOADS + "," + UPDATED_DOWNLOADS);

        // Get all the attachmentList where downloads equals to UPDATED_DOWNLOADS
        defaultAttachmentShouldNotBeFound("downloads.in=" + UPDATED_DOWNLOADS);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByDownloadsIsNullOrNotNull() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where downloads is not null
        defaultAttachmentShouldBeFound("downloads.specified=true");

        // Get all the attachmentList where downloads is null
        defaultAttachmentShouldNotBeFound("downloads.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttachmentsByDownloadsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where downloads greater than or equals to DEFAULT_DOWNLOADS
        defaultAttachmentShouldBeFound("downloads.greaterOrEqualThan=" + DEFAULT_DOWNLOADS);

        // Get all the attachmentList where downloads greater than or equals to UPDATED_DOWNLOADS
        defaultAttachmentShouldNotBeFound("downloads.greaterOrEqualThan=" + UPDATED_DOWNLOADS);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByDownloadsIsLessThanSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where downloads less than or equals to DEFAULT_DOWNLOADS
        defaultAttachmentShouldNotBeFound("downloads.lessThan=" + DEFAULT_DOWNLOADS);

        // Get all the attachmentList where downloads less than or equals to UPDATED_DOWNLOADS
        defaultAttachmentShouldBeFound("downloads.lessThan=" + UPDATED_DOWNLOADS);
    }


    @Test
    @Transactional
    public void getAllAttachmentsBySizeIsEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where size equals to DEFAULT_SIZE
        defaultAttachmentShouldBeFound("size.equals=" + DEFAULT_SIZE);

        // Get all the attachmentList where size equals to UPDATED_SIZE
        defaultAttachmentShouldNotBeFound("size.equals=" + UPDATED_SIZE);
    }

    @Test
    @Transactional
    public void getAllAttachmentsBySizeIsInShouldWork() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where size in DEFAULT_SIZE or UPDATED_SIZE
        defaultAttachmentShouldBeFound("size.in=" + DEFAULT_SIZE + "," + UPDATED_SIZE);

        // Get all the attachmentList where size equals to UPDATED_SIZE
        defaultAttachmentShouldNotBeFound("size.in=" + UPDATED_SIZE);
    }

    @Test
    @Transactional
    public void getAllAttachmentsBySizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where size is not null
        defaultAttachmentShouldBeFound("size.specified=true");

        // Get all the attachmentList where size is null
        defaultAttachmentShouldNotBeFound("size.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttachmentsBySizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where size greater than or equals to DEFAULT_SIZE
        defaultAttachmentShouldBeFound("size.greaterOrEqualThan=" + DEFAULT_SIZE);

        // Get all the attachmentList where size greater than or equals to UPDATED_SIZE
        defaultAttachmentShouldNotBeFound("size.greaterOrEqualThan=" + UPDATED_SIZE);
    }

    @Test
    @Transactional
    public void getAllAttachmentsBySizeIsLessThanSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where size less than or equals to DEFAULT_SIZE
        defaultAttachmentShouldNotBeFound("size.lessThan=" + DEFAULT_SIZE);

        // Get all the attachmentList where size less than or equals to UPDATED_SIZE
        defaultAttachmentShouldBeFound("size.lessThan=" + UPDATED_SIZE);
    }


    @Test
    @Transactional
    public void getAllAttachmentsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where createdAt equals to DEFAULT_CREATED_AT
        defaultAttachmentShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the attachmentList where createdAt equals to UPDATED_CREATED_AT
        defaultAttachmentShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultAttachmentShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the attachmentList where createdAt equals to UPDATED_CREATED_AT
        defaultAttachmentShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where createdAt is not null
        defaultAttachmentShouldBeFound("createdAt.specified=true");

        // Get all the attachmentList where createdAt is null
        defaultAttachmentShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttachmentsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where createdAt greater than or equals to DEFAULT_CREATED_AT
        defaultAttachmentShouldBeFound("createdAt.greaterOrEqualThan=" + DEFAULT_CREATED_AT);

        // Get all the attachmentList where createdAt greater than or equals to UPDATED_CREATED_AT
        defaultAttachmentShouldNotBeFound("createdAt.greaterOrEqualThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where createdAt less than or equals to DEFAULT_CREATED_AT
        defaultAttachmentShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the attachmentList where createdAt less than or equals to UPDATED_CREATED_AT
        defaultAttachmentShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }


    @Test
    @Transactional
    public void getAllAttachmentsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where createdBy equals to DEFAULT_CREATED_BY
        defaultAttachmentShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the attachmentList where createdBy equals to UPDATED_CREATED_BY
        defaultAttachmentShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultAttachmentShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the attachmentList where createdBy equals to UPDATED_CREATED_BY
        defaultAttachmentShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllAttachmentsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where createdBy is not null
        defaultAttachmentShouldBeFound("createdBy.specified=true");

        // Get all the attachmentList where createdBy is null
        defaultAttachmentShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttachmentsByContractIsEqualToSomething() throws Exception {
        // Initialize the database
        Contract contract = ContractResourceIntTest.createEntity(em);
        em.persist(contract);
        em.flush();
        attachment.setContract(contract);
        attachmentRepository.saveAndFlush(attachment);
        Long contractId = contract.getId();

        // Get all the attachmentList where contract equals to contractId
        defaultAttachmentShouldBeFound("contractId.equals=" + contractId);

        // Get all the attachmentList where contract equals to contractId + 1
        defaultAttachmentShouldNotBeFound("contractId.equals=" + (contractId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultAttachmentShouldBeFound(String filter) throws Exception {
        restAttachmentMockMvc.perform(get("/api/attachments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].contentType").value(hasItem(DEFAULT_CONTENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].filename").value(hasItem(DEFAULT_FILENAME.toString())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH.toString())))
            .andExpect(jsonPath("$.[*].downloads").value(hasItem(DEFAULT_DOWNLOADS)))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultAttachmentShouldNotBeFound(String filter) throws Exception {
        restAttachmentMockMvc.perform(get("/api/attachments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingAttachment() throws Exception {
        // Get the attachment
        restAttachmentMockMvc.perform(get("/api/attachments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAttachment() throws Exception {
        // Initialize the database
        attachmentService.save(attachment);

        int databaseSizeBeforeUpdate = attachmentRepository.findAll().size();

        // Update the attachment
        Attachment updatedAttachment = attachmentRepository.findById(attachment.getId()).get();
        // Disconnect from session so that the updates on updatedAttachment are not directly saved in db
        em.detach(updatedAttachment);
        updatedAttachment
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .contentType(UPDATED_CONTENT_TYPE)
            .filename(UPDATED_FILENAME)
            .path(UPDATED_PATH)
            .downloads(UPDATED_DOWNLOADS)
            .size(UPDATED_SIZE)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY);

        restAttachmentMockMvc.perform(put("/api/attachments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAttachment)))
            .andExpect(status().isOk());

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate);
        Attachment testAttachment = attachmentList.get(attachmentList.size() - 1);
        assertThat(testAttachment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAttachment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAttachment.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
        assertThat(testAttachment.getFilename()).isEqualTo(UPDATED_FILENAME);
        assertThat(testAttachment.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testAttachment.getDownloads()).isEqualTo(UPDATED_DOWNLOADS);
        assertThat(testAttachment.getSize()).isEqualTo(UPDATED_SIZE);
        assertThat(testAttachment.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAttachment.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void updateNonExistingAttachment() throws Exception {
        int databaseSizeBeforeUpdate = attachmentRepository.findAll().size();

        // Create the Attachment

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restAttachmentMockMvc.perform(put("/api/attachments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attachment)))
            .andExpect(status().isBadRequest());

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAttachment() throws Exception {
        // Initialize the database
        attachmentService.save(attachment);

        int databaseSizeBeforeDelete = attachmentRepository.findAll().size();

        // Get the attachment
        restAttachmentMockMvc.perform(delete("/api/attachments/{id}", attachment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Attachment.class);
        Attachment attachment1 = new Attachment();
        attachment1.setId(1L);
        Attachment attachment2 = new Attachment();
        attachment2.setId(attachment1.getId());
        assertThat(attachment1).isEqualTo(attachment2);
        attachment2.setId(2L);
        assertThat(attachment1).isNotEqualTo(attachment2);
        attachment1.setId(null);
        assertThat(attachment1).isNotEqualTo(attachment2);
    }
}
