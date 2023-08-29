package com.automatedemailing.app.automatedemailing.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.automatedemailing.app.automatedemailing.Dto.RegistrationDto;
import com.automatedemailing.app.automatedemailing.entity.Student;



public interface StudentRepository extends JpaRepository<Student, Long> {

	Optional<Student> findByEmail(String email);

	Optional<Student> findByFullName(String fullName);

	Optional<Student> findById(Long id);
	
	
	void deleteById(Long id);

	void save(RegistrationDto registerDto);
}
