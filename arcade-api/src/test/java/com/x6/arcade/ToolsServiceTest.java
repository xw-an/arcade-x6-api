package com.x6.arcade;

import com.x6.arcade.service.ITestToolService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class ToolsServiceTest {

    @Autowired
    private ITestToolService testToolService;

    @Test
    public void testListTools() {
        log.info("testListTools");
    }
}
