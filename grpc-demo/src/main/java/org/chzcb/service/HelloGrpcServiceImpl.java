package org.chzcb.service;

import io.examples.HelloServiceGrpc;
import io.examples.SayReq;
import io.examples.SayResp;
import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class HelloGrpcServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {

    public static int PORT = 50001;

    public void start() {
        try {
            Server server = NettyServerBuilder.forPort(PORT)
                    .addService(this)
                    .build().start();
            log.info("start at {}, {}", PORT, server);
        } catch (IOException e) {

        }
    }


    @Override
    public void say(SayReq request, StreamObserver<SayResp> responseObserver) {
        log.info("recv server req: {}", request);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        responseObserver.onNext(SayResp.newBuilder().setReplyContent("i'm ok!").build());
        responseObserver.onCompleted();
    }
}
