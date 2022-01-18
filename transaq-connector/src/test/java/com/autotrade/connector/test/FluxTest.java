package com.autotrade.connector.test;

import lombok.SneakyThrows;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class FluxTest {

    public Flux<String> fruits() {
        return Flux.fromIterable(List.of("Mango", "Orange", "Banana"));
    }

    public Mono<String> fruit() {
        return Mono.just("Apple");
    }

    @SneakyThrows
    public static void main(String[] args) {
        FluxTest fluxTest = new FluxTest();

        StepVerifier.create(fluxTest.fruits())
                .expectNext("Mango", "Orange", "Banana")
                .verifyComplete();

        fluxTest.fruits()
//                .publishOn(Schedulers.parallel())
//                .delayElements(Duration.ofMillis(new Random().nextInt(1000)))
                .map(String::toUpperCase)
                .filter(s -> s.length() > 5)
//                .onErrorContinue()    //TODO
//                .doOnNext(s -> {
//                    System.out.println("doOnNext " + s);
//                })
                .log("beforeFlatMap.")
//                .flatMap(s -> Flux.just(s.split(""))
//                        .delayElements(Duration.ofMillis(new Random().nextInt(500))))
                .concatMap(s -> Flux.just(s.split(""))
                        .delayElements(Duration.ofMillis(new Random().nextInt(1000))))
                .log("afterFlatMap.")
                .subscribe(s -> {
                    System.out.println("subscribe " + s);
                });

        System.out.println("after flux");

//        fluxTest.fruit()
////                .doOnNext(s -> {
////                    System.out.println("Mono doOnNext " + s);
////                })
//                .log("beforeFlatMap.")
//                .flatMapMany(s -> Flux.just(s.split("")))
//                .log("beforeFlatMap.")
//                .subscribe(s -> {
//                    System.out.println("Mono subscribe " + s);
//                });


        Thread.sleep(15_000);
    }
}
