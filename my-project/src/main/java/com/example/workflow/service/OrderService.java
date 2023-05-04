package com.example.workflow.service;


import com.example.workflow.model.Lieu;
import com.example.workflow.model.PauseDejProcess;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    Logger log = LoggerFactory.getLogger(OrderService.class);


    private final RuntimeService runtimeService;
    private final TaskService taskService;

    public OrderService(RuntimeService runtimeService, TaskService taskService) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
    }

    public PauseDejProcess wantToEat(String idDemande) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Manger", idDemande);
        log.info("Process {} created", processInstance.getId());
        return mapFromProcessInstance(processInstance);
    }

    public List<PauseDejProcess> getAll() {
        return runtimeService.createProcessInstanceQuery()
                .list()
                .stream()
                .map(this::mapFromProcessInstance)
                .collect(Collectors.toList());
    }

    private PauseDejProcess mapFromProcessInstance(ProcessInstance processInstance) {
        Map<String, Object> variables = runtimeService.getVariables(processInstance.getId());
        List<PauseDejProcess.Tache> taches = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .list()
                .stream()
                .map(this::mapTask)
                .collect(Collectors.toList());

        return new PauseDejProcess(processInstance.getId(), processInstance.getBusinessKey(), variables, taches);
    }

    private PauseDejProcess.Tache mapTask(Task task) {
        return new PauseDejProcess.Tache(task.getId(), task.getTaskDefinitionKey());
    }

    public Optional<PauseDejProcess> getByBusinessKey(String businessKey) {
        return Optional.ofNullable(runtimeService.createProcessInstanceQuery()
                        .processDefinitionKey("Manger")
                .processInstanceBusinessKey(businessKey)
                .singleResult())
                .map(this::mapFromProcessInstance);
    }

    public Optional<PauseDejProcess> setLieu(String businessKey, Lieu lieu) {
        Optional<List<PauseDejProcess.Tache>> taches = getByBusinessKey(businessKey)
                .map(PauseDejProcess::getNextTask);
        if(taches.isEmpty()) return Optional.empty();
        Optional<String> idTacheLieu = taches.get()
                .stream()
                .filter(tache -> "ChoixLieu".equals(tache.getName()))
                .findFirst()
                .map(PauseDejProcess.Tache::getId);
        if(idTacheLieu.isEmpty()) return Optional.empty();
        taskService.complete(idTacheLieu.get(), Map.of("lieuRepas", lieu.getLieu()));
        return getByBusinessKey(businessKey);
    }


    public Optional<PauseDejProcess> signal(String businessKey) {
        String processId = getByBusinessKey(businessKey)
                .map(PauseDejProcess::getId)
                .get();
        runtimeService.signalEventReceived( "signal", processId);

        return getByBusinessKey(businessKey);
    }

    public void sendOrder(DelegateExecution execution) {
        log.info("Sending order");
    }

    public Optional<PauseDejProcess> order(String businessKey, Lieu lieu) {
        Optional<List<PauseDejProcess.Tache>> taches = getByBusinessKey(businessKey)
                .map(PauseDejProcess::getNextTask);
        if(taches.isEmpty()) return Optional.empty();
        Optional<String> idTache = taches.get()
                .stream()
                .filter(tache -> "Activity_0bvxeld".equals(tache.getName()))
                .findFirst()
                .map(PauseDejProcess.Tache::getId);
        if(idTache.isEmpty()) return Optional.empty();
        taskService.complete(idTache.get());
        return getByBusinessKey(businessKey);
    }

    public Optional<PauseDejProcess> message(String businessKey) {
        String processId = getByBusinessKey(businessKey)
                .map(PauseDejProcess::getId)
                .get();
        runtimeService.messageEventReceived( "go-back-work", processId);
        return getByBusinessKey(businessKey);
    }
}
