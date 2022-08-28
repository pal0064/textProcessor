package controllers;

import com.opencsv.CSVWriter;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.mvc.*;
import views.html.index;

import java.io.FileWriter;
import java.nio.file.Path;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static play.libs.Scala.asScala;

/**
 * This class uses a custom body parser to change the upload type.
 */
@Singleton
public class HomeController extends Controller {
    private final Form<FormData> form;
    private final AssetsFinder assetsFinder;
    private final play.data.FormFactory formFactory;
    private MessagesApi messagesApi;
    private final Logger logger = LoggerFactory.getLogger(getClass()) ;
    @Inject
    public HomeController(play.data.FormFactory formFactory, MessagesApi messagesApi,AssetsFinder assetsFinder) {
        this.formFactory = formFactory;
        this.form = formFactory.form(FormData.class);
        this.messagesApi = messagesApi;
        this.assetsFinder = assetsFinder;
    }

    public Result index(Http.Request request ) {
        Form<FormData> form = formFactory.form(FormData.class).bindFromRequest(request);
        Messages messages = this.messagesApi.preferred(request);
        return ok(index.render(form, request, messages));
    }
    /**
     * This method uses MyMultipartFormDataBodyParser as the body parser
     */
//    @BodyParser.Of(MyMultipartFormDataBodyParser.class)
    public Result submit(Http.Request request) throws IOException {
//        final Http.MultipartFormData<File> formData = request.body().asMultipartFormData();
//        File file = formData.getFile("file");
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



    public Result  outputHandler(FormData data) throws IOException{
        if ("FILE".equals(data.getOutputType())) {
            return                  ok(createCsvFile());
        }
        return ok(views.html.summary.render(data.getInputText() + getSummary(data.getInputText()), assetsFinder));

    }

    public static String readFile(Path filePath,Charset encoding) throws IOException {
        return Files.readString(filePath, encoding);
    }

    public static File createCsvFile() throws  IOException{
        File file = File.createTempFile("summary_", ".csv");
        try {
            // create FileWriter object with file as parameter
            FileWriter outputFile = new FileWriter(file);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputFile);

            // create a List which contains String array
            List<String[]> data = new ArrayList<String[]>();
            data.add(new String[] { "Stats Name", "Class", "Marks" });
            data.add(new String[] { "Aman", "10", "620" });
            data.add(new String[] { "Suraj", "10", "630" });
            writer.writeAll(data);
            // closing writer connection
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return  file;
    }

public String getSummary(String inputText) {
    long numberOfSentences = 0;
    long numberOfWords = 0;
    long numberOfNouns = 0;
    String NOUN_IDENTIFIER = "NN";
    Properties props = new Properties();
    props.setProperty("annotators", "tokenize,ssplit,pos");
    props.setProperty("coref.algorithm", "neural");
    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
    CoreDocument document = new CoreDocument(inputText);
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
    String summary = String.join(
            "\n",
            "\n\nSummary:\n",
            "Number of Sentences : " + numberOfSentences,
            "Number of Words : " + numberOfWords,
            "Number of Nouns : " + numberOfNouns
    );
    return summary;

}



//    @BodyParser.Of(value = BodyParser.Text.class, maxLength = 10 * 1024)
//    public static Result upload() {
//
//        FilePart picture = body.getFile("picture");
//        if (picture != null) {
//            String fileName = picture.getFilename();
//            String contentType = picture.getContentType();
//            File file = picture.getFile();
//            return ok("File uploaded");
//        } else {
//            flash("error", "Missing file");
//            return redirect(routes.Application.index());
//        }
//    }
    }