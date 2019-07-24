/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big
 * Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed
 * or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered,
 * disassembled or decompiled, without express written authorization from Stratio Big Data Inc.,
 * Sucursal en España.
 */
package com.stratio.architecture.multihdfs.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
@RestController
@Slf4j
public class ErrorHandler {

  @ExceptionHandler({Exception.class})
  public ErrorResponse exceptionHandler(Exception exception, HttpServletResponse response) {
    ErrorResponse errorResponse = new ErrorResponse(EnumError.UNEXPECTED_ERROR);
    response.setStatus(errorResponse.getCode());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    log.error(EnumError.UNEXPECTED_ERROR.getMessage(), exception);
    return errorResponse;
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ErrorResponse httpRequestMethodNotSupportedHandler(
      HttpRequestMethodNotSupportedException exception, HttpServletResponse response) {
    if (log.isDebugEnabled())
      log.debug(EnumError.REQUEST_METHOD_NOT_SUPPORTED.getMessage(), exception);

    ErrorResponse errorResponse = new ErrorResponse(EnumError.REQUEST_METHOD_NOT_SUPPORTED);
    response.setStatus(errorResponse.getCode());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    log.debug(EnumError.REQUEST_METHOD_NOT_SUPPORTED.getDescription());
    return errorResponse;
  }

  @ExceptionHandler({MethodArgumentTypeMismatchException.class,
      HttpMediaTypeException.class, MethodArgumentNotValidException.class,
      HttpMessageNotReadableException.class})
  public ErrorResponse badRequestException(Exception exception, HttpServletResponse response) {
    if (log.isDebugEnabled())
      log.debug(EnumError.BAD_REQUEST.getMessage(), exception);

    ErrorResponse errorResponse = new ErrorResponse(EnumError.BAD_REQUEST);
    response.setStatus(errorResponse.getCode());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    log.debug(EnumError.BAD_REQUEST.getDescription());
    return errorResponse;
  }


  @ExceptionHandler({PreconditionException.class})
  public ErrorResponse preconditionException(Exception exception, HttpServletResponse response) {
    if (log.isDebugEnabled())
      log.debug(EnumError.PRECONDITION_ERROR.getMessage(), exception);

    ErrorResponse errorResponse = new ErrorResponse(EnumError.PRECONDITION_ERROR);
    response.setStatus(errorResponse.getCode());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    log.debug(EnumError.PRECONDITION_ERROR.getDescription());
    return errorResponse;
  }

  @ExceptionHandler({HDFSNotFoundException.class})
  public ErrorResponse hdfsNotFoundException(Exception exception, HttpServletResponse response) {
    log.warn(EnumError.NOT_FOUND_ERROR.getMessage());
    if (log.isDebugEnabled())
      log.debug(EnumError.NOT_FOUND_ERROR.getMessage(), exception);
    ErrorResponse errorResponse = new ErrorResponse(EnumError.NOT_FOUND_ERROR);
    response.setStatus(errorResponse.getCode());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    log.error(EnumError.NOT_FOUND_ERROR.getDescription());
    return errorResponse;

  }

}
