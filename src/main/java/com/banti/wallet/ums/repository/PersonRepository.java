package com.banti.wallet.ums.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import com.banti.wallet.ums.model.Person;

public interface PersonRepository extends JpaRepository<Person,Long>
{
    @Query("select p from Person p where p.mobileNo = :mobileNo ")
	Person findByMobileNo(@Param("mobileNo") String mobileNo);

    @Query("select p from Person p where p.userName = :userName ")
	Person findPersonByUserName(@Param("userName")String userName);


}
