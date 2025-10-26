package com.chalabysolutions.sudoku.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Entity
@Data
@Table(name = "user_entity")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message="{NotNull.User.firstName}")
    private String firstName;

    @NotNull(message="{NotNull.User.lastName}")
    private String lastName;

    @NotNull(message="{NotNull.User.email}")
    private String email;

}
