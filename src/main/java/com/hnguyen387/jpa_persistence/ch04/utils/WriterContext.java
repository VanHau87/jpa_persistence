package com.hnguyen387.jpa_persistence.ch04.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class WriterContext<T> {
	private WriteStrategy<T> writeStrategy;

	public WriterContext(WriteStrategy<T> writeStrategy) {
		this.writeStrategy = writeStrategy;
	}

	public WriteStrategy<T> getWriteStrategy() {
		return writeStrategy;
	}

	public void setWriteStrategy(WriteStrategy<T> writeStrategy) {
		this.writeStrategy = writeStrategy;
	}
	
	public void writeFile(Path path, List<T> ts) {
		checkOrCreate(path);
		writeStrategy.write(path, ts);
	}
	private void checkOrCreate(Path path) {
        try {
            if (!Files.exists(path)) {
            	Files.createDirectories(path);
            }
        } catch (IOException e) {
            System.err.println("Error creating directory: " + e.getMessage());
        }
    }
}
