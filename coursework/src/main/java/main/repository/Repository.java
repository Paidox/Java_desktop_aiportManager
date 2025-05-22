package main.repository;

import java.nio.file.Path;
import java.util.List;

public interface Repository<T> 
{
  void outputList(List<T> t, Path file);

  void outputList(List<T> t, String fileName);

  List<T> readList(Path file);

  List<T> readList(String fileName);
}

