package tech.jannotti.billing.core.commons.http.client;

import java.io.IOException;
import java.util.Arrays;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

public class HttpResponseHandler implements ResponseHandler<String> {

    private int[] acceptedHttpStatus;

    private Integer httpStatus;

    public HttpResponseHandler(int[] acceptedHttpStatus) {
        this.acceptedHttpStatus = acceptedHttpStatus;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {

        httpStatus = httpResponse.getStatusLine().getStatusCode();

        int index = Arrays.binarySearch(acceptedHttpStatus, httpStatus);
        if (index < 0)
            throw new ClientProtocolException("Resposta HTTP com status invalido [" + httpStatus + "]");

        HttpEntity responseEntity = httpResponse.getEntity();
        String response = EntityUtils.toString(responseEntity);

        return response;
    }

}