package com.lagab.cmanager.services;

import com.lagab.cmanager.persistance.model.User;
import com.lagab.cmanager.persistance.repository.UserRepository;
import com.lagab.cmanager.services.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author gabriel
 * @since 31/07/2018.
 */
@Service("userService")
public class UserService implements DefaultService<User,Long>{
    /**
     *
     */
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public long count() {
        return userRepository.count();
    }

    @Override
    public long count(Specification specification) {
        return userRepository.count(specification);
    }

    @Override
    public Long create(User entity) {
        return null;
    }

    @Override
    public void update(Long id, User user) {
        User persisted  = userRepository.findById(id).orElseThrow(() -> new NotFoundException(User.class, id));

        persisted.setUsername(user.getUsername());
        persisted.setEmail(user.getEmail());
        persisted.setName(user.getName());
        persisted.setFirstName(user.getFirstName());
        persisted.setAddress(user.getAddress());
        persisted.setZipCode(user.getZipCode());
        persisted.setCity(user.getCity());
        persisted.setPhone(user.getPhone());
        persisted.setFacebookUid(user.getFacebookUid());
        persisted.setFacebookName(user.getFacebookName());
        persisted.setFacebookData(user.getFacebookData());
        persisted.setGoogleUid(user.getGoogleUid());
        persisted.setGoogleName(user.getGoogleName());
        persisted.setGoogleData(user.getGoogleData());
        persisted.setFacebookAccessToken(user.getFacebookAccessToken());
        persisted.setGoogleAccessToken(user.getGoogleAccessToken());
        persisted.setBiography(user.getBiography());
        persisted.setGender(user.getGender());
        persisted.setDateOfBirth(user.getDateOfBirth());

        persisted.setRoles(user.getRoles());

        userRepository.save(persisted);

    }

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> findAll(Specification specification, Pageable pageable) {
        return userRepository.findAll(specification,pageable);
    }

    @Override
    public Optional<User> findOne(Specification specification) {
        return userRepository.findOne(specification);
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void delete(Long id) {
        User persisted = userRepository.findById(id).orElseThrow(() -> new NotFoundException(User.class, id));
        userRepository.delete(persisted);
    }


    }
