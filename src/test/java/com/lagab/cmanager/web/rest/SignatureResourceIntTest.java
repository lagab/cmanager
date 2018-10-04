package com.lagab.cmanager.web.rest;

import com.lagab.cmanager.CmanagerApp;

import com.lagab.cmanager.domain.Signature;
import com.lagab.cmanager.domain.SignatureRequest;
import com.lagab.cmanager.repository.SignatureRepository;
import com.lagab.cmanager.service.SignatureService;
import com.lagab.cmanager.web.rest.errors.ExceptionTranslator;
import com.lagab.cmanager.service.dto.SignatureCriteria;
import com.lagab.cmanager.service.SignatureQueryService;

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
 * Test class for the SignatureResource REST controller.
 *
 * @see SignatureResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CmanagerApp.class)
public class SignatureResourceIntTest {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;

    private static final Status DEFAULT_STATUS = Status.ANY;
    private static final Status UPDATED_STATUS = Status.DRAFT;

    private static final String DEFAULT_DECLINE_REASON = "AAAAAAAAAA";
    private static final String UPDATED_DECLINE_REASON = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_LAST_VIEWED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_VIEWED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_LAST_REMINDED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_REMINDED_AT = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private SignatureRepository signatureRepository;

    

    @Autowired
    private SignatureService signatureService;

    @Autowired
    private SignatureQueryService signatureQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSignatureMockMvc;

    private Signature signature;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SignatureResource signatureResource = new SignatureResource(signatureService, signatureQueryService);
        this.restSignatureMockMvc = MockMvcBuilders.standaloneSetup(signatureResource)
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
    public static Signature createEntity(EntityManager em) {
        Signature signature = new Signature()
            .email(DEFAULT_EMAIL)
            .name(DEFAULT_NAME)
            .order(DEFAULT_ORDER)
            .status(DEFAULT_STATUS)
            .declineReason(DEFAULT_DECLINE_REASON)
            .lastViewedAt(DEFAULT_LAST_VIEWED_AT)
            .lastRemindedAt(DEFAULT_LAST_REMINDED_AT);
        return signature;
    }

    @Before
    public void initTest() {
        signature = createEntity(em);
    }

