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
import service.nlptools.NLPService;
import util.file.csv.CSVFileUtils;
import views.html.index;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** This class uses a custom body parser to change the upload type. */
@Singleton
public class HomeController extends Controller {
  private final Form<FormData> form;
  private final AssetsFinder assetsFinder;
  private final play.data.FormFactory formFactory;
  private final CSVFileUtils csvFileUtils;
  private final NLPService nlpService;
  private MessagesApi messagesApi;
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
    String content = data.getContent();
    Summary summary = nlpService.getSummary(content);
    if ("FILE".equals(data.getOutputType())) {
      List<Summary> summaries = new ArrayList<Summary>();
      summaries.add(summary);
      System.out.println(summary.organizations);
      return ok(csvFileUtils.<Summary>createCsvFile("summary", summaries));
    }
    return ok(
        views.html.summary.render(WordUtils.wrap(content, 120) + summary.getText(), assetsFinder));
  }
}
