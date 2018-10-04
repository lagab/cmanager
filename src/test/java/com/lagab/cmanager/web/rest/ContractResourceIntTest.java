package com.lagab.cmanager.web.rest;

import com.lagab.cmanager.CmanagerApp;

import com.lagab.cmanager.domain.Contract;
import com.lagab.cmanager.domain.SignatureRequest;
import com.lagab.cmanager.domain.Project;
import com.lagab.cmanager.repository.ContractRepository;
import com.lagab.cmanager.service.ContractService;
import com.lagab.cmanager.web.rest.errors.ExceptionTranslator;
import com.lagab.cmanager.service.dto.ContractCriteria;
import com.lagab.cmanager.service.ContractQueryService;

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

import com.lagab.cmanager.domain.enumeration.Status;
/**
 * Test class for the ContractResource REST controller.
 *
 * @see ContractResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CmanagerApp.class)
public class ContractResourceIntTest {

    private static final String DEFAULT_UUID = "AAAAAAAAAA";
    private static final String UPDATED_UUID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.ANY;
    private static final Status UPDATED_STATUS = Status.DRAFT;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_REQUEST_ACESS = false;
    private static final Boolean UPDATED_REQUEST_ACESS = true;

    private static final LocalDate DEFAULT_LAST_ACTIVITY_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_ACTIVITY_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_EXPIRES_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRES_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    @Autowired
    private ContractRepository contractRepository;

    

    @Autowired
    private ContractService contractService;

    @Autowired
    private ContractQueryService contractQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restContractMockMvc;

    private Contract contract;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ContractResource contractResource = new ContractResource(contractService, contractQueryService);
        this.restContractMockMvc = MockMvcBuilders.standaloneSetup(contractResource)
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
    public static Contract createEntity(EntityManager em) {
        Contract contract = new Contract()
            .uuid(DEFAULT_UUID)
            .name(DEFAULT_NAME)
            .subject(DEFAULT_SUBJECT)
            .status(DEFAULT_STATUS)
            .description(DEFAULT_DESCRIPTION)
            .content(DEFAULT_CONTENT)
            .requestAcess(DEFAULT_REQUEST_ACESS)
            .lastActivityAt(DEFAULT_LAST_ACTIVITY_AT)
            .expiresAt(DEFAULT_EXPIRES_AT)
            .type(DEFAULT_TYPE);
        return contract;
    }

    @Before
    public void initTest() {
        contract = createEntity(em);
    }

    @Test
    @Transactional
    public void createContract() throws Exception {
        int databaseSizeBeforeCreate = contractRepository.findAll().size();

        // Create the Contract
        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contract)))
            .andExpect(status().isCreated());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeCreate + 1);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testContract.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContract.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testContract.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testContract.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testContract.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testContract.isRequestAcess()).isEqualTo(DEFAULT_REQUEST_ACESS);
        assertThat(testContract.getLastActivityAt()).isEqualTo(DEFAULT_LAST_ACTIVITY_AT);
        assertThat(testContract.getExpiresAt()).isEqualTo(DEFAULT_EXPIRES_AT);
        assertThat(testContract.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createContractWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contractRepository.findAll().size();

        // Create the Contract with an existing ID
        contract.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contract)))
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractRepository.findAll().size();
        // set the field null
        contract.setUuid(null);

        // Create the Contract, which fails.

        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contract)))
            .andExpect(status().isBadRequest());

        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractRepository.findAll().size();
        // set the field null
        contract.setName(null);

        // Create the Contract, which fails.

        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contract)))
            .andExpect(status().isBadRequest());

        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSubjectIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractRepository.findAll().size();
        // set the field null
        contract.setSubject(null);

        // Create the Contract, which fails.

        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contract)))
            .andExpect(status().isBadRequest());

        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastActivityAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractRepository.findAll().size();
        // set the field null
        contract.setLastActivityAt(null);

        // Create the Contract, which fails.

        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contract)))
            .andExpect(status().isBadRequest());

        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllContracts() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList
        restContractMockMvc.perform(get("/api/contracts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contract.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].requestAcess").value(hasItem(DEFAULT_REQUEST_ACESS.booleanValue())))
            .andExpect(jsonPath("$.[*].lastActivityAt").value(hasItem(DEFAULT_LAST_ACTIVITY_AT.toString())))
            .andExpect(jsonPath("$.[*].expiresAt").value(hasItem(DEFAULT_EXPIRES_AT.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }
    

    @Test
    @Transactional
    public void getContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get the contract
        restContractMockMvc.perform(get("/api/contracts/{id}", contract.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contract.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.requestAcess").value(DEFAULT_REQUEST_ACESS.booleanValue()))
            .andExpect(jsonPath("$.lastActivityAt").value(DEFAULT_LAST_ACTIVITY_AT.toString()))
            .andExpect(jsonPath("$.expiresAt").value(DEFAULT_EXPIRES_AT.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getAllContractsByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where uuid equals to DEFAULT_UUID
        defaultContractShouldBeFound("uuid.equals=" + DEFAULT_UUID);

        // Get all the contractList where uuid equals to UPDATED_UUID
        defaultContractShouldNotBeFound("uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    public void getAllContractsByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where uuid in DEFAULT_UUID or UPDATED_UUID
        defaultContractShouldBeFound("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID);

        // Get all the contractList where uuid equals to UPDATED_UUID
        defaultContractShouldNotBeFound("uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    public void getAllContractsByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where uuid is not null
        defaultContractShouldBeFound("uuid.specified=true");

        // Get all the contractList where uuid is null
        defaultContractShouldNotBeFound("uuid.specified=false");
    }

    @Test
    @Transactional
    public void getAllContractsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where name equals to DEFAULT_NAME
        defaultContractShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the contractList where name equals to UPDATED_NAME
        defaultContractShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllContractsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where name in DEFAULT_NAME or UPDATED_NAME
        defaultContractShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the contractList where name equals to UPDATED_NAME
        defaultContractShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllContractsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where name is not null
        defaultContractShouldBeFound("name.specified=true");

        // Get all the contractList where name is null
        defaultContractShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllContractsBySubjectIsEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where subject equals to DEFAULT_SUBJECT
        defaultContractShouldBeFound("subject.equals=" + DEFAULT_SUBJECT);

        // Get all the contractList where subject equals to UPDATED_SUBJECT
        defaultContractShouldNotBeFound("subject.equals=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    public void getAllContractsBySubjectIsInShouldWork() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where subject in DEFAULT_SUBJECT or UPDATED_SUBJECT
        defaultContractShouldBeFound("subject.in=" + DEFAULT_SUBJECT + "," + UPDATED_SUBJECT);

        // Get all the contractList where subject equals to UPDATED_SUBJECT
        defaultContractShouldNotBeFound("subject.in=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    public void getAllContractsBySubjectIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where subject is not null
        defaultContractShouldBeFound("subject.specified=true");

        // Get all the contractList where subject is null
        defaultContractShouldNotBeFound("subject.specified=false");
    }

    @Test
    @Transactional
    public void getAllContractsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where status equals to DEFAULT_STATUS
        defaultContractShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the contractList where status equals to UPDATED_STATUS
        defaultContractShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllContractsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultContractShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the contractList where status equals to UPDATED_STATUS
        defaultContractShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllContractsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where status is not null
        defaultContractShouldBeFound("status.specified=true");

        // Get all the contractList where status is null
        defaultContractShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllContractsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where description equals to DEFAULT_DESCRIPTION
        defaultContractShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the contractList where description equals to UPDATED_DESCRIPTION
        defaultContractShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllContractsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultContractShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the contractList where description equals to UPDATED_DESCRIPTION
        defaultContractShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllContractsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where description is not null
        defaultContractShouldBeFound("description.specified=true");

        // Get all the contractList where description is null
        defaultContractShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllContractsByContentIsEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where content equals to DEFAULT_CONTENT
        defaultContractShouldBeFound("content.equals=" + DEFAULT_CONTENT);

        // Get all the contractList where content equals to UPDATED_CONTENT
        defaultContractShouldNotBeFound("content.equals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void getAllContractsByContentIsInShouldWork() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where content in DEFAULT_CONTENT or UPDATED_CONTENT
        defaultContractShouldBeFound("content.in=" + DEFAULT_CONTENT + "," + UPDATED_CONTENT);

        // Get all the contractList where content equals to UPDATED_CONTENT
        defaultContractShouldNotBeFound("content.in=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void getAllContractsByContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where content is not null
        defaultContractShouldBeFound("content.specified=true");

        // Get all the contractList where content is null
        defaultContractShouldNotBeFound("content.specified=false");
    }

    @Test
    @Transactional
    public void getAllContractsByRequestAcessIsEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where requestAcess equals to DEFAULT_REQUEST_ACESS
        defaultContractShouldBeFound("requestAcess.equals=" + DEFAULT_REQUEST_ACESS);

        // Get all the contractList where requestAcess equals to UPDATED_REQUEST_ACESS
        defaultContractShouldNotBeFound("requestAcess.equals=" + UPDATED_REQUEST_ACESS);
    }

    @Test
    @Transactional
    public void getAllContractsByRequestAcessIsInShouldWork() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where requestAcess in DEFAULT_REQUEST_ACESS or UPDATED_REQUEST_ACESS
        defaultContractShouldBeFound("requestAcess.in=" + DEFAULT_REQUEST_ACESS + "," + UPDATED_REQUEST_ACESS);

        // Get all the contractList where requestAcess equals to UPDATED_REQUEST_ACESS
        defaultContractShouldNotBeFound("requestAcess.in=" + UPDATED_REQUEST_ACESS);
    }

    @Test
    @Transactional
    public void getAllContractsByRequestAcessIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where requestAcess is not null
        defaultContractShouldBeFound("requestAcess.specified=true");

        // Get all the contractList where requestAcess is null
        defaultContractShouldNotBeFound("requestAcess.specified=false");
    }

    @Test
    @Transactional
    public void getAllContractsByLastActivityAtIsEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where lastActivityAt equals to DEFAULT_LAST_ACTIVITY_AT
        defaultContractShouldBeFound("lastActivityAt.equals=" + DEFAULT_LAST_ACTIVITY_AT);

        // Get all the contractList where lastActivityAt equals to UPDATED_LAST_ACTIVITY_AT
        defaultContractShouldNotBeFound("lastActivityAt.equals=" + UPDATED_LAST_ACTIVITY_AT);
    }

    @Test
    @Transactional
    public void getAllContractsByLastActivityAtIsInShouldWork() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where lastActivityAt in DEFAULT_LAST_ACTIVITY_AT or UPDATED_LAST_ACTIVITY_AT
        defaultContractShouldBeFound("lastActivityAt.in=" + DEFAULT_LAST_ACTIVITY_AT + "," + UPDATED_LAST_ACTIVITY_AT);

        // Get all the contractList where lastActivityAt equals to UPDATED_LAST_ACTIVITY_AT
        defaultContractShouldNotBeFound("lastActivityAt.in=" + UPDATED_LAST_ACTIVITY_AT);
    }

    @Test
    @Transactional
    public void getAllContractsByLastActivityAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where lastActivityAt is not null
        defaultContractShouldBeFound("lastActivityAt.specified=true");

        // Get all the contractList where lastActivityAt is null
        defaultContractShouldNotBeFound("lastActivityAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllContractsByLastActivityAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where lastActivityAt greater than or equals to DEFAULT_LAST_ACTIVITY_AT
        defaultContractShouldBeFound("lastActivityAt.greaterOrEqualThan=" + DEFAULT_LAST_ACTIVITY_AT);

        // Get all the contractList where lastActivityAt greater than or equals to UPDATED_LAST_ACTIVITY_AT
        defaultContractShouldNotBeFound("lastActivityAt.greaterOrEqualThan=" + UPDATED_LAST_ACTIVITY_AT);
    }

    @Test
    @Transactional
    public void getAllContractsByLastActivityAtIsLessThanSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where lastActivityAt less than or equals to DEFAULT_LAST_ACTIVITY_AT
        defaultContractShouldNotBeFound("lastActivityAt.lessThan=" + DEFAULT_LAST_ACTIVITY_AT);

        // Get all the contractList where lastActivityAt less than or equals to UPDATED_LAST_ACTIVITY_AT
        defaultContractShouldBeFound("lastActivityAt.lessThan=" + UPDATED_LAST_ACTIVITY_AT);
    }


    @Test
    @Transactional
    public void getAllContractsByExpiresAtIsEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where expiresAt equals to DEFAULT_EXPIRES_AT
        defaultContractShouldBeFound("expiresAt.equals=" + DEFAULT_EXPIRES_AT);

        // Get all the contractList where expiresAt equals to UPDATED_EXPIRES_AT
        defaultContractShouldNotBeFound("expiresAt.equals=" + UPDATED_EXPIRES_AT);
    }

    @Test
    @Transactional
    public void getAllContractsByExpiresAtIsInShouldWork() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where expiresAt in DEFAULT_EXPIRES_AT or UPDATED_EXPIRES_AT
        defaultContractShouldBeFound("expiresAt.in=" + DEFAULT_EXPIRES_AT + "," + UPDATED_EXPIRES_AT);

        // Get all the contractList where expiresAt equals to UPDATED_EXPIRES_AT
        defaultContractShouldNotBeFound("expiresAt.in=" + UPDATED_EXPIRES_AT);
    }

    @Test
    @Transactional
    public void getAllContractsByExpiresAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where expiresAt is not null
        defaultContractShouldBeFound("expiresAt.specified=true");

        // Get all the contractList where expiresAt is null
        defaultContractShouldNotBeFound("expiresAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllContractsByExpiresAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where expiresAt greater than or equals to DEFAULT_EXPIRES_AT
        defaultContractShouldBeFound("expiresAt.greaterOrEqualThan=" + DEFAULT_EXPIRES_AT);

        // Get all the contractList where expiresAt greater than or equals to UPDATED_EXPIRES_AT
        defaultContractShouldNotBeFound("expiresAt.greaterOrEqualThan=" + UPDATED_EXPIRES_AT);
    }

    @Test
    @Transactional
    public void getAllContractsByExpiresAtIsLessThanSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where expiresAt less than or equals to DEFAULT_EXPIRES_AT
        defaultContractShouldNotBeFound("expiresAt.lessThan=" + DEFAULT_EXPIRES_AT);

        // Get all the contractList where expiresAt less than or equals to UPDATED_EXPIRES_AT
        defaultContractShouldBeFound("expiresAt.lessThan=" + UPDATED_EXPIRES_AT);
    }


    @Test
    @Transactional
    public void getAllContractsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where type equals to DEFAULT_TYPE
        defaultContractShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the contractList where type equals to UPDATED_TYPE
        defaultContractShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllContractsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultContractShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the contractList where type equals to UPDATED_TYPE
        defaultContractShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllContractsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList where type is not null
        defaultContractShouldBeFound("type.specified=true");

        // Get all the contractList where type is null
        defaultContractShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllContractsByRequestsIsEqualToSomething() throws Exception {
        // Initialize the database
        SignatureRequest requests = SignatureRequestResourceIntTest.createEntity(em);
        em.persist(requests);
        em.flush();
        contract.addRequests(requests);
        contractRepository.saveAndFlush(contract);
        Long requestsId = requests.getId();

        // Get all the contractList where requests equals to requestsId
        defaultContractShouldBeFound("requestsId.equals=" + requestsId);

        // Get all the contractList where requests equals to requestsId + 1
        defaultContractShouldNotBeFound("requestsId.equals=" + (requestsId + 1));
    }


    @Test
    @Transactional
    public void getAllContractsByProjectIsEqualToSomething() throws Exception {
        // Initialize the database
        Project project = ProjectResourceIntTest.createEntity(em);
        em.persist(project);
        em.flush();
        contract.setProject(project);
        contractRepository.saveAndFlush(contract);
        Long projectId = project.getId();

        // Get all the contractList where project equals to projectId
        defaultContractShouldBeFound("projectId.equals=" + projectId);

        // Get all the contractList where project equals to projectId + 1
        defaultContractShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultContractShouldBeFound(String filter) throws Exception {
        restContractMockMvc.perform(get("/api/contracts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contract.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].requestAcess").value(hasItem(DEFAULT_REQUEST_ACESS.booleanValue())))
            .andExpect(jsonPath("$.[*].lastActivityAt").value(hasItem(DEFAULT_LAST_ACTIVITY_AT.toString())))
            .andExpect(jsonPath("$.[*].expiresAt").value(hasItem(DEFAULT_EXPIRES_AT.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultContractShouldNotBeFound(String filter) throws Exception {
        restContractMockMvc.perform(get("/api/contracts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingContract() throws Exception {
        // Get the contract
        restContractMockMvc.perform(get("/api/contracts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContract() throws Exception {
        // Initialize the database
        contractService.save(contract);

        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Update the contract
        Contract updatedContract = contractRepository.findById(contract.getId()).get();
        // Disconnect from session so that the updates on updatedContract are not directly saved in db
        em.detach(updatedContract);
        updatedContract
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .subject(UPDATED_SUBJECT)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .content(UPDATED_CONTENT)
            .requestAcess(UPDATED_REQUEST_ACESS)
            .lastActivityAt(UPDATED_LAST_ACTIVITY_AT)
            .expiresAt(UPDATED_EXPIRES_AT)
            .type(UPDATED_TYPE);

        restContractMockMvc.perform(put("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedContract)))
            .andExpect(status().isOk());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testContract.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContract.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testContract.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testContract.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testContract.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testContract.isRequestAcess()).isEqualTo(UPDATED_REQUEST_ACESS);
        assertThat(testContract.getLastActivityAt()).isEqualTo(UPDATED_LAST_ACTIVITY_AT);
        assertThat(testContract.getExpiresAt()).isEqualTo(UPDATED_EXPIRES_AT);
        assertThat(testContract.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Create the Contract

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restContractMockMvc.perform(put("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contract)))
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteContract() throws Exception {
        // Initialize the database
        contractService.save(contract);

        int databaseSizeBeforeDelete = contractRepository.findAll().size();

        // Get the contract
        restContractMockMvc.perform(delete("/api/contracts/{id}", contract.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contract.class);
        Contract contract1 = new Contract();
        contract1.setId(1L);
        Contract contract2 = new Contract();
        contract2.setId(contract1.getId());
        assertThat(contract1).isEqualTo(contract2);
        contract2.setId(2L);
        assertThat(contract1).isNotEqualTo(contract2);
        contract1.setId(null);
        assertThat(contract1).isNotEqualTo(contract2);
    }
}
