package com.example.demo.service;

import com.example.demo.entity.Service;
import com.example.demo.entity.Area;
import com.example.demo.entity.ServiceType;
import com.example.demo.repository.ServiceRepository;
import com.example.demo.repository.AreaRepository;
import com.example.demo.repository.ServiceTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CatalogService {

    private final ServiceRepository serviceRepository;
    private final AreaRepository areaRepository;
    private final ServiceTypeRepository serviceTypeRepository;

    public List<Service> findAllActiveServices() {
        return serviceRepository.findAllActive();
    }

    public Optional<Service> findServiceById(Long id) {
        return serviceRepository.findById(id);
    }

    public List<Service> findServicesByArea(Long areaId) {
        return serviceRepository.findByAreaIdAndIsActiveTrue(areaId);
    }

    public List<Service> findServicesByType(Long serviceTypeId) {
        return serviceRepository.findByServiceTypeIdAndIsActiveTrue(serviceTypeId);
    }

    public List<Service> findServicesByAreaAndType(Long areaId, Long serviceTypeId) {
        return serviceRepository.findByAreaIdAndServiceTypeIdAndIsActiveTrue(areaId, serviceTypeId);
    }

    public List<Service> findPrivateServices() {
        return serviceRepository.findPrivateServices();
    }

    @Transactional
    public Service createService(Service service) {
        service.setCreatedAt(java.time.LocalDateTime.now());
        service.setUpdatedAt(java.time.LocalDateTime.now());
        
        Service savedService = serviceRepository.save(service);
        log.info("Created service with ID: {}", savedService.getId());
        return savedService;
    }

    @Transactional
    public Service updateService(Long id, Service service) {
        Service existingService = serviceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));

        existingService.setName(service.getName());
        existingService.setArea(service.getArea());
        existingService.setServiceType(service.getServiceType());
        existingService.setIsPrivateAvailable(service.getIsPrivateAvailable());
        existingService.setIsActive(service.getIsActive());
        existingService.setUpdatedAt(java.time.LocalDateTime.now());

        Service updatedService = serviceRepository.save(existingService);
        log.info("Updated service with ID: {}", updatedService.getId());
        return updatedService;
    }

    public List<Area> findAllAreas() {
        return areaRepository.findAll();
    }

    public Optional<Area> findAreaById(Long id) {
        return areaRepository.findById(id);
    }

    public List<ServiceType> findAllServiceTypes() {
        return serviceTypeRepository.findAll();
    }

    public Optional<ServiceType> findServiceTypeById(Long id) {
        return serviceTypeRepository.findById(id);
    }
}
