package com.lagab.cmanager.ws.controller;

/**
 * @author gabriel
 * @since 25/07/2018.
 */
import com.lagab.cmanager.persistance.model.Namespace;
import com.lagab.cmanager.persistance.repository.NamespaceRepository;
import com.lagab.cmanager.ws.exception.NamespaceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/namespaces")
public class NamespaceRestController {

    private NamespaceRepository namespaceRepository;

    @Autowired
    public NamespaceRestController(NamespaceRepository namespaceRepository) {
        this.namespaceRepository = namespaceRepository;
    }

    @GetMapping
    public  Iterable<Namespace> getAllNamespaces() {
        // This returns a JSON or XML with the users

        /*namespaceRepository.findAll((root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("path"), "toto"));
            predicates.add(builder.equal(root.get("parent").get("id"), 1));
            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        }, PageRequest.of(1, 20));*/

        return namespaceRepository.findAll();
    }
    @RequestMapping(path = "s/{slug}")
    public Namespace showNamespace(@PathVariable("slug") String slug){
        return namespaceRepository.findByPath(slug);
    }
    @RequestMapping(path = "{id}")
    public Namespace getNamespace(@PathVariable("id") Integer id){
        return namespaceRepository.findById(id.longValue()).orElseThrow(NamespaceNotFoundException::new);
    }
}
