package org.shirdrn.kodz.inaction.disruptor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public enum ExecutorFactory {
    
     INSTANCE;
    
     static Executor executor = Executors.newFixedThreadPool(10);
    
     public static ExecutorFactory getInstance() {
          return INSTANCE;
     }
    
     public Executor getExecutor() {
          return executor;
     }
    
     public void addShoudownHook() {
          Runtime.getRuntime().addShutdownHook(new Thread() {
               @Override
               public void run() {
                    ((ExecutorService) executor).shutdown();
               }
          });
     }

}