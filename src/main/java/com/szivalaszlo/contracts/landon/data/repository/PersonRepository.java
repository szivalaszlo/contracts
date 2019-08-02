package com.szivalaszlo.contracts.landon.data.repository;

import com.szivalaszlo.contracts.landon.data.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;


public interface PersonRepository extends JpaRepository<Person, Integer> {
    Person findById(int id);
    List<Person> findAll();
    List<Person> findByLastName(String lastName);
    List<Person> findByFirstName(String firstName);
    List<Person> findByFirstNameAndLastName(String firstName, String lastName);
    List<Person> findByDateOfBirth(LocalDate dateOfBirth);
}
