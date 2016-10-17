package org.strokova.gamestore.util;

import org.strokova.gamestore.exception.InternalErrorException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author vstrokova, 07.10.2016.
 */
public class FileUtils {
    private static final String ZIP_FILE_EXTENSION = ".zip";
    private static final String TXT_FILE_EXTENSION = ".txt";

    public static Path preparePermanentDirectory(String packageName, String appName) {
        if (packageName == null || packageName.isEmpty()
                || appName == null || appName.isEmpty()) {
            throw new InternalErrorException("packageName and appName cannot be null or empty");
        }

        Path permDir = Paths.get(PathsManager.UPLOADS_DIR +
                File.separator + packageName + File.separator + appName);
        createDirectoryIfNotExist(permDir);
        return permDir;
    }

    public static Path prepareTempDirectory(String dirName) {
        Path tempDir = Paths.get(PathsManager.UPLOADS_TEMP_DIR + File.separator + dirName);
        createDirectoryIfNotExist(tempDir);
        return tempDir;
    }

    public static void createDirectoryIfNotExist(Path path) {
        if (Files.notExists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new InternalErrorException("Cannot create directory: " + path, e);
            }
        }
    }

    public static void deleteFile(Path dir) {
        deleteFile(new File(dir.toString()));
    }

    private static boolean deleteFile(File file) {
        File[] content = file.listFiles();
        if (content != null) {
            for (File f : content) {
                deleteFile(f);
            }
        }
        return file.delete();
    }

    public static Path copyFile(Path srcFilePath, Path destDirectory) {
        Path destFilePath = destDirectory.resolve(srcFilePath.getFileName());
        try {
            Files.copy(srcFilePath, destFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new InternalErrorException("Cannot copy " + srcFilePath + " to " + destDirectory, e);
        }
        return destFilePath;
    }

    public static boolean isTxt(String fileName) {
        return fileName.endsWith(TXT_FILE_EXTENSION);
    }

    public static boolean isZip(String fileName) {
        return fileName.endsWith(ZIP_FILE_EXTENSION);
    }

    public static boolean isImage(Path imagePath) {
        try {
            return Files.probeContentType(imagePath).startsWith("image/");
        } catch (IOException e) {
            throw new InternalErrorException("Cannot check path: " + imagePath, e);
        }
    }
}
