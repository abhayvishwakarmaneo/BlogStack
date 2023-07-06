package com.blogstack.exceptions;

public class BlogstackDataNotFoundException extends IllegalStateException{

    private static final long serialVersionUID = 1L;

    public BlogstackDataNotFoundException(String message) {
        super(message);
    }

    public BlogstackDataNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}