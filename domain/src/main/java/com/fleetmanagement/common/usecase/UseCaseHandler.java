package com.fleetmanagement.common.usecase;

import com.fleetmanagement.common.model.UseCase;

public interface UseCaseHandler<E, T extends UseCase> {

    E handle(T useCase);
}
