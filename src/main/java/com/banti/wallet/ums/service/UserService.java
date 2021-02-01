package com.banti.wallet.ums.service;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.banti.wallet.ums.model.User;
import com.banti.wallet.ums.repository.UserRepository;

@Service
@Transactional
public class UserService {
	
    @Autowired
    private UserRepository userRepo;
    
    public User findByMobileNo(String request) {
       return userRepo.findByMobileNo(request);
    }
    
    public List<User> listAll() {
        return userRepo.findAll();
    }
     
    public void save(User user) {
        userRepo.save(user);
    }
     
    public User get(Long id) {
        return userRepo.findById(id).get();
    }
     
    public void delete(Long id) {
        userRepo.deleteById(id);
    }
}