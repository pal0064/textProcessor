package util.file;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

/** This class is used to provide all file related operations */
public class FileUtils {

  // TODO provide customization of file reading options
  public static String readFile(Path filePath, Charset encoding) throws IOException {
    return Files.readString(filePath, encoding);
  }
}
