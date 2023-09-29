package com.fleetmanagement.common.rest;

import java.util.List;

public record DataResponse<T>(List<T> itemList) {
}
