package org.shirdrn.kodz.inaction.disruptor;

public class EuclideanDistanceEvent {
	
	Point x;
	Point y;
	int dimension;
	double distaince = 0.0;

	public EuclideanDistanceEvent() {
		super();
	}

	public EuclideanDistanceEvent(Point x, Point y) {
		this.x = x;
		this.y = y;
		this.dimension = x.getDimension();
	}

	public void computeDistance() {
		double sum = 0.0;
		for (int i = 0; i < dimension; i++) {
			double diff = x.getCoordinate().getElement() - y.getCoordinate().getElement();
			sum += diff * diff;
		}
		distaince = Math.sqrt(sum);
	}

	public double getDistaince() {
		return distaince;
	}
	
}
