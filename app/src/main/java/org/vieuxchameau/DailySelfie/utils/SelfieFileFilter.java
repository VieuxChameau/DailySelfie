package org.vieuxchameau.DailySelfie.utils;

import org.vieuxchameau.DailySelfie.MainActivity;

import java.io.File;
import java.io.FileFilter;

/**
 * FileFilter to accept only selfie file (*.jpg)
 */
public class SelfieFileFilter implements FileFilter {
	@Override
	public boolean accept(final File file) {
		final String fileExtension = Utils.getFileExtension(file.getName());
		return MainActivity.SELFIE_EXTENSION.equals(fileExtension);
	}
}
