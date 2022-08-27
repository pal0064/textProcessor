package controllers;

import play.data.Form;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.mvc.*;
import views.html.index;
import java.nio.file.Path;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import edu.stanford.nlp.simple.Token;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
/**
 * This class uses a custom body parser to change the upload type.
 */
@Singleton
public class HomeController extends Controller {

    private final play.data.FormFactory formFactory;
    private MessagesApi messagesApi;

    @Inject
    public HomeController(play.data.FormFactory formFactory, MessagesApi messagesApi) {
        this.formFactory = formFactory;
        this.messagesApi = messagesApi;
    }

    public Result index(Http.Request request ) {
        Form<FormData> form = formFactory.form(FormData.class).bindFromRequest(request);
        Messages messages = this.messagesApi.preferred(request);
        return ok(index.render(form, request, messages));
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

        Document doc = new Document(content);
        long numberOfSentences = 0;
        long numberOfWords = 0;
        long numberOfNouns = 0;
        for (Sentence sent : doc.sentences()) {
            numberOfSentences+=1;
            numberOfWords+= sent.words().size();
            for( Token token : sent.tokens()){
                numberOfWords+=1;
                if(token.posTag().startsWith("NN")){
                    numberOfNouns+=1;
                    System.out.println(token.word() + " " +  token.posTag());
                }

            }
        }
        String summary = String.join("\n",
                "Summary:",
                "Number of Sentences : " + numberOfSentences,
                "Number of Words : "+numberOfWords,
                "Number of Nouns : " + numberOfNouns
                );
         return ok(  content + summary);
    }

    private long operateOnTempFile(File file) throws IOException {
        final long size = Files.size(file.toPath());
        Files.deleteIfExists(file.toPath());
        return size;
    }

    public static String readFile(Path filePath,Charset encoding) throws IOException {
        return Files.readString(filePath, encoding);
    }

}

