package com.game.util;

import java.util.LinkedList;

/**
 * Created by chenhao on 5/16/2015.
 */
public class ThreadPool extends ThreadGroup{

    private boolean isAlive;
    private LinkedList taskQueue;
    private int threadID;
    private static int threadPoolID;

    public ThreadPool(int numThread){
        super("ThreadPool-" + (threadPoolID++));
        setDaemon(true);

        isAlive = true;

        taskQueue = new LinkedList();
        for (int i = 0; i < numThread; i++){
            new PooledThread().start();
        }
    }

    public synchronized void runTask(Runnable task){
        if (!isAlive){
            throw new IllegalStateException();
        }
        if (task != null){
            taskQueue.add(task);
            notify();
        }
    }

    protected synchronized Runnable getTask() throws InterruptedException{
        while (taskQueue.size() == 0){
            if (!isAlive){
                return null;
            }
            wait();
        }
        return (Runnable)taskQueue.removeFirst();
    }

    /*close the thread pool immediately
    * All threads are stopped,and any waiting tasks are not executed.
    * Once a threadPool is closed,no more tasks can be run on this threadPool*/
    public synchronized void close(){
        if (isAlive){
            isAlive = false;
            taskQueue.clear();
            interrupt();
        }
    }

    /*close the thread pool and waits for all running threads to finish.Any waiting tasks are executed*/
    public void join(){
        //notify all waiting threads that this threadPool is no longer alive
        synchronized (this){
            isAlive = false;
            notifyAll();
        }
        //wait for all threads to finish
        Thread[] threads = new Thread[activeCount()];
        int count = enumerate(threads);
        for(int i =0; i < count; i++){
            try {
                threads[i].join();
            } catch (InterruptedException e) {
            }
        }
    }

    protected void threadStarted(){}

    protected void threadStopped(){}

    private class PooledThread extends Thread{

        public PooledThread(){
            super(ThreadPool.this,"PooledThread-" + (threadID++));
        }

        @Override
        public void run() {

            //signal that this thread has started
            threadStarted();

            while (!Thread.currentThread().isInterrupted()){
                //get the task to run
                Runnable task = null;
                try {
                    task = getTask();
                } catch (InterruptedException e) {}

                if (task == null){
                    return;
                }

                //run the task and eat any exceptions it throws
                try {
                    task.run();
                }catch (Throwable throwable){
                    uncaughtException(this,throwable);
                }
            }
            //signal that this thread has stopped;
            threadStopped();
        }
    }

 }
