package models;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvNumber;

public class Summary {
    @CsvBindByName(column = "Number Of Sentences")
    public long numberOfSentences;
    @CsvBindByName(column = "Number Of Words")
//    @CsvNumber("#")
    @CsvNumber("#.###")
    public long numberOfWords;
    @CsvBindByName(column = "Number Of Nouns")
//    @CsvNumber("#")
    @CsvNumber("#.###")
    public long numberOfNouns;
    public Summary(long numberOfSentences, long numberOfWords,long numberOfNouns) {
        this.numberOfSentences = numberOfSentences;
        this.numberOfWords = numberOfWords;
        this.numberOfNouns = numberOfNouns;
    }
    public String getText(){
        String summary = String.join(
                "\n",
                "\n\nSummary:\n",
                "Number of Sentences : " + numberOfSentences,
                "Number of Words : " + numberOfWords,
                "Number of Nouns : " + numberOfNouns
        );
        return  summary;
    }
}
