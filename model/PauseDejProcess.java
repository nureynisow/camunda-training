package com.example.workflow.model;

import java.util.List;
import java.util.Map;

public class PauseDejProcess {
    private Map<String, Object> variables;
    private String id;
    private String businessKey;
    private List<Tache> nextTask;

    public PauseDejProcess(String id, String businessKey, Map<String, Object> variables, List<Tache> nextTask) {
        this.id = id;
        this.businessKey = businessKey;
        this.variables = variables;
        this.nextTask = nextTask;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    public List<Tache> getNextTask() {
        return nextTask;
    }

    public static class Tache {
        private String id;
        private String name;

        public Tache(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
