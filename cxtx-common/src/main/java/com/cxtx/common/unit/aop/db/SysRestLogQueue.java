package com.cxtx.common.unit.aop.db;


import com.cxtx.common.domain.SysRestLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
public class SysRestLogQueue {

    private static Logger logger = LoggerFactory.getLogger(SysRestLogQueue.class);
    private BlockingQueue<SysRestLog> logs = new LinkedBlockingQueue();


    /**
     * 把数据加到任务队列中
     * 相当于生产者，然后唤醒消费者去消费
     * @param log
     */
    public void add(SysRestLog log) {

        synchronized(this) {
            try {
                //将给定元素设置到队列中，如果设置成功返回true, 否则返回false。如果是往限定了长度的队列中设置值，推荐使用offer()方法。
                this.logs.add(log);
                //当为队列中加入数据，队列就不为null，这时唤醒消费者线程去消费数据
                this.notifyAll();
            } catch (IllegalStateException var5) {
                logger.error("队列溢出");
                var5.printStackTrace();
            } catch (Exception var6) {
                logger.error("队列失败");
                var6.printStackTrace();
            }

        }
    }

    /**
     * 从队列中获取值
     * @return
     * @throws InterruptedException
     */
    public SysRestLog poll() throws InterruptedException {
        try {
            //在给定的时间里，从队列中获取值，时间到了直接调用普通的poll方法，为null则直接返回null。
            return (SysRestLog)this.logs.poll(1L, TimeUnit.SECONDS);
        } catch (Exception var2) {
            logger.error("队列失败");
            var2.printStackTrace();
            return null;
        }
    }

    /**
     * 判断这个无界队列是否为null
     * @return
     */
    public Boolean isEmpty() {
        return this.logs.isEmpty();
    }
}
