package org.shirdrn.kodz.inaction.disruptor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;

import org.shirdrn.kodz.inaction.disruptor.PointGeneratorFactory.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

public class DisruptorWizard {

	  static final Logger LOG = LoggerFactory.getLogger(DisruptorWizard.class);
	     final PointGeneratorFactory pointGeneratorFactory = PointGeneratorFactory.getInstance();
	     Executor executor = ExecutorFactory.getInstance().getExecutor();
	     final Disruptor<EuclideanDistanceEvent> disruptor;
	     final RingBuffer<EuclideanDistanceEvent> ringBuffer;
	     private final EventFactory<EuclideanDistanceEvent> eventFactory =
	               new EventFactory<EuclideanDistanceEvent>() {
	         
	          public EuclideanDistanceEvent newInstance() {
	               Pair<Point, Point> pair = pointGeneratorFactory.nextPointPair();
	               return new EuclideanDistanceEvent(pair.getKey(), pair.getValue());
	          }
	         
	     };
	     final EventHandler<EuclideanDistanceEvent> distanceHandler = new EventHandler<EuclideanDistanceEvent>() {
	         
	          @Override
	          public void onEvent(EuclideanDistanceEvent event, long sequence, boolean endOfBatch) throws Exception {
	               event.computeDistance();
	          }
	         
	     };
	    
	     final EventHandler<EuclideanDistanceEvent> sumHandler = new EventHandler<EuclideanDistanceEvent>() {
	         
	          @Override
	          public void onEvent(EuclideanDistanceEvent event, long sequence, boolean endOfBatch) throws Exception {
	               SumEvent.addTo(event);
	          }
	         
	     };
	     final int ringSize;
	    
	     public DisruptorWizard(int ringSize) {
	          this.ringSize = ringSize;
	          disruptor = new Disruptor<EuclideanDistanceEvent>(eventFactory, ringSize, executor);
	          disruptor.handleEventsWith(distanceHandler);
	          disruptor.after(distanceHandler).handleEventsWith(sumHandler);
	          ringBuffer = disruptor.start();
	          ExecutorFactory.getInstance().addShoudownHook();
	     }
	    
	     static class SumEvent {
	         
	          static double sum = 0.0;
	         
	          public static void addTo(EuclideanDistanceEvent distanceEvent) {
	               sum += distanceEvent.getDistaince();
	          }
	         
	          public static double getSum() {
	               return sum;
	          }
	         
	     }
	    
	     public void compute() {
	          for(int i=0; i<pointGeneratorFactory.getPointPairCount(); i++) {
	               long sequence = ringBuffer.next();
	               ringBuffer.getPreallocated(sequence);
	               ringBuffer.publish(sequence);
	          }
	     }
	    
	     public static void main(String[] args) throws Exception {
	         
	          LOG.info("DISRUPTORWIZARD");
	          DisruptorWizard wizard = new DisruptorWizard(4096);
	          DateFormat df = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
	          Date start = new Date();
	          LOG.info("Start at: " + df.format(start));
	          wizard.compute();
	          Date end = new Date();
	          LOG.info("Sum: " + SumEvent.getSum());
	          LOG.info("Timetaken: " + (end.getTime() - start.getTime()));
	          LOG.info("Finish at: " + df.format(end));
	         
	     }
}
