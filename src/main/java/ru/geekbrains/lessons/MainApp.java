package ru.geekbrains.lessons;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

public class MainApp {

    private static final Object lock = new Object();

    static final int SIZE = 20;
    static final int HALF = SIZE / 2;

    static float arr[] = new float[SIZE];
    static AtomicLong speed = new AtomicLong(0L);

    public static void main(String[] args)
    {
        for (int i = 0; i < SIZE; i++) {
            arr[i] = 1.0f;
        }
        long t = System.currentTimeMillis();
        System.out.println(Arrays.toString(arr));
        System.out.println("Скорость выполнения методом 1: " + Long.toString(System.currentTimeMillis() - t));
        //System.out.println(Arrays.toString(arr));

        for (int i = 0; i < SIZE; i++) {
            for (int i = 0; i < SIZE; i++) {
                arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
            arr[i] = 1.0f;
        }
        new MyThread().start();
        new MyThread2().start();
        System.out.println("Завершение main потока");
    }

    static class MyThread extends Thread {
        @Override
        public void run() {
            try {
                System.out.println("Запуск потока 1");
                long t = System.currentTimeMillis();
                float arr1[] = new float[HALF];
                System.out.println();
                System.out.println("arr = " + Arrays.toString(arr));
                System.arraycopy(arr, 0, arr1, 0, HALF);
                System.out.println("arr1 = " + Arrays.toString(arr1));
                for (int i = 0; i < HALF; i++) {
                    arr1[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
                }
                System.out.println("arr1 = " + Arrays.toString(arr1));
                System.arraycopy(arr1, 0, arr, 0, HALF);
                System.out.println("arr = " + Arrays.toString(arr));
                t = System.currentTimeMillis() - t;
                System.out.println("Поток 1 выполнил операцию за " + t);
                showSpeed(t);

                //System.out.println(Arrays.toString(arr));
                System.out.println("Завершение потока 1");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class MyThread2 extends Thread {
        @Override
        public void run() {
            try {
                System.out.println("Запуск потока 2");
                long t = System.currentTimeMillis();
                float arr1[] = new float[SIZE];
                System.out.println();
                System.out.println("arr = " + Arrays.toString(arr));
                System.arraycopy(arr, HALF, arr1, HALF, HALF);
                System.out.println("arr1 = " + Arrays.toString(arr1));
                for (int i = HALF; i < SIZE; i++) {
                    arr1[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
                }
                System.out.println("arr1 = " + Arrays.toString(arr1));
                System.arraycopy(arr1, HALF, arr, HALF, HALF);
                System.out.println("arr = " + Arrays.toString(arr));
                t = System.currentTimeMillis() - t;
                System.out.println("Поток 2 выполнил операцию за " + t);
                showSpeed(t);

                //System.out.println(Arrays.toString(arr));
                System.out.println("Завершение потока 2");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized static void showSpeed(long t) {
        if (speed.get() == 0) {
            speed = new AtomicLong(t);
            return;
        }
        speed = new AtomicLong(speed.get() > t ? speed.get() : t);
        System.out.println("Скорость выполнения методом 2: " + speed.get());
    }
}