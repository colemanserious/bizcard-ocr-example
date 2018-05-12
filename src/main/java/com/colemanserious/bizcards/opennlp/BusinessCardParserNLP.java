package com.colemanserious.bizcards.opennlp;

import com.colemanserious.bizcards.*;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class BusinessCardParserNLP implements BusinessCardParser {

    /**
     * NOTE from OpenNLP documentation:
     *  "The NameFinderME class is not thread safe, it must only be called from one thread.
     *  To use multiple threads multiple NameFinderME instances sharing the same model instance can be created."
     */
    NameFinderME nameFinder;

    public BusinessCardParserNLP()  {
        TokenNameFinderModel model;

        try (InputStream modelIn = this.getClass().getClassLoader().getResourceAsStream("en-ner-person.bin")) {
            model = new TokenNameFinderModel(modelIn);
        } catch (IOException io) {
            throw new RuntimeException("Unable to set up nlp parser");
        }

        nameFinder = new NameFinderME(model);
    }


    public ContactInfo getContactInfo(String text) {

        Map<String, Double> probableNames = new HashMap();
        String name = null;

        // Treat each line individually
        //  Assumption: Names stay on same line!
        String[] documentLines = text.split("\n");

        for (String line : documentLines) {

            // Check for name
            String[] lineText = line.split("\\s+");
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


            // Check for emailAddress
        }

        // Assumption: there's one name per business card
        //   use entry with maximum probability, in case nlp identifies more than one person's name
        String maxProbName = probableNames.entrySet().stream()
                .max((n1, n2) -> Double.compare(n1.getValue(), n2.getValue()))
                .get()
                .getKey();

        nameFinder.clearAdaptiveData();

        return new NLPContactInfo(maxProbName, null, null);


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
