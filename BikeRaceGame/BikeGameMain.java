import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

class Shared {
    static int goalDistanceMeters;
    static AtomicInteger rankPtr = new AtomicInteger(1);
    static CountDownLatch startLatch;
}

/// BIKER ///
class Biker implements Callable<Scorecard> {

    private final String name;

    public Biker(String name) {
        this.name = name;
    }

    public Scorecard call() throws Exception {
        Shared.startLatch.await(); 

        LocalTime startTime = LocalTime.now();
        int distanceCovered = 0;
        int milestone = 100;

        while (distanceCovered < Shared.goalDistanceMeters) {
            int speed = (int) (50 + 150 * Math.random());
            distanceCovered += speed;

            if (distanceCovered >= milestone && milestone < Shared.goalDistanceMeters) {
                System.out.println(name + " crossed " + milestone + " meters");
                milestone += 100;
            }

            Thread.sleep(speed);
        }

        int rank;
        rank = Shared.rankPtr.getAndIncrement();

        return new Scorecard(name, rank, startTime, LocalTime.now());
    }
}

/// SCORECARD ///
record Scorecard(
        String name,
        int rank,
        LocalTime startTime,
        LocalTime endTime,
        Duration duration
) {
    public Scorecard(String name, int rank, LocalTime startTime, LocalTime endTime) {
        this(name, rank, startTime, endTime, Duration.between(startTime, endTime));
    }

    public String toString() {
        return name + " finished at rank " + rank + " | Time Taken: " + duration.getSeconds() + "s " + (duration.toMillis() % 1000) + "ms";
    }
}

/// MAIN ///
public class BikeGameMain {
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter Race Distance (KM): ");
            Shared.goalDistanceMeters = (int) (Float.parseFloat(reader.readLine()) * 1000);

            System.out.print("Enter number of bikers: ");
            int bikerCount = Integer.parseInt(reader.readLine());

            Shared.startLatch = new CountDownLatch(3);
            ExecutorService executor = Executors.newFixedThreadPool(bikerCount);

            List<Future<Scorecard>> results = new ArrayList<>();

            for (int i = 0; i < bikerCount; i++) {
                System.out.print("Enter biker name: ");
                results.add(executor.submit(new Biker(reader.readLine())));
            }

            /// START COUNTDOWN ///
            String[] countdown = {"Ready!", "Set!!", "GO!!!"};
            for (int i = 0; i < 3; i++) {
                Thread.sleep(1000);
                Shared.startLatch.countDown();
                System.out.println(countdown[i]);
            }

            /// PRINT DASHBOARD ///
            List<Scorecard> finalScores = new ArrayList<>();

            for (Future<Scorecard> f : results) {
                finalScores.add(f.get());
            }

            finalScores.sort(Comparator.comparingInt(Scorecard::rank));

            for (Scorecard s : finalScores) {
                System.out.println(s);
            }

            executor.shutdown();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}