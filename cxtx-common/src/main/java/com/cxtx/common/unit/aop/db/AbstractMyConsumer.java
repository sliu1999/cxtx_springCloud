package com.cxtx.common.unit.aop.db;


public abstract class AbstractMyConsumer implements MyConsumer,Runnable {



    @Override
    public void run() {
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
