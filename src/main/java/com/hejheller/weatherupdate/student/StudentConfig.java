package com.hejheller.weatherupdate.student;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Configuration
public class StudentConfig {

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository repository) {
        return args -> {
            Student hauke = new Student(
                    1L,
                    "Hauke",
                    "hauke.heller@gmail.com",
                    LocalDate.of(1987, Month.MAY, 26
                    )
            );
            Student maris = new Student(
                    "Maris",
                    "maris.dornia@gmail.com",
                    LocalDate.of(1986, Month.MAY, 30
                    )
            );

            repository.saveAll(List.of(hauke, maris));
        };
    }
}
