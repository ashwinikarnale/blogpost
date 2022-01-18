package com.ash.project.blogpost.repository;

import com.ash.project.blogpost.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    @Query(value = "select * from users u where u.email=?1", nativeQuery = true)
    User findByUserName(String email);

    User findById(int id);

    List<User> findAll();

    User findByEmail(String email);
}
