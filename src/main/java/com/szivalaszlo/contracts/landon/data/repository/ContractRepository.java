package com.szivalaszlo.contracts.landon.data.repository;

import com.szivalaszlo.contracts.landon.data.entity.Contract;
import com.szivalaszlo.contracts.landon.data.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Integer> {
    List<Contract> findAll();
    Contract findById(int id);
    List<Contract> findByStartDate(LocalDate startDate);
    List<Contract> findByEndDate(LocalDate endDate);
    List<Contract> findByStartDateBefore(LocalDate targetDate);
    List<Contract> findByEndDateAfter(LocalDate targetDate);
    //List<Contract> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate targetDate);
    Contract findByDocumentNumber(String documentNumber);



}
