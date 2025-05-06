package com.skillshare.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillshare.model.Resource;
import com.skillshare.model.ResourceType;
import com.skillshare.repository.ResourceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResourceService {
    private final ResourceRepository resourceRepository;

    public Page<Resource> getAllResources(Pageable pageable) {
        return resourceRepository.findAll(pageable);
    }

    public Resource getResourceById(String id) {
        return resourceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Resource not found"));
    }

    public Page<Resource> searchResources(String keyword, Pageable pageable) {
        return resourceRepository.searchResources(keyword, pageable);
    }

    public Page<Resource> getResourcesByCategory(String category, Pageable pageable) {
        return resourceRepository.findBySkillCategory(category, pageable);
    }

    public Page<Resource> getResourcesByType(ResourceType type, Pageable pageable) {
        return resourceRepository.findByResourceType(type, pageable);
    }

    @Transactional
    public Resource createResource(Resource resource) {
        resource.prePersist();
        return resourceRepository.save(resource);
    }

    @Transactional
    public Resource updateResource(String id, Resource resourceDetails) {
        Resource resource = getResourceById(id);
        
        resource.setTitle(resourceDetails.getTitle());
        resource.setDescription(resourceDetails.getDescription());
        resource.setUrl(resourceDetails.getUrl());
        resource.setResourceType(resourceDetails.getResourceType());
        resource.setSkillCategory(resourceDetails.getSkillCategory());
        
        resource.preUpdate();
        return resourceRepository.save(resource);
    }

    @Transactional
    public void deleteResource(String id) {
        Resource resource = getResourceById(id);
        resourceRepository.delete(resource);
    }

    public Page<Resource> getUserResources(String userId, Pageable pageable) {
        return resourceRepository.findByUser_Id(userId, pageable);
    }
}