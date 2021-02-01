package com.banti.wallet.ums.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.banti.wallet.ums.model.User;

public interface UserRepository extends JpaRepository<User,Long>
{
    @Query("select u from User u where u.mobileNo= :mobileNo ")
	User findByMobileNo(@Param("mobileNo")String mobileNo);
}
