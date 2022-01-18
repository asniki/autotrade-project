package com.autotrade.connector.test;

import com.autotrade.connector.model.callback.Callback;
import com.sun.jna.Pointer;
import lombok.SneakyThrows;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class FluxTest2 {

    private static FluxSink<String> handler;

    @SneakyThrows
    public static void main(String[] args) {

        // 1
//        Flux
//                .<String>create(emitter -> {
//                    handler = emitter;
//                }, FluxSink.OverflowStrategy.BUFFER)
//                .log("OriginalPublisher")
//                .publishOn(Schedulers.parallel())
//                .subscribe(s -> System.out.println("consumed: " + s + "; thread: " + Thread.currentThread().getName())); // невозможно получить ссылку на Flux

        // 2
//        ConnectableFlux<String> connectableFlux = Flux
//                .<String>create(emitter -> {
//                    handler = emitter;
//                }, FluxSink.OverflowStrategy.BUFFER)
//                .log("OriginalPublisher.")
//                .publishOn(Schedulers.parallel()) //TODO нужно ли, если сообщения кладутся в отдельном потоке
//                .publish();
//
//        connectableFlux
//                .log("ConnectablePublisher.")
//                .subscribe(s -> System.out.println("consumed: " + s + "; thread: " + Thread.currentThread().getName()));
//
//        connectableFlux.connect(); // не понятно, кто будет определять, когда пора вызвать connect()

        // 3
        Flux<String> flux = Flux
                .<String>create(emitter -> {
                    handler = emitter;
                }, FluxSink.OverflowStrategy.BUFFER)
                .log("OriginalPublisher.")
                .publishOn(Schedulers.parallel())
                .publish()
                .autoConnect(0);

        flux.subscribe(s -> System.out.println("consumed1: " + s + "; thread: " + Thread.currentThread().getName()));
        flux.subscribe(s -> System.out.println("consumed2: " + s + "; thread: " + Thread.currentThread().getName()));



        Map<Boolean, List<Consumer<? super String>>> callbackConsumers = Map.of(
                true, List.of(
                        s -> System.out.println("first consumer: " + s + "; thread: " + Thread.currentThread().getName()),
                        s -> System.out.println("second consumer: " + s + "; thread: " + Thread.currentThread().getName())
                ));

        callbackConsumers.keySet().forEach(k -> {
            Flux<String> filter = flux.filter(s -> Integer.parseInt(s) % 10 < 5 == k);
            callbackConsumers.get(k).forEach(filter::subscribe);
        });

        flux
                .filter(s -> !callbackConsumers.containsKey(Integer.parseInt(s) % 10 < 5))
                .subscribe(s -> System.out.println("unhandled: " + s));


//        flux
//                .groupBy(s -> Integer.parseInt(s) % 10 < 5)
//                .map(g -> g.publishOn(Schedulers.parallel()).log("Map."))
//                .subscribe(f -> f.subscribe(s -> System.out.println("consumed in map: " + s + "; thread: " + Thread.currentThread().getName())));
//                .subscribe(f -> f.subscribe(s -> System.out.println("consumed in map: " + s + "; key: " + f.key() + "; thread: " + Thread.currentThread().getName())));
//                .subscribe(f -> {
//                    if(callbackConsumers.containsKey(f.key()))
//                        callbackConsumers.get(f.key()).forEach(f::subscribe);
//                    else
//                        System.out.println("unhandled callback: " + f.key());
//                });

//                .flatMap(groupedFlux -> groupedFlux.publishOn(Schedulers.parallel()).log("Map."));


        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            int i = 0;
            System.out.println("producer thread: " + Thread.currentThread().getName());
            while(true) {
                System.out.println("produced: " + i);
                handler.next(String.valueOf(i++));
                try {
                    Thread.sleep(new Random().nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        System.out.println("main thread: " + Thread.currentThread().getName());

//        Thread.sleep(10_000);
    }
}
