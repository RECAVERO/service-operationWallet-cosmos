package com.nttdata.application.rest;

import com.nttdata.btask.interfaces.OperationWalletService;
import com.nttdata.btask.rest_client.WalletApi;
import com.nttdata.domain.models.OperationWalletDto;
import com.nttdata.domain.models.ResponseDto;
import com.nttdata.domain.models.WalletDto;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.Date;

@Path("/exchange")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class operationWalletResource {
  private final OperationWalletService operationWalletService;

  @RestClient
  WalletApi walletApi;

  public operationWalletResource(OperationWalletService operationWalletService) {
    this.operationWalletService = operationWalletService;
  }

  @POST
  @Path("/signIn")
  public Uni<WalletDto> signInWallet(WalletDto walletDto) {
    return walletApi.signIn(walletDto);
  }

  @POST
  @Path("/register")
  @Counted(name = "count_register_wallet")
  @Timed(name = "time_register_wallet")
  public Uni<ResponseDto<WalletDto>> registerWallet(WalletDto walletDto) {
    ResponseDto<WalletDto> responseDto = new ResponseDto<>();
    return walletApi.getWallet(walletDto).map(c->{
      if(c == null){
        responseDto.setStatus("ok");
      }else{
        responseDto.setStatus("ko");
      }
      return responseDto;
    }).call(doc->{
      if(responseDto.getStatus().equals("ok")){
        responseDto.setStatus("201");
        responseDto.setMsg("Se Registro Correctamente ...");
        responseDto.setObject(walletDto);
        return walletApi.add(walletDto).replaceWith(walletDto);
      }else{
        responseDto.setStatus("404");
        responseDto.setMsg("Ya existe registrado el number cell ...");
        return Uni.createFrom().item(walletDto);
      }
    });
  }

  @POST
  @Path("/search")
  public Uni<WalletDto> getWallet(WalletDto walletDto) {
    return walletApi.getWallet(walletDto);
  }

  @POST
  @Path("/wallet/deposit")
  public Uni<WalletDto> setWalletDeposit(WalletDto walletDto) {
    return walletApi.depositWallet(walletDto).map(c->{
      return c;
    }).call(doc->{
      OperationWalletDto operationWalletDto = new OperationWalletDto();
      operationWalletDto.setAmount(walletDto.getAmount());
      operationWalletDto.setNroDocument(doc.getNroDocument());
      operationWalletDto.setTypeDocument(doc.getTypeDocument());
      operationWalletDto.setNumberCard(doc.getNumberCard());
      operationWalletDto.setNumberTelephone(walletDto.getNumberTelephone());
      operationWalletDto.setTypeOperation(1);
      return operationWalletService.registerOperation(operationWalletDto);
    });
  }

  @POST
  @Path("/wallet/withdrawal")
  public Uni<WalletDto> setWalletWithdrawal(WalletDto walletDto) {

    return walletApi.withdrawalWallet(walletDto).map(c->{

      return c;
    }).call(doc->{
      OperationWalletDto operationWalletDto = new OperationWalletDto();
      operationWalletDto.setAmount(-1*walletDto.getAmount());
      operationWalletDto.setNroDocument(doc.getNroDocument());
      operationWalletDto.setTypeDocument(doc.getTypeDocument());
      operationWalletDto.setNumberCard(doc.getNumberCard());
      operationWalletDto.setNumberTelephone(walletDto.getNumberTelephone());
      operationWalletDto.setTypeOperation(2);
      return operationWalletService.registerOperation(operationWalletDto);
    });
  }

  @POST
  @Path("/motion")
  public Multi<OperationWalletDto> registerMotion(OperationWalletDto operationWalletDto) {
    return operationWalletService.getMotion(operationWalletDto);
  }

  @POST
  @Path("/all/withdrawal")
  public Multi<OperationWalletDto> getMotionWithdrawal(OperationWalletDto operationWalletDto) {
    return operationWalletService.getMotionOperation(operationWalletDto);
  }



}
