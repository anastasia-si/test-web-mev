package edu.dfci.cccb.mev.annotation.domain.probe.contract;



public interface ProbeAnnotationsFactory {
  ProbeAnnotations create(String platformId, String type);  
}
