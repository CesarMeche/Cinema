package co.edu.uptc.server.persistence;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import co.edu.uptc.server.model.pojos.Auditorium;
import co.edu.uptc.server.model.pojos.Movie;
import co.edu.uptc.server.model.pojos.Schedule;
import co.edu.uptc.server.model.pojos.Book;
import co.edu.uptc.server.structures.avltree.*;

public class FileManager {

    // Rutas relativas de los recursos dentro del JAR
    private String moviesResource = "/Movies.json";
    private String auditoriumsResource = "/Auditoriums.json";
    private String schedulesResource = "/Shedules.json";
    private String booksResource = "/Books.json";

    // Directorio externo para guardar los cambios (opcional)
    private File moviesFile = new File("data/Movies.json");
    private File auditoriumsFile = new File("data/Auditoriums.json");
    private File schedulesFile = new File("data/Shedules.json");
    private File booksFile = new File("data/Books.json");

    private ObjectMapper mapper;

    public FileManager() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        SimpleModule module = new SimpleModule();
        module.addSerializer(AVLTree.class, new AVLTreeSerializer());
        module.addDeserializer(AVLTree.class, new AVLTreeDeserializer());
        mapper.registerModule(module);
    }

    /**
     * Obtiene los datos desde los recursos (empaquetados en el JAR).
     */
    public List<List> getData() {
        List<List> data = new ArrayList<>();
        data.add(readFromResource(moviesResource, new TypeReference<List<Movie>>() {}));
        data.add(readFromResource(auditoriumsResource, new TypeReference<List<Auditorium>>() {}));
        data.add(readFromResource(schedulesResource, new TypeReference<List<Schedule>>() {}));
        data.add(readFromResource(booksResource, new TypeReference<List<Book>>() {}));
        return data;
    }

    /**
     * Guarda los datos en archivos externos.
     */
    public void saveData(List<List> data) {
        write(data.get(0), moviesFile);
        write(data.get(1), auditoriumsFile);
        write(data.get(2), schedulesFile);
        write(data.get(3), booksFile);
    }

    /**
     * Lee un recurso desde el JAR usando InputStream.
     */
    public <T> List<T> readFromResource(String resourcePath, TypeReference<List<T>> typeReference) {
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                System.out.println("Recurso no encontrado: " + resourcePath);
                return Collections.emptyList();
            }
            return mapper.readValue(is, typeReference);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Escribe los datos en un archivo externo.
     */
    public <T> void write(List<T> list, File file) {
        try {
            // Asegurarse de que el directorio exista
            File parentDir = file.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lee datos desde un archivo externo.
     */
    public <T> List<T> readFromFile(File file, TypeReference<List<T>> typeReference) {
        try {
            if (!file.exists() || file.length() == 0) {
                return Collections.emptyList();
            }
            return mapper.readValue(file, typeReference);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Alternativa: si quieres que en algunos casos cargue desde archivos externos.
     */
    public List<List> getDataFromFiles() {
        List<List> data = new ArrayList<>();
        data.add(readFromFile(moviesFile, new TypeReference<List<Movie>>() {}));
        data.add(readFromFile(auditoriumsFile, new TypeReference<List<Auditorium>>() {}));
        data.add(readFromFile(schedulesFile, new TypeReference<List<Schedule>>() {}));
        data.add(readFromFile(booksFile, new TypeReference<List<Book>>() {}));
        return data;
    }
}
