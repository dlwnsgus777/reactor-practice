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
      final long start1 = System.nanoTime();

      Flux.range(1, 5)
            .flatMap(MonoMapTest::getMonoUser1)
            .collectList()
            .doFinally(endType -> log.info("Time taken FLAT MAP: " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start1) + " milliseconds."))
            .subscribe(System.out::println);

//      getMonoUser1().zipWith(getMonoUser2())
//            .map(tuple -> {
//               return tuple.getT1() + tuple.getT2();
//            }).doFinally(endType -> log.info("Time taken ZIP: " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start1) + " milliseconds."))
//            .subscribe(log::info);

//      getMonoUser1().flatMap(
//            user -> getMonoUser2().map(name -> user + name)
//      ).doFinally(endType -> log.info("Time taken FlatMap: " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start) + " milliseconds."))
//            .subscribe(log::info);

      System.out.println("?? ----- ");

      final long start = System.nanoTime();

      Mono.just(1)
            .map(i -> getMonoUser1(i).subscribe())
            .doFinally(endType -> log.info("Time taken MAP: " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start) + " milliseconds."))
            .subscribe(System.out::println);

      sleep(10000);

   }

   public static String getUser() throws InterruptedException {
      System.out.println("hello");
      sleep(1000);
      return "hotire";
   }


   public static String getUser3() throws InterruptedException {
      log.info("hello3");
      sleep(3000);
      log.info("hello3 done");
      return "hotire3";
   }

   public static String getUser2(int i) throws InterruptedException {
      log.info("hello2");
      sleep(1000);
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
