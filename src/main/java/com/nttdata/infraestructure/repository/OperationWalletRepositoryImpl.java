package com.nttdata.infraestructure.repository;

import com.nttdata.domain.contract.OperationWalletRepository;
import com.nttdata.domain.models.OperationWalletDto;
import com.nttdata.domain.models.SumTotal;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.quarkus.mongodb.reactive.ReactiveMongoDatabase;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.enterprise.context.ApplicationScoped;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;

@ApplicationScoped
public class OperationWalletRepositoryImpl implements OperationWalletRepository {
  private final ReactiveMongoClient reactiveMongoClient;

  public OperationWalletRepositoryImpl(ReactiveMongoClient reactiveMongoClient) {
    this.reactiveMongoClient = reactiveMongoClient;
  }

  @Override
  public Uni<OperationWalletDto> registerOperation(OperationWalletDto operationWalletDto) {
    ReactiveMongoDatabase database = reactiveMongoClient.getDatabase("exchanges");
    ReactiveMongoCollection<Document> collection = database.getCollection("exchanges");
    Document document = new Document()
        .append("nroDocument", operationWalletDto.getNroDocument())
        .append("typeDocument", operationWalletDto.getTypeDocument())
        .append("numberCard", operationWalletDto.getNumberCard())
        .append("numberTelephone", operationWalletDto.getNumberTelephone())
        .append("amount", operationWalletDto.getAmount())
        .append("typeOperation", operationWalletDto.getTypeOperation())
        .append("registrationDate", this.getDateNow())
        .append("created_datetime", this.getDateNow())
        .append("updated_datetime", this.getDateNow())
        .append("active", "S");
    return collection.insertOne(document).replaceWith(operationWalletDto);
  }

  @Override
  public Multi<OperationWalletDto> getMotionOperation(OperationWalletDto operationWalletDto) {
    ReactiveMongoDatabase database = reactiveMongoClient.getDatabase("exchanges");
    ReactiveMongoCollection<Document> collection = database.getCollection("exchanges");
    SumTotal s = new SumTotal();
    Bson filter = combine(
        eq("numberTelephone", operationWalletDto.getNumberTelephone()),
        eq("typeOperation", operationWalletDto.getTypeOperation())
        );
    System.out.println(filter);
    return collection
        .find(filter).map(doc ->{
          OperationWalletDto operationWallet = new OperationWalletDto();
          operationWallet.setTypeOperation(doc.getInteger("typeOperation"));
          operationWallet.setNroDocument(doc.getLong("nroDocument"));
          operationWallet.setAmount(doc.getDouble("amount"));
          operationWallet.setNumberTelephone(doc.getString("numberTelephone"));
          operationWallet.setRegistrationDate(doc.getString("registrationDate"));

          s.setTotal(s.getTotal() + doc.getDouble("amount"));
          operationWallet.setSumAmount(s.getTotal());
          System.out.println(operationWallet.getRegistrationDate().substring(0,10));
          return operationWallet;
        }).select()
        .where(c->c.getRegistrationDate().substring(0,10).equals(this.getDateNow().substring(0,10)));
  }

  @Override
  public Multi<OperationWalletDto> getMotion(OperationWalletDto operationWalletDto) {
    ReactiveMongoDatabase database = reactiveMongoClient.getDatabase("exchanges");
    ReactiveMongoCollection<Document> collection = database.getCollection("exchanges");
    SumTotal s = new SumTotal();
    Bson filter = combine(
        eq("numberTelephone", operationWalletDto.getNumberTelephone())
    );

    return collection
        .find(filter).map(doc ->{
          OperationWalletDto operationWallet = new OperationWalletDto();
          operationWallet.setTypeOperation(doc.getInteger("typeOperation"));
          operationWallet.setNroDocument(doc.getLong("nroDocument"));
          operationWallet.setAmount(doc.getDouble("amount"));
          operationWallet.setNumberTelephone(doc.getString("numberTelephone"));
          operationWallet.setRegistrationDate(doc.getString("registrationDate"));
          operationWallet.setActive(doc.getString("active"));
          return operationWallet;
        }).filter(c->c.getActive().equals("S"));
  }

  private static String getDateNow(){
    Date date = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return formatter.format(date).toString();
  }
}
