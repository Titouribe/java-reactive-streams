package com.reactive.app;

import com.reactive.app.model.entities.Comment;
import com.reactive.app.model.entities.User;
import com.reactive.app.model.entities.UserWithComment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@SpringBootApplication
public class ReactiveStreamsPracticeApplication implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(ReactiveStreamsPracticeApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ReactiveStreamsPracticeApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        //iterable();

        //iterableWithFlatMap();

        //convertUserToString();

        //convertFluxToMono();

        //exampleUserWithCommentsFlatMap();

        //exampleUserWithCommentsZipWith();

        //exampleUserWithCommentsZipWith2();

        exampleZipWithAndRange();

    }

    public void iterable() throws Exception {

        List<String> namesList = List.of("Jorge", "Luis", "Andres", "Karolyn");

        Flux<String> namesFlux = Flux.fromIterable(namesList);

        Flux<User> userFlux = namesFlux.map(name -> new User(name, "default"))
                .filter(user -> user.getName().equalsIgnoreCase("karolyn"))
                .doOnNext(user -> {
                    if (user == null) {
                        throw new NullPointerException("El nombre no puede estar vacio");
                    }
                })
                .map(user -> {
                    user.setName(user.getName().toUpperCase());
                    return user;
                });


        namesFlux.subscribe(element -> logger.info(element),
                error -> logger.error(error.getMessage()),
                new Runnable() {
                    @Override
                    public void run() {
                        logger.info("Finalizo el flujo");
                    }
                });

        userFlux.subscribe(user -> logger.info(user.getName().concat(" ").concat(user.getLastName())));
    }

    public void iterableWithFlatMap() throws Exception {

        List<String> namesList = List.of("Jorge", "Luis", "Andres", "Karolyn");

        Flux.fromIterable(namesList).map(name -> new User(name, "default"))
                .flatMap(user -> {
                    if (user.getName().equalsIgnoreCase("karolyn")) {
                        return Mono.just(user);
                    }
                    return Mono.empty();
                })
                .map(user -> {
                    user.setName(user.getName().toUpperCase());
                    return user;
                })
                .subscribe(user -> logger.info(user.toString()));
    }

    public void convertUserToString() throws Exception {

        List<User> userList = List.of(
                new User("Jorge", "Uribe"),
                new User("Karolyn", "Mendez"),
                new User("Jose", "Restrepo"),
                new User("Karolyn", "Zapata")
        );

        Flux.fromIterable(userList).doOnNext(user -> {
                    if (user.getName() == null || user.getLastName() == null) {
                        throw new NullPointerException("Name or last name must not be empty");
                    }
                }).map(user -> user.getName().concat(" ").concat(user.getLastName()))
                .flatMap(nameString -> {
                    if (nameString.contains("Karolyn")) {
                        return Mono.just(nameString);
                    }
                    return Mono.empty();
                })
                .subscribe(logger::info);
    }

    public void convertFluxToMono() throws Exception {

        List<User> userList = List.of(
                new User("Jorge", "Uribe"),
                new User("Karolyn", "Mendez"),
                new User("Jose", "Restrepo"),
                new User("Karolyn", "Zapata")
        );

        Flux.fromIterable(userList)
                .collectList()
                .subscribe(user -> {
                    logger.info(user.toString());
                });
    }

    public void exampleUserWithCommentsFlatMap() throws Exception {

        Mono<User> userMono = Mono.fromCallable(() -> new User("user", "practice"));

        Mono<Comment> commentMono = Mono.fromCallable(() -> {

            Comment comment = new Comment();

            comment.addComment("test comment");
            comment.addComment("second test comment");
            comment.addComment("practice with reactive streams");

            return comment;
        });

        userMono.flatMap(user -> commentMono.map(comment -> new UserWithComment(user, comment)))
                .subscribe(usserComment -> logger.info(usserComment.toString()));
    }

    public void exampleUserWithCommentsZipWith() throws Exception {

        Mono<User> userMono = Mono.fromCallable(() -> new User("user", "practice"));

        Mono<Comment> commentMono = Mono.fromCallable(() -> {

            Comment comment = new Comment();

            comment.addComment("test comment");
            comment.addComment("second test comment");
            comment.addComment("practice with reactive streams");

            return comment;
        });

        userMono.zipWith(commentMono, UserWithComment::new)
                .subscribe(userWithComment -> logger.info(userWithComment.toString()));
    }

    public void exampleUserWithCommentsZipWith2() throws Exception {

        Mono<User> userMono = Mono.fromCallable(() -> new User("user", "practice"));

        Mono<Comment> commentMono = Mono.fromCallable(() -> {

            Comment comment = new Comment();

            comment.addComment("test comment");
            comment.addComment("second test comment");
            comment.addComment("practice with reactive streams");

            return comment;
        });

        userMono.zipWith(commentMono)
                .map(tuple -> new UserWithComment(tuple.getT1(), tuple.getT2()))
                .subscribe(userWithComment -> logger.info(userWithComment.toString()));
    }

    public void exampleZipWithAndRange() throws Exception {

        Flux.just(1,2,3,4,5,6,7,8,9,10)
                .map(number -> number * 2)
                .zipWith(Flux.range(0,4), (first, second) -> String.format("First: %d, second: %d", first, second))
                .subscribe(logger::info);

    }
}
