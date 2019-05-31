package com.ahgj.community.canal.configurator;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * 容器注入自定义RestTemplate
 * @author Hohn
 */
@Configuration
public class RestClientConfiguration {

    /**
     * 创建 Spring WebClient ClosableHttpClient ,默认会创建一个大小为5的连接池
     * 自定义设置相关属性
     * https/http的TCP通信协议类型    SSL/TLS
     * 最大连接数                    默认3000个
     * 路由链接数                   默认400个
     * socket通信链接超时时间      默认60s
     * 链接时长超时时间           默认60s
     * 请求超时                 默认10s
     *
     * @return httpClient
     */
    @Bean
    public RestTemplate restTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        //在调用SSL之前重写TrustAllStrategy类的验证方法,设置可信任,取消检测SSL,避免需要证书忽略效验,
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();
        //创建https协议ssl连接工厂实例
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, null, null, NoopHostnameVerifier.INSTANCE);
        //设置请求协议类型(https/http)的链接方式
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", csf)
                .build();
        //连接池管理器配置注册器
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        //最大连接数3000
        connectionManager.setMaxTotal(3000);
        //路由链接数400 /socket链接超时60s /链接时长超时60s /请求链接超时10s
        connectionManager.setDefaultMaxPerRoute(400);
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(60000)
                .setConnectTimeout(60000)
                .setConnectionRequestTimeout(10000)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        // 配置evict相关的两个方法
        // evictExpiredConnections  处理类似CLOSE_WAIT状态的异常链接
        // evictIdleConnections     处理IDLE状态的链接，其内部源码会开启一个定时任务去检测。
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .evictExpiredConnections()
                .evictIdleConnections(30, TimeUnit.SECONDS)
                .build();
        requestFactory.setHttpClient(httpClient);
        return new RestTemplate(requestFactory);
    }
}
