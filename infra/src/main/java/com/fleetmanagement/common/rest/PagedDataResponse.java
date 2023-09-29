package com.fleetmanagement.common.rest;

import java.util.List;

public record PagedDataResponse<T>(List<T> itemList, Integer page, Integer size) {
}
