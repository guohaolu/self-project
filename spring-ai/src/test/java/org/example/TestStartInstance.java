package org.example;

import com.volcengine.ApiClient;
import com.volcengine.ApiException;
import com.volcengine.ecs.EcsApi;
import com.volcengine.ecs.model.StartInstanceRequest;
import com.volcengine.sign.Credentials;

/**
 * TestStartInstance
 * 接入豆包大模型测试
 */
public class TestStartInstance {
    private static final String AK = "AKLTNWJjMGNlZjNjOTY1NDdhMjg3YjkyOWYxMzc2ZjdmZWE";
    private static final String SK = "TlRjNFpUSXpObUV6TWpBM05ERmxZV0pqTURNd01HUm1OV0l4TURVM1kyTQ==";
    private static final String TOKEN = "43c5ad0d-38f8-418c-ac4b-05ac9c1adf30";

    public static void main(String[] args) throws Exception {
        // 注意示例代码安全，代码泄漏会导致AK/SK泄漏，有极大的安全风险。
        String region = "cn-beijing";

        ApiClient apiClient = new ApiClient()
                .setCredentials(Credentials.getCredentials(AK, SK, TOKEN))
                .setRegion(region);

        EcsApi api = new EcsApi(apiClient);

        StartInstanceRequest startInstanceRequest = new StartInstanceRequest();

        try {
            // 复制代码运行示例，请自行打印API返回值。
            api.startInstance(startInstanceRequest);
        } catch (ApiException e) {
            // 复制代码运行示例，请自行打印API错误信息。
            System.out.println(e.getResponseBody());
        }
    }
}
