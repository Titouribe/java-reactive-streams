package com.reactive.app.model.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserWithComment {

    private User user;
    private Comment comment;

    public UserWithComment(User user, Comment comment) {
        this.user = user;
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "UserWithComment{" +
                "user=" + user +
                ", comment=" + comment +
                '}';
    }
}
