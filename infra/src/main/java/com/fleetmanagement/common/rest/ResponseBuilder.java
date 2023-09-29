package com.fleetmanagement.common.rest;

import java.util.List;

public class ResponseBuilder {
  public static <T> ApiResponse<T> build(T data) {
    return new ApiResponse<>(data);
  }

  public static <T> ApiResponse<DataResponse<T>> build(List<T> itemList) {
    return new ApiResponse<>(new DataResponse<>(itemList));
  }

  public static <T> ApiResponse<PagedDataResponse<T>> build(
      List<T> itemList, Integer page, Integer size) {
    return new ApiResponse<>(new PagedDataResponse<>(itemList, page, size));
  }
}
