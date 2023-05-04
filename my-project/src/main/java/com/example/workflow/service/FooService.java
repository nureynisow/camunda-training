package com.example.workflow.service;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FooService  implements JavaDelegate {
    Logger log = LoggerFactory.getLogger(FooService.class);
    public void foo() {
        log.info("do foo");
        throw new BpmnError("Error1", "Error occured");
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Check meteo");
        execution.setVariable("temperature", 27);
        execution.setVariable("humidity", 0.1);
    }
}
