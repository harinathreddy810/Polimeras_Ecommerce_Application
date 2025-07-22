//package com.Polimeras.Service;
//
//import com.Polimeras.Entity.Users;
//import com.Polimeras.Repository.UsersRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//public class MyUserDetailService implements UserDetailsService {
//    @Autowired
//    private UsersRepository usersRepository;
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<Users> user = usersRepository.findByUsername(username) ;
//        if (user.isPresent()){
//            var userObj = user.get();
//            return User.builder()
//                    .username(userObj.getUsername())
//                    .password(userObj.getPassword()) //1111
//                    .roles(userObj.getRole())
//                    .build();
//        }else {
//            throw new UsernameNotFoundException(username);
//        }
//    }
//}

package com.Polimeras.Service;

import com.Polimeras.Entity.Users;
import com.Polimeras.Repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        /*
         *  Spring Security flags
         *  ---------------------
         *  disabled               – if true ➜ cannot authenticate
         *  accountLocked          – if true ➜ cannot authenticate
         *  accountExpired         – if true ➜ cannot authenticate
         *  credentialsExpired     – if true ➜ cannot authenticate
         */
        boolean isBlockedRole   = "BLOCKED".equalsIgnoreCase(user.getRole());
        boolean isEnabled       = user.isEnabled();          // your DB column
        boolean accountNonLocked = !isBlockedRole;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;

        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(
                        user.getRole().startsWith("ROLE_")
                                ? user.getRole()
                                : "ROLE_" + user.getRole())   // guarantees prefix
        );

        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .disabled(!isEnabled)
                .accountLocked(!accountNonLocked)
                .accountExpired(!accountNonExpired)
                .credentialsExpired(!credentialsNonExpired)
                .build();
    }
}




