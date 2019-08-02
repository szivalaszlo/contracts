package com.szivalaszlo.contracts.landon.data.repository;

import com.szivalaszlo.contracts.landon.data.entity.Contactdetails;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface ContactdetailsRepository extends JpaRepository<Contactdetails, Integer> {
    List<Contactdetails> findAll();
    Contactdetails findById(int id);
}
