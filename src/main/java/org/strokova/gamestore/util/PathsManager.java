package org.strokova.gamestore.util;

public class PathsManager {

    private static String HOME_DIR = initializeHome();
    public static String UPLOADS_DIR = initializeUploads();
    public static String UPLOADS_TEMP_DIR = initializeUploadsTemp();
    public static String UPLOAD_MULTIPART_TEMP_DIR = initializeUploadMultipartTempDir();

    private static String initializeHome() {
        String home = System.getenv().get("GAMESTORE_HOME");
        if (home == null) {
            home = System.getProperty("user.home") + "/.gamestore";
        }
        return home;
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
