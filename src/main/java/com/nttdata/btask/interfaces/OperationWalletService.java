package com.nttdata.btask.interfaces;

import com.nttdata.domain.models.OperationWalletDto;
import com.nttdata.domain.models.WalletDto;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

public interface OperationWalletService {
  Uni<OperationWalletDto> registerOperation(OperationWalletDto operationWalletDto);
  Multi<OperationWalletDto> getMotionOperation(OperationWalletDto operationWalletDto);
  Multi<OperationWalletDto> getMotion(OperationWalletDto operationWalletDto);
}
