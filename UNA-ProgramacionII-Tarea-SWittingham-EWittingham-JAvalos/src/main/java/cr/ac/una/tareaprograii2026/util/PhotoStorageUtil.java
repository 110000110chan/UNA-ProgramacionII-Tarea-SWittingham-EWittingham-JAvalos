package cr.ac.una.tareaprograii2026.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

public final class PhotoStorageUtil {

    private static final Path PHOTOS_DIRECTORY = Paths.get(
            "src", "main", "resources", "cr", "ac", "una", "tareaprograii2026", "resources", "fotos"
    );

    private PhotoStorageUtil() {
    }

    public static Path getPhotosDirectory() {
        return Paths.get(System.getProperty("user.dir")).resolve(PHOTOS_DIRECTORY).normalize();
    }

    public static String copyPhotoToProject(Path sourcePath, String prefix) throws IOException {
        Path photosDirectory = getPhotosDirectory();
        Files.createDirectories(photosDirectory);

        String fileName = sourcePath.getFileName().toString();
        String extension = "";
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = fileName.substring(dotIndex);
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        Path destinationPath = photosDirectory.resolve(prefix + "_" + timestamp + extension);
        Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        return toRelativePath(destinationPath);
    }

    public static String saveBufferedImage(BufferedImage bufferedImage, String prefix) throws IOException {
        Path photosDirectory = getPhotosDirectory();
        Files.createDirectories(photosDirectory);

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        Path destinationPath = photosDirectory.resolve(prefix + "_" + timestamp + ".png");
        ImageIO.write(bufferedImage, "PNG", destinationPath.toFile());
        return toRelativePath(destinationPath);
    }

    public static String toRelativePath(Path path) {
        Path baseDirectory = Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();
        return baseDirectory.relativize(path.toAbsolutePath().normalize()).toString();
    }

    public static Image createImageFromPath(String path) {
        try {
            if (path == null || path.isBlank()) {
                return null;
            }
            if (path.startsWith("http") || path.startsWith("file:")) {
                return new Image(path, true);
            }

            Path resolvedPath = Paths.get(path);
            if (!resolvedPath.isAbsolute()) {
                resolvedPath = Paths.get(System.getProperty("user.dir")).resolve(resolvedPath).normalize();
            }

            return new Image(resolvedPath.toUri().toString(), true);
        } catch (Exception ex) {
            return null;
        }
    }
}