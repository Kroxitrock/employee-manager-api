package com.andreystrinski.employee.manager.employee.exception;

public class SameStatusException extends RuntimeException {

  private static final String MESSAGE_TEMPLATE = "Status change error: the employee was already in that status %s!";

  public SameStatusException(String statusName) {
    super(String.format(MESSAGE_TEMPLATE, statusName));
  }
}
