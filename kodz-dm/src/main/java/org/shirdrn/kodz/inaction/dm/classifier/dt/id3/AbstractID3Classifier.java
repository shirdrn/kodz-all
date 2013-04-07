package org.shirdrn.kodz.inaction.dm.classifier.dt.id3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.shirdrn.kodz.inaction.dm.classifier.AbstractClassifier;
import org.shirdrn.kodz.inaction.dm.classifier.Attr;
import org.shirdrn.kodz.inaction.dm.classifier.dt.DTDataModel;

public abstract class AbstractID3Classifier<T> extends AbstractClassifier<T> {

	protected String lineSeparator = "\t";
	protected DTDataModel dataModel;
	
	@Override
	public void setupModel() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(""));
			String line = null;
			while((line = br.readLine()) != null) {
				line = line.trim();
				if(!line.isEmpty()) {
					String[] fields = line.split(lineSeparator);
					for(int i=0; i<fields.length-1; i++) {
						Attr attr = new Attr();
						attr.setName(fields[i]);
						attributes.add(attr);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	
}
