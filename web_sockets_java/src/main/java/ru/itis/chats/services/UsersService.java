package ru.itis.chats.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.itis.chats.dto.UserDto;
import ru.itis.chats.models.Role;
import ru.itis.chats.models.User;
import ru.itis.chats.repositories.UsersRepository;

import java.util.HashMap;
import java.util.Map;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public UserDto registerUserByName(String username) {
        User user = User.builder()
                .username(username)
                .role(Role.USER)
                .build();
        usersRepository.saveUser(user);

        Map<String, Object> claims = new HashMap<>();

        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        claims.put("role", user.getRole().toString());

        String token = Jwts.builder().setClaims(claims)
                .setSubject(user.getId().toString())
                // .setIssuedAt(createdDate)
                .setId(claims.get("userId").toString())
                //.setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();

        // to user
        return UserDto.builder()
                .username(user.getUsername())
                .token(token)
                .role(user.getRole())
                .build();
    }

}
