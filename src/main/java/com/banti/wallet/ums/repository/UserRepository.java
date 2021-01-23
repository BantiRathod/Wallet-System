package com.banti.wallet.ums.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banti.wallet.ums.model.User;

public interface UserRepository extends JpaRepository<User,Integer>
{ 
}
