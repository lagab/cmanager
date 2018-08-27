package com.lagab.cmanager.ws.controller;

/**
 * @author gabriel
 * @since 25/07/2018.
 */
import com.lagab.cmanager.persistance.model.Namespace;
import com.lagab.cmanager.persistance.repository.NamespaceRepository;
import com.lagab.cmanager.services.exception.NotFoundException;
import com.lagab.cmanager.ws.util.PathPattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/namespaces")
public class NamespaceRestController {

    private NamespaceRepository namespaceRepository;

    @Autowired
    public NamespaceRestController(NamespaceRepository namespaceRepository) {
        this.namespaceRepository = namespaceRepository;
    }

    @GetMapping
    public  Iterable<Namespace> findAll() {
        // This returns a JSON or XML with the users

        /*namespaceRepository.findAll((root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("path"), "toto"));
            predicates.add(builder.equal(root.get("parent").get("id"), 1));
            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        }, PageRequest.of(1, 20));*/

        return namespaceRepository.findAll();
    }

    @RequestMapping(path = "/{id:" + PathPattern.ID_PATTERN + "}", method = RequestMethod.GET)
    public Namespace get(@PathVariable("id") Integer id){
        return namespaceRepository.findById(id.longValue()).orElseThrow(() -> new NotFoundException(Namespace.class, id));
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createNamespace(@RequestBody Namespace namespace, HttpServletRequest request, HttpServletResponse response){
        Namespace namespaceCreated = namespaceRepository.save(namespace);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/" + namespaceCreated.getId()).build().toUri();
        System.out.println(uri);
        return ResponseEntity.created(uri).build();
    }

    @RequestMapping(path = "/{id:" + PathPattern.ID_PATTERN + "}", method = RequestMethod.PUT)
    public Namespace update(@PathVariable Long id, @RequestBody Namespace namespace){
        Optional<Namespace> namespaceOptional = namespaceRepository.findById(id);
        if(!namespaceOptional.isPresent()){
            return  null;
        }
        Namespace currentNamespace = namespaceOptional.get();
        currentNamespace.setName(namespace.getName());
        currentNamespace.setDescription(namespace.getDescription());
        namespace.setId(id);
        return namespaceRepository.save(namespace);
    }

    @RequestMapping(path = "/{id:" + PathPattern.ID_PATTERN + "}", method = RequestMethod.DELETE)
    public void  deleteNamespace(@PathVariable("id") Integer id){
        namespaceRepository.findById(id.longValue()).orElseThrow(() -> new NotFoundException(Namespace.class, id));
        namespaceRepository.deleteById(id.longValue());
    }
}
