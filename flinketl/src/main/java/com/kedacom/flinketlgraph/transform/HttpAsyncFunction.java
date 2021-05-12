package com.kedacom.flinketlgraph.transform;

import com.kedacom.flinketlgraph.CommonUtils;
import com.kedacom.flinketlgraph.json.Graphnode;
import com.kedacom.flinketlgraph.json.Httpasynctransformspec;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class HttpAsyncFunction extends RichAsyncFunction<Object, Object> implements CheckpointedFunction {
    private Httpasynctransformspec spec;
    private ObjectMapper mapper = new ObjectMapper();

    private transient TransmetricState transmetricState;
    private transient ListState<TransmetricState> checkpointStates;

    private transient CloseableHttpAsyncClient httpclient;

    public HttpAsyncFunction(Graphnode trans) throws Exception {
        this.spec = mapper.convertValue(trans.getElementconfig(), Httpasynctransformspec.class);
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);

        if (transmetricState == null){
            transmetricState = new TransmetricState();
        }
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(spec.getSockettimeout().intValue()*1000)
                .setConnectTimeout(spec.getConnecttimeout().intValue()*1000) // connect tcp timeout
                //.setConnectionRequestTimeout(5*1000)
                // three paremeters default is -1
                .build();

        List<Header> listheader = new LinkedList<>();
        spec.getHttpheaders().forEach(item ->{
            listheader.add(new BasicHeader(item.getKey(), item.getValue()));
        });

        httpclient = HttpAsyncClients.custom()
                .setMaxConnTotal(spec.getHttpconcurrency().intValue()*2)
                .setMaxConnPerRoute(spec.getHttpconcurrency().intValue()*2)
                .setDefaultHeaders(listheader)

                .setDefaultRequestConfig(requestConfig).build();
        httpclient.start();
    }

    @Override
    public void close() throws Exception {
        super.close();
        if (httpclient != null){
            httpclient.close();
        }
    }

    @Override
    public void asyncInvoke(Object input, ResultFuture<Object> resultFuture) throws Exception {

        if (input instanceof HttpRequestBean){
            transmetricState.processAddCount(1L);
            sendrequest((HttpRequestBean)input, resultFuture, 1);
        }else {
            throw new RuntimeException("input data is not httprequestbean");
        }
    }

    public void sendrequest(HttpRequestBean req, ResultFuture<Object> resultFuture, int retritimes){
        try {
            //System.out.println("sendrequest times " + retritimes + req.getBody());
            dealwithrequest(req, resultFuture, retritimes);
        }catch (Exception e){
            dealwithexception(req, e, retritimes, false, resultFuture);
        }
    }

    public void dealwithrequest(HttpRequestBean req, ResultFuture<Object> resultFuture, int retritimes) throws Exception{
        HttpUriRequest httpUriRequest = null;
        if ("post".equalsIgnoreCase(req.getMethod())){
            HttpEntity entity = null;
            Object body = req.getBody();
            if ("string".equalsIgnoreCase(req.getBodytype())){
                entity = new StringEntity((String)body, Consts.UTF_8);
            }else{
                entity = new ByteArrayEntity((byte[])req.getBody());
            }
            HttpPost postreq = new HttpPost(req.getUrl());
            postreq.setEntity(entity);
            httpUriRequest = postreq;
        }else {
            HttpGet getreq = new HttpGet(req.getUrl());
            httpUriRequest = getreq;
        }
        req.getHeaders().forEach(httpUriRequest::setHeader);

        httpclient.execute(httpUriRequest, new FutureCallback<HttpResponse>() {
            private int retries = retritimes;

            @Override
            public void completed(HttpResponse httpResponse) {
                try {
                    HttpResponseBean response = new HttpResponseBean();
                    response.setStatudcode(httpResponse.getStatusLine().getStatusCode());
                    response.setExtradata(req.getExtradata());

                    Object respBody = null;
                    if ("string".equalsIgnoreCase(req.getBodytype())){
                        respBody = EntityUtils.toString(httpResponse.getEntity());
                    }else {
                        respBody = EntityUtils.toByteArray(httpResponse.getEntity());
                    }
                    response.setBody(respBody);

                    resultFuture.complete(Collections.singletonList(response));
                } catch (IOException e) {
                    dealwithexception(req, e, this.retries, true, resultFuture);
                }
            }

            @Override
            public void failed(Exception e) {
                dealwithexception(req, e, this.retries, true, resultFuture);
            }

            @Override
            public void cancelled() {
                System.out.println("*************** httpasyncfunction cancelled now this can not happen");
            }
        });
    }

    private void dealwithexception(HttpRequestBean req, Exception e, int times, boolean bneedretries, ResultFuture<Object> resultFuture){
        times = times+1;
        if (bneedretries && times <= spec.getRetries()){
            sendrequest(req, resultFuture, times);
        }else{
            if (spec.getThrowhttpexception()){
                resultFuture.completeExceptionally(e);
            }else{
                resultFuture.complete(null);
            }
            transmetricState.update(1L, e.getMessage());
        }
    }

    @Override
    public void timeout(Object input, ResultFuture<Object> resultFuture) throws Exception {
        //throw new RuntimeException("asnyc http request timeout. ...");
        System.out.println("*************** httpasyncfunction timeout now this can not happen");
        resultFuture.complete(null);
    }

    @Override
    public void snapshotState(FunctionSnapshotContext context) throws Exception {
        checkpointStates.clear();
        checkpointStates.add(transmetricState);
    }

    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {
        ListStateDescriptor<TransmetricState> descriptor =
                new ListStateDescriptor<>("httpasynctransform", TransmetricState.class);
        checkpointStates = context.getOperatorStateStore().getListState(descriptor);
        if (context.isRestored()){
            transmetricState = CommonUtils.restoreTransmetricState(checkpointStates);
        }
    }
}
