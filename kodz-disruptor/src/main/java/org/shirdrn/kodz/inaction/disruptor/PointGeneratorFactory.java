package org.shirdrn.kodz.inaction.disruptor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum PointGeneratorFactory {

	INSTANCE;

	static final Logger LOG = LoggerFactory
			.getLogger(PointGeneratorFactory.class);
	static final List<Pair<Point, Point>> pointPairs = new ArrayList<Pair<Point, Point>>();
	volatile int pointer = 0;
	int pointPairCount = 10000;
	int dimension = 3;
	int range = 100;
	static final Random r = new Random(System.currentTimeMillis());
	static final String PATH_SEPARATOR = getPathSeparator();
	static final String WORKSPACE = System.getProperty("user.dir");
	static final String tempFile = WORKSPACE
			+ PATH_SEPARATOR
			+ "src"
			+ PATH_SEPARATOR
			+ "main"
			+ PATH_SEPARATOR
			+ "java"
			+ PATH_SEPARATOR
			+ PointGeneratorFactory.class.getPackage().getName()
					.replaceAll("\\.", PATH_SEPARATOR) + PATH_SEPARATOR
			+ "point_pairs.sample";
	static final AtomicBoolean loaded = new AtomicBoolean(false);

	static final String MODE = "IN-VM";

	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				File file = new File(tempFile);
				if (file.exists()) {
					file.delete();
				}
			}
		});
	}

	public static PointGeneratorFactory getInstance() {
		LOG.info("Total point pair count: " + INSTANCE.pointPairCount);
		check();
		return INSTANCE;
	}

	private static String getPathSeparator() {
		String os = System.getProperty("os.name");
		String pathSeparator = null;
		if (os.contains("windows")) {
			pathSeparator = "\\";
		} else {
			pathSeparator = "/";
		}
		return pathSeparator;
	}

	private static void check() {
    	if(MODE.equals("IN-VM")) {
    		generateEphemeralSample();
    	} else {
    		if(!loaded.get()) {
                loaded.set(true);
                File f = new File(tempFile);
                if(f.exists() && f.isFile()) {
                     LOG.info("Sample file \"" + tempFile + "\" exists.");
                     readSampleAndLoadIntoMemory();
                } else {
                     generatePersistentSample();
                }
           }
    	}
         
    }

	private static void readSampleAndLoadIntoMemory() {
		LOG.info("Read point pair sample file: " + tempFile);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(tempFile));
			String line = null;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (!line.isEmpty()) {
					String[] a = line.split("\\s+");
					Point p1 = createPoint(a[0]);
					Point p2 = createPoint(a[1]);
					pointPairs.add(new Pair<Point, Point>(p1, p2));
				}
			}
			LOG.info("Point pairs loaded: " + pointPairs.size());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static Point createPoint(String point) {
		double[] a = new double[INSTANCE.dimension];
		String[] elements = point.substring(1, point.length() - 1).split(",");
		for (int i = 0; i < elements.length; i++) {
			a[i] = Double.parseDouble(elements[i]);
		}
		return new Point(a);
	}

	private static void generatePersistentSample() {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(tempFile));
			generateEphemeralSample();
			LOG.info("Write ephemeral point pairs to file: " + tempFile);
			// write to a temporary file
			int count = 0;
			for (Pair<Point, Point> pair : pointPairs) {
				writer.write(pair.getKey().toString() + "\t"
						+ pair.getValue().toString());
				writer.newLine();
				++count;
			}
			LOG.info("Finish to write: " + count + ", " + tempFile);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void generateEphemeralSample() {
		LOG.info("Start to generate point pairs...");
		for (int i = 0; i < INSTANCE.pointPairCount; i++) {
			double[] a = new double[INSTANCE.dimension];
			for (int j = 0; j < INSTANCE.dimension; j++) {
				a[j] = r.nextInt(INSTANCE.range) * r.nextDouble();
			}
			Point p1 = new Point(a);
			a = new double[INSTANCE.dimension];
			for (int j = 0; j < INSTANCE.dimension; j++) {
				a[j] = r.nextInt(INSTANCE.range) * r.nextDouble();
			}
			Point p2 = new Point(a);
			Pair<Point, Point> pair = new Pair<Point, Point>(p1, p2);
			pointPairs.add(pair);
		}
		LOG.info("Generate point pairs ramdomly: " + pointPairs.size());
	}

	public Pair<Point, Point> nextPointPair() {
		return pointPairs.get(pointer++);
	}

	public int getPointPairCount() {
		return pointPairCount;
	}

	public void reset() {
		this.pointer = 0;
	}

	public static class Pair<K, V> {
		K key;
		V value;

		public Pair(K key, V value) {
			super();
			this.key = key;
			this.value = value;
		}

		public K getKey() {
			return key;
		}

		public void setKey(K key) {
			this.key = key;
		}

		public V getValue() {
			return value;
		}

		public void setValue(V value) {
			this.value = value;
		}
	}

}