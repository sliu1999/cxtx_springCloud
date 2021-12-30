package com.cxtx.common.unit.aop.db;


public abstract class AbstractMyConsumer implements MyConsumer,Runnable {



    @Override
    public void run() {
        //自旋，在synchronize外写空循环，避免现场阻塞进入其他cpu产生重构缓存
        while(true) {
            try {
                this.consume();
            } catch (InterruptedException var2) {
                var2.printStackTrace();
                return;
            }
        }
    }
}
