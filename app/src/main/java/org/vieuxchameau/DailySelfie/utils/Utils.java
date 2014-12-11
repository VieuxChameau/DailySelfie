package org.vieuxchameau.DailySelfie.utils;

import java.io.File;

public final class Utils {
	private Utils() {
	}

	public static String getFileExtension(final String absolutePath) {
		final int lastIndexOf = absolutePath.lastIndexOf('.');
		if (lastIndexOf == -1) {
			return "";
		}
		return absolutePath.substring(lastIndexOf, absolutePath.length());
	}

	public static String getFileNameWithoutExtension(final String absolutePath) {
		final String fileName = absolutePath.substring(absolutePath.lastIndexOf(File.separatorChar) + 1);
		return fileName.substring(0, fileName.indexOf('.'));
	}
}
