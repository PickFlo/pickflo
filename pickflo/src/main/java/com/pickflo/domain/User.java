package com.pickflo.domain;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
@Table(name = "USERS")
public class User implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	private Long id;
	
	@Basic(optional = false)
	@Column(name = "USER_EMAIL", unique = true)
	private String email;
	
	@Basic(optional = false)
	@Column(name = "USER_PASSWORD")
	private String password;
	
	@Basic(optional = false)
	@Column(name = "USER_NICKNAME")
	private String nickname;

	@Basic(optional = false)
	@Column(name = "USER_BIRTH")
	private LocalDate birth;
	
	@Basic(optional = false)
	@Column(name = "USER_GENDER")
	private Integer gender;   //0:남성 1:여성
	
	@Basic(optional = false)
	@Column(name = "USER_ROLE")
	private String userRole; 

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList();
	}

	@Override
	public String getUsername() {
		
		return this.email;
	}

}