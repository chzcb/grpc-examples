package org.chzcb.service;

import io.examples.HelloServiceGrpc;
import io.examples.SayReq;
import io.examples.SayResp;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.NettyServerBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class HelloGrpcServiceRouterImpl extends HelloServiceGrpc.HelloServiceImplBase {

    public static int PORT = 50002;

    public void start() {
        try {
            Server server = NettyServerBuilder.forPort(PORT)
                    .addService(this)
                    .build().start();
            log.info("start at {}, {}", PORT, server);
        } catch (IOException e) {

        }
    }

    ManagedChannel channel;

    public HelloServiceGrpc.HelloServiceBlockingStub helloServiceBlockingStub() {
        if (channel == null) {
            channel = NettyChannelBuilder.forAddress("127.0.0.1", HelloGrpcServiceImpl.PORT)
                    .negotiationType(NegotiationType.PLAINTEXT)
                    .build();
        }
        return HelloServiceGrpc.newBlockingStub(channel).withDeadlineAfter(100, TimeUnit.MILLISECONDS);
    }

    @Override
    public void say(SayReq request, StreamObserver<SayResp> responseObserver) {
        log.info("recv router req: {}", request);
        SayResp resp;
        try {
            HelloServiceGrpc.HelloServiceBlockingStub stub = helloServiceBlockingStub();
            resp = stub.say(SayReq.newBuilder().setContent("router " + request.getContent()).build());
        } catch (Exception e) {
            log.info(e.getLocalizedMessage(), e);
            resp = SayResp.newBuilder().setReplyContent("error").build();
        }
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }
}
