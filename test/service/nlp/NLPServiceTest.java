package service.nlp;

import models.Summary;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** This is to test NLP service operations */
public class NLPServiceTest {

  /** To test if getSummary function is working fine for single sentence */
  @Test
  public void getSummaryForSingleSentence() {
    String content =
        "Gondy Leroy is Professor in MIS and Associate Dean for Research at the University of Arizona.";
    List<String> expectedOrgs = new ArrayList<String>();
    expectedOrgs.add("University of Arizona");
    Summary expectedSummary = new Summary(1, 17, 9, expectedOrgs);
    Summary actualSummary = new NLPService().getSummary(content);
    Assert.assertTrue(EqualsBuilder.reflectionEquals(expectedSummary, actualSummary));
  }

  /** To test if getSummary function is working fine for multiple sentences */
  @Test
  public void getSummaryForMultipleSentences() {
    String content =
        "Before coming to Eller in 2013, she joined Claremont Graduate University after earning her PhD "
            + "in Management Information Systems from the University of Arizona in 2003.  Her research "
            + "focuses on natural language processing and machine learning with a practical and positive "
            + "impact. Current example projects are the creation of an online editor for text and audio "
            + "simplification and automated annotation of behaviors matching diagnostic criteria of autism "
            + "spectrum disorders (ASD) in EHR. She has won grants from NIH, AHRQ, NSF, Microsoft Research, "
            + "and several foundations totaling $5.7M as principal investigator, and another $2.1M as "
            + "co-investigator. She earned a BS/MS degree (1996) in experimental psychology from the "
            + "University of Leuven (1996) in Belgium and a MS (1999) and PhD (2003) degree in management "
            + "information systems from the University of Arizona. In addition to her research, "
            + "she wrote the book “Designing User Studies in Informatics” (Springer, 2011) and "
            + "is an active contributor to increasing diversity and inclusion, e.g., through "
            + "her “Tomorrow’s Leaders Equipped for Diversity” program at the Eller College of Management. "
            + "If you would like to subscribe to the Eller Research listserv, send an email message to "
            + "list@list.arizona.edu(link sends e-mail) with the message subject "
            + "being \"subscribe ellerresearch\". David Kauchak’s main research interests lie in natural "
            + "language processing (NLP), with a particular focus on applied NLP. For example, his current "
            + "research focuses on text simplification, which aims to reduce the complexity of text while "
            + "maintaining the content.";
    List<String> expectedOrgs =
        new ArrayList<String>(
            Arrays.asList(
                "NSF",
                "University of Arizona",
                "Eller Research",
                "Microsoft Research",
                "NLP",
                "University of Leuven",
                "Claremont Graduate University",
                "AHRQ",
                "NIH",
                "Eller College of Management"));

    Summary expectedSummary = new Summary(9, 289, 100, expectedOrgs);
    Summary actualSummary = new NLPService().getSummary(content);
    Assert.assertTrue(EqualsBuilder.reflectionEquals(expectedSummary, actualSummary));
  }
}
