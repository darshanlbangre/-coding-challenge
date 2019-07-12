package com.coding.challenge.repository;

import com.coding.challenge.entity.Subscriber;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * The HealthRepository JPA interface.
 */
@Repository
public interface HealthRepository extends CrudRepository<Subscriber, Long> {

}
