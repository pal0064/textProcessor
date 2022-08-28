package service.nlptools;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import models.Summary;

import java.util.*;

public class NLPService {
  public CoreDocument getAnnotatedDocument(String content) {
    Properties props = new Properties();
    props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");
    props.setProperty("coref.algorithm", "neural");
    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
    CoreDocument document = new CoreDocument(content);
    pipeline.annotate(document);
    return document;
  }

  public Summary getSummary(String content) {
    CoreDocument document = getAnnotatedDocument(content);
    long numberOfSentences = 0;
    long numberOfWords = 0;
    long numberOfNouns = 0;
    boolean orgCheck = false;
    HashMap<String, String> organizationsInvolved = new HashMap<>();
    for (CoreSentence sentence : document.sentences()) {
      numberOfSentences += 1;
      List<String> currentOrg = new ArrayList<String>();
      for (CoreLabel token : sentence.tokens()) {
        numberOfWords += 1;
        if (isTokenNoun(token)) {
          numberOfNouns += 1;
        }
        if (isTokenOrganization(token)) {
          orgCheck = true;
          currentOrg.add(token.word());
        } else {
          if (orgCheck) {
            String org = String.join(" ", currentOrg);
            organizationsInvolved.put(org.toLowerCase(), org);
            currentOrg = new ArrayList<String>();
          }
          orgCheck = false;
        }
      }
    }
    return new Summary(
        numberOfSentences,
        numberOfWords,
        numberOfNouns,
        new ArrayList<String>(organizationsInvolved.values()));
  }

  public boolean isTokenNoun(CoreLabel token) {
    String NOUN_IDENTIFIER = "NN";
    return token.tag().startsWith(NOUN_IDENTIFIER);
  }

  public boolean isTokenOrganization(CoreLabel token) {
    String ORG_IDENTIFIER = "ORGANIZATION";
    return token.ner().equals(ORG_IDENTIFIER);
  }

  public NLPService() {}
}
