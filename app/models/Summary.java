package models;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvNumber;
import org.apache.commons.text.WordUtils;

import java.util.List;

public class Summary {
  @CsvBindByName(column = "Number Of Sentences")
  public long numberOfSentences;

  @CsvBindByName(column = "Number Of Words")
  public long numberOfWords;

  @CsvBindByName(column = "Number Of Nouns")
  public long numberOfNouns;

  @CsvBindAndSplitByName(column = "Organizations", elementType = String.class, writeDelimiter = ",")
  public List<String> organizations;

  public Summary(
      long numberOfSentences, long numberOfWords, long numberOfNouns, List<String> organizations) {
    this.numberOfSentences = numberOfSentences;
    this.numberOfWords = numberOfWords;
    this.numberOfNouns = numberOfNouns;
    this.organizations = organizations;
  }

  public String getText() {
    String summary =
        String.join(
            "\n",
            "\nSummary:\n",
            "\nNumber of Sentences : " + numberOfSentences,
            "\nNumber of Words : " + numberOfWords,
            "\nNumber of Nouns : " + numberOfNouns,
            "\nOrganizations : " + WordUtils.wrap(String.join(", ", organizations), 120));
    return summary;
  }
}
