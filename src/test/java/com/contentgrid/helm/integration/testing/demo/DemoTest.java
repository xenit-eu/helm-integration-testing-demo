package com.contentgrid.helm.integration.testing.demo;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import com.contentgrid.helm.Helm;
import com.contentgrid.junit.jupiter.helm.HelmTest;
import com.contentgrid.junit.jupiter.k8s.K8sTestUtils;
import com.contentgrid.junit.jupiter.k8s.KubernetesTestCluster;
import com.contentgrid.junit.jupiter.k8s.providers.K3sCiliumDefaultDenyCoreDNSClusterProvider;
import io.fabric8.kubernetes.client.KubernetesClient;
import java.net.ConnectException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@KubernetesTestCluster(providers = K3sCiliumDefaultDenyCoreDNSClusterProvider.class)
@HelmTest(chart = "classpath:demo-chart")
public class DemoTest {

    static KubernetesClient kubernetesClient;
    static Helm helmClient;

    @BeforeAll
    static void setUp() {
        helmClient.install().cwd();
        K8sTestUtils.waitUntilDeploymentsReady(20, List.of("demo-deployment"), kubernetesClient);
    }

    @Test
    void testStatusSuccess() {
        Awaitility.await().atMost(1, TimeUnit.MINUTES)
                .ignoreException(ConnectException.class)
                .untilAsserted(() -> {

                            given()
                                    .header("Host", "demo-chart.local")
                                    .get("http://172.17.0.1/data.json")
                                    .then()
                                    .statusCode(200)
                                    .body("status", equalTo("success"));

                        }
                );
    }

}
