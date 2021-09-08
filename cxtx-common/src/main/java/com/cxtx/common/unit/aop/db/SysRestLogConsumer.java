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
            synchronized(this.sysRestLogQueue) {
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
