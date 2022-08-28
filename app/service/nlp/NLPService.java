package service.nlp;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import models.Summary;

import java.util.*;

/** This is used to provide NLP related operations */
public class NLPService {

  /**
   * This uses the CoreNLP Api which is more customizable than the Simple NLP API Using only
   * required annotators increases the speed of processing. Customization of algorithm is possible,
   * could be added in the web form as a input
   *
   * @param content input content to be processed
   * @return NLP Library annotated document
   */
  // TODO NLP properties customization
  public CoreDocument getAnnotatedDocument(String content) {
    Properties props = new Properties();
    props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");
    props.setProperty("coref.algorithm", "neural");
    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
    CoreDocument document = new CoreDocument(content);
    pipeline.annotate(document);
    return document;
  }

  /**
   * This generates the final summary stats from the annotated document words() or tags() function
   * could have been used but using tokens() is better in terms of processing.
   *
   * @param content input content to be processed
   * @return NLP summary stats
   */
  //  TODO to support a large number of stat variables,
  //   this should be broken down into multiple functions to improve readability
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
