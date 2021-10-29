package com.cxtx.common.unit.aop.db;

import com.cxtx.common.domain.SysRestLog;
import com.cxtx.common.service.SysRestLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SysRestLogConsumer extends AbstractMyConsumer implements Runnable,MyConsumer {
    private static Logger logger = LoggerFactory.getLogger(SysRestLogConsumer.class);
    private SysRestLogQueue sysRestLogQueue;
    private SysRestLogService sysRestLogService;
    private String consumerId;

    public SysRestLogConsumer(SysRestLogQueue sysRestLogQueue, SysRestLogService sysRestLogService, String consumerId) {
        this.sysRestLogQueue = sysRestLogQueue;
        this.sysRestLogService = sysRestLogService;
        this.consumerId = consumerId;
    }

    /**
     * 上锁给当前this，此方法顺序执行 且当前this对象是单例的，spring维护的bean默认是单例的
     * @throws InterruptedException
     */
    @Override
    public synchronized void consume() throws InterruptedException {
        logger.info(this.consumerId + "开始执行消费");
        SysRestLog log = this.sysRestLogQueue.poll();
        if (log != null) {
            logger.info(this.consumerId + "执行入库: " + log.toString());
            this.sysRestLogService.insert(log);
        }

        logger.info(this.consumerId + "执行结束");
    }

    /**
     * synchronized用于解决生产者消费者公用一个盘子的问题
     * 保证盘子里有东西才能消费，如果不加则会一直执行消费
     * aop 使用的是用户线程，所以开单独线程来执行日志入库
     */
    @Override
    public void run() {
        while(true) {
            //对sysRestLogQueue上锁，必须拿到这个对象锁，才能执行其中的方法
            synchronized(this.sysRestLogQueue) {
                //拿到这个 对象锁之后，如果任务为空，则调用wait()释放对象锁，这个线程就阻塞在这了
                //当发生系统调用，会产生日志，调用这个对象的add()方法，add方法中的notify(),会唤醒这个对象锁，使其从阻塞状态出来
                //线程重新运行时，执行循环，这个对象不为空，则执行消费
                while(this.sysRestLogQueue.isEmpty()) {
                    try {
                        logger.info(this.consumerId + "执行等待……");
                        //当队列为null时，调用wait()阻塞该线程，使其不向下执行（消费），指导另一个线程调用notifyAll()唤醒它
                        this.sysRestLogQueue.wait();
                    } catch (Exception var5) {
                        var5.printStackTrace();
                    }
                }

                try {
                    this.consume();
                } catch (InterruptedException var4) {
                    var4.printStackTrace();
                    return;
                }
            }
        }
    }
}
