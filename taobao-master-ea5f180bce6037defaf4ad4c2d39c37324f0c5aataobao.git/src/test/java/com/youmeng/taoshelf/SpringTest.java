package com.youmeng.taoshelf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.taobao.api.internal.toplink.embedded.websocket.exception.ErrorCode;
import com.youmeng.taoshelf.quartz.task.MaxEndTime;



@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void test() {
        logger.info("Hello World");
    }
    public static void main(String[] args) {
    	MaxEndTime max = MaxEndTime.MAXENDTIME;
    	System.out.println(max.getTime());
    }
}




