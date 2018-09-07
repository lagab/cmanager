package com.lagab.cmanager.web.rest;

import com.lagab.cmanager.CmanagerApp;

import com.lagab.cmanager.domain.Signature;
import com.lagab.cmanager.repository.SignatureRepository;
import com.lagab.cmanager.service.SignatureService;
import com.lagab.cmanager.web.rest.errors.ExceptionTranslator;

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
        final SignatureResource signatureResource = new SignatureResource(signatureService);
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
