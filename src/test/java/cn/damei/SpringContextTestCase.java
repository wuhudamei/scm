package cn.damei;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: Wei Liu
 * Date: 13-6-24
 * Time: PM3:55
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
@Transactional
@ActiveProfiles("development")
public class SpringContextTestCase extends org.springside.modules.test.spring.SpringContextTestCase {
}
