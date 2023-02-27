package com.nttdata.btask.service;

import com.nttdata.btask.interfaces.OperationWalletService;
import com.nttdata.domain.contract.OperationWalletRepository;
import com.nttdata.domain.models.OperationWalletDto;
import com.nttdata.domain.models.WalletDto;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class OperationWalletServiceImpl implements OperationWalletService {
  private final OperationWalletRepository operationWalletRepository;

  public OperationWalletServiceImpl(OperationWalletRepository operationWalletRepository) {
    this.operationWalletRepository = operationWalletRepository;
  }

  @Override
  public Uni<OperationWalletDto> registerOperation(OperationWalletDto operationWalletDto) {
    return operationWalletRepository.registerOperation(operationWalletDto);
  }

  @Override
  public Multi<OperationWalletDto> getMotionOperation(OperationWalletDto operationWalletDto) {
    return operationWalletRepository.getMotionOperation(operationWalletDto);
  }

  @Override
  public Multi<OperationWalletDto> getMotion(OperationWalletDto operationWalletDto) {
    return operationWalletRepository.getMotion(operationWalletDto);
  }
}
