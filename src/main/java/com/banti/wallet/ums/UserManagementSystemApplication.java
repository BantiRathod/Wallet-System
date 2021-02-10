package com.banti.wallet.ums;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@EntityScan("com.mine.alien") // path of the entity model
@EnableJpaRepositories("com.banti.wallet.ums.repository")
@EnableElasticsearchRepositories("com.banti.wallet.ums.elasticsearch.repositories")
@SpringBootApplication
public class UserManagementSystemApplication {

	public static void main(String[] args) 
	{
		SpringApplication.run(UserManagementSystemApplication.class, args);
	}
	
}
