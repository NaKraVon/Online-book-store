package com.example.demo.service.user.impl;

import com.example.demo.dto.user.UserRegistrationRequestDto;
import com.example.demo.dto.user.UserResponseDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.exception.RegistrationException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.model.enums.RoleName;
import com.example.demo.repository.role.RoleRepository;
import com.example.demo.repository.user.UserRepository;
import com.example.demo.service.user.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException(
                    "Email already exists: " + requestDto.getEmail()
            );
        }
        User user = userMapper.toUser(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setShippingAddress(requestDto.getShippingAddress());
        Role userRole = roleRepository.findByRoleName(RoleName.USER)
                .orElseThrow(() -> new EntityNotFoundException("Can't find role by name: "
                        + RoleName.USER.name()));
        user.setRoles(Set.of(userRole));
        return userMapper.toUserResponse(userRepository.save(user));
    }
}
