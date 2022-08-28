package controllers;

import play.data.validation.Constraints;
import play.libs.Files.TemporaryFile;
import play.mvc.Http.MultipartFormData.FilePart;

import java.io.File;

/**
 * A form processing DTO that maps to the widget form.
 *
 * Using a class specifically for form binding reduces the chances
 * of a parameter tampering attack and makes code clearer, because
 * you can define constraints against the class.
 */
public class FormData {

    private String inputText;

    private FilePart<TemporaryFile> inputFile;

    private String outputType;


    public FormData() {
    }

    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public FilePart<TemporaryFile> getInputFile() {
        return inputFile;
    }

    public void setInputFile(FilePart<TemporaryFile> inputFile) {
        this.inputFile = inputFile;
    }


    public String getOutputType() {
        return outputType;
    }
    public void setOutputType(String outputType) {
        this.outputType = outputType;
    }

}
