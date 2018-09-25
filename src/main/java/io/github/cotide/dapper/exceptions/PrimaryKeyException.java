package io.github.cotide.dapper.exceptions;

public class PrimaryKeyException extends RuntimeException {


    public PrimaryKeyException(String message)
    {
        super(message);
    }


    public PrimaryKeyException(){
        super("Primary key not found in model class");
    }
}
