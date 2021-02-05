/*
 * package com.banti.wallet.ums.jwtSpringSecutity.services;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.security.crypto.password.PasswordEncoder; import
 * org.springframework.stereotype.Service;
 * 
 * import com.banti.wallet.ums.jwtSpringSecutity.models.JwtUser; import
 * com.banti.wallet.ums.jwtSpringSecutity.repositories.JwtUserRepository;
 * 
 * @Service public class JwtUserService {
 * 
 * @Autowired private JwtUserRepository jwtUserRepository;
 * 
 * @Autowired private PasswordEncoder bcryptEncoder;
 * 
 * public void save(JwtUser jwtUser) { //TO SAVE PASSWORD IN ENCRYPTED FORM
 * jwtUser.setPassword(bcryptEncoder.encode(jwtUser.getPassword()));
 * jwtUserRepository.save(jwtUser); }
 * 
 * public JwtUser getUser(String username) { return
 * jwtUserRepository.findById(username).get(); }
 * 
 * }
 */