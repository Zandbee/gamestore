package org.strokova.gamestore.util;

public final class PathUtils {

    private static final String HOME_DIR = initializeHome();
    public static final String UPLOADS_DIR = initializeUploads();
    public static final String UPLOADS_TEMP_DIR = initializeUploadsTemp();
    public static final String UPLOAD_MULTIPART_TEMP_DIR = initializeUploadMultipartTempDir();

    private PathUtils() {
    }

    private static String initializeHome() {
        return System.getProperty("user.home") + "/.gamestore";
    }

    private static String initializeUploads() {
        return HOME_DIR + "/uploads";
    }

    private static String initializeUploadsTemp() {
        return UPLOADS_DIR + "/tmp";
    }

    private static String initializeUploadMultipartTempDir() {
        return HOME_DIR + "/multipart_tmp";
    }
}
