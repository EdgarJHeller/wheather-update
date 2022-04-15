package com.hejheller.weatherupdate.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldFindStudentByEmail() {
        // given
        String email = "jamila@gmail.com";
        Student jamila = new Student(
                "Jamila",
                email,
                LocalDate.of(1987, Month.MAY, 26
                )
        );
        underTest.save(jamila);

        // when
        boolean expected = underTest.selectStudentExitsByEmail(email);

        // then
        assertThat(expected).isTrue();
    }

    @Test
    void itShouldNotFindStudentByEmail() {
        //given
        String email = "jamila@gmail.com";

        //when
        boolean expected = underTest.selectStudentExitsByEmail(email);

        //then
        assertThat(expected).isFalse();
    }

}