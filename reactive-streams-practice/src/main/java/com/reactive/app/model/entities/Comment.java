package com.reactive.app.model.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Comment {

    private List<String> comments;

    public Comment(){
        this.comments = new ArrayList<>();
    }

    public void addComment(String comment) {
        this.comments.add(comment);
    }

    @Override
    public String toString() {
        return "comments: " +  comments;
    }
}
