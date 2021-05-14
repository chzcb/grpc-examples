package org.chzcb.examples;

import io.examples.HelloServiceGrpc;
import io.examples.SayReq;
import io.examples.SayResp;
import io.grpc.ManagedChannelBuilder;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import org.chzcb.service.HelloGrpcServiceImpl;
import org.chzcb.service.HelloGrpcServiceRouterImpl;

public class ContextCanceledTest {
    public static void main(String[] args) {
        //服务
        HelloGrpcServiceImpl helloGrpcService = new HelloGrpcServiceImpl();
        helloGrpcService.start();

        //中转服务
        HelloGrpcServiceRouterImpl helloGrpcServiceRouter = new HelloGrpcServiceRouterImpl();
        helloGrpcServiceRouter.start();

        SayReq req = SayReq.newBuilder().setContent("hello world").build();
        SayResp resp = HelloServiceGrpc.newBlockingStub(NettyChannelBuilder.forAddress("127.0.0.1", HelloGrpcServiceRouterImpl.PORT).negotiationType(NegotiationType.PLAINTEXT).build()).say(req);
        System.out.println("result:"+resp);
    }
}
