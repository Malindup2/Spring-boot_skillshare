package com.skillshare.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.skillshare.model.Resource;
import com.skillshare.model.ResourceType;

@Repository
public interface ResourceRepository extends MongoRepository<Resource, String> {
    Page<Resource> findBySkillCategory(String skillCategory, Pageable pageable);
    
    Page<Resource> findByResourceType(ResourceType resourceType, Pageable pageable);
    
    @Query("{ $or: [ " +
           "{ 'title': { $regex: ?0, $options: 'i' } }, " +
           "{ 'description': { $regex: ?0, $options: 'i' } }, " +
           "{ 'skillCategory': { $regex: ?0, $options: 'i' } } " +
           "] }")
    Page<Resource> searchResources(String keyword, Pageable pageable);
    
    Page<Resource> findByUser_Id(String userId, Pageable pageable);
}