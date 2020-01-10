package org.acme;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

/**
 * DeveloperRepository
 */
@ApplicationScoped
public class DeveloperRepository implements PanacheRepository<Developer> {

    @Transactional
    public Developer create(Developer developer){
        persist(developer);
        return developer;
    }

    public Developer findByName(String name){
        return find("name", name).firstResult();
    }
}