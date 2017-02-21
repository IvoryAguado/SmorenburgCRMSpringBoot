package me.smorenburg.api.rest.media;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MediaRepository extends JpaRepository<Media, Long> {

    List<Media> findAll();

    Media findByFileName(String fileName);

}
