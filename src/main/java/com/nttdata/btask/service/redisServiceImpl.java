package com.nttdata.btask.service;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.redis.client.Command;
import io.vertx.mutiny.redis.client.Redis;
import io.vertx.mutiny.redis.client.Request;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class redisServiceImpl {
  private final Redis redis;

  public redisServiceImpl(Redis redis) {
    this.redis = redis;
  }

  Uni<Void> redisAdd(String key, String value){
    return redis.send(Request.cmd(Command.SET).arg(key).arg(value)).replaceWithVoid();
  }

  Uni<String> getRedis(String key){
    return redis.send(Request.cmd(Command.GET).arg(key)).map(response -> {
      if(response == null){
        return "Sin data";
      }else{
        System.out.println(response);
        return response.toString();
      }

    });
  }

}
