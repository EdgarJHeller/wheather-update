package com.hejheller.weatherupdate.student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    private StudentService underTest;

    @BeforeEach
    void setUp() {
        underTest = new StudentService(studentRepository);
    }

    @Test
    void canGetStudent() {
        // when
        underTest.getAllStudents();

        // then
        verify(studentRepository).findAll();
    }

    @Test
    void canAddNewStudent() {
        // given
        Student jamila = new Student(
                "Jamila",
                "jamila@gmail.com",
                LocalDate.of(1987, Month.MAY, 26
                )
        );

        // when
        underTest.addNewStudent(jamila);

        // then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(studentArgumentCaptor.capture());
        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(jamila);

    }

    @Test
    void willThrowWhenEmailIsTaken() {
        // given
        Student student = new Student(
                "Jamila",
                "jamila@gmail.com",
                LocalDate.of(1987, Month.MAY, 26
                )
        );
        given(studentRepository.selectStudentExitsByEmail(student.getEmail()))
                .willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> underTest.addNewStudent(student))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Email " + student.getEmail() + " taken");
        verify(studentRepository, never()).save(any());
    }

    @Test
    void canDeleteStudentByIdThatExists() {
        // given
        Student student = new Student(
                1L,
                "Jamila",
                "jamila@gmail.com",
                LocalDate.of(1987, Month.MAY, 26
                )
        );
        given(studentRepository.existsById(student.getId()))
                .willReturn(true);

        // when
        underTest.deleteStudent(student.getId());

        // then
        ArgumentCaptor<Long> studentIdArgumentCaptor = ArgumentCaptor.forClass(student.getId().getClass());
        verify(studentRepository).deleteById(studentIdArgumentCaptor.capture());
        Long capturedStudentId = studentIdArgumentCaptor.getValue();
        assertThat(capturedStudentId).isEqualTo(student.getId());
    }

    @Test
    void willThrowWhenDeleteStudentByIdThatDoesNotExist() {
        // given
        Student student = new Student(
                1L,
                "Jamila",
                "jamila@gmail.com",
                LocalDate.of(1987, Month.MAY, 26
                )
        );

        // when
        // then
        assertThatThrownBy(() -> underTest.deleteStudent(student.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Student with id " + student.getId() + " does not exist");
        verify(studentRepository, never()).deleteById(any());
    }

    @Test
    void canUpdateStudentThatExistsWithNewName() {
        // given
        Student student = new Student(
                1L,
                "Jamila",
                "jamila@gmail.com",
                LocalDate.of(1987, Month.MAY, 26
                )
        );
        String newName = "Jamilia";
        given(studentRepository.findById(student.getId()))
                .willReturn(Optional.of(student));

        // when
        underTest.updateStudent(1L, newName, "jamilia@gmail.com");
        // then
        String actual = (studentRepository.findById(1L).isPresent() ? studentRepository.findById(1L).get().getName() : "");
        assertThat(actual).isEqualTo(newName);
    }

    @Test
    void canUpdateStudentThatExistsWithNewEmail() {
        // given
        Student student = new Student(
                1L,
                "Jamila",
                "jamila@gmail.com",
                LocalDate.of(1987, Month.MAY, 26
                )
        );
        String newEmail = "jamilia@gmail.com";
        given(studentRepository.findById(student.getId()))
                .willReturn(Optional.of(student));

        // when
        underTest.updateStudent(1L, "Jamila", newEmail);
        // then
        String actual = (studentRepository.findById(1L).isPresent() ? studentRepository.findById(1L).get().getEmail() : "");
        assertThat(actual).isEqualTo(newEmail);
    }

    @Test
    void willThrowWhenUpdateEmailToAlreadyExists() {
        // given
        Student student = new Student(
                1L,
                "Jamila",
                "jamila@gmail.com",
                LocalDate.of(1987, Month.MAY, 26
                )
        );
        String newEmail = "jamilia@gmail.com";
        given(studentRepository.findById(student.getId()))
                .willReturn(Optional.of(student));
        given(studentRepository.selectStudentExitsByEmail(newEmail))
                .willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> underTest.updateStudent(1L, "Jamila", newEmail))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("email taken");
    }

}