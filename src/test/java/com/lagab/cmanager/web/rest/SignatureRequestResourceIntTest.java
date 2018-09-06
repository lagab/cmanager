package com.lagab.cmanager.web.rest;

import com.lagab.cmanager.CmanagerApp;

import com.lagab.cmanager.domain.SignatureRequest;
import com.lagab.cmanager.repository.SignatureRequestRepository;
import com.lagab.cmanager.service.SignatureRequestService;
import com.lagab.cmanager.service.dto.SignatureRequestDTO;
import com.lagab.cmanager.service.mapper.SignatureRequestMapper;
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
import java.util.List;


import static com.lagab.cmanager.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SignatureRequestResource REST controller.
 *
 * @see SignatureRequestResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CmanagerApp.class)
public class SignatureRequestResourceIntTest {

    private static final String DEFAULT_REQUESTER_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_REQUESTER_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_CC_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_CC_EMAIL = "BBBBBBBBBB";

    @Autowired
    private SignatureRequestRepository signatureRequestRepository;


    @Autowired
    private SignatureRequestMapper signatureRequestMapper;
    

    @Autowired
    private SignatureRequestService signatureRequestService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSignatureRequestMockMvc;

    private SignatureRequest signatureRequest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SignatureRequestResource signatureRequestResource = new SignatureRequestResource(signatureRequestService);
        this.restSignatureRequestMockMvc = MockMvcBuilders.standaloneSetup(signatureRequestResource)
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
    public static SignatureRequest createEntity(EntityManager em) {
        SignatureRequest signatureRequest = new SignatureRequest()
            .requesterEmail(DEFAULT_REQUESTER_EMAIL)
            .title(DEFAULT_TITLE)
            .subject(DEFAULT_SUBJECT)
            .message(DEFAULT_MESSAGE)
            .ccEmail(DEFAULT_CC_EMAIL);
        return signatureRequest;
    }

    @Before
    public void initTest() {
        signatureRequest = createEntity(em);
    }

