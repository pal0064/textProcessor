package controllers;

import com.opencsv.CSVWriter;
import com.opencsv.bean.FuzzyMappingStrategyBuilder;
import com.opencsv.bean.MappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import models.Summary;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.index;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/** This class uses a custom body parser to change the upload type. */
@Singleton
public class HomeController extends Controller {
  private final Form<FormData> form;
  private final AssetsFinder assetsFinder;
  private final play.data.FormFactory formFactory;
  private MessagesApi messagesApi;
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Inject
  public HomeController(
      play.data.FormFactory formFactory, MessagesApi messagesApi, AssetsFinder assetsFinder) {
    this.formFactory = formFactory;
    this.form = formFactory.form(FormData.class);
    this.messagesApi = messagesApi;
    this.assetsFinder = assetsFinder;
  }

  public Result index(Http.Request request) {
    Form<FormData> form = formFactory.form(FormData.class).bindFromRequest(request);
    Messages messages = this.messagesApi.preferred(request);
    return ok(index.render(form, request, messages));
  }
  /** This method uses MyMultipartFormDataBodyParser as the body parser */
  public Result submit(Http.Request request)
      throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {

    final Form<FormData> boundForm = form.bindFromRequest(request);
    if (boundForm.hasErrors()) {
      logger.error("errors = {}", boundForm.errors());
      Messages messages = messagesApi.preferred(request);
      return badRequest(index.render(boundForm, request, messages));
    } else {
      FormData data = boundForm.get();
      return outputHandler(data);
    }
  }

  public Result outputHandler(FormData data)
      throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
    Summary summary = getSummary(data);
    if ("FILE".equals(data.getOutputType())) {
      return ok(createCsvFile(summary));
    }
    return ok(views.html.summary.render(data.getInputText() + summary.getText(), assetsFinder));
  }

  public static String readFile(Path filePath, Charset encoding) throws IOException {
    return Files.readString(filePath, encoding);
  }

  public static File createCsvFile(Summary summary)
      throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
    File file = File.createTempFile("summary_", ".csv");
    Writer writer = new FileWriter(file);
    List<Summary> summaries = new ArrayList<Summary>();
    summaries.add(summary);
    MappingStrategy<Summary> strategy = new FuzzyMappingStrategyBuilder<Summary>().build();
    strategy.setType(Summary.class);
    StatefulBeanToCsv<Summary> sbc =
        new StatefulBeanToCsvBuilder<Summary>(writer)
            .withMappingStrategy(strategy)
            .withApplyQuotesToAll(false)
            .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
            .build();
    sbc.write(summaries);
    writer.close();
    return file;
  }

  public Summary getSummary(FormData form) throws IOException {
    String inputText = form.getInputText();
    String content;
    if (StringUtils.isBlank(inputText) || StringUtils.isEmpty(inputText)) {
      Http.MultipartFormData.FilePart<play.libs.Files.TemporaryFile> tmpFilePart =
          form.getInputFile();
      play.libs.Files.TemporaryFile file = tmpFilePart.getRef();
      content = readFile(file.path(), StandardCharsets.UTF_8);
    } else {
      content = inputText;
    }
    long numberOfSentences = 0;
    long numberOfWords = 0;
    long numberOfNouns = 0;
    String NOUN_IDENTIFIER = "NN";
    Properties props = new Properties();
    props.setProperty("annotators", "tokenize,ssplit,pos");
    props.setProperty("coref.algorithm", "neural");
    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
    CoreDocument document = new CoreDocument(content);
    pipeline.annotate(document);
    for (CoreSentence sentence : document.sentences()) {
      numberOfSentences += 1;
      for (CoreLabel token : sentence.tokens()) {
        numberOfWords += 1;
        if (token.tag().startsWith(NOUN_IDENTIFIER)) {
          numberOfNouns += 1;
        }
      }
    }
    return new Summary(numberOfSentences, numberOfWords, numberOfNouns);
  }
}
