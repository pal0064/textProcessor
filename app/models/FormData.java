package models;

import org.apache.commons.lang3.StringUtils;
import play.data.validation.Constraints;
import play.libs.Files.TemporaryFile;
import play.mvc.Http;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static util.file.FileUtils.readFile;

/** A form processing DTO that maps to the FormData. */
public class FormData {

  private String inputText;

  private Http.MultipartFormData.FilePart<TemporaryFile> inputFile;

  @Constraints.Required private String outputType;

  public FormData() {}

  public String getInputText() {
    return inputText;
  }

  public void setInputText(String inputText) {
    this.inputText = inputText;
  }

  public Http.MultipartFormData.FilePart<TemporaryFile> getInputFile() {
    return inputFile;
  }

  public void setInputFile(Http.MultipartFormData.FilePart<TemporaryFile> inputFile) {
    this.inputFile = inputFile;
  }

  public String getOutputType() {
    return outputType;
  }

  public void setOutputType(String outputType) {
    this.outputType = outputType;
  }

  /**
   * It returns the input content based on inputFile or inputText if both are provided then
   * inputText is used.
   *
   * @return input content to be processed
   * @throws IOException
   */
  // TODO handling of a bulk file
  public String getContent() throws IOException {
    String content;
    if (StringUtils.isBlank(inputText) || StringUtils.isEmpty(inputText)) {
      Http.MultipartFormData.FilePart<play.libs.Files.TemporaryFile> tmpFilePart = inputFile;
      play.libs.Files.TemporaryFile file = tmpFilePart.getRef();
      content = readFile(file.path(), StandardCharsets.UTF_8);
    } else {
      content = inputText;
    }
    return content;
  }
}
