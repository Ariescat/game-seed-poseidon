package com.mmorpg.framework.asyncdb;

import com.mmorpg.framework.threads.CoreThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class SyncDBExecutor {

    private final static Logger logger = LoggerFactory.getLogger(SyncDBExecutor.class);

    private final int STEP;

    private final int SPEED;

    private final int TRY_TIME;

    private final int SIZE;

    private volatile boolean stop = true;

    private Thread sumitSyncDBTaskThread;

    private ExecutorService executorService;

    private AtomicLong countSync = new AtomicLong();

    private final BlockingQueue<AsynDBEntity> syncQueue = new LinkedTransferQueue<AsynDBEntity>();

    private ScheduledExecutorService monitor;

    private ExceptionCallback callback;

    public SyncDBExecutor(ExceptionCallback callback, int size, int step, int speed, int tryTime) {
        this.SPEED = speed;
        this.STEP = step;
        this.SIZE = size;
        this.TRY_TIME = tryTime;
        this.callback = callback;
        start();
    }

    public SyncDBExecutor(int size, int step, int speed, int tryTime) {
        this.SPEED = speed;
        this.STEP = step;
        this.SIZE = size;
        this.TRY_TIME = tryTime;
        this.start();
    }

    public boolean submit(AsynDBEntity synchronizable) {
        if (this.stop) {
            return false;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("submit synchronizable : {}", synchronizable);
        }
        return this.syncQueue.add(synchronizable);
    }

    public boolean shutdown() throws InterruptedException {
        if (this.stop) {
            return false;
        }

        logger.info("SyncDBExecutor shutdown");
        synchronized (this) {
            this.stop = true;
            this.sumitSyncDBTaskThread.join();
            logger.info("SyncDBExecutor executorService shutdowned");
            this.monitor.shutdown();
            return true;
        }
    }

    private ExecutorService createExecutorService() {
        return new ThreadPoolExecutor(this.SIZE, this.SIZE, 0L, TimeUnit.MILLISECONDS,
                new LinkedTransferQueue<Runnable>(),
                new CoreThreadFactory("SyncDbExecutor", false));
    }

    public boolean start() {
        if (this.stop) {
            synchronized (this) {
                this.executorService = createExecutorService();
                logger.info("SyncDBExecutor start");
                this.stop = false;
                this.sumitSyncDBTaskThread = new Thread(new SyncController(), "Sumit SyncDBTask Thread");
                this.sumitSyncDBTaskThread.start();
                this.monitor = Executors.newScheduledThreadPool(1, new CoreThreadFactory("SyncDBMonitor"));
                monitor.scheduleWithFixedDelay(new Runnable() {
                    private long preTime = 0L;

                    @Override
                    public void run() {
                        long thisTime = countSync.get();
                        ThreadPoolExecutor executorPool = (ThreadPoolExecutor) executorService;
                        if (logger.isInfoEnabled()) {
                            logger.info("sumit queue : {} # execute task number : {}"
                                            + " # sync total number : {} # exe number/min: {}",
                                    syncQueue.size(), executorPool.getQueue().size(),
                                    thisTime, thisTime - preTime);
                        }
                        this.preTime = thisTime;
                    }
                }, 0, 60L, TimeUnit.SECONDS);
                return true;
            }
        }
        return false;
    }

    private class SyncController implements Runnable {
        private SyncController() {
        }

        @Override
        public void run() {
            long rate = 1000 / (SPEED / STEP);
            ExecutorService currentService = executorService;
            while (true) {
                ThreadPoolExecutor pool = (ThreadPoolExecutor) currentService;
                for (int i = 0; i < STEP; ++i) {
                    final AsynDBEntity synchronizable = syncQueue.poll();
                    if (synchronizable != null) {
                        currentService.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    synchronizable.trySync(TRY_TIME);
                                    if (countSync.get() == Long.MAX_VALUE) {
                                        countSync.compareAndSet(Long.MAX_VALUE, 0L);
                                    }
                                    countSync.incrementAndGet();
                                } catch (Exception e) {
                                    callback.onException(e);
                                }
                            }
                        });
                    }
                }


                if (stop) {
                    if (!syncQueue.isEmpty()) {
                        // 要求停服，但同步队列非空，则不休眠，快速提交所有同步对象
                        continue;
                    } else if (!currentService.isShutdown()) {
                        // 同步队列所有元素已经被提交，这个时候才能Shutdown执行器
                        logger.info("SyncDBExecutor executorService shutdowning");
                        currentService.shutdown();
                    } else {
                        // 已经调用shutdown，则等待执行器终止
                        while (!currentService.isTerminated()) {
                            try {
                                Thread.sleep(100L);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        // 到此所有元素被真正完全同步，syncThread即将退出
                        if (pool.getQueue().size() != 0) {
                            logger.info("DataSyncService Sync not Complete, but IS Terminated !!");
                        }
                        break;
                    }
                } else {
                    // 没有停服，通过休眠来控制同步速率
                    try {
                        Thread.sleep(rate);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
