package co.edu.uptc.server.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import co.edu.uptc.server.model.pojos.Auditorium;
import co.edu.uptc.server.model.pojos.Book;
import co.edu.uptc.server.model.pojos.Movie;
import co.edu.uptc.server.model.pojos.Schedule;

public class FileManager {
	private File moviesFile;
	private File schedulesFile;
	private File booksFile;
	private File auditoriumsFile;
	private ObjectMapper mapper;

	public FileManager() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		this.moviesFile = new File("src\\main\\resources\\Movies.json");
		this.auditoriumsFile = new File("src\\main\\resources\\Auditoriums.json");
		this.schedulesFile = new File("src\\main\\resources\\Shedules.json");
		this.booksFile = new File("src\\main\\resources\\Books.json");
	}

	public List<List> getData() {
		// TODO pasar todo esto a bien xdd
		List<List> data = new ArrayList<>();
		data.add(read(moviesFile, new TypeReference<List<Movie>>() {
		}));
		data.add(read(auditoriumsFile, new TypeReference<List<Auditorium>>() {
		}));
		data.add(read(schedulesFile, new TypeReference<List<Schedule>>() {
		}));
		// data.add(read(booksFile, new TypeReference<List<Book>>() {}));
		return data;
	}

	public void saveData(List<List> data) {
		write(data.get(0), moviesFile);
		write(data.get(1), auditoriumsFile);
		write(data.get(2), schedulesFile);
		// write(data.get(3), booksFile);
	}

	public <T> void write(List<T> list, File file) {
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(file, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public <T> List<T> read(File file, TypeReference<List<T>> typeReference) {
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
}
