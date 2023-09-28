package com.debit.logaggregator.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

/**
 *  Контроллер для проверки эндпоинтов. Должен быть удален в проде.
 * @author Bogdan Lesin
 */
@RestController
@RequestMapping("test")
public class TestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);
    @GetMapping(value = "", produces = TEXT_PLAIN_VALUE)
    public String test(final @RequestParam("name") String name) {
        LOGGER.info("test info");
        LOGGER.warn("test warn");
        return String.format("Hello, it is test for mister %s", name);
    }
}