    @Test
    @Transactional
    public void createSignature() throws Exception {
        int databaseSizeBeforeCreate = signatureRepository.findAll().size();

        // Create the Signature
        restSignatureMockMvc.perform(post("/api/signatures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(signature)))
            .andExpect(status().isCreated());

        // Validate the Signature in the database
        List<Signature> signatureList = signatureRepository.findAll();
        assertThat(signatureList).hasSize(databaseSizeBeforeCreate + 1);
        Signature testSignature = signatureList.get(signatureList.size() - 1);
        assertThat(testSignature.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testSignature.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSignature.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testSignature.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSignature.getDeclineReason()).isEqualTo(DEFAULT_DECLINE_REASON);
        assertThat(testSignature.getLastViewedAt()).isEqualTo(DEFAULT_LAST_VIEWED_AT);
        assertThat(testSignature.getLastRemindedAt()).isEqualTo(DEFAULT_LAST_REMINDED_AT);
    }

    @Test
    @Transactional
    public void createSignatureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = signatureRepository.findAll().size();

        // Create the Signature with an existing ID
        signature.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSignatureMockMvc.perform(post("/api/signatures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(signature)))
            .andExpect(status().isBadRequest());

        // Validate the Signature in the database
        List<Signature> signatureList = signatureRepository.findAll();
        assertThat(signatureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = signatureRepository.findAll().size();
        // set the field null
        signature.setEmail(null);

        // Create the Signature, which fails.

        restSignatureMockMvc.perform(post("/api/signatures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(signature)))
            .andExpect(status().isBadRequest());

        List<Signature> signatureList = signatureRepository.findAll();
        assertThat(signatureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = signatureRepository.findAll().size();
        // set the field null
        signature.setName(null);

        // Create the Signature, which fails.

        restSignatureMockMvc.perform(post("/api/signatures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(signature)))
            .andExpect(status().isBadRequest());

        List<Signature> signatureList = signatureRepository.findAll();
        assertThat(signatureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrderIsRequired() throws Exception {
        int databaseSizeBeforeTest = signatureRepository.findAll().size();
        // set the field null
        signature.setOrder(null);

        // Create the Signature, which fails.

        restSignatureMockMvc.perform(post("/api/signatures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(signature)))
            .andExpect(status().isBadRequest());

        List<Signature> signatureList = signatureRepository.findAll();
        assertThat(signatureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastViewedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = signatureRepository.findAll().size();
        // set the field null
        signature.setLastViewedAt(null);

        // Create the Signature, which fails.

        restSignatureMockMvc.perform(post("/api/signatures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(signature)))
            .andExpect(status().isBadRequest());

        List<Signature> signatureList = signatureRepository.findAll();
        assertThat(signatureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastRemindedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = signatureRepository.findAll().size();
        // set the field null
        signature.setLastRemindedAt(null);

        // Create the Signature, which fails.

        restSignatureMockMvc.perform(post("/api/signatures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(signature)))
            .andExpect(status().isBadRequest());

        List<Signature> signatureList = signatureRepository.findAll();
        assertThat(signatureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSignatures() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get all the signatureList
        restSignatureMockMvc.perform(get("/api/signatures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(signature.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].declineReason").value(hasItem(DEFAULT_DECLINE_REASON.toString())))
            .andExpect(jsonPath("$.[*].lastViewedAt").value(hasItem(DEFAULT_LAST_VIEWED_AT.toString())))
            .andExpect(jsonPath("$.[*].lastRemindedAt").value(hasItem(DEFAULT_LAST_REMINDED_AT.toString())));
    }
    

    @Test
    @Transactional
    public void getSignature() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get the signature
        restSignatureMockMvc.perform(get("/api/signatures/{id}", signature.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(signature.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.declineReason").value(DEFAULT_DECLINE_REASON.toString()))
            .andExpect(jsonPath("$.lastViewedAt").value(DEFAULT_LAST_VIEWED_AT.toString()))
            .andExpect(jsonPath("$.lastRemindedAt").value(DEFAULT_LAST_REMINDED_AT.toString()));
    }

    @Test
    @Transactional
    public void getAllSignaturesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get all the signatureList where email equals to DEFAULT_EMAIL
        defaultSignatureShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the signatureList where email equals to UPDATED_EMAIL
        defaultSignatureShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllSignaturesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get all the signatureList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultSignatureShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the signatureList where email equals to UPDATED_EMAIL
        defaultSignatureShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllSignaturesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get all the signatureList where email is not null
        defaultSignatureShouldBeFound("email.specified=true");

        // Get all the signatureList where email is null
        defaultSignatureShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    public void getAllSignaturesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get all the signatureList where name equals to DEFAULT_NAME
        defaultSignatureShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the signatureList where name equals to UPDATED_NAME
        defaultSignatureShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSignaturesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get all the signatureList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSignatureShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the signatureList where name equals to UPDATED_NAME
        defaultSignatureShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSignaturesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get all the signatureList where name is not null
        defaultSignatureShouldBeFound("name.specified=true");

        // Get all the signatureList where name is null
        defaultSignatureShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllSignaturesByOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get all the signatureList where order equals to DEFAULT_ORDER
        defaultSignatureShouldBeFound("order.equals=" + DEFAULT_ORDER);

        // Get all the signatureList where order equals to UPDATED_ORDER
        defaultSignatureShouldNotBeFound("order.equals=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    public void getAllSignaturesByOrderIsInShouldWork() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get all the signatureList where order in DEFAULT_ORDER or UPDATED_ORDER
        defaultSignatureShouldBeFound("order.in=" + DEFAULT_ORDER + "," + UPDATED_ORDER);

        // Get all the signatureList where order equals to UPDATED_ORDER
        defaultSignatureShouldNotBeFound("order.in=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    public void getAllSignaturesByOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get all the signatureList where order is not null
        defaultSignatureShouldBeFound("order.specified=true");

        // Get all the signatureList where order is null
        defaultSignatureShouldNotBeFound("order.specified=false");
    }

    @Test
    @Transactional
    public void getAllSignaturesByOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get all the signatureList where order greater than or equals to DEFAULT_ORDER
        defaultSignatureShouldBeFound("order.greaterOrEqualThan=" + DEFAULT_ORDER);

        // Get all the signatureList where order greater than or equals to UPDATED_ORDER
        defaultSignatureShouldNotBeFound("order.greaterOrEqualThan=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    public void getAllSignaturesByOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get all the signatureList where order less than or equals to DEFAULT_ORDER
        defaultSignatureShouldNotBeFound("order.lessThan=" + DEFAULT_ORDER);

        // Get all the signatureList where order less than or equals to UPDATED_ORDER
        defaultSignatureShouldBeFound("order.lessThan=" + UPDATED_ORDER);
    }


    @Test
    @Transactional
    public void getAllSignaturesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get all the signatureList where status equals to DEFAULT_STATUS
        defaultSignatureShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the signatureList where status equals to UPDATED_STATUS
        defaultSignatureShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllSignaturesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get all the signatureList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultSignatureShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the signatureList where status equals to UPDATED_STATUS
        defaultSignatureShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllSignaturesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get all the signatureList where status is not null
        defaultSignatureShouldBeFound("status.specified=true");

        // Get all the signatureList where status is null
        defaultSignatureShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllSignaturesByDeclineReasonIsEqualToSomething() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get all the signatureList where declineReason equals to DEFAULT_DECLINE_REASON
        defaultSignatureShouldBeFound("declineReason.equals=" + DEFAULT_DECLINE_REASON);

        // Get all the signatureList where declineReason equals to UPDATED_DECLINE_REASON
        defaultSignatureShouldNotBeFound("declineReason.equals=" + UPDATED_DECLINE_REASON);
    }

    @Test
    @Transactional
    public void getAllSignaturesByDeclineReasonIsInShouldWork() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get all the signatureList where declineReason in DEFAULT_DECLINE_REASON or UPDATED_DECLINE_REASON
        defaultSignatureShouldBeFound("declineReason.in=" + DEFAULT_DECLINE_REASON + "," + UPDATED_DECLINE_REASON);

        // Get all the signatureList where declineReason equals to UPDATED_DECLINE_REASON
        defaultSignatureShouldNotBeFound("declineReason.in=" + UPDATED_DECLINE_REASON);
    }

    @Test
    @Transactional
    public void getAllSignaturesByDeclineReasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get all the signatureList where declineReason is not null
        defaultSignatureShouldBeFound("declineReason.specified=true");

        // Get all the signatureList where declineReason is null
        defaultSignatureShouldNotBeFound("declineReason.specified=false");
    }

    @Test
    @Transactional
    public void getAllSignaturesByLastViewedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get all the signatureList where lastViewedAt equals to DEFAULT_LAST_VIEWED_AT
        defaultSignatureShouldBeFound("lastViewedAt.equals=" + DEFAULT_LAST_VIEWED_AT);

        // Get all the signatureList where lastViewedAt equals to UPDATED_LAST_VIEWED_AT
        defaultSignatureShouldNotBeFound("lastViewedAt.equals=" + UPDATED_LAST_VIEWED_AT);
    }

    @Test
    @Transactional
    public void getAllSignaturesByLastViewedAtIsInShouldWork() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get all the signatureList where lastViewedAt in DEFAULT_LAST_VIEWED_AT or UPDATED_LAST_VIEWED_AT
        defaultSignatureShouldBeFound("lastViewedAt.in=" + DEFAULT_LAST_VIEWED_AT + "," + UPDATED_LAST_VIEWED_AT);

        // Get all the signatureList where lastViewedAt equals to UPDATED_LAST_VIEWED_AT
        defaultSignatureShouldNotBeFound("lastViewedAt.in=" + UPDATED_LAST_VIEWED_AT);
    }

    @Test
    @Transactional
    public void getAllSignaturesByLastViewedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get all the signatureList where lastViewedAt is not null
        defaultSignatureShouldBeFound("lastViewedAt.specified=true");

        // Get all the signatureList where lastViewedAt is null
        defaultSignatureShouldNotBeFound("lastViewedAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllSignaturesByLastViewedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get all the signatureList where lastViewedAt greater than or equals to DEFAULT_LAST_VIEWED_AT
        defaultSignatureShouldBeFound("lastViewedAt.greaterOrEqualThan=" + DEFAULT_LAST_VIEWED_AT);

        // Get all the signatureList where lastViewedAt greater than or equals to UPDATED_LAST_VIEWED_AT
        defaultSignatureShouldNotBeFound("lastViewedAt.greaterOrEqualThan=" + UPDATED_LAST_VIEWED_AT);
    }

    @Test
    @Transactional
    public void getAllSignaturesByLastViewedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get all the signatureList where lastViewedAt less than or equals to DEFAULT_LAST_VIEWED_AT
        defaultSignatureShouldNotBeFound("lastViewedAt.lessThan=" + DEFAULT_LAST_VIEWED_AT);

        // Get all the signatureList where lastViewedAt less than or equals to UPDATED_LAST_VIEWED_AT
        defaultSignatureShouldBeFound("lastViewedAt.lessThan=" + UPDATED_LAST_VIEWED_AT);
    }


    @Test
    @Transactional
    public void getAllSignaturesByLastRemindedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get all the signatureList where lastRemindedAt equals to DEFAULT_LAST_REMINDED_AT
        defaultSignatureShouldBeFound("lastRemindedAt.equals=" + DEFAULT_LAST_REMINDED_AT);

        // Get all the signatureList where lastRemindedAt equals to UPDATED_LAST_REMINDED_AT
        defaultSignatureShouldNotBeFound("lastRemindedAt.equals=" + UPDATED_LAST_REMINDED_AT);
    }

    @Test
    @Transactional
    public void getAllSignaturesByLastRemindedAtIsInShouldWork() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get all the signatureList where lastRemindedAt in DEFAULT_LAST_REMINDED_AT or UPDATED_LAST_REMINDED_AT
        defaultSignatureShouldBeFound("lastRemindedAt.in=" + DEFAULT_LAST_REMINDED_AT + "," + UPDATED_LAST_REMINDED_AT);

        // Get all the signatureList where lastRemindedAt equals to UPDATED_LAST_REMINDED_AT
        defaultSignatureShouldNotBeFound("lastRemindedAt.in=" + UPDATED_LAST_REMINDED_AT);
    }

    @Test
    @Transactional
    public void getAllSignaturesByLastRemindedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get all the signatureList where lastRemindedAt is not null
        defaultSignatureShouldBeFound("lastRemindedAt.specified=true");

        // Get all the signatureList where lastRemindedAt is null
        defaultSignatureShouldNotBeFound("lastRemindedAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllSignaturesByLastRemindedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get all the signatureList where lastRemindedAt greater than or equals to DEFAULT_LAST_REMINDED_AT
        defaultSignatureShouldBeFound("lastRemindedAt.greaterOrEqualThan=" + DEFAULT_LAST_REMINDED_AT);

        // Get all the signatureList where lastRemindedAt greater than or equals to UPDATED_LAST_REMINDED_AT
        defaultSignatureShouldNotBeFound("lastRemindedAt.greaterOrEqualThan=" + UPDATED_LAST_REMINDED_AT);
    }

    @Test
    @Transactional
    public void getAllSignaturesByLastRemindedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get all the signatureList where lastRemindedAt less than or equals to DEFAULT_LAST_REMINDED_AT
        defaultSignatureShouldNotBeFound("lastRemindedAt.lessThan=" + DEFAULT_LAST_REMINDED_AT);

        // Get all the signatureList where lastRemindedAt less than or equals to UPDATED_LAST_REMINDED_AT
        defaultSignatureShouldBeFound("lastRemindedAt.lessThan=" + UPDATED_LAST_REMINDED_AT);
    }


    @Test
    @Transactional
    public void getAllSignaturesByRequestIsEqualToSomething() throws Exception {
        // Initialize the database
        SignatureRequest request = SignatureRequestResourceIntTest.createEntity(em);
        em.persist(request);
        em.flush();
        signature.setRequest(request);
        signatureRepository.saveAndFlush(signature);
        Long requestId = request.getId();

        // Get all the signatureList where request equals to requestId
        defaultSignatureShouldBeFound("requestId.equals=" + requestId);

        // Get all the signatureList where request equals to requestId + 1
        defaultSignatureShouldNotBeFound("requestId.equals=" + (requestId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSignatureShouldBeFound(String filter) throws Exception {
        restSignatureMockMvc.perform(get("/api/signatures?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(signature.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].declineReason").value(hasItem(DEFAULT_DECLINE_REASON.toString())))
            .andExpect(jsonPath("$.[*].lastViewedAt").value(hasItem(DEFAULT_LAST_VIEWED_AT.toString())))
            .andExpect(jsonPath("$.[*].lastRemindedAt").value(hasItem(DEFAULT_LAST_REMINDED_AT.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSignatureShouldNotBeFound(String filter) throws Exception {
        restSignatureMockMvc.perform(get("/api/signatures?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingSignature() throws Exception {
        // Get the signature
        restSignatureMockMvc.perform(get("/api/signatures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSignature() throws Exception {
        // Initialize the database
        signatureService.save(signature);

        int databaseSizeBeforeUpdate = signatureRepository.findAll().size();

        // Update the signature
        Signature updatedSignature = signatureRepository.findById(signature.getId()).get();
        // Disconnect from session so that the updates on updatedSignature are not directly saved in db
        em.detach(updatedSignature);
        updatedSignature
            .email(UPDATED_EMAIL)
            .name(UPDATED_NAME)
            .order(UPDATED_ORDER)
            .status(UPDATED_STATUS)
            .declineReason(UPDATED_DECLINE_REASON)
            .lastViewedAt(UPDATED_LAST_VIEWED_AT)
            .lastRemindedAt(UPDATED_LAST_REMINDED_AT);

        restSignatureMockMvc.perform(put("/api/signatures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSignature)))
            .andExpect(status().isOk());

        // Validate the Signature in the database
        List<Signature> signatureList = signatureRepository.findAll();
        assertThat(signatureList).hasSize(databaseSizeBeforeUpdate);
        Signature testSignature = signatureList.get(signatureList.size() - 1);
        assertThat(testSignature.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSignature.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSignature.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testSignature.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSignature.getDeclineReason()).isEqualTo(UPDATED_DECLINE_REASON);
        assertThat(testSignature.getLastViewedAt()).isEqualTo(UPDATED_LAST_VIEWED_AT);
        assertThat(testSignature.getLastRemindedAt()).isEqualTo(UPDATED_LAST_REMINDED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingSignature() throws Exception {
        int databaseSizeBeforeUpdate = signatureRepository.findAll().size();

        // Create the Signature

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restSignatureMockMvc.perform(put("/api/signatures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(signature)))
            .andExpect(status().isBadRequest());

        // Validate the Signature in the database
        List<Signature> signatureList = signatureRepository.findAll();
        assertThat(signatureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSignature() throws Exception {
        // Initialize the database
        signatureService.save(signature);

        int databaseSizeBeforeDelete = signatureRepository.findAll().size();

        // Get the signature
        restSignatureMockMvc.perform(delete("/api/signatures/{id}", signature.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Signature> signatureList = signatureRepository.findAll();
        assertThat(signatureList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Signature.class);
        Signature signature1 = new Signature();
        signature1.setId(1L);
        Signature signature2 = new Signature();
        signature2.setId(signature1.getId());
        assertThat(signature1).isEqualTo(signature2);
        signature2.setId(2L);
        assertThat(signature1).isNotEqualTo(signature2);
        signature1.setId(null);
        assertThat(signature1).isNotEqualTo(signature2);
    }
}
