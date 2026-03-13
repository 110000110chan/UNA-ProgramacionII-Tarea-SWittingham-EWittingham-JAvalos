package cr.ac.una.tareaprograii2026.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author michw
 */
public class FileManager {

    private ObjectMapper createMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }

    private File resolveFile(String filename) {
        return new File(filename + ".txt");
    }

    public <T> void serialization(List<T> list, String filename) {
        try {
            ObjectMapper objectMapper = createMapper();
            File targetFile = resolveFile(filename);
            File parent = targetFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            objectMapper.writeValue(targetFile, list);
            System.out.println("Lista guardada exitosamente en " + filename);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al guardar la lista en el archivo.");
        }
    }

    public <T> List<T> deserialization(String filename, Class<T> classType) {
        List<T> list = new ArrayList<>();
        try {
            File sourceFile = resolveFile(filename);
            if (!sourceFile.exists() || sourceFile.length() == 0) {
                return list;
            }

            ObjectMapper objectMapper = createMapper();
            list = objectMapper.readValue(sourceFile, objectMapper.getTypeFactory().constructCollectionType(List.class, classType));
            System.out.println("Lista leída exitosamente desde " + filename);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al leer la lista desde el archivo.");
        }
        return list;
    }
}

