package com.ipiecoles.java.java350.repository;

import com.ipiecoles.java.java350.model.Employe;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
class EmployeRepositoryTest {

    @Autowired
    EmployeRepository employeRepository;

    @BeforeEach
    void setUp(){
        employeRepository.deleteAll();
    }

    @Test
    void testFindLastMatricule0Employe(){
        //Given
        //When
        String lastMatricule = employeRepository.findLastMatricule();

        //Then
        Assertions.assertThat(lastMatricule).isNull();
    }

    @Test
    void testFindLastMatriculeUnEmploye(){
        //Given
        employeRepository.save(new Employe("Doe", "John", "T46521", LocalDate.now(), 2000d, 1, 1.0));

        //When
        String lastMatricule = employeRepository.findLastMatricule();

        //Then
        Assertions.assertThat(lastMatricule).isEqualTo("46521");
    }

    @Test
    void testFindLastMatriculeTroisEmployes(){
        //Given
        employeRepository.save(new Employe("Doe", "John", "T76544", LocalDate.now(), 1200d, 1, 1.0));
        employeRepository.save(new Employe("Doe", "Jane", "M65735", LocalDate.now(), 1200d, 1, 1.0));
        employeRepository.save(new Employe("Doe", "John", "C65378", LocalDate.now(), 3000d, 1, 1.0));

        //When
        String lastMatricule = employeRepository.findLastMatricule();

        //Then
        Assertions.assertThat(lastMatricule).isEqualTo("76544");
    }

    @Test
    void testAvgPerformanceWhereMatriculeStartsWith(){
        //Given
        employeRepository.save(new Employe("Laurent", "Ruquier", "C10000", LocalDate.now(), 2000d, 7, 1.0));
        employeRepository.save(new Employe("Harry", "Potter", "C20000", LocalDate.now(), 1300d, 3, 1.0));
        employeRepository.save(new Employe("Macron", "Emmanuel", "C30000", LocalDate.now(), 1000d, 5, 1.0));

        //When
        Double performanceAvg = employeRepository.avgPerformanceWhereMatriculeStartsWith("C");

        //Then
        Assertions.assertThat(performanceAvg).isEqualTo(5d);
    }
}