package com.pizza.startup;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.pizza.service.ValidationService;
import com.pizza.util.FileUtils;

public class ValidationTypeJsPopulator extends ResourceStartupTask {
	
	static final String DESTINATION_MEDIA_PATH = "generated/<usertype>.js";
	private ValidationService validationService;
	
	protected void doStartupActions() {
		
		writeValidationTypesJSFromDB();
	}
	
	private void writeValidationTypesJSFromDB() {
		final String vtJson =  validationService.buildValidationTypeJSON();
		final String mediaPath = DESTINATION_MEDIA_PATH.replace("<usertype>", "anon");
		final File baseDir = new File(FileUtils.getWebappRoot());
		final File messageJS = new File(baseDir, mediaPath);
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(messageJS, false), "UTF-8"));
			try {
				writer.write(vtJson);
				writer.write("\n");
			}
			finally {
				writer.close();
			}
		}
		catch(final IOException e) {
			throw new RuntimeException("Error writing to file " + messageJS.getAbsolutePath());
		}
	}

	public void setValidationService(ValidationService validationService) {
		this.validationService = validationService;
	}

}
