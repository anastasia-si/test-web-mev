package edu.dfci.cccb.mev.annotation.elasticsearch.perf.cols10;

import edu.dfci.cccb.mev.annotation.elasticsearch.perf.AbstractPerfTestNumerify;

public class PerfTestNumerify200K_10cols extends AbstractPerfTestNumerify {

//run 1
//  samples: 3
//  max:     8949
//  average: 8647.0
//  median:  8359
//run 2
//  samples: 3
//  max:     8405
//  average: 7850.666666666667
//  median:  7459
//run 3 maxheap 4097 gcinitfraction: 50%
//  samples: 3
//  max:     7930
//  average: 7697.666666666667
//  median:  7355
  @Override
  protected long getNUMCOLS () {
    return 10;
  }

  @Override
  protected int getNUMROWS () {
    return 200*1000;
  }

}
  