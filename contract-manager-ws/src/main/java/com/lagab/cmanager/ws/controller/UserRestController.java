package com.lagab.cmanager.ws.controller;

/**
 * @author gabriel
 * @since 25/07/2018.
 */
import com.lagab.cmanager.persistance.model.User;
import com.lagab.cmanager.services.UserService;
import com.lagab.cmanager.ws.util.PathPattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserRestController extends AbstractCrudController<User,Long>  {

    private UserService userService;

    /**
     * Constructor.
     *
     * @param service the crud service.
     */
    public UserRestController(UserService service) {
        super(service);
        this.userService = service;
    }


    public UserService getUserService() {
        return userService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public Page<User> findAll(Specification specification, Pageable pageable) {
        return userService.findAll(specification, pageable);
    }

    @RequestMapping(path = "/{id:" + PathPattern.ID_PATTERN + "}", method = RequestMethod.GET)
    public Resource<User> get(@PathVariable("id") Long id){
        return asResource(findById(id));
    }

    @RequestMapping(path = "/{name:" + PathPattern.NAME_PATTERN + "}", method = RequestMethod.GET)
    public Resource<User> get(@PathVariable String username){
        return asResource(userService.findByUsername(username).get());
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Long> create(@RequestBody User user){
        return checkAndCreate(user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/{id:" + PathPattern.ID_PATTERN + "}", method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody User user){
        return checkAndUpdate(id,user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/{id:" + PathPattern.ID_PATTERN + "}", method = RequestMethod.DELETE)
    public ResponseEntity<Void>  deleteNamespace(@PathVariable("id") Long id){
        return delete(id);
    }
}
