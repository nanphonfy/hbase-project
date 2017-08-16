package cn.sibat.hbase.take;

import cn.sibat.hbase.take.util.StringUtil;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by NAN on 2017/5/10.
 */
public class AbstractTest {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private long start = 0;

    @Before
    public void before() {
        start = System.currentTimeMillis();
    }

    @After
    public void after() {
        logger.info("cost:{} ms", System.currentTimeMillis() - start);
        logger.info("cost:{} ", StringUtil.getDuration(System.currentTimeMillis() - start));
    }
}
