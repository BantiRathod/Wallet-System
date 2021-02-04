package com.banti.wallet.ums.service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banti.wallet.ums.enums.PersonStatus;
import com.banti.wallet.ums.model.User;
import com.banti.wallet.ums.repository.UserRepository;
import com.banti.wallet.ums.requestEntities.UserRequestEntity;

@Service
@Transactional
public class UserService {
	
    @Autowired
    private UserRepository userRepo;
    
    public User findByMobileNo(String mobileNo) {
       return userRepo.findByMobileNo(mobileNo);
    }
    
    public List<User> listAll() {
        return userRepo.findAll();
    }
     
    public void updateUser(User user) throws NoSuchElementException
    {
    	 User existUser =  userRepo.findById(user.getUserId()).get();
    	 
         existUser.setUserName(user.getUserName());                         
         existUser.setFirstName(user.getFirstName());                       
         existUser.setLastName(user.getLastName());                          
         existUser.setAddress(user.getAddress());
         existUser.setMobileNo(user.getMobileNo());
         userRepo.save(existUser);
    }
    public User saveUser(UserRequestEntity user) {
    	User realUser=new User();
    	realUser.setUserName(user.getUserName());
    	realUser.setAddress(user.getAddress());
    	realUser.setFirstName(user.getFirstName());
    	realUser.setLastName(user.getLastName());
    	realUser.setMobileNo(user.getMobileNo());
    	realUser.setEmail(user.getEmail());
    	realUser.setRegisterDate(new Date());
    	realUser.setStatus(PersonStatus.ACTIVE.name());
        return userRepo.save(realUser);
    }
     
    public User get(Long id) {
        return userRepo.findById(id).get();
    }
     
    public void delete(Long id) {
        userRepo.deleteById(id);
    }
}