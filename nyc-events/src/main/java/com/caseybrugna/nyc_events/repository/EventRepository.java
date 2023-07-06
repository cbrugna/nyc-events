package com.caseybrugna.nyc_events.repository;

import com.caseybrugna.nyc_events.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource
public interface EventRepository extends JpaRepository<Event, String> {
    // Define custom queries or methods if needed
}
