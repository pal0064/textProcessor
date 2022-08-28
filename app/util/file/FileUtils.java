package util.file;

import java.io.IOException;

/** This class is used to provide all file related operations */
public class FileUtils {

  // TODO provide customization of file reading options
  public static String readFile(java.nio.file.Path filePath, java.nio.charset.Charset encoding)
      throws IOException {
    return java.nio.file.Files.readString(filePath, encoding);
  }
}
