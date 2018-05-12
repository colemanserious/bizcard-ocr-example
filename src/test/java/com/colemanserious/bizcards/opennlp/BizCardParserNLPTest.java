package com.colemanserious.bizcards.opennlp;

import com.colemanserious.bizcards.BusinessCardParser;
import com.colemanserious.bizcards.ContactInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for examples 1 - 3 are those described for BusinessCard OCR challenge
 *  at https://www.asymmetrik.com/programming-challenges/
 */
public class BizCardParserNLPTest {

    BusinessCardParser parser;

    @BeforeAll
    public static void setupForAllTests() {

    }

    @BeforeEach
    public void setupBeforeEachTest() {
        parser = new BusinessCardParserNLP();
    }

    public void genericTest( String document, String name, String phoneNumber, String emailAddress ) {

        ContactInfo info = parser.getContactInfo(document);
        assertEquals(name, info.getName());
        //assertEquals(info.getPhoneNumber(), phoneNumber);
        //assertEquals(info.getEmailAddress(),  emailAddress);

    }
    @Test
    public void testCaseExample1() {

        genericTest("ASYMMETRIK LTD\n" +
                "Mike Smith\n" +
                "Senior Software Engineer\n" +
                "(410)555-1234\n" +
                "msmith@asymmetrik.com",
                "Mike Smith", "4105551234", "msmith@asymmetrik.com");

    }

    @Test
    public void testCaseExample2() {

        genericTest("Foobar Technologies\n" +
                "Analytic Developer\n" +
                "Lisa Haung\n" +
                "1234 Sentry Road\n" +
                "Columbia, MD 12345\n" +
                "Phone: 410-555-1234\n" +
                "Fax: 410-555-4321\n" +
                "lisa.haung@foobartech.com",
                "Lisa Haung", "4105551234", "lisa.haung@foobartech.com");
    }

    @Test
    public void testCaseExample3() {

        genericTest("Arthur Wilson\n" +
                "Software Engineer\n" +
                "Decision & Security Technologies\n" +
                "ABC Technologies\n" +
                "123 North 11th Street\n" +
                "Suite 229\n" +
                "Arlington, VA 22209\n" +
                "Tel: +1 (703) 555-1259\n" +
                "Fax: +1 (703) 555-1200\n" +
                "awilson@abctech.com",
                "Arthur Wilson", "17035551259", "awilson@abctech.com");
    }


}
