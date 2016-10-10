package org.strokova.gamestore.util;

public class PathsManager {

    private static String HOME_DIR = initializeHome();
    public static String UPLOADS_DIR = initializeUploads();
    public static String UPLOADS_TEMP_DIR = initializeUploadsTemp();
    public static String UPLOAD_MULTIPART_TEMP_DIR = initializeUploadMultipartTempDir();

    private static final String ENVIRONMENT_GAMESTORE_HOME = "GAMESTORE_HOME";
    private static final String SYSTEM_USER_HOME = "user.home";
    private static final String GAMESTORE_HOME_DIR

    private static String initializeHome() {
        String home = System.getenv().get(ENVIRONMENT_GAMESTORE_HOME);
        if (home == null) {
            home = System.getProperty(SYSTEM_USER_HOME) + "/.gamestore";
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
