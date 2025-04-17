package com.example.fpoeuno.models;
//Card's class
public class Cards {
    String cardImageUrl,cardType,cardColor;
    byte cardNumber;
    //constructor
    public Cards(String cardImageUrl,String cardType, String cardColor, byte cardNumber) {
        this.cardImageUrl = cardImageUrl;
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.cardNumber = cardNumber;}
    //getters
    public String getCardImageUrl() {return cardImageUrl;}
    public String getCardType() {return cardType;}
    public String getCardColor() {return cardColor;}
    public byte getCardNumber() {return cardNumber;}

    //setters
    public void setCardImageUrl(String cardImageUrl) {this.cardImageUrl = cardImageUrl;}
    public void setCardType(String cardType) {this.cardType = cardType;}
    public void setCardColor(String cardColor) {this.cardColor = cardColor;}
    public void setCardNumber(byte cardNumber) {this.cardNumber = cardNumber;}

}
