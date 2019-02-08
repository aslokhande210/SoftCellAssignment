package com.softcell.assignment.utils;

public interface Regex {
    String NAME_REGEX = "[a-zA-Z]{3,30}";
    String PAN_CARD_REGEX = "^[A-Z]{5}\\d{4}[A-Z]{1}$";
    String AADHAAR_CARD_REGEX = "^[0-9]{12}$";
    String VOTER_ID_REGEX = "^[A-Z]{3}\\d{7}$";
}
