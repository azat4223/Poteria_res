class Counter{
    private volatile long counter = 0;
    long getCounter() { return counter; }
    void increment() { counter++;  }
}
public static class InterferenceExample {
    private static final int HUNDRED_MILLION = 100_000_000;
    private final AtomicInteger stopSignal = new AtomicInteger();
    private int lostCounter = 0;
    boolean stop() {
        return stopSignal.incrementAndGet() > HUNDRED_MILLION;
    }
    void incrementLostCounter() {
        lostCounter++;
    }
    int getLostCounter() {
        return lostCounter;
    }
    void example() throws InterruptedException {
        InterferenceThread thread1 = new InterferenceThread(this);
        InterferenceThread thread2 = new InterferenceThread(this);
        long startTime = System.currentTimeMillis();
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        long endTime = System.currentTimeMillis();

        System.out.println("Ожидание (AtomicInteger стоп-сигнал): " + HUNDRED_MILLION);
        System.out.println("Потрачено времени: " + (endTime - startTime) + " мс");
        System.out.println("Результат (счетчик с потерями): " + getLostCounter());
        System.out.println("Потеряно инкрементов: " + (HUNDRED_MILLION - getLostCounter()));
    }
}
public static class InterferenceThread extends Thread {
    private final InterferenceExample checker;
    InterferenceThread(InterferenceExample checker) {
        this.checker = checker;
    }
    @Override
    public void run() {
        while (!checker.stop()) {
            checker.incrementLostCounter();
        }
    }
}
public static void main(String[] args) throws InterruptedException {
    new InterferenceExample().example();
}
