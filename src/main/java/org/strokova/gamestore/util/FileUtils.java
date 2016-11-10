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
public final class FileUtils {

    private static final String ZIP_FILE_EXTENSION = ".zip";
    private static final String TXT_FILE_EXTENSION = ".txt";
    public static final String ZIP_INNER_FILE_SEPARATOR = "/";


    private FileUtils() {
    }

    public static Path preparePermanentDirectory(String parentDirName, String packageName, String appName) {
        if (packageName == null || packageName.isEmpty()
                || appName == null || appName.isEmpty()) {
            throw new InternalErrorException("packageName and appName cannot be null or empty");
        }

        Path permDir = Paths.get(parentDirName +
                File.separator + packageName + File.separator + appName);
        return createDirectoryIfNotExist(permDir);
    }

    public static Path prepareTempDirectory(String parentDirName, String dirName) {
        Path tempDir = Paths.get(parentDirName + File.separator + dirName);
        return createDirectoryIfNotExist(tempDir);
    }

    public static Path createDirectoryIfNotExist(Path path) {
        if (Files.notExists(path)) {
            try {
                return Files.createDirectories(path);
            } catch (IOException e) {
                throw new InternalErrorException("Cannot create directory: " + path, e);
            }
        }
        return path;
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
            return Files.copy(srcFilePath, destFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new InternalErrorException("Cannot copy " + srcFilePath + " to " + destDirectory, e);
        }
    }

    public static Path copyTo(File f, Path dir) {
        if (f == null) {
            return null;
        }
        return FileUtils.copyFile(f.toPath(), dir);
    }

    public static Path getRelativePathInUploads(String uploadsDirName, Path absolutePath) {
        return Paths.get(uploadsDirName).relativize(absolutePath);
    }

    public static boolean isInsideInnerFolderInZip(String fileName) {
        return fileName.contains(ZIP_INNER_FILE_SEPARATOR);
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
