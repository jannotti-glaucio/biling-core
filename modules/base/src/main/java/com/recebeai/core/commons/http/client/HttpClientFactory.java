package tech.jannotti.billing.core.commons.http.client;

import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.http.Consts;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.commons.http.HttpConstants;
import tech.jannotti.billing.core.commons.http.client.exception.HttpClientConfigException;

@Component
public class HttpClientFactory {

    @Value("${core.httpClient.pooling.max.total}")
    private Integer poolingMaxTotal; // Quantidade maxima de conexoes do pool

    @Value("${core.httpClient.pooling.max.perRoute}")
    private Integer poolingMaxPerRoute; // Quantidade maxima de conexoes por rota

    @Value("${core.httpClient.pooling.request.timeout}")
    private Integer poolingRequestTimeout; // Tempo limite para esperar liberar uma conexao no pool

    @Value("${core.httpClient.socket.buffer.size}")
    private Integer socketBufferSize; // Define o tamanho do buffer para envio e recebimento via socket

    @Value("${core.httpClient.keyStore.path}")
    private String keyStorePath;

    @Value("${core.httpClient.keyStore.type}")
    private String keyStoreType;

    @Value("${core.httpClient.keyStore.password}")
    private String keyStorePassword;

    private Registry<ConnectionSocketFactory> createSocketFactoryRegistry(boolean validateSSL) {

        ConnectionSocketFactory httpsSocketFactory = createHttpsSocketFactory(validateSSL);
        ConnectionSocketFactory httpSocketFactory = createHttpSocketFactory();

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
            .register(HttpConstants.HTTPS_SCHEMA, httpsSocketFactory)
            .register(HttpConstants.HTTP_SCHEMA, httpSocketFactory)
            .build();
        return socketFactoryRegistry;
    }

    private ConnectionSocketFactory createHttpsSocketFactory(boolean validateSSL) {

        // Carrega o keystore
        KeyStore trustStore = loadKeyStore();

        HostnameVerifier hostnameVerifier = null;
        SSLContext sslContext = null;

        if (!validateSSL) {
            // Configura pra nao validar SSL
            hostnameVerifier = new NoopHostnameVerifier();
            try {
                if (trustStore != null)
                    sslContext = SSLContexts.custom().loadTrustMaterial(trustStore, TrustAllStrategy.INSTANCE).build();
                else
                    sslContext = SSLContexts.custom().loadTrustMaterial(TrustAllStrategy.INSTANCE).build();
            } catch (Exception e) {
                throw new HttpClientConfigException("Erro instanciando contexto SSL", e);
            }

        } else {
            hostnameVerifier = new DefaultHostnameVerifier();
            try {
                if (trustStore != null)
                    sslContext = SSLContexts.custom().loadKeyMaterial(trustStore, keyStorePassword.toCharArray()).build();
                else
                    sslContext = SSLContexts.createDefault();
            } catch (Exception e) {
                throw new HttpClientConfigException("Erro instanciando contexto SSL", e);
            }
        }

        // Configura pra fazer apenas conexoes TLS 1.2
        String[] protocols = new String[] { HttpConstants.TLS_1_2_PROTOCOL };

        SSLConnectionSocketFactory sslSockerFactory = new SSLConnectionSocketFactory(sslContext, protocols, null,
            hostnameVerifier);
        return sslSockerFactory;
    }

    private KeyStore loadKeyStore() {

        URL keyStoreURL = this.getClass().getResource(keyStorePath);
        if (keyStoreURL == null)
            return null;

        KeyStore trustStore = null;
        try {
            trustStore = KeyStore.getInstance(keyStoreType);
            InputStream inputStream = keyStoreURL.openStream();
            try {
                trustStore.load(inputStream, keyStorePassword.toCharArray());
            } finally {
                inputStream.close();
            }
        } catch (Exception e) {
            throw new HttpClientConfigException("Erro carregando keystore", e);
        }
        return trustStore;
    }

    private ConnectionSocketFactory createHttpSocketFactory() {
        return new PlainConnectionSocketFactory();
    }

    private PoolingHttpClientConnectionManager createConnectionManager(Registry<ConnectionSocketFactory> socketFactoryRegistry) {

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connectionManager.setMaxTotal(poolingMaxTotal);
        connectionManager.setDefaultMaxPerRoute(poolingMaxPerRoute);

        SocketConfig.Builder socketConfigBuilder = SocketConfig.custom();
        socketConfigBuilder.setTcpNoDelay(true); // Habilita o TCP_NODELAY para melhorar a performance
        SocketConfig socketConfig = socketConfigBuilder.build();
        connectionManager.setDefaultSocketConfig(socketConfig);

        ConnectionConfig.Builder connectionConfigBuilder = ConnectionConfig.custom();
        connectionConfigBuilder.setBufferSize(socketBufferSize);
        connectionConfigBuilder.setCharset(Consts.UTF_8); // Define o enconding padrao
        ConnectionConfig connectionConfig = connectionConfigBuilder.build();
        connectionManager.setDefaultConnectionConfig(connectionConfig);

        return connectionManager;
    }

    public CloseableHttpClient createClient() {
        return createClient(true, null);
    }

    public CloseableHttpClient createClient(HttpRequestInterceptor interceptor) {
        return createClient(true, interceptor);
    }

    public CloseableHttpClient createClient(boolean validateSSL) {
        return createClient(validateSSL, null);
    }

    public CloseableHttpClient createClient(boolean validateSSL, HttpRequestInterceptor interceptor) {

        HttpClientBuilder httpClientBuilder = HttpClients.custom();

        Registry<ConnectionSocketFactory> socketFactoryRegistry = createSocketFactoryRegistry(validateSSL);
        PoolingHttpClientConnectionManager connectionManager = createConnectionManager(socketFactoryRegistry);
        httpClientBuilder.setConnectionManager(connectionManager);

        RequestConfig.Builder configBuilder = RequestConfig.custom();
        configBuilder.setConnectionRequestTimeout(poolingRequestTimeout);

        RequestConfig config = configBuilder.build();
        httpClientBuilder.setDefaultRequestConfig(config);

        httpClientBuilder.setConnectionReuseStrategy(new NoConnectionReuseStrategy());

        if (interceptor != null)
            httpClientBuilder.addInterceptorFirst(interceptor);

        CloseableHttpClient httpClient = httpClientBuilder.build();
        return httpClient;
    }

}
