package edu.mobile.complaint.model;

public class ElectricityConsumer {

    private int id;

    private String consumerName;

    private String consumerNumber;

    private String consumerPhoneNumber;

    private String consumerAadhaarNumber;

    private String consumerAddress;

    public int getId() {
        return id;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public String getConsumerNumber() {
        return consumerNumber;
    }

    public void setConsumerNumber(String consumerNumber) {
        this.consumerNumber = consumerNumber;
    }

    public String getConsumerPhoneNumber() {
        return consumerPhoneNumber;
    }

    public void setConsumerPhoneNumber(String consumerPhoneNumber) {
        this.consumerPhoneNumber = consumerPhoneNumber;
    }

    public String getConsumerAadhaarNumber() {
        return consumerAadhaarNumber;
    }

    public void setConsumerAadhaarNumber(String consumerAadhaarNumber) {
        this.consumerAadhaarNumber = consumerAadhaarNumber;
    }

    public String getConsumerAddress() {
        return consumerAddress;
    }

    public void setConsumerAddress(String consumerAddress) {
        this.consumerAddress = consumerAddress;
    }
}
