package controllers;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import models.FormData;
import models.Summary;
import org.apache.commons.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.nlp.NLPService;
import util.file.csv.CSVFileUtils;
import views.html.index;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** This class is the controller for the home page of the web application. */
@Singleton
public class HomeController extends Controller {
  private final Form<FormData> form;
  private final AssetsFinder assetsFinder;
  private final play.data.FormFactory formFactory;
  private final CSVFileUtils csvFileUtils;
  private final NLPService nlpService;
  private final MessagesApi messagesApi;
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Inject
  public HomeController(
      FormFactory formFactory,
      MessagesApi messagesApi,
      AssetsFinder assetsFinder,
      CSVFileUtils csvFileUtils,
      NLPService nlpService) {
    this.formFactory = formFactory;
    this.form = formFactory.form(FormData.class);
    this.messagesApi = messagesApi;
    this.assetsFinder = assetsFinder;
    this.csvFileUtils = csvFileUtils;
    this.nlpService = nlpService;
  }

  /**
   * Returns a web form to get input from the user
   *
   * @param request a http request for the homepage
   * @return the web form for text processing
   */
  public Result index(Http.Request request) {
    Form<FormData> form = formFactory.form(FormData.class).bindFromRequest(request);
    Messages messages = this.messagesApi.preferred(request);
    return ok(index.render(form, request, messages));
  }

  /**
   * Returns NLP summary stats via csv file or screen after binding the http request to the
   * FormData. Also, it checks for the errors in webform
   *
   * @see models.Summary
   * @see FormData
   * @param request a http request having webform data
   * @return NLP Summary stats of the input text
   * @throws IOException
   * @throws CsvRequiredFieldEmptyException
   * @throws CsvDataTypeMismatchException
   */
  // TODO Better error handling and return error codes
  public Result submit(Http.Request request)
      throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {

    final Form<FormData> boundForm = form.bindFromRequest(request);
    if (boundForm.hasErrors()) {
      logger.error("errors = {}", boundForm.errors());
      Messages messages = messagesApi.preferred(request);
      return badRequest(index.render(boundForm, request, messages));
    } else {
      FormData data = boundForm.get();
      return process(data);
    }
  }

  /**
   * This method gets the input content based on input type (file or text) and NLP service generates
   * stats from the content Based on the output type, it returns a file or the webpage containing
   * summary stats.
   *
   * @param data Submitted webform
   * @return NLP Summary stats of the input text
   * @throws IOException
   * @throws CsvRequiredFieldEmptyException
   * @throws CsvDataTypeMismatchException
   */
  // TODO More output file formats, customization of NLP library properties,Using Enums and
  // Constants in
  // place of hard-coded variables,and bulk file handling etc.
  public Result process(FormData data)
      throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
    String content = data.getContent();
    Summary summary = nlpService.getSummary(content);
    if ("FILE".equals(data.getOutputType())) {
      List<Summary> summaries = new ArrayList<Summary>();
      summaries.add(summary);
      return ok(csvFileUtils.<Summary>createCsvFile("summary", summaries));
    }
    return ok(
        views.html.summary.render(WordUtils.wrap(content, 120) + summary.getText(), assetsFinder));
  }
}
