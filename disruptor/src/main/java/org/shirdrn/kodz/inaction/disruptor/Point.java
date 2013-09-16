package org.shirdrn.kodz.inaction.disruptor;

public class Point {
	
	int dimension;
	Coordinate coordinate;

	public Point(double[] coordinateElements) {
		dimension = coordinateElements.length;
		Coordinate thisCoordinate = coordinate = new Coordinate(
				coordinateElements[0]);
		for (int i = 1; i < this.dimension; i++) {
			Coordinate next = new Coordinate(coordinateElements[i]);
			thisCoordinate.setNext(next);
			thisCoordinate = next;
		}
	}

	public int getDimension() {
		return dimension;
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("(");
		Coordinate thisCoordinate = coordinate;
		while (thisCoordinate != null) {
			sb.append(thisCoordinate.element);
			thisCoordinate = thisCoordinate.next;
			if (thisCoordinate == null) {
				sb.append(")");
			} else {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	class Coordinate {

		double element;
		Coordinate next;

		public Coordinate(double element) {
			this.element = element;
		}

		public Coordinate getNext() {
			return next;
		}

		public double getElement() {
			return element;
		}

		public void setNext(Coordinate next) {
			this.next = next;
		}

	}
}
