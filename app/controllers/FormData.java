package controllers;

import play.libs.Files.TemporaryFile;
import play.mvc.Http;

/**
 * A form processing DTO that maps to the widget form.
 *
 * <p>Using a class specifically for form binding reduces the chances of a parameter tampering
 * attack and makes code clearer, because you can define constraints against the class.
 */
public class FormData {

  private String inputText;

  private Http.MultipartFormData.FilePart<TemporaryFile> inputFile;

  private String outputType;

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
}
