package com.cau.vostom.user.repository;

import com.cau.vostom.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{

}
