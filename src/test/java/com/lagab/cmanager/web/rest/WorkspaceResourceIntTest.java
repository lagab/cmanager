package com.lagab.cmanager.web.rest;

import com.lagab.cmanager.CmanagerApp;

import com.lagab.cmanager.domain.Workspace;
import com.lagab.cmanager.domain.Project;
import com.lagab.cmanager.repository.WorkspaceRepository;
import com.lagab.cmanager.service.WorkspaceService;
import com.lagab.cmanager.web.rest.errors.ExceptionTranslator;
import com.lagab.cmanager.service.dto.WorkspaceCriteria;
import com.lagab.cmanager.service.WorkspaceQueryService;

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

import com.lagab.cmanager.domain.enumeration.Visibility;
/**
 * Test class for the WorkspaceResource REST controller.
 *
 * @see WorkspaceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CmanagerApp.class)
public class WorkspaceResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_SLUG = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_AVATAR = "AAAAAAAAAA";
    private static final String UPDATED_AVATAR = "BBBBBBBBBB";

    private static final Boolean DEFAULT_REQUEST_ACESS = false;
    private static final Boolean UPDATED_REQUEST_ACESS = true;

    private static final Visibility DEFAULT_VISIBILITY = Visibility.PRIVATE;
    private static final Visibility UPDATED_VISIBILITY = Visibility.PROTECTED;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    

    @Autowired
    private WorkspaceService workspaceService;

    @Autowired
    private WorkspaceQueryService workspaceQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWorkspaceMockMvc;

    private Workspace workspace;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WorkspaceResource workspaceResource = new WorkspaceResource(workspaceService, workspaceQueryService);
        this.restWorkspaceMockMvc = MockMvcBuilders.standaloneSetup(workspaceResource)
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
    public static Workspace createEntity(EntityManager em) {
        Workspace workspace = new Workspace()
            .name(DEFAULT_NAME)
            .slug(DEFAULT_SLUG)
            .description(DEFAULT_DESCRIPTION)
            .avatar(DEFAULT_AVATAR)
            .requestAcess(DEFAULT_REQUEST_ACESS)
            .visibility(DEFAULT_VISIBILITY);
        return workspace;
    }

    @Before
    public void initTest() {
        workspace = createEntity(em);
    }

    @Test
    @Transactional
    public void createWorkspace() throws Exception {
        int databaseSizeBeforeCreate = workspaceRepository.findAll().size();

        // Create the Workspace
        restWorkspaceMockMvc.perform(post("/api/workspaces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workspace)))
            .andExpect(status().isCreated());

        // Validate the Workspace in the database
        List<Workspace> workspaceList = workspaceRepository.findAll();
        assertThat(workspaceList).hasSize(databaseSizeBeforeCreate + 1);
        Workspace testWorkspace = workspaceList.get(workspaceList.size() - 1);
        assertThat(testWorkspace.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWorkspace.getSlug()).isEqualTo(DEFAULT_SLUG);
        assertThat(testWorkspace.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWorkspace.getAvatar()).isEqualTo(DEFAULT_AVATAR);
        assertThat(testWorkspace.isRequestAcess()).isEqualTo(DEFAULT_REQUEST_ACESS);
        assertThat(testWorkspace.getVisibility()).isEqualTo(DEFAULT_VISIBILITY);
    }

    @Test
    @Transactional
    public void createWorkspaceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = workspaceRepository.findAll().size();

        // Create the Workspace with an existing ID
        workspace.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkspaceMockMvc.perform(post("/api/workspaces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workspace)))
            .andExpect(status().isBadRequest());

        // Validate the Workspace in the database
        List<Workspace> workspaceList = workspaceRepository.findAll();
        assertThat(workspaceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = workspaceRepository.findAll().size();
        // set the field null
        workspace.setName(null);

        // Create the Workspace, which fails.

        restWorkspaceMockMvc.perform(post("/api/workspaces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workspace)))
            .andExpect(status().isBadRequest());

        List<Workspace> workspaceList = workspaceRepository.findAll();
        assertThat(workspaceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSlugIsRequired() throws Exception {
        int databaseSizeBeforeTest = workspaceRepository.findAll().size();
        // set the field null
        workspace.setSlug(null);

        // Create the Workspace, which fails.

        restWorkspaceMockMvc.perform(post("/api/workspaces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workspace)))
            .andExpect(status().isBadRequest());

        List<Workspace> workspaceList = workspaceRepository.findAll();
        assertThat(workspaceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWorkspaces() throws Exception {
        // Initialize the database
        workspaceRepository.saveAndFlush(workspace);

        // Get all the workspaceList
        restWorkspaceMockMvc.perform(get("/api/workspaces?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workspace.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].avatar").value(hasItem(DEFAULT_AVATAR.toString())))
            .andExpect(jsonPath("$.[*].requestAcess").value(hasItem(DEFAULT_REQUEST_ACESS.booleanValue())))
            .andExpect(jsonPath("$.[*].visibility").value(hasItem(DEFAULT_VISIBILITY.toString())));
    }
    

    @Test
    @Transactional
    public void getWorkspace() throws Exception {
        // Initialize the database
        workspaceRepository.saveAndFlush(workspace);

        // Get the workspace
        restWorkspaceMockMvc.perform(get("/api/workspaces/{id}", workspace.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(workspace.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.avatar").value(DEFAULT_AVATAR.toString()))
            .andExpect(jsonPath("$.requestAcess").value(DEFAULT_REQUEST_ACESS.booleanValue()))
            .andExpect(jsonPath("$.visibility").value(DEFAULT_VISIBILITY.toString()));
    }

    @Test
    @Transactional
    public void getAllWorkspacesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        workspaceRepository.saveAndFlush(workspace);

        // Get all the workspaceList where name equals to DEFAULT_NAME
        defaultWorkspaceShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the workspaceList where name equals to UPDATED_NAME
        defaultWorkspaceShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllWorkspacesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        workspaceRepository.saveAndFlush(workspace);

        // Get all the workspaceList where name in DEFAULT_NAME or UPDATED_NAME
        defaultWorkspaceShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the workspaceList where name equals to UPDATED_NAME
        defaultWorkspaceShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllWorkspacesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        workspaceRepository.saveAndFlush(workspace);

        // Get all the workspaceList where name is not null
        defaultWorkspaceShouldBeFound("name.specified=true");

        // Get all the workspaceList where name is null
        defaultWorkspaceShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllWorkspacesBySlugIsEqualToSomething() throws Exception {
        // Initialize the database
        workspaceRepository.saveAndFlush(workspace);

        // Get all the workspaceList where slug equals to DEFAULT_SLUG
        defaultWorkspaceShouldBeFound("slug.equals=" + DEFAULT_SLUG);

        // Get all the workspaceList where slug equals to UPDATED_SLUG
        defaultWorkspaceShouldNotBeFound("slug.equals=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    public void getAllWorkspacesBySlugIsInShouldWork() throws Exception {
        // Initialize the database
        workspaceRepository.saveAndFlush(workspace);

        // Get all the workspaceList where slug in DEFAULT_SLUG or UPDATED_SLUG
        defaultWorkspaceShouldBeFound("slug.in=" + DEFAULT_SLUG + "," + UPDATED_SLUG);

        // Get all the workspaceList where slug equals to UPDATED_SLUG
        defaultWorkspaceShouldNotBeFound("slug.in=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    public void getAllWorkspacesBySlugIsNullOrNotNull() throws Exception {
        // Initialize the database
        workspaceRepository.saveAndFlush(workspace);

        // Get all the workspaceList where slug is not null
        defaultWorkspaceShouldBeFound("slug.specified=true");

        // Get all the workspaceList where slug is null
        defaultWorkspaceShouldNotBeFound("slug.specified=false");
    }

    @Test
    @Transactional
    public void getAllWorkspacesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        workspaceRepository.saveAndFlush(workspace);

        // Get all the workspaceList where description equals to DEFAULT_DESCRIPTION
        defaultWorkspaceShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the workspaceList where description equals to UPDATED_DESCRIPTION
        defaultWorkspaceShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllWorkspacesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        workspaceRepository.saveAndFlush(workspace);

        // Get all the workspaceList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultWorkspaceShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the workspaceList where description equals to UPDATED_DESCRIPTION
        defaultWorkspaceShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllWorkspacesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        workspaceRepository.saveAndFlush(workspace);

        // Get all the workspaceList where description is not null
        defaultWorkspaceShouldBeFound("description.specified=true");

        // Get all the workspaceList where description is null
        defaultWorkspaceShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllWorkspacesByAvatarIsEqualToSomething() throws Exception {
        // Initialize the database
        workspaceRepository.saveAndFlush(workspace);

        // Get all the workspaceList where avatar equals to DEFAULT_AVATAR
        defaultWorkspaceShouldBeFound("avatar.equals=" + DEFAULT_AVATAR);

        // Get all the workspaceList where avatar equals to UPDATED_AVATAR
        defaultWorkspaceShouldNotBeFound("avatar.equals=" + UPDATED_AVATAR);
    }

    @Test
    @Transactional
    public void getAllWorkspacesByAvatarIsInShouldWork() throws Exception {
        // Initialize the database
        workspaceRepository.saveAndFlush(workspace);

        // Get all the workspaceList where avatar in DEFAULT_AVATAR or UPDATED_AVATAR
        defaultWorkspaceShouldBeFound("avatar.in=" + DEFAULT_AVATAR + "," + UPDATED_AVATAR);

        // Get all the workspaceList where avatar equals to UPDATED_AVATAR
        defaultWorkspaceShouldNotBeFound("avatar.in=" + UPDATED_AVATAR);
    }

    @Test
    @Transactional
    public void getAllWorkspacesByAvatarIsNullOrNotNull() throws Exception {
        // Initialize the database
        workspaceRepository.saveAndFlush(workspace);

        // Get all the workspaceList where avatar is not null
        defaultWorkspaceShouldBeFound("avatar.specified=true");

        // Get all the workspaceList where avatar is null
        defaultWorkspaceShouldNotBeFound("avatar.specified=false");
    }

    @Test
    @Transactional
    public void getAllWorkspacesByRequestAcessIsEqualToSomething() throws Exception {
        // Initialize the database
        workspaceRepository.saveAndFlush(workspace);

        // Get all the workspaceList where requestAcess equals to DEFAULT_REQUEST_ACESS
        defaultWorkspaceShouldBeFound("requestAcess.equals=" + DEFAULT_REQUEST_ACESS);

        // Get all the workspaceList where requestAcess equals to UPDATED_REQUEST_ACESS
        defaultWorkspaceShouldNotBeFound("requestAcess.equals=" + UPDATED_REQUEST_ACESS);
    }

    @Test
    @Transactional
    public void getAllWorkspacesByRequestAcessIsInShouldWork() throws Exception {
        // Initialize the database
        workspaceRepository.saveAndFlush(workspace);

        // Get all the workspaceList where requestAcess in DEFAULT_REQUEST_ACESS or UPDATED_REQUEST_ACESS
        defaultWorkspaceShouldBeFound("requestAcess.in=" + DEFAULT_REQUEST_ACESS + "," + UPDATED_REQUEST_ACESS);

        // Get all the workspaceList where requestAcess equals to UPDATED_REQUEST_ACESS
        defaultWorkspaceShouldNotBeFound("requestAcess.in=" + UPDATED_REQUEST_ACESS);
    }

    @Test
    @Transactional
    public void getAllWorkspacesByRequestAcessIsNullOrNotNull() throws Exception {
        // Initialize the database
        workspaceRepository.saveAndFlush(workspace);

        // Get all the workspaceList where requestAcess is not null
        defaultWorkspaceShouldBeFound("requestAcess.specified=true");

        // Get all the workspaceList where requestAcess is null
        defaultWorkspaceShouldNotBeFound("requestAcess.specified=false");
    }

    @Test
    @Transactional
    public void getAllWorkspacesByVisibilityIsEqualToSomething() throws Exception {
        // Initialize the database
        workspaceRepository.saveAndFlush(workspace);

        // Get all the workspaceList where visibility equals to DEFAULT_VISIBILITY
        defaultWorkspaceShouldBeFound("visibility.equals=" + DEFAULT_VISIBILITY);

        // Get all the workspaceList where visibility equals to UPDATED_VISIBILITY
        defaultWorkspaceShouldNotBeFound("visibility.equals=" + UPDATED_VISIBILITY);
    }

    @Test
    @Transactional
    public void getAllWorkspacesByVisibilityIsInShouldWork() throws Exception {
        // Initialize the database
        workspaceRepository.saveAndFlush(workspace);

        // Get all the workspaceList where visibility in DEFAULT_VISIBILITY or UPDATED_VISIBILITY
        defaultWorkspaceShouldBeFound("visibility.in=" + DEFAULT_VISIBILITY + "," + UPDATED_VISIBILITY);

        // Get all the workspaceList where visibility equals to UPDATED_VISIBILITY
        defaultWorkspaceShouldNotBeFound("visibility.in=" + UPDATED_VISIBILITY);
    }

    @Test
    @Transactional
    public void getAllWorkspacesByVisibilityIsNullOrNotNull() throws Exception {
        // Initialize the database
        workspaceRepository.saveAndFlush(workspace);

        // Get all the workspaceList where visibility is not null
        defaultWorkspaceShouldBeFound("visibility.specified=true");

        // Get all the workspaceList where visibility is null
        defaultWorkspaceShouldNotBeFound("visibility.specified=false");
    }

    @Test
    @Transactional
    public void getAllWorkspacesByProjectsIsEqualToSomething() throws Exception {
        // Initialize the database
        Project projects = ProjectResourceIntTest.createEntity(em);
        em.persist(projects);
        em.flush();
        workspace.addProjects(projects);
        workspaceRepository.saveAndFlush(workspace);
        Long projectsId = projects.getId();

        // Get all the workspaceList where projects equals to projectsId
        defaultWorkspaceShouldBeFound("projectsId.equals=" + projectsId);

        // Get all the workspaceList where projects equals to projectsId + 1
        defaultWorkspaceShouldNotBeFound("projectsId.equals=" + (projectsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultWorkspaceShouldBeFound(String filter) throws Exception {
        restWorkspaceMockMvc.perform(get("/api/workspaces?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workspace.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].avatar").value(hasItem(DEFAULT_AVATAR.toString())))
            .andExpect(jsonPath("$.[*].requestAcess").value(hasItem(DEFAULT_REQUEST_ACESS.booleanValue())))
            .andExpect(jsonPath("$.[*].visibility").value(hasItem(DEFAULT_VISIBILITY.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultWorkspaceShouldNotBeFound(String filter) throws Exception {
        restWorkspaceMockMvc.perform(get("/api/workspaces?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingWorkspace() throws Exception {
        // Get the workspace
        restWorkspaceMockMvc.perform(get("/api/workspaces/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorkspace() throws Exception {
        // Initialize the database
        workspaceService.save(workspace);

        int databaseSizeBeforeUpdate = workspaceRepository.findAll().size();

        // Update the workspace
        Workspace updatedWorkspace = workspaceRepository.findById(workspace.getId()).get();
        // Disconnect from session so that the updates on updatedWorkspace are not directly saved in db
        em.detach(updatedWorkspace);
        updatedWorkspace
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .description(UPDATED_DESCRIPTION)
            .avatar(UPDATED_AVATAR)
            .requestAcess(UPDATED_REQUEST_ACESS)
            .visibility(UPDATED_VISIBILITY);

        restWorkspaceMockMvc.perform(put("/api/workspaces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWorkspace)))
            .andExpect(status().isOk());

        // Validate the Workspace in the database
        List<Workspace> workspaceList = workspaceRepository.findAll();
        assertThat(workspaceList).hasSize(databaseSizeBeforeUpdate);
        Workspace testWorkspace = workspaceList.get(workspaceList.size() - 1);
        assertThat(testWorkspace.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWorkspace.getSlug()).isEqualTo(UPDATED_SLUG);
        assertThat(testWorkspace.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWorkspace.getAvatar()).isEqualTo(UPDATED_AVATAR);
        assertThat(testWorkspace.isRequestAcess()).isEqualTo(UPDATED_REQUEST_ACESS);
        assertThat(testWorkspace.getVisibility()).isEqualTo(UPDATED_VISIBILITY);
    }

    @Test
    @Transactional
    public void updateNonExistingWorkspace() throws Exception {
        int databaseSizeBeforeUpdate = workspaceRepository.findAll().size();

        // Create the Workspace

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restWorkspaceMockMvc.perform(put("/api/workspaces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workspace)))
            .andExpect(status().isBadRequest());

        // Validate the Workspace in the database
        List<Workspace> workspaceList = workspaceRepository.findAll();
        assertThat(workspaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWorkspace() throws Exception {
        // Initialize the database
        workspaceService.save(workspace);

        int databaseSizeBeforeDelete = workspaceRepository.findAll().size();

        // Get the workspace
        restWorkspaceMockMvc.perform(delete("/api/workspaces/{id}", workspace.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Workspace> workspaceList = workspaceRepository.findAll();
        assertThat(workspaceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Workspace.class);
        Workspace workspace1 = new Workspace();
        workspace1.setId(1L);
        Workspace workspace2 = new Workspace();
        workspace2.setId(workspace1.getId());
        assertThat(workspace1).isEqualTo(workspace2);
        workspace2.setId(2L);
        assertThat(workspace1).isNotEqualTo(workspace2);
        workspace1.setId(null);
        assertThat(workspace1).isNotEqualTo(workspace2);
    }
}
