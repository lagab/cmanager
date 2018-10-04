package com.lagab.cmanager.web.rest;

import com.lagab.cmanager.CmanagerApp;

import com.lagab.cmanager.domain.SignatureRequest;
import com.lagab.cmanager.domain.Signature;
import com.lagab.cmanager.domain.Contract;
import com.lagab.cmanager.repository.SignatureRequestRepository;
import com.lagab.cmanager.service.SignatureRequestService;
import com.lagab.cmanager.web.rest.errors.ExceptionTranslator;
import com.lagab.cmanager.service.dto.SignatureRequestCriteria;
import com.lagab.cmanager.service.SignatureRequestQueryService;

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
    private SignatureRequestService signatureRequestService;

    @Autowired
    private SignatureRequestQueryService signatureRequestQueryService;

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
        final SignatureRequestResource signatureRequestResource = new SignatureRequestResource(signatureRequestService, signatureRequestQueryService);
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
        restSignatureRequestMockMvc.perform(post("/api/signature-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(signatureRequest)))
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

        // An entity with an existing ID cannot be created, so this API call must fail
        restSignatureRequestMockMvc.perform(post("/api/signature-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(signatureRequest)))
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

        restSignatureRequestMockMvc.perform(post("/api/signature-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(signatureRequest)))
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

        restSignatureRequestMockMvc.perform(post("/api/signature-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(signatureRequest)))
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
    public void getAllSignatureRequestsByRequesterEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        signatureRequestRepository.saveAndFlush(signatureRequest);

        // Get all the signatureRequestList where requesterEmail equals to DEFAULT_REQUESTER_EMAIL
        defaultSignatureRequestShouldBeFound("requesterEmail.equals=" + DEFAULT_REQUESTER_EMAIL);

        // Get all the signatureRequestList where requesterEmail equals to UPDATED_REQUESTER_EMAIL
        defaultSignatureRequestShouldNotBeFound("requesterEmail.equals=" + UPDATED_REQUESTER_EMAIL);
    }

    @Test
    @Transactional
    public void getAllSignatureRequestsByRequesterEmailIsInShouldWork() throws Exception {
        // Initialize the database
        signatureRequestRepository.saveAndFlush(signatureRequest);

        // Get all the signatureRequestList where requesterEmail in DEFAULT_REQUESTER_EMAIL or UPDATED_REQUESTER_EMAIL
        defaultSignatureRequestShouldBeFound("requesterEmail.in=" + DEFAULT_REQUESTER_EMAIL + "," + UPDATED_REQUESTER_EMAIL);

        // Get all the signatureRequestList where requesterEmail equals to UPDATED_REQUESTER_EMAIL
        defaultSignatureRequestShouldNotBeFound("requesterEmail.in=" + UPDATED_REQUESTER_EMAIL);
    }

    @Test
    @Transactional
    public void getAllSignatureRequestsByRequesterEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        signatureRequestRepository.saveAndFlush(signatureRequest);

        // Get all the signatureRequestList where requesterEmail is not null
        defaultSignatureRequestShouldBeFound("requesterEmail.specified=true");

        // Get all the signatureRequestList where requesterEmail is null
        defaultSignatureRequestShouldNotBeFound("requesterEmail.specified=false");
    }

    @Test
    @Transactional
    public void getAllSignatureRequestsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        signatureRequestRepository.saveAndFlush(signatureRequest);

        // Get all the signatureRequestList where title equals to DEFAULT_TITLE
        defaultSignatureRequestShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the signatureRequestList where title equals to UPDATED_TITLE
        defaultSignatureRequestShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllSignatureRequestsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        signatureRequestRepository.saveAndFlush(signatureRequest);

        // Get all the signatureRequestList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultSignatureRequestShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the signatureRequestList where title equals to UPDATED_TITLE
        defaultSignatureRequestShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllSignatureRequestsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        signatureRequestRepository.saveAndFlush(signatureRequest);

        // Get all the signatureRequestList where title is not null
        defaultSignatureRequestShouldBeFound("title.specified=true");

        // Get all the signatureRequestList where title is null
        defaultSignatureRequestShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllSignatureRequestsBySubjectIsEqualToSomething() throws Exception {
        // Initialize the database
        signatureRequestRepository.saveAndFlush(signatureRequest);

        // Get all the signatureRequestList where subject equals to DEFAULT_SUBJECT
        defaultSignatureRequestShouldBeFound("subject.equals=" + DEFAULT_SUBJECT);

        // Get all the signatureRequestList where subject equals to UPDATED_SUBJECT
        defaultSignatureRequestShouldNotBeFound("subject.equals=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    public void getAllSignatureRequestsBySubjectIsInShouldWork() throws Exception {
        // Initialize the database
        signatureRequestRepository.saveAndFlush(signatureRequest);

        // Get all the signatureRequestList where subject in DEFAULT_SUBJECT or UPDATED_SUBJECT
        defaultSignatureRequestShouldBeFound("subject.in=" + DEFAULT_SUBJECT + "," + UPDATED_SUBJECT);

        // Get all the signatureRequestList where subject equals to UPDATED_SUBJECT
        defaultSignatureRequestShouldNotBeFound("subject.in=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    public void getAllSignatureRequestsBySubjectIsNullOrNotNull() throws Exception {
        // Initialize the database
        signatureRequestRepository.saveAndFlush(signatureRequest);

        // Get all the signatureRequestList where subject is not null
        defaultSignatureRequestShouldBeFound("subject.specified=true");

        // Get all the signatureRequestList where subject is null
        defaultSignatureRequestShouldNotBeFound("subject.specified=false");
    }

    @Test
    @Transactional
    public void getAllSignatureRequestsByMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        signatureRequestRepository.saveAndFlush(signatureRequest);

        // Get all the signatureRequestList where message equals to DEFAULT_MESSAGE
        defaultSignatureRequestShouldBeFound("message.equals=" + DEFAULT_MESSAGE);

        // Get all the signatureRequestList where message equals to UPDATED_MESSAGE
        defaultSignatureRequestShouldNotBeFound("message.equals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllSignatureRequestsByMessageIsInShouldWork() throws Exception {
        // Initialize the database
        signatureRequestRepository.saveAndFlush(signatureRequest);

        // Get all the signatureRequestList where message in DEFAULT_MESSAGE or UPDATED_MESSAGE
        defaultSignatureRequestShouldBeFound("message.in=" + DEFAULT_MESSAGE + "," + UPDATED_MESSAGE);

        // Get all the signatureRequestList where message equals to UPDATED_MESSAGE
        defaultSignatureRequestShouldNotBeFound("message.in=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllSignatureRequestsByMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        signatureRequestRepository.saveAndFlush(signatureRequest);

        // Get all the signatureRequestList where message is not null
        defaultSignatureRequestShouldBeFound("message.specified=true");

        // Get all the signatureRequestList where message is null
        defaultSignatureRequestShouldNotBeFound("message.specified=false");
    }

    @Test
    @Transactional
    public void getAllSignatureRequestsByCcEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        signatureRequestRepository.saveAndFlush(signatureRequest);

        // Get all the signatureRequestList where ccEmail equals to DEFAULT_CC_EMAIL
        defaultSignatureRequestShouldBeFound("ccEmail.equals=" + DEFAULT_CC_EMAIL);

        // Get all the signatureRequestList where ccEmail equals to UPDATED_CC_EMAIL
        defaultSignatureRequestShouldNotBeFound("ccEmail.equals=" + UPDATED_CC_EMAIL);
    }

    @Test
    @Transactional
    public void getAllSignatureRequestsByCcEmailIsInShouldWork() throws Exception {
        // Initialize the database
        signatureRequestRepository.saveAndFlush(signatureRequest);

        // Get all the signatureRequestList where ccEmail in DEFAULT_CC_EMAIL or UPDATED_CC_EMAIL
        defaultSignatureRequestShouldBeFound("ccEmail.in=" + DEFAULT_CC_EMAIL + "," + UPDATED_CC_EMAIL);

        // Get all the signatureRequestList where ccEmail equals to UPDATED_CC_EMAIL
        defaultSignatureRequestShouldNotBeFound("ccEmail.in=" + UPDATED_CC_EMAIL);
    }

    @Test
    @Transactional
    public void getAllSignatureRequestsByCcEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        signatureRequestRepository.saveAndFlush(signatureRequest);

        // Get all the signatureRequestList where ccEmail is not null
        defaultSignatureRequestShouldBeFound("ccEmail.specified=true");

        // Get all the signatureRequestList where ccEmail is null
        defaultSignatureRequestShouldNotBeFound("ccEmail.specified=false");
    }

    @Test
    @Transactional
    public void getAllSignatureRequestsBySignaturesIsEqualToSomething() throws Exception {
        // Initialize the database
        Signature signatures = SignatureResourceIntTest.createEntity(em);
        em.persist(signatures);
        em.flush();
        signatureRequest.addSignatures(signatures);
        signatureRequestRepository.saveAndFlush(signatureRequest);
        Long signaturesId = signatures.getId();

        // Get all the signatureRequestList where signatures equals to signaturesId
        defaultSignatureRequestShouldBeFound("signaturesId.equals=" + signaturesId);

        // Get all the signatureRequestList where signatures equals to signaturesId + 1
        defaultSignatureRequestShouldNotBeFound("signaturesId.equals=" + (signaturesId + 1));
    }


    @Test
    @Transactional
    public void getAllSignatureRequestsByContractIsEqualToSomething() throws Exception {
        // Initialize the database
        Contract contract = ContractResourceIntTest.createEntity(em);
        em.persist(contract);
        em.flush();
        signatureRequest.setContract(contract);
        signatureRequestRepository.saveAndFlush(signatureRequest);
        Long contractId = contract.getId();

        // Get all the signatureRequestList where contract equals to contractId
        defaultSignatureRequestShouldBeFound("contractId.equals=" + contractId);

        // Get all the signatureRequestList where contract equals to contractId + 1
        defaultSignatureRequestShouldNotBeFound("contractId.equals=" + (contractId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSignatureRequestShouldBeFound(String filter) throws Exception {
        restSignatureRequestMockMvc.perform(get("/api/signature-requests?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(signatureRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].requesterEmail").value(hasItem(DEFAULT_REQUESTER_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.toString())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].ccEmail").value(hasItem(DEFAULT_CC_EMAIL.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSignatureRequestShouldNotBeFound(String filter) throws Exception {
        restSignatureRequestMockMvc.perform(get("/api/signature-requests?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
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
        signatureRequestService.save(signatureRequest);

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

        restSignatureRequestMockMvc.perform(put("/api/signature-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSignatureRequest)))
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

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restSignatureRequestMockMvc.perform(put("/api/signature-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(signatureRequest)))
            .andExpect(status().isBadRequest());

        // Validate the SignatureRequest in the database
        List<SignatureRequest> signatureRequestList = signatureRequestRepository.findAll();
        assertThat(signatureRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSignatureRequest() throws Exception {
        // Initialize the database
        signatureRequestService.save(signatureRequest);

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
}
