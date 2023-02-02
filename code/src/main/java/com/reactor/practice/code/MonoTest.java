package com.reactor.practice.code;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

@Slf4j
public class MonoTest {
   public static void main(String[] args) throws InterruptedException  {
      final long start1 = System.nanoTime();
      getMonoUser1().zipWith(getMonoUser2())
            .map(tuple -> {
               return tuple.getT1() + tuple.getT2();
            }).doFinally(endType -> log.info("Time taken ZIP: " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start1) + " milliseconds."))
            .subscribe(log::info);


//      long beforeTime = System.currentTimeMillis();
//      Mono<String> test = getMonoUser1().zipWith(getMonoUser2())
//            .map(tuple -> {
//               return tuple.getT1() + tuple.getT2();
//            });
//
//
//      long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
//      long secDiffTime = (afterTime - beforeTime)/1000; //두 시간에 차 계산
//
      final long start = System.nanoTime();
      long beforeTime = System.currentTimeMillis();
      getMonoUser1().flatMap(
            user -> getMonoUser2().map(name -> user + name)
      ).doFinally(endType -> log.info("Time taken FlatMap: " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start) + " milliseconds."))
      .subscribe(log::info);

      long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
      long secDiffTime = (afterTime - beforeTime)/1000; //두 시간에 차 계산
      System.out.println("시간차이(m) : "+secDiffTime);

      System.out.println("?? ----- ");

      sleep(5000);

//      long beforeTime = System.currentTimeMillis();
//      Mono.zip(getMonoUser1(), getMonoUser2()).flatMap(tuple -> {
//         return Mono.just(tuple.getT1() + tuple.getT2());
//      }).subscribe(log::info);
//
//      long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
//      long secDiffTime = (afterTime - beforeTime)/1000; //두 시간에 차 계산
//      System.out.println("시간차이(m) : "+secDiffTime);
//
//      System.out.println("?? ----- ");

//      Mono<String> mono1 = Mono.just(getUser2());
//      Mono<String> mono2 = Mono.just(getUser3());
//
//      long beforeTime1 = System.currentTimeMillis();
//      Mono<String> test1 = mono1.zipWith(mono2)
//            .map(tuple -> {
//               return tuple.getT1() + tuple.getT2();
//            });
//
//      test1.subscribe(log::info);
//      long afterTime1 = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
//      long secDiffTime1 = (afterTime1 - beforeTime1)/1000; //두 시간에 차 계산
//      System.out.println("시간차이(m) : "+secDiffTime1);
//
//      System.out.println("??");

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

   public static String getUser2() throws InterruptedException {
      log.info("hello2");
      sleep(1000);
      log.info("hello2 done");
      return "hotire2";
   }

   public static Mono<String> getMonoUser1() {
      return Mono.fromCallable(MonoTest::getUser2).subscribeOn(Schedulers.boundedElastic());
//      return Mono.fromCallable(MonoTest::getUser2);
   }

   public static Mono<String> getMonoUser2() {
      return Mono.fromCallable(MonoTest::getUser3).subscribeOn(Schedulers.boundedElastic());
//      return Mono.fromCallable(MonoTest::getUser3);
   }
}
