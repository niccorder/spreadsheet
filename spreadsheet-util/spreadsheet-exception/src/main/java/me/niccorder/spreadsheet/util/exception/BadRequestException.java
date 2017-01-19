package me.niccorder.spreadsheet.util.exception;

import java.io.IOException;

/**
 * Thrown when we have sent a bad request.
 */
public class BadRequestException extends IOException {
  public BadRequestException(String s) {
    super(s);
  }
}
