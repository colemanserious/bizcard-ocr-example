package com.colemanserious.bizcards;

public interface ContactInfo {

    /**
     *
     * @return the full name of the individual (eg. John Smith, Susan Malick)
     */
    String getName();

    /**
     *
     * @return the phone number formatted as a sequence of digits
     */
    String getPhoneNumber();

    /**
     *
     * @return email address
     */
    String getEmailAddress();

}
