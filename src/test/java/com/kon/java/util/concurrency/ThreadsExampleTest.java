package com.kon.java.util.concurrency;

import com.kon.java.util.cuncurrency.ConcurrentUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;
import java.util.function.LongBinaryOperator;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import static com.kon.java.util.cuncurrency.ConcurrentUtils.callable;
import static com.kon.java.util.cuncurrency.ConcurrentUtils.stop;

@ExtendWith(MockitoExtension.class)
public class ThreadsExampleTest {
    private static final Logger logger = Logger.getLogger(ThreadsExampleTest.class.getName());

    ConcurrentHashMap<String, String> map;

    // for Atomic
    private static final int NUM_INCREMENTS = 1000;
    private static AtomicInteger atomicInt = new AtomicInteger(0);

    // Locks
    private static final int NUM_INCREMENT = 10000;
    private static ReentrantLock lock = new ReentrantLock();
    private static int count = 0;

    // LongAdded to increment and to add
    private static LongAdder adder = new LongAdder();



    @BeforeAll
    static void beforeAll() {
        System.out.println("A static method in your class that is called before all of its tests run");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("A static method in your test class that is after all od its test run");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("A method is called before each individual test rubs");

        map = new ConcurrentHashMap<>();
        map.putIfAbsent("foo", "bar");
        map.putIfAbsent("han", "solo");
        map.putIfAbsent("r2", "d2");
        map.putIfAbsent("c3", "p0");

    }

    @AfterEach
    void afterEach() {
        System.out.println("A method is called after each individual test rubs");
    }

    /**
     * Executors example
     */
    @Test
    @DisplayName("Threads 1 test 1")
    void testThreads1Test1() {
        Runnable runnable = () -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("Hello " + threadName);
        };

        runnable.run();

        Thread thread = new Thread(runnable);
        thread.start();

