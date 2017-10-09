package me.deepak.api.exceptions;

/**
 * Created by Deepak Mishra
 */

public class NoMoreDataException extends Exception {

    int pageNumber; int pageSize;

    public NoMoreDataException(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }
}
