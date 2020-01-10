package org.acme;

import javax.persistence.Column;
import javax.persistence.Entity;
import io.quarkus.hibernate.orm.panache.PanacheEntity;;

/**
 * DeveloperPanache
 */
@Entity
public class DeveloperPanache extends PanacheEntity {

    @Column(unique = true)
    public String name;

    @Column()
    public int age;
}