package com.reactor.practice.code;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

@Slf4j
public class MonoMapTest {
   public static void main(String[] args) throws InterruptedException  {
//      final long start1 = System.nanoTime();
//
//      Flux.range(1, 5)
//            .flatMap(MonoMapTest::getMonoUser1)
//            .collectList()
//            .doFinally(endType -> log.info("Time taken FLAT MAP: " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start1) + " milliseconds."))
//            .subscribe(System.out::println);

      System.out.println("?? ----- ");

      final long start = System.nanoTime();

//      Flux.range(1, 5)
//            .concatMap(MonoMapTest::getMonoUser1)
//            .collectList()
//            .doFinally(endType -> log.info("Time taken MAP: " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start) + " milliseconds."))
//            .subscribe(System.out::println);


      Flux.range(1, 5)
            .map(i -> {
               try {
                  i = plus(i);
               } catch (InterruptedException e) {
                  e.printStackTrace();
               }
               return i;
            })
            .flatMap(MonoMapTest::getMonoUser1)
            .collectList()
            .doFinally(endType -> log.info("Time taken MAP: " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start) + " milliseconds."))
            .subscribe(System.out::println);


      sleep(500000000);

   }

   public static int plus(int i) throws InterruptedException {
      System.out.println("hello");
      sleep(1000);
      return i + 1;
   }


   public static String getUser3() throws InterruptedException {
      log.info("hello3");
      sleep(3000);
      log.info("hello3 done");
      return "hotire3";
   }

   public static String getUser2(int i) throws InterruptedException {
      log.info("hello2");
      sleep(i * 1000L);
      log.info("hello2 done");
      return "hotire " + i;
   }

   public static Mono<String> getMonoUser1(int i) {
      return Mono.fromCallable(() -> getUser2(i)).subscribeOn(Schedulers.boundedElastic());
   }

   public static Mono<String> getMonoUser2() {
      return Mono.fromCallable(MonoMapTest::getUser3).subscribeOn(Schedulers.boundedElastic());
//      return Mono.fromCallable(MonoTest::getUser3);
   }
}
