package services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileService {
  public List<File> indexFiles = new ArrayList<File>();

  public void findFiles(String name, File file) {
    File[] list = file.listFiles();
    if (list != null) {
      for (File fil : list) {
        if (fil.isDirectory()) {
          findFiles(name, fil);
        } else if (name.equalsIgnoreCase(fil.getName())) {
          indexFiles.add(fil);
        }
      }
    }
  }
}
