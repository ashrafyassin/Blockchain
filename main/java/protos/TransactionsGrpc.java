package protos;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.17.0)",
    comments = "Source: Transactions.proto")
public final class TransactionsGrpc {

  private TransactionsGrpc() {}

  public static final String SERVICE_NAME = "protos.Transactions";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<protos.TransactionsProto.Transaction,
      protos.TransactionsProto.Reply> getSendTransactionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendTransaction",
      requestType = protos.TransactionsProto.Transaction.class,
      responseType = protos.TransactionsProto.Reply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<protos.TransactionsProto.Transaction,
      protos.TransactionsProto.Reply> getSendTransactionMethod() {
    io.grpc.MethodDescriptor<protos.TransactionsProto.Transaction, protos.TransactionsProto.Reply> getSendTransactionMethod;
    if ((getSendTransactionMethod = TransactionsGrpc.getSendTransactionMethod) == null) {
      synchronized (TransactionsGrpc.class) {
        if ((getSendTransactionMethod = TransactionsGrpc.getSendTransactionMethod) == null) {
          TransactionsGrpc.getSendTransactionMethod = getSendTransactionMethod = 
              io.grpc.MethodDescriptor.<protos.TransactionsProto.Transaction, protos.TransactionsProto.Reply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "protos.Transactions", "SendTransaction"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.TransactionsProto.Transaction.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.TransactionsProto.Reply.getDefaultInstance()))
                  .setSchemaDescriptor(new TransactionsMethodDescriptorSupplier("SendTransaction"))
                  .build();
          }
        }
     }
     return getSendTransactionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<protos.TransactionsProto.Block,
      protos.TransactionsProto.Reply> getAddBlockToChainMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AddBlockToChain",
      requestType = protos.TransactionsProto.Block.class,
      responseType = protos.TransactionsProto.Reply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<protos.TransactionsProto.Block,
      protos.TransactionsProto.Reply> getAddBlockToChainMethod() {
    io.grpc.MethodDescriptor<protos.TransactionsProto.Block, protos.TransactionsProto.Reply> getAddBlockToChainMethod;
    if ((getAddBlockToChainMethod = TransactionsGrpc.getAddBlockToChainMethod) == null) {
      synchronized (TransactionsGrpc.class) {
        if ((getAddBlockToChainMethod = TransactionsGrpc.getAddBlockToChainMethod) == null) {
          TransactionsGrpc.getAddBlockToChainMethod = getAddBlockToChainMethod = 
              io.grpc.MethodDescriptor.<protos.TransactionsProto.Block, protos.TransactionsProto.Reply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "protos.Transactions", "AddBlockToChain"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.TransactionsProto.Block.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.TransactionsProto.Reply.getDefaultInstance()))
                  .setSchemaDescriptor(new TransactionsMethodDescriptorSupplier("AddBlockToChain"))
                  .build();
          }
        }
     }
     return getAddBlockToChainMethod;
  }

  private static volatile io.grpc.MethodDescriptor<protos.TransactionsProto.blockID,
      protos.TransactionsProto.Block> getChooseBlockMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ChooseBlock",
      requestType = protos.TransactionsProto.blockID.class,
      responseType = protos.TransactionsProto.Block.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<protos.TransactionsProto.blockID,
      protos.TransactionsProto.Block> getChooseBlockMethod() {
    io.grpc.MethodDescriptor<protos.TransactionsProto.blockID, protos.TransactionsProto.Block> getChooseBlockMethod;
    if ((getChooseBlockMethod = TransactionsGrpc.getChooseBlockMethod) == null) {
      synchronized (TransactionsGrpc.class) {
        if ((getChooseBlockMethod = TransactionsGrpc.getChooseBlockMethod) == null) {
          TransactionsGrpc.getChooseBlockMethod = getChooseBlockMethod = 
              io.grpc.MethodDescriptor.<protos.TransactionsProto.blockID, protos.TransactionsProto.Block>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "protos.Transactions", "ChooseBlock"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.TransactionsProto.blockID.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.TransactionsProto.Block.getDefaultInstance()))
                  .setSchemaDescriptor(new TransactionsMethodDescriptorSupplier("ChooseBlock"))
                  .build();
          }
        }
     }
     return getChooseBlockMethod;
  }

  private static volatile io.grpc.MethodDescriptor<protos.TransactionsProto.blockID,
      protos.TransactionsProto.Removed> getRemoveBlockMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RemoveBlock",
      requestType = protos.TransactionsProto.blockID.class,
      responseType = protos.TransactionsProto.Removed.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<protos.TransactionsProto.blockID,
      protos.TransactionsProto.Removed> getRemoveBlockMethod() {
    io.grpc.MethodDescriptor<protos.TransactionsProto.blockID, protos.TransactionsProto.Removed> getRemoveBlockMethod;
    if ((getRemoveBlockMethod = TransactionsGrpc.getRemoveBlockMethod) == null) {
      synchronized (TransactionsGrpc.class) {
        if ((getRemoveBlockMethod = TransactionsGrpc.getRemoveBlockMethod) == null) {
          TransactionsGrpc.getRemoveBlockMethod = getRemoveBlockMethod = 
              io.grpc.MethodDescriptor.<protos.TransactionsProto.blockID, protos.TransactionsProto.Removed>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "protos.Transactions", "RemoveBlock"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.TransactionsProto.blockID.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.TransactionsProto.Removed.getDefaultInstance()))
                  .setSchemaDescriptor(new TransactionsMethodDescriptorSupplier("RemoveBlock"))
                  .build();
          }
        }
     }
     return getRemoveBlockMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TransactionsStub newStub(io.grpc.Channel channel) {
    return new TransactionsStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TransactionsBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new TransactionsBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TransactionsFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new TransactionsFutureStub(channel);
  }

  /**
   */
  public static abstract class TransactionsImplBase implements io.grpc.BindableService {

    /**
     */
    public void sendTransaction(protos.TransactionsProto.Transaction request,
        io.grpc.stub.StreamObserver<protos.TransactionsProto.Reply> responseObserver) {
      asyncUnimplementedUnaryCall(getSendTransactionMethod(), responseObserver);
    }

    /**
     */
    public void addBlockToChain(protos.TransactionsProto.Block request,
        io.grpc.stub.StreamObserver<protos.TransactionsProto.Reply> responseObserver) {
      asyncUnimplementedUnaryCall(getAddBlockToChainMethod(), responseObserver);
    }

    /**
     */
    public void chooseBlock(protos.TransactionsProto.blockID request,
        io.grpc.stub.StreamObserver<protos.TransactionsProto.Block> responseObserver) {
      asyncUnimplementedUnaryCall(getChooseBlockMethod(), responseObserver);
    }

    /**
     */
    public void removeBlock(protos.TransactionsProto.blockID request,
        io.grpc.stub.StreamObserver<protos.TransactionsProto.Removed> responseObserver) {
      asyncUnimplementedUnaryCall(getRemoveBlockMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSendTransactionMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                protos.TransactionsProto.Transaction,
                protos.TransactionsProto.Reply>(
                  this, METHODID_SEND_TRANSACTION)))
          .addMethod(
            getAddBlockToChainMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                protos.TransactionsProto.Block,
                protos.TransactionsProto.Reply>(
                  this, METHODID_ADD_BLOCK_TO_CHAIN)))
          .addMethod(
            getChooseBlockMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                protos.TransactionsProto.blockID,
                protos.TransactionsProto.Block>(
                  this, METHODID_CHOOSE_BLOCK)))
          .addMethod(
            getRemoveBlockMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                protos.TransactionsProto.blockID,
                protos.TransactionsProto.Removed>(
                  this, METHODID_REMOVE_BLOCK)))
          .build();
    }
  }

  /**
   */
  public static final class TransactionsStub extends io.grpc.stub.AbstractStub<TransactionsStub> {
    private TransactionsStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TransactionsStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TransactionsStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TransactionsStub(channel, callOptions);
    }

    /**
     */
    public void sendTransaction(protos.TransactionsProto.Transaction request,
        io.grpc.stub.StreamObserver<protos.TransactionsProto.Reply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendTransactionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void addBlockToChain(protos.TransactionsProto.Block request,
        io.grpc.stub.StreamObserver<protos.TransactionsProto.Reply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAddBlockToChainMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void chooseBlock(protos.TransactionsProto.blockID request,
        io.grpc.stub.StreamObserver<protos.TransactionsProto.Block> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getChooseBlockMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void removeBlock(protos.TransactionsProto.blockID request,
        io.grpc.stub.StreamObserver<protos.TransactionsProto.Removed> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRemoveBlockMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class TransactionsBlockingStub extends io.grpc.stub.AbstractStub<TransactionsBlockingStub> {
    private TransactionsBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TransactionsBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TransactionsBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TransactionsBlockingStub(channel, callOptions);
    }

    /**
     */
    public protos.TransactionsProto.Reply sendTransaction(protos.TransactionsProto.Transaction request) {
      return blockingUnaryCall(
          getChannel(), getSendTransactionMethod(), getCallOptions(), request);
    }

    /**
     */
    public protos.TransactionsProto.Reply addBlockToChain(protos.TransactionsProto.Block request) {
      return blockingUnaryCall(
          getChannel(), getAddBlockToChainMethod(), getCallOptions(), request);
    }

    /**
     */
    public protos.TransactionsProto.Block chooseBlock(protos.TransactionsProto.blockID request) {
      return blockingUnaryCall(
          getChannel(), getChooseBlockMethod(), getCallOptions(), request);
    }

    /**
     */
    public protos.TransactionsProto.Removed removeBlock(protos.TransactionsProto.blockID request) {
      return blockingUnaryCall(
          getChannel(), getRemoveBlockMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class TransactionsFutureStub extends io.grpc.stub.AbstractStub<TransactionsFutureStub> {
    private TransactionsFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TransactionsFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TransactionsFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TransactionsFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<protos.TransactionsProto.Reply> sendTransaction(
        protos.TransactionsProto.Transaction request) {
      return futureUnaryCall(
          getChannel().newCall(getSendTransactionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<protos.TransactionsProto.Reply> addBlockToChain(
        protos.TransactionsProto.Block request) {
      return futureUnaryCall(
          getChannel().newCall(getAddBlockToChainMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<protos.TransactionsProto.Block> chooseBlock(
        protos.TransactionsProto.blockID request) {
      return futureUnaryCall(
          getChannel().newCall(getChooseBlockMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<protos.TransactionsProto.Removed> removeBlock(
        protos.TransactionsProto.blockID request) {
      return futureUnaryCall(
          getChannel().newCall(getRemoveBlockMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SEND_TRANSACTION = 0;
  private static final int METHODID_ADD_BLOCK_TO_CHAIN = 1;
  private static final int METHODID_CHOOSE_BLOCK = 2;
  private static final int METHODID_REMOVE_BLOCK = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final TransactionsImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(TransactionsImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SEND_TRANSACTION:
          serviceImpl.sendTransaction((protos.TransactionsProto.Transaction) request,
              (io.grpc.stub.StreamObserver<protos.TransactionsProto.Reply>) responseObserver);
          break;
        case METHODID_ADD_BLOCK_TO_CHAIN:
          serviceImpl.addBlockToChain((protos.TransactionsProto.Block) request,
              (io.grpc.stub.StreamObserver<protos.TransactionsProto.Reply>) responseObserver);
          break;
        case METHODID_CHOOSE_BLOCK:
          serviceImpl.chooseBlock((protos.TransactionsProto.blockID) request,
              (io.grpc.stub.StreamObserver<protos.TransactionsProto.Block>) responseObserver);
          break;
        case METHODID_REMOVE_BLOCK:
          serviceImpl.removeBlock((protos.TransactionsProto.blockID) request,
              (io.grpc.stub.StreamObserver<protos.TransactionsProto.Removed>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class TransactionsBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    TransactionsBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return protos.TransactionsProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Transactions");
    }
  }

  private static final class TransactionsFileDescriptorSupplier
      extends TransactionsBaseDescriptorSupplier {
    TransactionsFileDescriptorSupplier() {}
  }

  private static final class TransactionsMethodDescriptorSupplier
      extends TransactionsBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    TransactionsMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (TransactionsGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new TransactionsFileDescriptorSupplier())
              .addMethod(getSendTransactionMethod())
              .addMethod(getAddBlockToChainMethod())
              .addMethod(getChooseBlockMethod())
              .addMethod(getRemoveBlockMethod())
              .build();
        }
      }
    }
    return result;
  }
}
