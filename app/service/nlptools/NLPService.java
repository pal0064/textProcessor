package service.nlptools;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import models.Summary;

import java.util.Properties;

public class NLPService {
  public CoreDocument getAnnotatedDocument(String content) {
    Properties props = new Properties();
    props.setProperty("annotators", "tokenize,ssplit,pos");
    props.setProperty("coref.algorithm", "neural");
    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
    CoreDocument document = new CoreDocument(content);
    pipeline.annotate(document);
    return document;
  }

  public Summary getSummary(String content) {
    long numberOfSentences = 0;
    long numberOfWords = 0;
    long numberOfNouns = 0;
    CoreDocument document = getAnnotatedDocument(content);
    for (CoreSentence sentence : document.sentences()) {
      numberOfSentences += 1;
      for (CoreLabel token : sentence.tokens()) {
        numberOfWords += 1;
        if (isTokenNoun(token)) {
          numberOfNouns += 1;
        }
      }
    }
    return new Summary(numberOfSentences, numberOfWords, numberOfNouns);
  }

  public boolean isTokenNoun(CoreLabel token) {
    String NOUN_IDENTIFIER = "NN";
    return token.tag().startsWith(NOUN_IDENTIFIER);
  }

  public NLPService() {}
}
