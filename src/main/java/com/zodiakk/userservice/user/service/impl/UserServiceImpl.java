package com.zodiakk.userservice.user.service.impl;

import com.zodiakk.userservice.common.cache.CacheNames;
import com.zodiakk.userservice.user.dto.request.CreateUserRequestDto;
import com.zodiakk.userservice.user.dto.request.UpdateUserRequestDto;
import com.zodiakk.userservice.user.dto.request.UserFilterRequestDto;
import com.zodiakk.userservice.user.dto.response.UserResponseDto;
import com.zodiakk.userservice.user.dto.response.UserShortResponseDto;
import com.zodiakk.userservice.user.entity.User;
import com.zodiakk.userservice.user.exception.UserAlreadyExistsException;
import com.zodiakk.userservice.user.exception.UserNotFoundException;
import com.zodiakk.userservice.user.mapper.UserMapper;
import com.zodiakk.userservice.user.repository.UserRepository;
import com.zodiakk.userservice.user.repository.specification.UserSpecification;
import com.zodiakk.userservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    @Transactional
    @CachePut(value = CacheNames.USERS, key = "#result.id")
    public UserResponseDto create(CreateUserRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new UserAlreadyExistsException(request.getEmail());

        User user = userMapper.toEntity(request);

        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    @Override
    @Cacheable(value = CacheNames.USERS, key = "#id")
    public UserResponseDto getById(UUID id) {
        return userRepository.findByIdWithCards(id).map(userMapper::toResponse)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    @Cacheable(value = CacheNames.USERS_SHORT, key = "#id")
    public UserShortResponseDto getShortById(UUID id) {
        return userRepository.findById(id).map(userMapper::toShortResponse)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public Page<UserShortResponseDto> getAllShort(UserFilterRequestDto filter, Pageable pageable) {
        Specification<User> specification = buildSpecification(filter);

        return userRepository.findAll(specification, pageable).map(userMapper::toShortResponse);
    }

    @Override
    public Page<UserResponseDto> getAll(UserFilterRequestDto filter, Pageable pageable) {
        Specification<User> specification = buildSpecification(filter);

        return userRepository.findAll(specification, pageable).map(userMapper::toResponse);
    }

    @Override
    @Transactional
    @CacheEvict(value = {CacheNames.USERS, CacheNames.USERS_SHORT}, key = "#id")
    public UserResponseDto update(UUID id, UpdateUserRequestDto request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userMapper.updateToEntity(request, user);

        User updatedUser = userRepository.save(user);

        return userMapper.toResponse(updatedUser);
    }

    @Override
    @Transactional
    @CacheEvict(value = {CacheNames.USERS, CacheNames.USERS_SHORT}, key = "#id")
    public void activate(UUID id) {
        if (!userRepository.existsById(id)) throw new UserNotFoundException(id);

        userRepository.activate(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = {CacheNames.USERS, CacheNames.USERS_SHORT}, key = "#id")
    public void deactivate(UUID id) {
        if (!userRepository.existsById(id)) throw new UserNotFoundException(id);

        userRepository.deactivate(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = CacheNames.USERS, key = "#id")
    public void delete(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userRepository.delete(user);
    }

    private Specification<User> buildSpecification(UserFilterRequestDto filter) {
        return Specification.allOf(
                UserSpecification.hasName(filter.getName()),
                UserSpecification.hasSurname(filter.getSurname()),
                UserSpecification.hasActive(filter.getActive())
        );
    }
}