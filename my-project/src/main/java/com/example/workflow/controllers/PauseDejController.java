package com.example.workflow.controllers;


import com.example.workflow.model.Lieu;
import com.example.workflow.model.PauseDejProcess;
import com.example.workflow.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/pausedej")
public class PauseDejController {

    private final OrderService orderService;

    public PauseDejController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PauseDejProcess iWantToEat(@RequestBody PauseDejProcess pauseDejProcess) {
        return orderService.wantToEat(pauseDejProcess.getBusinessKey());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PauseDejProcess> getAllProcess() {
        return orderService.getAll();
    }

    @GetMapping("/{businessKey}")
    public ResponseEntity<PauseDejProcess> getProcess(@PathVariable String businessKey) {
        return orderService.getByBusinessKey(businessKey)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{businessKey}/signal")
    public ResponseEntity<PauseDejProcess> signal(@PathVariable String businessKey) {
        return orderService.signal(businessKey)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{businessKey}/message")
    public ResponseEntity<PauseDejProcess> message(@PathVariable String businessKey) {
        return orderService.message(businessKey)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{businessKey}/lieu")
    public ResponseEntity<PauseDejProcess> addLieu(@PathVariable String businessKey, @RequestBody Lieu lieu) {
        return orderService.setLieu(businessKey, lieu)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{businessKey}/order")
    public ResponseEntity<PauseDejProcess> order(@PathVariable String businessKey, @RequestBody Lieu lieu) {
        return orderService.order(businessKey, lieu)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
