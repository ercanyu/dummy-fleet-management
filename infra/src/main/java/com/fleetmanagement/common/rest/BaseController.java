package com.fleetmanagement.common.rest;

import java.util.List;

public class BaseController {

  public <T> ApiResponse<T> respond(T data) {
    return ResponseBuilder.build(data);
  }

  public <T> ApiResponse<DataResponse<T>> respond(List<T> list) {
    return ResponseBuilder.build(list);
  }

  public <T> ApiResponse<ErrorResponse> respond(ErrorResponse errorResponse) {
    return ResponseBuilder.build(errorResponse);
  }
}
