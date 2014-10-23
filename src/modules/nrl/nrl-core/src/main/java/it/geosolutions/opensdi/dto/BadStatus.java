package it.geosolutions.opensdi.dto;
public class BadStatus {
    String errorMessage;
    boolean status = false;

    public BadStatus(String msg) { errorMessage = msg; }
}
