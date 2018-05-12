package com.colemanserious.bizcards.opennlp;

import com.colemanserious.bizcards.BusinessCardParser;
import com.colemanserious.bizcards.ContactInfo;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.RegexValidator;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class BusinessCardParserNLP implements BusinessCardParser {

    /**
     * NOTE from OpenNLP documentation:
     * "The NameFinderME class is not thread safe, it must only be called from one thread.
     * To use multiple threads multiple NameFinderME instances sharing the same model instance can be created."
     */
    NameFinderME nameFinder;

    EmailValidator emailValidator;
    RegexValidator phoneNumberRegExValidator;

    public BusinessCardParserNLP() {
        TokenNameFinderModel model;

        try (InputStream modelIn = this.getClass().getClassLoader().getResourceAsStream("en-ner-person.bin")) {
            model = new TokenNameFinderModel(modelIn);
        } catch (IOException io) {
            throw new RuntimeException("Unable to set up nlp parser");
        }

        nameFinder = new NameFinderME(model);
        emailValidator = EmailValidator.getInstance();

        // Dev note for helping with testing further regex: http://reg-exp.com/

        //  (xxx)xxx-xxxx, xxx-xxx-xxxx,  (where spacer: -, ., or space)
        String phoneExpr = "\\(?(\\d{3})\\)?[-\\.\\s]?(\\d{3})[-\\.\\s]?(\\d{4})";
        phoneNumberRegExValidator = new RegexValidator(
                new String[]{
                    "(\\d{10})",  // 10 digits - simplest case
                    "^" + phoneExpr,
                    "Phone:\\s?" + phoneExpr,
                    "Tel:\\s?\\+(\\d{1})\\s?" + phoneExpr
                },
                false  // not case sensitive
        );
    }

    public ContactInfo getContactInfo(String text) {

        Map<String, Double> probableNames = new HashMap();
        String name = null, emailAddress = null, phoneNumber = null;

        String testedEmailAddress, testedPhoneNumber;

        // Treat each line individually
        //  Assumption: Names, phone numbers, email addresses stay on same line!
        String[] documentLines = text.split("\n");

        for (String line : documentLines) {

            String[] lineText = line.split("\\s+");

            // Check for name
            Span[] spans = nameFinder.find(lineText);

            for (Span span : spans) {
                if (span.getType().equals("person")) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = span.getStart(); i < span.getEnd(); i++) {
                        sb.append(lineText[i]).append(" ");
                    }
                    name = sb.toString().trim();
                    probableNames.put(name, span.getProb());
                }
            }

            // Check for phone number
            testedPhoneNumber = getValidPhoneNumber(line);
            if (testedPhoneNumber != null) {
                phoneNumber = testedPhoneNumber;
            }

            // Check for emailAddress
            for (String possibleAddress : lineText) {
                testedEmailAddress = getValidEmailAddress(possibleAddress);
                if (testedEmailAddress != null) {
                    emailAddress = testedEmailAddress;
                }
            }
        }

        // Assumption: there's at most one name per business card
        //  (If we're not really given a business card, may be none)
        if (probableNames.entrySet().size() > 0) {

            //   use entry with maximum probability, in case nlp identifies more than one person's name
            name = probableNames.entrySet().stream()
                    .max((n1, n2) -> Double.compare(n1.getValue(), n2.getValue()))
                    .get()
                    .getKey();
        }

        nameFinder.clearAdaptiveData();

        return new NLPContactInfo(name, phoneNumber, emailAddress);

    }

    String getValidEmailAddress(String checkText) {
        if (emailValidator.isValid(checkText)) {
            return checkText;
        }
        return null;
    }

    String getValidPhoneNumber(String textLine) {

        String checkText = textLine.trim();
        if (phoneNumberRegExValidator.isValid(checkText)) {
            String[] digits = phoneNumberRegExValidator.match(checkText);
            if (digits != null) {
                return String.join("", digits);
            }
        }
        return null;
    }

    class NLPContactInfo implements ContactInfo {

        private String name = null;
        private String phoneNumber = null;
        private String email = null;

        NLPContactInfo(String name, String phoneNumber, String email) {
            this.name = name;
            this.phoneNumber = phoneNumber;
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public String getEmailAddress() {
            return email;
        }

    }
}
