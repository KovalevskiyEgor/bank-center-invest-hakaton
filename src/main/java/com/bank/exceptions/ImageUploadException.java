package com.bank.exceptions;


public class ImageUploadException extends RuntimeException{
    public ImageUploadException(String message) {
        super(message);
    }
}