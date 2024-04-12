package mipt.tinkoff.demo;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;



//
//public class Demo003 {
//
//    public static void main(String[] args) {
//        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
//        IntStream.range(0, 1000)
//            .forEach(i ->
//                executor.submit(() -> {
//                    Thread.sleep(Duration.ofSeconds(1));
//                    for(int i1 = 0; i1 < 1000; i1++) {
//                        System.out.print(i + "->" + i1 + "; ");
//                    }
//                    System.out.println("");
//                    return i;
//                })
//            );
//        }
//    }
//
//}
