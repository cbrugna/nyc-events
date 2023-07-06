package com.caseybrugna.nyc_events.repository;

import com.caseybrugna.nyc_events.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource
public interface ArtistRepository extends JpaRepository<Artist, String> {
    // Define custom queries or methods if needed
}
