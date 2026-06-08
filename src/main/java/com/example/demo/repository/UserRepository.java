package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u JOIN FETCH u.role WHERE u.id = :id")
    Optional<User> findByIdWithRole(@Param("id") Long id);

    @Query("SELECT u FROM User u JOIN FETCH u.role WHERE u.email = :email")
    Optional<User> findByEmailWithRole(@Param("email") String email);
    
    List<User> findByRoleIdAndIsActiveTrue(@Param("roleId") Long roleId);
    
    List<User> findByIsActiveTrueOrderByFullName();
    
    @Query("SELECT u FROM User u WHERE u.isActive = true ORDER BY u.fullName")
    List<User> findAllActive();
    
    boolean existsByEmail(String email);
}