    @Test
    @Transactional
    public void createSignatureRequest() throws Exception {
        int databaseSizeBeforeCreate = signatureRequestRepository.findAll().size();

        // Create the SignatureRequest
        SignatureRequestDTO signatureRequestDTO = signatureRequestMapper.toDto(signatureRequest);
        restSignatureRequestMockMvc.perform(post("/api/signature-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(signatureRequestDTO)))
            .andExpect(status().isCreated());

        // Validate the SignatureRequest in the database
        List<SignatureRequest> signatureRequestList = signatureRequestRepository.findAll();
        assertThat(signatureRequestList).hasSize(databaseSizeBeforeCreate + 1);
        SignatureRequest testSignatureRequest = signatureRequestList.get(signatureRequestList.size() - 1);
        assertThat(testSignatureRequest.getRequesterEmail()).isEqualTo(DEFAULT_REQUESTER_EMAIL);
        assertThat(testSignatureRequest.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSignatureRequest.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testSignatureRequest.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testSignatureRequest.getCcEmail()).isEqualTo(DEFAULT_CC_EMAIL);
    }

    @Test
    @Transactional
    public void createSignatureRequestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = signatureRequestRepository.findAll().size();

        // Create the SignatureRequest with an existing ID
        signatureRequest.setId(1L);
        SignatureRequestDTO signatureRequestDTO = signatureRequestMapper.toDto(signatureRequest);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSignatureRequestMockMvc.perform(post("/api/signature-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(signatureRequestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SignatureRequest in the database
        List<SignatureRequest> signatureRequestList = signatureRequestRepository.findAll();
        assertThat(signatureRequestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkRequesterEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = signatureRequestRepository.findAll().size();
        // set the field null
        signatureRequest.setRequesterEmail(null);

        // Create the SignatureRequest, which fails.
        SignatureRequestDTO signatureRequestDTO = signatureRequestMapper.toDto(signatureRequest);

        restSignatureRequestMockMvc.perform(post("/api/signature-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(signatureRequestDTO)))
            .andExpect(status().isBadRequest());

        List<SignatureRequest> signatureRequestList = signatureRequestRepository.findAll();
        assertThat(signatureRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = signatureRequestRepository.findAll().size();
        // set the field null
        signatureRequest.setTitle(null);

        // Create the SignatureRequest, which fails.
        SignatureRequestDTO signatureRequestDTO = signatureRequestMapper.toDto(signatureRequest);

        restSignatureRequestMockMvc.perform(post("/api/signature-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(signatureRequestDTO)))
            .andExpect(status().isBadRequest());

        List<SignatureRequest> signatureRequestList = signatureRequestRepository.findAll();
        assertThat(signatureRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSignatureRequests() throws Exception {
        // Initialize the database
        signatureRequestRepository.saveAndFlush(signatureRequest);

        // Get all the signatureRequestList
        restSignatureRequestMockMvc.perform(get("/api/signature-requests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(signatureRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].requesterEmail").value(hasItem(DEFAULT_REQUESTER_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.toString())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].ccEmail").value(hasItem(DEFAULT_CC_EMAIL.toString())));
    }
    

    @Test
    @Transactional
    public void getSignatureRequest() throws Exception {
        // Initialize the database
        signatureRequestRepository.saveAndFlush(signatureRequest);

        // Get the signatureRequest
        restSignatureRequestMockMvc.perform(get("/api/signature-requests/{id}", signatureRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(signatureRequest.getId().intValue()))
            .andExpect(jsonPath("$.requesterEmail").value(DEFAULT_REQUESTER_EMAIL.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT.toString()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()))
            .andExpect(jsonPath("$.ccEmail").value(DEFAULT_CC_EMAIL.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingSignatureRequest() throws Exception {
        // Get the signatureRequest
        restSignatureRequestMockMvc.perform(get("/api/signature-requests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSignatureRequest() throws Exception {
        // Initialize the database
        signatureRequestRepository.saveAndFlush(signatureRequest);

        int databaseSizeBeforeUpdate = signatureRequestRepository.findAll().size();

        // Update the signatureRequest
        SignatureRequest updatedSignatureRequest = signatureRequestRepository.findById(signatureRequest.getId()).get();
        // Disconnect from session so that the updates on updatedSignatureRequest are not directly saved in db
        em.detach(updatedSignatureRequest);
        updatedSignatureRequest
            .requesterEmail(UPDATED_REQUESTER_EMAIL)
            .title(UPDATED_TITLE)
            .subject(UPDATED_SUBJECT)
            .message(UPDATED_MESSAGE)
            .ccEmail(UPDATED_CC_EMAIL);
        SignatureRequestDTO signatureRequestDTO = signatureRequestMapper.toDto(updatedSignatureRequest);

        restSignatureRequestMockMvc.perform(put("/api/signature-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(signatureRequestDTO)))
            .andExpect(status().isOk());

        // Validate the SignatureRequest in the database
        List<SignatureRequest> signatureRequestList = signatureRequestRepository.findAll();
        assertThat(signatureRequestList).hasSize(databaseSizeBeforeUpdate);
        SignatureRequest testSignatureRequest = signatureRequestList.get(signatureRequestList.size() - 1);
        assertThat(testSignatureRequest.getRequesterEmail()).isEqualTo(UPDATED_REQUESTER_EMAIL);
        assertThat(testSignatureRequest.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSignatureRequest.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testSignatureRequest.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testSignatureRequest.getCcEmail()).isEqualTo(UPDATED_CC_EMAIL);
    }

    @Test
    @Transactional
    public void updateNonExistingSignatureRequest() throws Exception {
        int databaseSizeBeforeUpdate = signatureRequestRepository.findAll().size();

        // Create the SignatureRequest
        SignatureRequestDTO signatureRequestDTO = signatureRequestMapper.toDto(signatureRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restSignatureRequestMockMvc.perform(put("/api/signature-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(signatureRequestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SignatureRequest in the database
        List<SignatureRequest> signatureRequestList = signatureRequestRepository.findAll();
        assertThat(signatureRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSignatureRequest() throws Exception {
        // Initialize the database
        signatureRequestRepository.saveAndFlush(signatureRequest);

        int databaseSizeBeforeDelete = signatureRequestRepository.findAll().size();

        // Get the signatureRequest
        restSignatureRequestMockMvc.perform(delete("/api/signature-requests/{id}", signatureRequest.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SignatureRequest> signatureRequestList = signatureRequestRepository.findAll();
        assertThat(signatureRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SignatureRequest.class);
        SignatureRequest signatureRequest1 = new SignatureRequest();
        signatureRequest1.setId(1L);
        SignatureRequest signatureRequest2 = new SignatureRequest();
        signatureRequest2.setId(signatureRequest1.getId());
        assertThat(signatureRequest1).isEqualTo(signatureRequest2);
        signatureRequest2.setId(2L);
        assertThat(signatureRequest1).isNotEqualTo(signatureRequest2);
        signatureRequest1.setId(null);
        assertThat(signatureRequest1).isNotEqualTo(signatureRequest2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SignatureRequestDTO.class);
        SignatureRequestDTO signatureRequestDTO1 = new SignatureRequestDTO();
        signatureRequestDTO1.setId(1L);
        SignatureRequestDTO signatureRequestDTO2 = new SignatureRequestDTO();
        assertThat(signatureRequestDTO1).isNotEqualTo(signatureRequestDTO2);
        signatureRequestDTO2.setId(signatureRequestDTO1.getId());
        assertThat(signatureRequestDTO1).isEqualTo(signatureRequestDTO2);
        signatureRequestDTO2.setId(2L);
        assertThat(signatureRequestDTO1).isNotEqualTo(signatureRequestDTO2);
        signatureRequestDTO1.setId(null);
        assertThat(signatureRequestDTO1).isNotEqualTo(signatureRequestDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(signatureRequestMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(signatureRequestMapper.fromId(null)).isNull();
    }
}
