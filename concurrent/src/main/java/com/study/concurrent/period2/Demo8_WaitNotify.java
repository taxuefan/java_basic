package com.study.concurrent.period2;

public class Demo8_WaitNotify {

    public static void main(String args[]) throws Exception {
        Demo8_WaitNotify demo = new Demo8_WaitNotify();
        //demo.test0();
        demo.test1_normal();
        //demo.test2_DeadLock();
    }

    public static Object iceCream = null;

    public void test0(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(this);
               /* try {
                    synchronized (this){
                        System.out.println("子线程进入等待");
                        wait();
                        System.out.println("等待结束");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            }
        }).start();

        System.out.println(this);

/*        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (this){
            notifyAll();
        }*/


    }

    /** 正常的wait/notify */
    public void test1_normal() throws Exception {
        //开启一个线程，代表小朋友
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (iceCream == null) { //若没有冰激凌
                    synchronized (this) {
                        System.out.println("小朋友拿到锁。。。");
                        try {
                            System.out.println("没有冰激凌，小朋友不开心，等待...");
                            this.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                System.out.println("小朋友买到冰激凌，开心回家");
            }
        }).start();

        Thread.sleep(3000L);    // 3秒之后
        iceCream = new Object();      //店员做了一个冰激凌

        synchronized (this) {
            System.out.println("店员拿到锁。。。");
            this.notifyAll();
            System.out.println("通知小朋友");
        }
    }

    /** 会导致程序永久等待的wait/notify */
    public void test2_DeadLock() throws Exception {
        //开启一个线程，代表小朋友
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (iceCream == null) {    //若没有冰激凌
                    try {
                        Thread.sleep(5000L);
                        System.out.println("没有冰激凌，小朋友不开心，等待...");
                        synchronized (this){
                            this.wait();
                        }
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
                System.out.println("小朋友买到冰激凌，开心回家");
            }
        }).start();

        Thread.sleep(3000L);        // 3秒之后
        iceCream = new Object();          // 店员做了一个冰激凌

        synchronized (this){    //通知小朋友
            this.notifyAll();
        }
        System.out.println("通知小朋友");
    }
}
