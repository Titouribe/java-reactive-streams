package com.reactive.app.model.entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String name;
    private String lastName;

    @Override
    public String toString() {
        return this.name + " " + this.lastName;
    }
}
