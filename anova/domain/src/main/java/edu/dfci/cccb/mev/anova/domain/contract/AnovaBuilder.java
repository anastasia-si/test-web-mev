package edu.dfci.cccb.mev.anova.domain.contract;

import edu.dfci.cccb.mev.dataset.domain.contract.AnalysisBuilder;

public interface AnovaBuilder extends AnalysisBuilder<AnovaBuilder, Anova> {
  
  AnovaBuilder groupSelections(String[] theSelections); 
  AnovaBuilder pValue(double p);
  AnovaBuilder multipleTestCorrectionFlag(boolean b);
}
