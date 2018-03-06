package cn.damei.scm.common.utils;

import com.google.common.collect.Lists;
import cn.damei.scm.common.Constants;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.List;

public final class HttpUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * @param url        请求地址
     * @param parameters 提交的请求参数
     */
    public static String get(String url, final NameValuePair... parameters) {
        if (StringUtils.isBlank(url)) {
            throw new IllegalArgumentException("get请求地址不能为空");
        } else {
            String respTxt = StringUtils.EMPTY;
            HttpClient httpClient = createHttpClientInstance();
            try {
                if (null != parameters && parameters.length > 0) {
                    String encodedParams = encodeParameters(parameters);
                    if (-1 == url.indexOf("?")) {
                        url += "?" + encodedParams;
                    } else {
                        url += "&" + encodedParams;
                    }
                }

                HttpGet getMethod = new HttpGet(url);

                //设置请求和传输超时时间
                RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(4000).setConnectTimeout(2000)
                        .build();
                getMethod.setConfig(requestConfig);
                HttpResponse resp = httpClient.execute(getMethod);
                if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    respTxt = EntityUtils.toString(resp.getEntity(), Constants.DEFAULT_ENCODING);
                }
            } catch (Exception e) {
                logger.error("httpGet请求异常：{},错误详细是：{}", url, e.getMessage());
            } finally {
                HttpClientUtils.closeQuietly(httpClient);
            }

            return respTxt;
        }
    }

    public static String postJson(String postUrl, String json) {
        HttpClient client = createHttpClientInstance();
        HttpPost post = new HttpPost(postUrl);
        String respResult = null;
        try {
            StringEntity stringEntity = new StringEntity(json, Constants.DEFAULT_ENCODING);
            stringEntity.setContentEncoding(Constants.DEFAULT_ENCODING);
            stringEntity.setContentType(JsonUtils.JSON_TYPE);
            post.setEntity(stringEntity);

            HttpResponse resp = client.execute(post);
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = resp.getEntity();
                respResult = EntityUtils.toString(entity, Constants.DEFAULT_ENCODING);
            }
        } catch (Exception ex) {
            logger.error("httpPost json异常：{},错误详细是：{}", postUrl, ex.getMessage());
        } finally {
            HttpClientUtils.closeQuietly(client);
        }
        return respResult;
    }


    public static String post(String postUrl, final NameValuePair... parameters) {
        HttpClient client = createHttpClientInstance();
        String respResult = null;
        try {
            HttpPost httPost = new HttpPost(postUrl);

            if (ArrayUtils.isNotEmpty(parameters)) {
                List<NameValuePair> params = Lists.newArrayList(parameters);
                httPost.setEntity(new UrlEncodedFormEntity(params, Constants.DEFAULT_ENCODING));
            }

            HttpResponse resp = client.execute(httPost);
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = resp.getEntity();
                respResult = EntityUtils.toString(entity);
            }
        } catch (Exception ex) {
            logger.error("httpPost请求发生异常：地址：{},错误详细是：{}", postUrl, ex.getMessage());
        } finally {
            HttpClientUtils.closeQuietly(client);
        }
        return respResult;
    }


    /**
     * 将NameValuePairs数组转变为字符串
     */
    private static String encodeParameters(final NameValuePair[] nameValues) {
        if (nameValues == null || nameValues.length == 0) {
            return StringUtils.EMPTY;
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < nameValues.length; i++) {
            NameValuePair nameValue = nameValues[i];
            if (i == 0) {
                buffer.append(nameValue.getName() + "=" + nameValue.getValue());
            } else {
                buffer.append("&" + nameValue.getName() + "=" + nameValue.getValue());
            }
        }
        return buffer.toString();
    }

    /**
     * 依据浏览器将下载文件名编码，以防乱码
     *
     * @param filename
     * @param request
     * @return
     */
    public static String encodeFilename(String filename, HttpServletRequest request) {
        /**
         * 获取客户端浏览器和操作系统信息 在IE浏览器中得到的是：User-Agent=Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; Maxthon; Alexa
         * Toolbar) 在Firefox中得到的是：User-Agent=Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.7.10) Gecko/20050717
         * Firefox/1.0.6
         */
        String agent = request.getHeader("USER-AGENT");
        if (agent == null) {
            return filename;
        }
        try {
            if (-1 != agent.indexOf("MSIE")) {
                String newFileName = URLEncoder.encode(filename, Constants.DEFAULT_ENCODING);
                newFileName = StringUtils.replace(newFileName, "+", "%20");
                if (newFileName.length() > 150) {
                    newFileName = new String(filename.getBytes(Constants.DEFAULT_ENCODING), "ISO8859-1");
                    newFileName = StringUtils.replace(newFileName, " ", "%20");
                }
                return newFileName;
            }
            if (-1 != agent.indexOf("Mozilla")) {
                return "=?UTF-8?B?" + (new String(Base64.encodeBase64(filename.getBytes("UTF-8")))) + "?=";
            }

            return filename;
        } catch (Exception ex) {
            return filename;
        }
    }

    public static HttpClient createHttpClientInstance() {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        RequestConfig.Builder reqConfigBuilder = RequestConfig.custom();
        reqConfigBuilder.setSocketTimeout(6000);
        reqConfigBuilder.setConnectTimeout(3000);
        reqConfigBuilder.setConnectionRequestTimeout(3000);
        RequestConfig reqConfig = reqConfigBuilder.build();

        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(SSLContexts.createDefault(),
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        httpClientBuilder.setSSLSocketFactory(sslsf);

        httpClientBuilder.setDefaultRequestConfig(reqConfig);
        return httpClientBuilder.build();
    }
}