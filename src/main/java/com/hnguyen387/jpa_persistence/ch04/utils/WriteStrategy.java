package com.hnguyen387.jpa_persistence.ch04.utils;

import java.nio.file.Path;
import java.util.List;

public interface WriteStrategy<T> {
	void write(Path path, List<T> ts);
}
