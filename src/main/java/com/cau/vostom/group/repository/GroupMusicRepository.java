package com.cau.vostom.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cau.vostom.group.domain.GroupMusic;

import java.util.List;

public interface GroupMusicRepository extends JpaRepository<GroupMusic, Long>{

    boolean existsByMusicIdAndGroupId(Long musicId, Long groupId);
    List<GroupMusic> findByGroupId(Long groupId);
    void deleteByMusicIdAndGroupId(Long musicId, Long groupId);
}
