package org.shirdrn.datamining.classification.classifier;

public class Test {

	static double log2(double x) {
		return Math.log(x) / Math.log(2);
	}
	
	public static void main(String[] args) {
		double result = 0.0;
		
		// H(OUTLOOK) = - 5/14 * log2(5/14) - 5/14 * log2(5/14) - 4/14 * log2(4/14)
		result = -(double)5/14 * log2((double)5/14)
				-(double)5/14 * log2((double)5/14)
				-(double)4/14 * log2((double)4/14);
		
		// H(TEMPERATURE) = - 4/14 * log2(4/14) - 6/14 * log2(6/14) - 4/14 * log2(4/14)
		result = -(double)4/14 * log2((double)4/14)
				-(double)6/14 * log2((double)6/14)
				-(double)4/14 * log2((double)4/14);
		
		// H(HUMIDITY) = - 7/14 * log2(7/14) - 7/14 * log2(7/14)
		result = -(double)7/14 * log2((double)7/14)
				-(double)7/14 * log2((double)7/14);
		
		// H(WINDY) = - 6/14 * log2(6/14) - 8/14 * log2(8/14)
		result = -(double)6/14 * log2((double)6/14)
				-(double)8/14 * log2((double)8/14);
		
//		// Info(WINDY) = 6/14 * [- 3/6 * log2(3/6) – 3/6 * log2(3/6)] + 8/14 * [ - 6/8 * log2(6/8) - 2/8 * log2(2/8)]
//		result = -(double)6/14 * ((double)3/6 * log2((double)3/6) + (double)3/6 * log2((double)3/6))
//				-(double)8/14 * ((double)6/8 * log2((double)6/8) + (double)2/8 * log2((double)2/8));
//		
//		// Info(D) = -9/14 * log2(9/14) - 5/14 * log2(5/14) = 0.940
//		result = -(double)9/14 * log2((double)9/14)
//				-(double)5/14 * log2((double)5/14);
		
		System.out.println(result);
		System.out.println(
				0.246/1.577406282852345 + "\n" + 
				0.029 / 1.5566567074628228 + "\n" +
				0.048/0.9852281360342516);

	}

}
