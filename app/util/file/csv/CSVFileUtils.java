package util.file.csv;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class CSVFileUtils {
  public <T> File createCsvFile(String filePrefix, List<T> data)
      throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
    File file = File.createTempFile(filePrefix, ".csv");
    Writer writer = new FileWriter(file);
    StatefulBeanToCsv<T> sbc =
        new StatefulBeanToCsvBuilder<T>(writer).withApplyQuotesToAll(false).build();
    sbc.write(data);
    writer.close();
    return file;
  }
}
