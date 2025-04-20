package com.pickflo.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pickflo.domain.User;
import com.pickflo.dto.CustomUserDetails;
import com.pickflo.dto.UserSignupDto;
import com.pickflo.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncoder;


	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		log.info("email={}", email);
		User user = userRepo.findByEmail(email).orElseThrow();
		if (user != null) {
			return new CustomUserDetails(user); // entity.get();
		} else {
			throw new UsernameNotFoundException(email + ": 일치하는 이메일이 없습니다");
		}
	}

	@Transactional
	public User create(UserSignupDto dto) {
		log.info("create(user={})", dto);

		// DTO를 엔티티로 변환
		User user = dto.toEntity(passwordEncoder);

		// 엔티티를 데이터베이스에 저장
		return userRepo.save(user);
	}

	@Transactional
	public User findByEmail(String email) {
		Optional<User> memberOptional = userRepo.findByEmail(email);
		return memberOptional.orElse(null);
	}

	public boolean checkPassword(String password) {
		Optional<User> member = userRepo.findByPassword(password);
		if (!member.isPresent()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean checkEmail(String email) {
		Optional<User> member = userRepo.findByEmail(email);
		if (!member.isPresent()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean checkNickname(String nickname) {
		Optional<User> member = userRepo.findByNickname(nickname);
		if (!member.isPresent()) {
			return true;
		} else {
			return false;
		}
	}

}
