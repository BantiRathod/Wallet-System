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
    private UserRepository repo;
     
    public List<User> listAll() {
        return repo.findAll();
    }
     
    public void save(User user) {
        repo.save(user);
    }
     
    public User get(Long id) {
        return repo.findById(id).get();
    }
     
    public void delete(Long id) {
        repo.deleteById(id);
    }
}