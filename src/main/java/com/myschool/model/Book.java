package com.myschool.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.*;

@Entity(name = "Book")
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at")
    @NotNull
    private LocalDateTime createdAt;

    @Column(name = "book_name")
    @NotNull
    private String bookName;

    @ManyToOne
    @JoinColumn(
            name = "student_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "student_book_fk"
            )
    )
    private Student student;

    public Book() {
    }

    public Book(String bookName,
                LocalDateTime createdAt) {
        this.createdAt = createdAt;
        this.bookName = bookName;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Student getStudent() {
        return student;
    }

    public String getBookName() {
        return bookName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", bookName='" + bookName + '\'' +
                ", student=" + student +
                '}';
    }
}
