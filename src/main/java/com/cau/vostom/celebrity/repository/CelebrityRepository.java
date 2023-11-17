package com.cau.vostom.celebrity.repository;

import com.cau.vostom.celebrity.domain.Celebrity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CelebrityRepository extends JpaRepository<Celebrity, Long> {

    List<Celebrity> findByCelebrityName(String celebrityName);
}
