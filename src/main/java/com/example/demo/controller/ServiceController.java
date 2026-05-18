package com.example.demo.controller;

import com.example.demo.entity.Area;
import com.example.demo.entity.Service;
import com.example.demo.entity.ServiceType;
import com.example.demo.service.CatalogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
@Slf4j
public class ServiceController {

    private final CatalogService catalogService;

    @GetMapping("/areas")
    public ResponseEntity<List<Area>> getAllAreas() {
        return ResponseEntity.ok(catalogService.findAllAreas());
    }

    @GetMapping("/service-types")
    public ResponseEntity<List<ServiceType>> getAllServiceTypes() {
        return ResponseEntity.ok(catalogService.findAllServiceTypes());
    }

    @GetMapping
    public ResponseEntity<List<Service>> getAllServices() {
        List<Service> services = catalogService.findAllActiveServices();
        return ResponseEntity.ok(services);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Service> getServiceById(@PathVariable Long id) {
        Optional<Service> service = catalogService.findServiceById(id);
        return service.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/area/{areaId}")
    public ResponseEntity<List<Service>> getServicesByArea(@PathVariable Long areaId) {
        List<Service> services = catalogService.findServicesByArea(areaId);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/type/{serviceTypeId}")
    public ResponseEntity<List<Service>> getServicesByType(@PathVariable Long serviceTypeId) {
        List<Service> services = catalogService.findServicesByType(serviceTypeId);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/area/{areaId}/type/{serviceTypeId}")
    public ResponseEntity<List<Service>> getServicesByAreaAndType(
            @PathVariable Long areaId, 
            @PathVariable Long serviceTypeId) {
        List<Service> services = catalogService.findServicesByAreaAndType(areaId, serviceTypeId);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/private")
    public ResponseEntity<List<Service>> getPrivateServices() {
        List<Service> services = catalogService.findPrivateServices();
        return ResponseEntity.ok(services);
    }

    @PostMapping
    public ResponseEntity<Service> createService(@RequestBody Service service) {
        try {
            Service createdService = catalogService.createService(service);
            return ResponseEntity.ok(createdService);
        } catch (Exception e) {
            log.error("Error creating service", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Service> updateService(@PathVariable Long id, @RequestBody Service service) {
        try {
            Service updatedService = catalogService.updateService(id, service);
            return ResponseEntity.ok(updatedService);
        } catch (Exception e) {
            log.error("Error updating service with id: {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }
}
