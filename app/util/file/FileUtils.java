package util.file;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {

  public static String readFile(Path filePath, Charset encoding) throws IOException {
    return Files.readString(filePath, encoding);
  }
}
