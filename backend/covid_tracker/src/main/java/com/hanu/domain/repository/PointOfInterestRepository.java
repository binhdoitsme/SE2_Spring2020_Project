package com.hanu.domain.repository;

import com.hanu.base.Repository;
import com.hanu.domain.model.PointOfInterest;
import com.hanu.util.di.Dependency;

@Dependency
public interface PointOfInterestRepository extends Repository<PointOfInterest, Integer>{
}
