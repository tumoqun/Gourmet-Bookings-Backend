package com.example.demo.controller;

import com.example.demo.dto.ServiceSupplierProjection;
import com.example.demo.entity.Area;
import com.example.demo.entity.DistanceBand;
import com.example.demo.entity.Service;
import com.example.demo.entity.ServiceType;
import com.example.demo.service.CatalogService;
import com.example.demo.service.SupplierService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
@Slf4j
public class ServiceController {

    private final CatalogService catalogService;
    private final SupplierService supplierService;

    @GetMapping("/areas")
    @PreAuthorize("hasAnyAuthority('ORDERS_READ', 'ASSIGNMENTS_READ')")
    public ResponseEntity<List<Area>> getAllAreas() {
        return ResponseEntity.ok(catalogService.findAllAreas());
    }

    @GetMapping("/service-types")
    @PreAuthorize("hasAnyAuthority('ORDERS_READ', 'ASSIGNMENTS_READ')")
    public ResponseEntity<List<ServiceType>> getAllServiceTypes() {
        return ResponseEntity.ok(catalogService.findAllServiceTypes());
    }

    @GetMapping("/distance-bands")
    @PreAuthorize("hasAnyAuthority('ORDERS_READ', 'ASSIGNMENTS_READ')")
    public ResponseEntity<List<DistanceBand>> getAllDistanceBands() {
        return ResponseEntity.ok(catalogService.findAllDistanceBands());
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ORDERS_READ', 'ASSIGNMENTS_READ')")
    public ResponseEntity<List<Service>> getAllServices() {
        List<Service> services = catalogService.findAllActiveServices();
        return ResponseEntity.ok(services);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ORDERS_READ', 'ASSIGNMENTS_READ')")
    public ResponseEntity<Service> getServiceById(@PathVariable Long id) {
        Optional<Service> service = catalogService.findServiceById(id);
        return service.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/area/{areaId}")
    @PreAuthorize("hasAnyAuthority('ORDERS_READ', 'ASSIGNMENTS_READ')")
    public ResponseEntity<List<Service>> getServicesByArea(@PathVariable Long areaId) {
        List<Service> services = catalogService.findServicesByArea(areaId);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/type/{serviceTypeId}")
    @PreAuthorize("hasAnyAuthority('ORDERS_READ', 'ASSIGNMENTS_READ')")
    public ResponseEntity<List<Service>> getServicesByType(@PathVariable Long serviceTypeId) {
        List<Service> services = catalogService.findServicesByType(serviceTypeId);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/area/{areaId}/type/{serviceTypeId}")
    @PreAuthorize("hasAnyAuthority('ORDERS_READ', 'ASSIGNMENTS_READ')")
    public ResponseEntity<List<Service>> getServicesByAreaAndType(
            @PathVariable Long areaId, 
            @PathVariable Long serviceTypeId) {
        List<Service> services = catalogService.findServicesByAreaAndType(areaId, serviceTypeId);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/private")
    @PreAuthorize("hasAnyAuthority('ORDERS_READ', 'ASSIGNMENTS_READ')")
    public ResponseEntity<List<Service>> getPrivateServices() {
        List<Service> services = catalogService.findPrivateServices();
        return ResponseEntity.ok(services);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ASSIGNMENTS_READ')")
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
    @PreAuthorize("hasAuthority('ASSIGNMENTS_READ')")
    public ResponseEntity<Service> updateService(@PathVariable Long id, @RequestBody Service service) {
        try {
            Service updatedService = catalogService.updateService(id, service);
            return ResponseEntity.ok(updatedService);
        } catch (Exception e) {
            log.error("Error updating service with id: {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}/suppliers")
    @PreAuthorize("hasAuthority('ASSIGNMENTS_READ')")
    public ResponseEntity<List<ServiceSupplierProjection>> getSuppliers(
            @PathVariable Long id, @RequestParam Long workId, @RequestParam String supplierType) {

        return ResponseEntity.ok(supplierService.getSuppliersByServiceId(id, workId, supplierType));
    }
}
