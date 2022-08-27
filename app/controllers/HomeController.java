package controllers;

import com.opencsv.CSVWriter;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
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

import edu.stanford.nlp.simple.Token;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
/**
 * This class uses a custom body parser to change the upload type.
 */
@Singleton
public class HomeController extends Controller {
    private final AssetsFinder assetsFinder;
    private final play.data.FormFactory formFactory;
    private MessagesApi messagesApi;

    @Inject
    public HomeController(play.data.FormFactory formFactory, MessagesApi messagesApi,AssetsFinder assetsFinder) {
        this.formFactory = formFactory;
        this.messagesApi = messagesApi;
        this.assetsFinder = assetsFinder;
    }

    public Result index(Http.Request request ) {
        Form<FormData> form = formFactory.form(FormData.class).bindFromRequest(request);
        Messages messages = this.messagesApi.preferred(request);
        return ok(index.render(form, request, messages));
    }


    public Result downloadFile(Http.Request request) throws IOException{
        System.out.println("download");
        File csvFile = createCsvFile(request.id());
        return ok(csvFile);
    }

    /**
     * This method uses MyMultipartFormDataBodyParser as the body parser
     */
    @BodyParser.Of(MyMultipartFormDataBodyParser.class)
    public Result upload(Http.Request request) throws IOException {
        final Http.MultipartFormData<File> formData = request.body().asMultipartFormData();
        final Http.MultipartFormData.FilePart<File> filePart = formData.getFile("name");
        final File file = filePart.getRef();
        String content = null;
        try {
            content = readFile(file.toPath(),StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
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
            numberOfSentences+=1;
            for( CoreLabel token : sentence.tokens()){
                numberOfWords+=1;
                if(token.tag().startsWith(NOUN_IDENTIFIER)){
                    numberOfNouns+=1;
                }

            }
        }
         String summary = String.join(
                "\n",
                 "\n\nSummary:\n",
                "Number of Sentences : " + numberOfSentences,
                "Number of Words : "+numberOfWords,
                "Number of Nouns : " + numberOfNouns
        );

        return ok(views.html.summary.render(content + summary,assetsFinder));
    }


    private long operateOnTempFile(File file) throws IOException {
        final long size = Files.size(file.toPath());
        Files.deleteIfExists(file.toPath());
        return size;
    }

    public static String readFile(Path filePath,Charset encoding) throws IOException {
        return Files.readString(filePath, encoding);
    }

    public static File createCsvFile(Long requestId) throws  IOException{
        File file = File.createTempFile("summary_"+requestId, ".csv");
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


    }