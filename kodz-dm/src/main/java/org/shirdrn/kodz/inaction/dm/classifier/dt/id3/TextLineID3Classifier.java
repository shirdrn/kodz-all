package org.shirdrn.kodz.inaction.dm.classifier.dt.id3;

import java.io.IOException;

import org.shirdrn.kodz.inaction.dm.classifier.ClassLabel;
import org.shirdrn.kodz.inaction.dm.classifier.DataRecord;

public class TextLineID3Classifier extends AbstractID3Classifier<String> {

	@Override
	public ClassLabel predict(DataRecord<String> record) {
		String line = record.getRecord();
		
		return null;
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}

}