        System.out.println("Done!");
    }

    @Test
    @DisplayName("Threads 1 test 2")
    void testThreads1Test2() {
        Runnable runnable = () -> {
            try {
                System.out.println("Foo " + Thread.currentThread().getName());
                Thread.sleep(1000);
                System.out.println("Bar " + Thread.currentThread().getName());
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Test
    @DisplayName("Threads 1 test 3")
    void testThreads1Test3() {
        Runnable runnable = () -> {
            try {
                System.out.println("Foo " + Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(1);
                System.out.println("Bar " + Thread.currentThread().getName());
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    /**
     *
     * CompletableFeature example
     */

    @Test
    @DisplayName("CompletableFeature example")
    void testCompletableFeature() {
        CompletableFuture<String> future = new CompletableFuture<>();

        future.complete("42");

        future
                .thenAccept(System.out::println)
                .thenAccept(v -> System.out.println("done"));
    }

    /**
     * ConcurrentHashMap example
     */
    @Test
    @DisplayName("ConcurrentHashMap - forEach")
    void testForEach() {
        map.forEach(1, (key, value) -> System.out.printf("key: %s; value: %s; thread: %s\n", key, value, Thread.currentThread().getName()));
        //  map.forEach(5, (key, value) -> System.out.printf("key: %s; value: %s; thread: %s\n", key, value, Thread.currentThread().getName()));

        System.out.println(map.mappingCount());
    }

    @Test
    @DisplayName("ConcurrentHashMap - test search")
    void testSearch() {
        System.out.println("\nsearch()\n");

        String result1 = map.search(1, (key, value) -> {
            System.out.println(Thread.currentThread().getName());
            if (key.equals("foo") && value.equals("bar")) {
                return "foobar";
            }
            return null;
        });

        System.out.println(result1);

        System.out.println("\nsearchValues()\n");

        String result2 = map.searchValues(1, value -> {
            System.out.println(Thread.currentThread().getName());
            if (value.length() > 3) {
                return value;
            }
            return null;
        });

        System.out.println(result2);
    }

    @Test
    @DisplayName("ConcurrentHashMap - test reduce")
    void testReduce() {
        String reduced = map.reduce(1, (key, value) -> key + "=" + value,
                (s1, s2) -> s1 + ", " + s2);

        System.out.println(reduced);
    }

    /**
     * Atomic - cosmetics
     */
    @Test
    @DisplayName("Atomic - test Increment")
    void testIncrement() {
        atomicInt.set(0);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, NUM_INCREMENTS)
                .forEach(i -> executor.submit(atomicInt::incrementAndGet));

        stop(executor); // ConcurrentUtils

        System.out.format("Increment: Expected=%d; Is=%d\n", NUM_INCREMENTS, atomicInt.get());
    }

    @Test
    @DisplayName("Atomic - test Accumulate")
    void testAccumulate() {
        atomicInt.set(0);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, NUM_INCREMENTS)
                .forEach(i -> {
                    Runnable task = () ->
                            atomicInt.accumulateAndGet(i, (n, m) -> n + m);
                    executor.submit(task);
                });

        stop(executor); //ConcurrentUtils

        System.out.format("Accumulate: %d\n", atomicInt.get());
    }

    @Test
    @DisplayName("Atomic - test Update")
    void testUpdate() {
        atomicInt.set(0);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, NUM_INCREMENTS)
                .forEach(i -> {
                    Runnable task = () ->
                            atomicInt.updateAndGet(n -> n + 2);
                    executor.submit(task);
                });

        stop(executor); // ConcurrentUtils

        System.out.format("Update: %d\n", atomicInt.get());
    }

    /**
     * Executors tests examples
     */
    @Test
    @DisplayName("Executors1")
    void testExecutors1() {
        long seconds = 3;

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(seconds);
                String name = Thread.currentThread().getName();
                System.out.println("task finished: " + name);
            }
            catch (InterruptedException e) {
                System.err.println("task interrupted");
            }
        });
        stop(executor);
    }

    @Test
    @DisplayName("Executors2 test1")
    void testExecutors2Test1() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(1);

        Future<Integer> future = executor.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                return 123;
            }
            catch (InterruptedException e) {
                throw new IllegalStateException("task interrupted", e);
            }
        });

        System.out.println("future done: " + future.isDone());

        Integer result = future.get();

        System.out.println("future done: " + future.isDone());
        System.out.print("result: " + result);

        executor.shutdownNow();
    }

    @Test
    @DisplayName("Executors2 test2")
    void testExecutors2Test2() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(1);

        Future<Integer> future = executor.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                return 123;
            }
            catch (InterruptedException e) {
                throw new IllegalStateException("task interrupted", e);
            }
        });

        executor.shutdownNow();
        System.out.println("future.get(): " + future.get());
    }

    @Test
    @DisplayName("Executors2 test3")
    void testExecutors2Test3() throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService executor = Executors.newFixedThreadPool(1);

        Future<Integer> future = executor.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                return 123;
            }
            catch (InterruptedException e) {
                throw new IllegalStateException("task interrupted", e);
            }
        });

        future.get(1, TimeUnit.SECONDS);
    }

    /**
     * Executors 3
     */
    @Test
    @DisplayName("Executors3 test1")
    void testExecutors3Test1() throws InterruptedException {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        Runnable task = () -> System.out.println("Scheduling: " + System.nanoTime());
        int delay = 3;
        ScheduledFuture<?> future = executor.schedule(task, delay, TimeUnit.SECONDS);

        TimeUnit.MILLISECONDS.sleep(1337);

        long remainingDelay = future.getDelay(TimeUnit.MILLISECONDS);
        System.out.printf("Remaining Delay: %sms\n", remainingDelay);
    }

    @Test
    @DisplayName("Executors3 test2")
    void testExecutors3Test2() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Runnable task = () -> System.out.println("Scheduling: " + System.nanoTime());
        int initialDelay = 0;
        int period = 1;
        executor.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.SECONDS);
    }

    @Test
    @DisplayName("Executors3 test3")
    void testExecutors3Test3() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        Runnable task = () -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                System.out.println("Scheduling: " + System.nanoTime());
            }
            catch (InterruptedException e) {
                System.err.println("task interrupted");
            }
        };

        executor.scheduleWithFixedDelay(task, 0, 1, TimeUnit.SECONDS);
    }

    @Test
    @DisplayName("Executors3 test4")
    void testExecutors3Test4() throws InterruptedException {
        ExecutorService executor = Executors.newWorkStealingPool();

        List<Callable<String>> callables = Arrays.asList(
                () -> "task1",
                () -> "task2",
                () -> "task3");

        executor.invokeAll(callables)
                .stream()
                .map(future -> {
                    try {
                        return future.get();
                    }
                    catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                })
                .forEach(System.out::println);

        executor.shutdown();
    }

    @Test
    @DisplayName("Executors3 test5")
    void testExecutors3Test5() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newWorkStealingPool();

        // Callable is created in my ConcurrentUtils class that is imported
        List<Callable<String>> callables = Arrays.asList(
                callable("task1", 2),
                callable("task2", 1),
                callable("task3", 3));

        String result = executor.invokeAny(callables);
        System.out.println(result);

        executor.shutdown();
    }

    /**
     * Locks
     */

    private static void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }

    @Test
    @DisplayName("Lock1")
    void testLock1() {
        count = 0;

        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, NUM_INCREMENT)
                .forEach(i -> executor.submit(ThreadsExampleTest::increment));

        ConcurrentUtils.stop(executor);

        System.out.println(count);
    }

    @Test
    @DisplayName("Lock2")
    void testLock2() {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        ReentrantLock lock = new ReentrantLock();

        executor.submit(() -> {
            lock.lock();
            try {
                ConcurrentUtils.sleep(1);
            } finally {
                lock.unlock();
            }
        });

        executor.submit(() -> {
            System.out.println("Locked: " + lock.isLocked());
            System.out.println("Held by me: " + lock.isHeldByCurrentThread());
            boolean locked = lock.tryLock();
            System.out.println("Lock acquired: " + locked);
        });

        ConcurrentUtils.stop(executor);
    }

    @Test
    @DisplayName("Lock3")
    void testLock3() {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Map<String, String> map = new HashMap<>();

        ReadWriteLock lock = new ReentrantReadWriteLock();

        executor.submit(() -> {
            lock.writeLock().lock();
            try {
                ConcurrentUtils.sleep(1);
                map.put("foo", "bar");
            } finally {
                lock.writeLock().unlock();
            }
        });

        Runnable readTask = () -> {
            lock.readLock().lock();
            try {
                System.out.println(map.get("foo"));
                ConcurrentUtils.sleep(1);
            } finally {
                lock.readLock().unlock();
            }
        };
        executor.submit(readTask);
        executor.submit(readTask);

        ConcurrentUtils.stop(executor);
    }

    @Test
    @DisplayName("Lock4")
    void testLock4() {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Map<String, String> map = new HashMap<>();

        StampedLock lock = new StampedLock();

        executor.submit(() -> {
            long stamp = lock.writeLock();
            try {
                ConcurrentUtils.sleep(1);
                map.put("foo", "bar");
            } finally {
                lock.unlockWrite(stamp);
            }
        });

        Runnable readTask = () -> {
            long stamp = lock.readLock();
            try {
                System.out.println(map.get("foo"));
                ConcurrentUtils.sleep(1);
            } finally {
                lock.unlockRead(stamp);
            }
        };
        executor.submit(readTask);
        executor.submit(readTask);

        ConcurrentUtils.stop(executor);
    }

    @Test
    @DisplayName("Lock5")
    void testLock5() {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        StampedLock lock = new StampedLock();

        executor.submit(() -> {
            long stamp = lock.tryOptimisticRead();
            try {
                System.out.println("Optimistic Lock Valid: " + lock.validate(stamp));
                ConcurrentUtils.sleep(1);
                System.out.println("Optimistic Lock Valid: " + lock.validate(stamp));
                ConcurrentUtils.sleep(2);
                System.out.println("Optimistic Lock Valid: " + lock.validate(stamp));
            } finally {
                lock.unlock(stamp);
            }
        });

        executor.submit(() -> {
            long stamp = lock.writeLock();
            try {
                System.out.println("Write Lock acquired");
                ConcurrentUtils.sleep(2);
            } finally {
                lock.unlock(stamp);
                System.out.println("Write done");
            }
        });

        ConcurrentUtils.stop(executor);
    }

    /**
     * StampedLock
     */
    @Test
    @DisplayName("Lock6")
    void testLock6() {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        StampedLock lock = new StampedLock();

        executor.submit(() -> {
            long stamp = lock.readLock();
            try {
                if (count == 0) {
                    stamp = lock.tryConvertToWriteLock(stamp);
                    if (stamp == 0L) {
                        System.out.println("Could not convert to write lock");
                        stamp = lock.writeLock();
                    }
                    count = 23;
                }
                System.out.println(count);
            } finally {
                lock.unlock(stamp);
            }
        });

        ConcurrentUtils.stop(executor);
    }

    /**
     * LongAccumulator test
     */
    @Test
    @DisplayName("LongAccumulator test")
    void testLongAccumulator() {
        LongBinaryOperator op = (x, y) -> 2 * x + y;
        LongAccumulator accumulator = new LongAccumulator(op, 1L);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, 10)
                .forEach(i -> executor.submit(() -> accumulator.accumulate(i)));

        ConcurrentUtils.stop(executor);

        System.out.format("Add: %d\n", accumulator.getThenReset());
    }

    @Test
    @DisplayName("LongAdder test Increment")
    void testLongAdderTestIncrement() {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, NUM_INCREMENT)
                .forEach(i -> executor.submit(adder::increment));

        ConcurrentUtils.stop(executor);

        System.out.format("Increment: Expected=%d; Is=%d\n", NUM_INCREMENT, adder.sumThenReset());
    }

    @Test
    @DisplayName("LongAdder test Add")
    void testLongAdderTestAdd() {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, NUM_INCREMENT)
                .forEach(i -> executor.submit(() -> adder.add(2)));

        ConcurrentUtils.stop(executor);

        System.out.format("Add: %d\n", adder.sumThenReset());
    }

    /**
     * Semaphores test
     */
    private static void incrementSemaphore() {
        Semaphore semaphore = new Semaphore(1);
        boolean permit = false;
        try {
            permit = semaphore.tryAcquire(5, TimeUnit.SECONDS);
            count++;
        }
        catch (InterruptedException e) {
            throw new RuntimeException("could not increment");
        }
        finally {
            if (permit) {
                semaphore.release();
            }
        }
    }

    @Test
    @DisplayName("Semaphore1 test Increment")
    void testSemaphore1Increment() {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, NUM_INCREMENT)
                .forEach(i -> executor.submit(ThreadsExampleTest::incrementSemaphore));

        ConcurrentUtils.stop(executor);

        System.out.println("Increment: " + count);

    }

    /**
     * Semaphore 2 test
     */

    private static void doWork() {
        Semaphore semaphore = new Semaphore(5);

        boolean permit = false;


        try {
            permit = semaphore.tryAcquire(1, TimeUnit.SECONDS);
            if (permit) {
                System.out.println("Semaphore acquired");
                ConcurrentUtils.sleep(5);
            } else {
                System.out.println("Could not acquire semaphore");
            }
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        } finally {
            if (permit) {
                semaphore.release();
            }
        }
    }

    @Test
    @DisplayName("Semaphore 2 test doWork")
    void testSemaphore2doWork() {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        IntStream.range(0, 10)
                .forEach(i -> executor.submit(ThreadsExampleTest::doWork));

        ConcurrentUtils.stop(executor);
    }

    /**
     * Synchronized tests
     */

    private static synchronized void incrementSync() {
        count = count + 1;
    }

    private static void incrementNonSync() {
        count = count + 1;
    }

    @Test
    @DisplayName("Synchronized 1 test SyncIncrement")
    void testSynchronized1SyncIncrement() {
        count = 0;

        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, NUM_INCREMENT)
                .forEach(i -> executor.submit(ThreadsExampleTest::incrementSync));

        ConcurrentUtils.stop(executor);

        System.out.println("   Sync: " + count);
    }

    @Test
    @DisplayName("Synchronized 1 test NonSyncIncrement")
    void testSynchronized1NoneSyncIncrement() {
        count = 0;

        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, NUM_INCREMENTS)
                .forEach(i -> executor.submit(ThreadsExampleTest::incrementNonSync));

        ConcurrentUtils.stop(executor);

        System.out.println("NonSync: " + count);
    }

    private static void incrementSynchronized() {
        synchronized (ThreadsExampleTest.class) {
            count = count + 1;
        }
    }

    @Test
    @DisplayName("Synchronized 2 test SyncIncrement")
    void testSynchronized2SyncIncrement() {
        count = 0;

        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, NUM_INCREMENT)
                .forEach(i -> executor.submit(ThreadsExampleTest::incrementSynchronized));

        ConcurrentUtils.stop(executor);

        System.out.println(count);
    }

}
