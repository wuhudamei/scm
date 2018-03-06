package cn.damei;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author liuwei
 */
public class PropertyHolderTest extends SpringContextTestCase {

    /**
     * 可被JVM参数覆盖 -Dserver.node_name=mynode
     */
    @Value("${server.node_name}")
    private String nodeName;

    @Test
    public void test() {
        assertThat(nodeName).isEqualTo("default");
    }
}
