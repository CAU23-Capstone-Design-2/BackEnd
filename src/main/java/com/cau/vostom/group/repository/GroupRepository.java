package com.cau.vostom.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cau.vostom.group.domain.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {

}
