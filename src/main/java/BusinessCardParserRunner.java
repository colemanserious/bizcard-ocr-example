import com.colemanserious.bizcards.BusinessCardParser;
import com.colemanserious.bizcards.ContactInfo;
import com.colemanserious.bizcards.opennlp.BusinessCardParserNLP;

public class BusinessCardParserRunner {
    public static void main(String[] args) {



        if (args.length > 0) {
            BusinessCardParser parser = new BusinessCardParserNLP();
            ContactInfo info = parser.getContactInfo(args[0]);

            System.out.println("\n  Info: ");
            System.out.println("-- name: " + info.getName());
            System.out.println("-- emailAddress: " + info.getEmailAddress());
            System.out.println("-- phoneNumber: " + info.getPhoneNumber());


        } else {
            System.out.println("Please provide the text of the business card to parse.");
        }

    }
}
