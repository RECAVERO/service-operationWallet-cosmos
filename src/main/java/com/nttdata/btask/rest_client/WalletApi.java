package com.nttdata.btask.rest_client;

import com.nttdata.domain.models.WalletDto;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@RegisterRestClient
@Path("/wallets")
public interface WalletApi {
  @POST
  @Path("/signIn")
  Uni<WalletDto> signIn(WalletDto walletDto);

  @POST
  Uni<WalletDto> add(WalletDto walletDto);

  @POST
  @Path("/search")
  Uni<WalletDto> getWallet(WalletDto walletDto);

  @POST
  @Path("/deposit")
  Uni<WalletDto> depositWallet(WalletDto walletDto);

  @POST
  @Path("/withdrawal")
  Uni<WalletDto> withdrawalWallet(WalletDto walletDto);
}
