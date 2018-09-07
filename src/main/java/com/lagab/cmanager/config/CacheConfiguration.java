package com.lagab.cmanager.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.lagab.cmanager.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(com.lagab.cmanager.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(com.lagab.cmanager.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.lagab.cmanager.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.lagab.cmanager.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.lagab.cmanager.domain.Workspace.class.getName(), jcacheConfiguration);
            cm.createCache(com.lagab.cmanager.domain.Workspace.class.getName() + ".projects", jcacheConfiguration);
            cm.createCache(com.lagab.cmanager.domain.Project.class.getName(), jcacheConfiguration);
            cm.createCache(com.lagab.cmanager.domain.Project.class.getName() + ".contracts", jcacheConfiguration);
            cm.createCache(com.lagab.cmanager.domain.Project.class.getName() + ".contactFolders", jcacheConfiguration);
            cm.createCache(com.lagab.cmanager.domain.Contract.class.getName(), jcacheConfiguration);
            cm.createCache(com.lagab.cmanager.domain.Contract.class.getName() + ".requests", jcacheConfiguration);
            cm.createCache(com.lagab.cmanager.domain.SignatureRequest.class.getName(), jcacheConfiguration);
            cm.createCache(com.lagab.cmanager.domain.SignatureRequest.class.getName() + ".signatures", jcacheConfiguration);
            cm.createCache(com.lagab.cmanager.domain.Signature.class.getName(), jcacheConfiguration);
            cm.createCache(com.lagab.cmanager.domain.ContactFolder.class.getName(), jcacheConfiguration);
            cm.createCache(com.lagab.cmanager.domain.ContactFolder.class.getName() + ".lists", jcacheConfiguration);
            cm.createCache(com.lagab.cmanager.domain.ContactList.class.getName(), jcacheConfiguration);
            cm.createCache(com.lagab.cmanager.domain.ContactList.class.getName() + ".contacts", jcacheConfiguration);
            cm.createCache(com.lagab.cmanager.domain.Contact.class.getName(), jcacheConfiguration);
            cm.createCache(com.lagab.cmanager.domain.EntityAuditEvent.class.getName(), jcacheConfiguration);
            cm.createCache(com.lagab.cmanager.domain.Attachment.class.getName(), jcacheConfiguration);
            cm.createCache(com.lagab.cmanager.domain.Attachment.class.getName() + ".tests", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
