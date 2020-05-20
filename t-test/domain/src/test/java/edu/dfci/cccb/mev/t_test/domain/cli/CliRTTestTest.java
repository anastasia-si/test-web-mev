package edu.dfci.cccb.mev.t_test.domain.cli;


/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import static edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type.COLUMN;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.Properties;

import javax.script.ScriptEngineManager;

import lombok.extern.log4j.Log4j;

import org.junit.Test;

import edu.dfci.cccb.mev.dataset.domain.contract.Dataset;
import edu.dfci.cccb.mev.dataset.domain.contract.Selection;
import edu.dfci.cccb.mev.dataset.domain.mock.MapBackedValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.mock.MockTsvInput;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleDatasetBuilder;
import edu.dfci.cccb.mev.dataset.domain.simple.SimpleSelection;
import edu.dfci.cccb.mev.dataset.domain.supercsv.SuperCsvComposerFactory;
import edu.dfci.cccb.mev.dataset.domain.supercsv.SuperCsvParserFactory;
import edu.dfci.cccb.mev.t_test.domain.contract.TTest;
import edu.dfci.cccb.mev.t_test.domain.contract.TTest.Entry;
import edu.dfci.cccb.mev.t_test.domain.impl.OneSampleTTestBuilder;
import edu.dfci.cccb.mev.t_test.domain.impl.PairedTTestBuilder;
import edu.dfci.cccb.mev.t_test.domain.impl.TwoSampleTTestBuilder;

/**
 * @author levk
 * 
 */
@Log4j
public class CliRTTestTest {

  private double pValTolerance=0.01;
  
  @Test
  public void one_sample_ttest_produces_correct_pValue () throws Exception {
    Dataset dataset = new SimpleDatasetBuilder ().setParserFactories (asList (new SuperCsvParserFactory ()))
                                                 .setValueStoreBuilder (new MapBackedValueStoreBuilder ())
                                                 .build (new MockTsvInput ("mock", "id\tsa\tsb\tsc\tsd\tse\tsf\n" +
                                                                                   "g1\t.1\t.2\t.3\t.4\t.5\t.6\n" +
                                                                                   "g2\t.4\t.5\t.6\t.7\t.8\t.9"));
    Selection control = new SimpleSelection ("control", new Properties (), asList ("sa", "sc", "sd","sb", "se", "sf"));
    dataset.dimension (COLUMN).selections ().put (control);
    TTest result =
                   new OneSampleTTestBuilder ().r (new ScriptEngineManager ().getEngineByName ("CliR"))
                        .composerFactory (new SuperCsvComposerFactory ())
                   .name ("one sample")
                 .dataset (dataset)
                 .controlSelection (control)
                 .pValue (0.05)
                 .multipleTestCorrectionFlag (false)
                 .oneSampleMean (0.52).build ();
    Iterable<Entry> ie=result.fullResults ();
    assertEquals (0.07656,
                  ie.iterator ().next ().pValue (),
                  pValTolerance);
    
  }
  
  
  @Test
  public void two_sample_ttest_produces_correct_pValue_for_unequal_variance () throws Exception {
    Dataset dataset = new SimpleDatasetBuilder ().setParserFactories (asList (new SuperCsvParserFactory ()))
                                                 .setValueStoreBuilder (new MapBackedValueStoreBuilder ())
                                                 .build (new MockTsvInput ("mock", "\tS1\tS2\tS3\tS4\tS5\tS6\tS7\tS8\tS9\tS10\nG1\t1.188514712463404699e+01\t1.204140233660780979e+01\t1.234081412888200902e+01\t1.163898490634033323e+01\t1.236031434743662594e+01\t1.263358678802757851e+01\t8.279956752711142842e-01\t1.370350032209602054e+00\t1.037994449169919786e+00\t1.799723880674055643e-01\nG2\t1.885238297671112173e+00\t1.336588864883923256e+00\t8.726933138377110932e-01\t1.000704965530543999e+00\t1.217150222216657252e+00\t1.665733953332364292e+00\t9.910461446315059764e-01\t8.893733573944051507e-01\t7.208001799521422948e-01\t1.426827801481250546e+00\nG3\t1.188388854775392689e+00\t2.248097448451613634e-02\t4.585539956464407263e-01\t6.321050648189079357e-01\t2.288129746374324203e+00\t1.450523320935948490e+00\t8.743715686250737307e-02\t1.158676271225091536e+00\t4.483649242829604864e-01\t9.220164357611759787e-01\nG4\t1.262581518849290063e+01\t1.097643408373854257e+01\t1.248733733542591118e+01\t1.183359244927881093e+01\t1.242754369105501233e+01\t1.154062810662884253e+01\t1.402161753382923148e+00\t1.110723816716849388e+00\t5.519559139121001934e-01\t1.191073330691207799e+00\nG5\t4.212630682277810901e-01\t1.237089222272245159e+00\t3.942134331403249758e-01\t6.571736269682573184e-01\t7.107561591312392402e-01\t-1.799850179649653548e-02\t5.900194955064834712e-01\t1.866021689210231704e+00\t1.727950989673915139e+00\t1.673238760870431552e+00\nG6\t1.623904902317880872e+00\t2.033737954042716289e+00\t1.059667307687251370e+00\t5.829633567125455107e-01\t7.066180780581627907e-01\t7.179278333133176648e-01\t-1.620837539166711527e-01\t1.103900303479948608e+00\t1.000963636766493581e+00\t1.224700740396261756e+00\nG7\t1.089347908225312800e+00\t5.027904142354412409e-01\t1.526308400742132365e+00\t7.910323975010494779e-01\t5.334721895871848973e-01\t-6.584953162406614879e-02\t1.487086356403074960e+00\t7.289110071229999432e-01\t9.902665697489260177e-01\t5.816465061333875308e-01\nG8\t8.485257423886340966e-01\t2.465461094159465461e+00\t1.349261064626203988e+00\t6.143236099481832468e-01\t1.185825425854624138e+00\t1.244199580018195528e+00\t1.853690475642675750e+00\t-3.717669448902238827e-02\t6.854264339399868122e-01\t1.383830132326594464e-01\nG9\t1.403874515733180672e+00\t2.140084051686685029e+00\t1.143839053529577043e+00\t1.207999104285488734e+00\t1.209300244849931039e+00\t2.379590313511170674e-02\t1.974749101374898563e+00\t1.070609451318704108e+00\t6.151091514422060008e-02\t9.736475782769393739e-01\nG10\t7.899488648703185678e-01\t1.999391228179024704e+00\t9.363406180457896077e-01\t8.661591803373676068e-01\t9.989733067095830643e-01\t6.946825858089700212e-01\t9.689452303609590311e-01\t8.093542982306978217e-02\t1.095035589870868353e+00\t9.723302437465745074e-01\nG11\t3.334731085908727577e-01\t3.426242846092957262e-01\t4.937020871446952397e-02\t4.302553294755867563e-01\t1.153920107411710028e+00\t1.070122611874620544e+00\t1.908926108120575504e+00\t1.952270474332367778e+00\t7.019370450079993884e-02\t4.295688624516629250e-01\nG12\t1.392406509736836329e+00\t1.372870779384700590e+00\t5.404078368283642408e-01\t9.577725746500534987e-01\t1.930301589849148236e+00\t1.065669702544272957e+00\t1.022885904312618166e+00\t6.427143424737080668e-01\t3.930179936499352267e-01\t9.990896938741014033e-01\nG13\t1.325633714376544425e+00\t7.040364121034963230e-01\t1.589435388962163076e+00\t2.284719993548981698e+00\t1.702734362224248121e+00\t1.412491307931115214e+00\t1.672459629461545694e+00\t1.073155180531838671e+00\t1.411681767952686073e+00\t6.238961028115959095e-01\nG14\t1.288950803566242254e+00\t1.028575691034351314e-01\t1.102664086811557587e+00\t4.699036879893047081e-01\t3.876866425390312898e-01\t1.416878751168134887e+00\t9.433454890035535279e-01\t1.413906277410074352e+00\t4.778216530655552963e-01\t4.268871754146941200e-02\nG15\t1.322327157267192854e+00\t1.030668314264236773e+00\t1.171973213151947357e+00\t7.051026378379774417e-01\t1.603389007795869015e-01\t1.013608197193421656e+00\t1.119755089175789919e+00\t8.846284618247358678e-01\t1.032390130479277301e+00\t1.563217432008172647e-01\nG16\t4.594649795375105317e-02\t1.391620831679649006e+00\t1.543638392839115081e+00\t1.250790629220879691e+00\t1.760626870246531972e+00\t-2.719273644194430961e-01\t1.228157198567842556e+00\t5.718455067192516061e-01\t1.223007328253810089e+00\t1.320469664101578511e+00\nG17\t1.277799857436148834e+01\t1.275249893990934069e+01\t1.233011570757031095e+01\t1.245797072549325613e+01\t1.144397636751770087e+01\t1.264147576903921966e+01\t9.861790550751070583e-01\t1.005304796569497894e+00\t9.890599521756782142e-01\t8.110532346285517225e-01\nG18\t1.581739895645190686e+00\t2.054658403772655628e-01\t1.277027695188764245e+00\t1.460942482509764773e+00\t1.475607347809501535e+00\t4.862547500778641174e-01\t3.462212302333083080e-01\t1.181704073194318472e+00\t1.447215677951656065e+00\t1.239332864373165588e+00\nG19\t8.294625023542446796e-01\t4.347079214591751395e-01\t1.285626053170358718e+00\t1.773088394837966764e+00\t1.184014487786790193e+00\t5.522823407713599586e-01\t5.705450257984490126e-01\t2.110152326668979228e+00\t1.325059483533218563e+00\t2.663088059378119210e-01\nG20\t7.120160033627074281e-01\t9.123263397371228489e-01\t1.431553131076708674e+00\t1.724438313837745040e+00\t1.327524752620593329e+00\t2.585233328583039780e-01\t9.156477637307659512e-01\t8.222344601683477805e-01\t7.215323977152153789e-01\t6.489097589989401005e-01"));
    Selection control = new SimpleSelection ("control", new Properties (), asList ("S1", "S2", "S3","S4", "S5", "S6"));
    Selection experiment = new SimpleSelection ("experiment", new Properties (), asList ("S7", "S8", "S9","S10"));

    dataset.dimension (COLUMN).selections ().put (control);
    dataset.dimension (COLUMN).selections ().put (experiment);

    TTest result =
                   new TwoSampleTTestBuilder ().r (new ScriptEngineManager ().getEngineByName ("CliR"))
                        .composerFactory (new SuperCsvComposerFactory ())
                   .name ("two sample")
                 .dataset (dataset)
                 .experimentSelection (experiment)
                 .controlSelection (control)
                 .pValue (0.05)
                 .equalVarianceFlag (false)
                 .multipleTestCorrectionFlag (false)
                 .build ();
    
    int checkEntry2=2;
    int checkEntry10=10;
    int index=1;
    for (Entry e : result.fullResults ()){
      if(index==checkEntry2){
        assertEquals (0.1795,
                      e.pValue (),
                      pValTolerance);
      }
      if(index==checkEntry10){
        assertEquals (0.41,
                      e.pValue (),
                      pValTolerance);
      }
      index++;
    }
  }
  
  @Test
  public void two_sample_ttest_produces_correct_pValue_for_equal_variance () throws Exception {
    Dataset dataset = new SimpleDatasetBuilder ().setParserFactories (asList (new SuperCsvParserFactory ()))
                                                 .setValueStoreBuilder (new MapBackedValueStoreBuilder ())
                                                 .build (new MockTsvInput ("mock", "\tS1\tS2\tS3\tS4\tS5\tS6\tS7\tS8\tS9\tS10\nG1\t1.188514712463404699e+01\t1.204140233660780979e+01\t1.234081412888200902e+01\t1.163898490634033323e+01\t1.236031434743662594e+01\t1.263358678802757851e+01\t8.279956752711142842e-01\t1.370350032209602054e+00\t1.037994449169919786e+00\t1.799723880674055643e-01\nG2\t1.885238297671112173e+00\t1.336588864883923256e+00\t8.726933138377110932e-01\t1.000704965530543999e+00\t1.217150222216657252e+00\t1.665733953332364292e+00\t9.910461446315059764e-01\t8.893733573944051507e-01\t7.208001799521422948e-01\t1.426827801481250546e+00\nG3\t1.188388854775392689e+00\t2.248097448451613634e-02\t4.585539956464407263e-01\t6.321050648189079357e-01\t2.288129746374324203e+00\t1.450523320935948490e+00\t8.743715686250737307e-02\t1.158676271225091536e+00\t4.483649242829604864e-01\t9.220164357611759787e-01\nG4\t1.262581518849290063e+01\t1.097643408373854257e+01\t1.248733733542591118e+01\t1.183359244927881093e+01\t1.242754369105501233e+01\t1.154062810662884253e+01\t1.402161753382923148e+00\t1.110723816716849388e+00\t5.519559139121001934e-01\t1.191073330691207799e+00\nG5\t4.212630682277810901e-01\t1.237089222272245159e+00\t3.942134331403249758e-01\t6.571736269682573184e-01\t7.107561591312392402e-01\t-1.799850179649653548e-02\t5.900194955064834712e-01\t1.866021689210231704e+00\t1.727950989673915139e+00\t1.673238760870431552e+00\nG6\t1.623904902317880872e+00\t2.033737954042716289e+00\t1.059667307687251370e+00\t5.829633567125455107e-01\t7.066180780581627907e-01\t7.179278333133176648e-01\t-1.620837539166711527e-01\t1.103900303479948608e+00\t1.000963636766493581e+00\t1.224700740396261756e+00\nG7\t1.089347908225312800e+00\t5.027904142354412409e-01\t1.526308400742132365e+00\t7.910323975010494779e-01\t5.334721895871848973e-01\t-6.584953162406614879e-02\t1.487086356403074960e+00\t7.289110071229999432e-01\t9.902665697489260177e-01\t5.816465061333875308e-01\nG8\t8.485257423886340966e-01\t2.465461094159465461e+00\t1.349261064626203988e+00\t6.143236099481832468e-01\t1.185825425854624138e+00\t1.244199580018195528e+00\t1.853690475642675750e+00\t-3.717669448902238827e-02\t6.854264339399868122e-01\t1.383830132326594464e-01\nG9\t1.403874515733180672e+00\t2.140084051686685029e+00\t1.143839053529577043e+00\t1.207999104285488734e+00\t1.209300244849931039e+00\t2.379590313511170674e-02\t1.974749101374898563e+00\t1.070609451318704108e+00\t6.151091514422060008e-02\t9.736475782769393739e-01\nG10\t7.899488648703185678e-01\t1.999391228179024704e+00\t9.363406180457896077e-01\t8.661591803373676068e-01\t9.989733067095830643e-01\t6.946825858089700212e-01\t9.689452303609590311e-01\t8.093542982306978217e-02\t1.095035589870868353e+00\t9.723302437465745074e-01\nG11\t3.334731085908727577e-01\t3.426242846092957262e-01\t4.937020871446952397e-02\t4.302553294755867563e-01\t1.153920107411710028e+00\t1.070122611874620544e+00\t1.908926108120575504e+00\t1.952270474332367778e+00\t7.019370450079993884e-02\t4.295688624516629250e-01\nG12\t1.392406509736836329e+00\t1.372870779384700590e+00\t5.404078368283642408e-01\t9.577725746500534987e-01\t1.930301589849148236e+00\t1.065669702544272957e+00\t1.022885904312618166e+00\t6.427143424737080668e-01\t3.930179936499352267e-01\t9.990896938741014033e-01\nG13\t1.325633714376544425e+00\t7.040364121034963230e-01\t1.589435388962163076e+00\t2.284719993548981698e+00\t1.702734362224248121e+00\t1.412491307931115214e+00\t1.672459629461545694e+00\t1.073155180531838671e+00\t1.411681767952686073e+00\t6.238961028115959095e-01\nG14\t1.288950803566242254e+00\t1.028575691034351314e-01\t1.102664086811557587e+00\t4.699036879893047081e-01\t3.876866425390312898e-01\t1.416878751168134887e+00\t9.433454890035535279e-01\t1.413906277410074352e+00\t4.778216530655552963e-01\t4.268871754146941200e-02\nG15\t1.322327157267192854e+00\t1.030668314264236773e+00\t1.171973213151947357e+00\t7.051026378379774417e-01\t1.603389007795869015e-01\t1.013608197193421656e+00\t1.119755089175789919e+00\t8.846284618247358678e-01\t1.032390130479277301e+00\t1.563217432008172647e-01\nG16\t4.594649795375105317e-02\t1.391620831679649006e+00\t1.543638392839115081e+00\t1.250790629220879691e+00\t1.760626870246531972e+00\t-2.719273644194430961e-01\t1.228157198567842556e+00\t5.718455067192516061e-01\t1.223007328253810089e+00\t1.320469664101578511e+00\nG17\t1.277799857436148834e+01\t1.275249893990934069e+01\t1.233011570757031095e+01\t1.245797072549325613e+01\t1.144397636751770087e+01\t1.264147576903921966e+01\t9.861790550751070583e-01\t1.005304796569497894e+00\t9.890599521756782142e-01\t8.110532346285517225e-01\nG18\t1.581739895645190686e+00\t2.054658403772655628e-01\t1.277027695188764245e+00\t1.460942482509764773e+00\t1.475607347809501535e+00\t4.862547500778641174e-01\t3.462212302333083080e-01\t1.181704073194318472e+00\t1.447215677951656065e+00\t1.239332864373165588e+00\nG19\t8.294625023542446796e-01\t4.347079214591751395e-01\t1.285626053170358718e+00\t1.773088394837966764e+00\t1.184014487786790193e+00\t5.522823407713599586e-01\t5.705450257984490126e-01\t2.110152326668979228e+00\t1.325059483533218563e+00\t2.663088059378119210e-01\nG20\t7.120160033627074281e-01\t9.123263397371228489e-01\t1.431553131076708674e+00\t1.724438313837745040e+00\t1.327524752620593329e+00\t2.585233328583039780e-01\t9.156477637307659512e-01\t8.222344601683477805e-01\t7.215323977152153789e-01\t6.489097589989401005e-01"));
    Selection control = new SimpleSelection ("control", new Properties (), asList ("S1", "S2", "S3","S4", "S5", "S6"));
    Selection experiment = new SimpleSelection ("experiment", new Properties (), asList ("S7", "S8", "S9","S10"));

    dataset.dimension (COLUMN).selections ().put (control);
    dataset.dimension (COLUMN).selections ().put (experiment);

    TTest result =
                   new TwoSampleTTestBuilder ().r (new ScriptEngineManager ().getEngineByName ("CliR"))
                        .composerFactory (new SuperCsvComposerFactory ())
                   .name ("two sample")
                 .dataset (dataset)
                 .experimentSelection (experiment)
                 .controlSelection (control)
                 .pValue (0.05)
                 .equalVarianceFlag (true)
                 .multipleTestCorrectionFlag (false)
                 .build ();
    
    int checkEntry2=2;
    int checkEntry10=10;
    int index=1;
    for (Entry e : result.fullResults ()){
      if(index==checkEntry2){
        assertEquals (0.1998,
                      e.pValue (),
                      pValTolerance);
        log.debug (e.toString ());
      }
      if(index==checkEntry10){
        assertEquals (0.407,
                      e.pValue (),
                      pValTolerance);
        log.debug (e.toString ());

      }
      index++;
    }

  }
  
  
  //TODO:This test fails, needs fix
  /*The error is something like this:
  ....
  #adjust the p-vals for multiple testing, if desired:
  if(CORRECT_FOR_MULTIPLE_TESTING)
  {
    p_vals<-p.adjust(p_vals, method="fdr")
  }
  
  results=data.frame(rownames(exp_data),p_vals)
  write.table(results, OUTFILE, sep='\t', row.names=F, col.names=F, quote=F)
  
  Standard output:
  
  Standard error:
  Error: subscript out of bounds
  Execution halted
  
  [DEBUG] : Two-sample t-test script failed
  Input dataset:
  id  S1  S2  S3  S4  S5  S6  S7  S8  S9  S10
  G1  1.3958642506081673  0.3916231968392997  0.6802546076106781  0.2842920356539057  1.1679787918031146  0.5575493844636111  0.6182690292679595  1.0642346281221293  1.6258117878488028  0.9864857446509974
  G2  1.1502445106687447  1.3880799751625719  1.3410883738218409  3.084720816624258 2.5818450802212416  2.367430922421943 1.0323621828407936  3.1177875444537237  0.4617892882371386  1.6028270330759116
  G3  0.4391889940305381  1.382298236091139 0.5729514752270052  0.9725989889844148  1.2482840091059657  0.892657809676699 1.3197541543823776  1.5402983526465595  1.1331274150019597  1.6761624129921446
  G4  1.9055613600165997  0.09356241886510197 0.2380285018347455  3.84740746318419  2.356291503584247 2.7945099307946974  0.6741638547114861  1.724432926869718 1.1959987200632622  1.7452336714089403
  G5  1.7193374095781333  0.34948461450507196 1.1676676307900755  0.47822302606851075 0.43991423652215633 0.6800108190568048  0.725324822750355 0.7806689816392596  0.6925188107811194  0.03442408758869031
  G6  1.4366217443732174  0.9752049715130082  1.1245562736722203  0.731299797880927 1.297314694459755 0.24859897545201803 2.137179543584942 0.4094669660443885  0.45751131643185566 0.8482968148400671
  G7  0.8715438551197592  0.6180182417077844  1.1699182709635378  0.2862357749855996  1.308055771196736 0.37794604167237944 0.7388802606792712  0.9796859959757529  1.243557217256899 0.08095219518818875
  G8  1.243952955865607 1.440989147751387 0.23023679219644602 0.6786898563519169  1.1777472499323156  2.1310451956640084  0.7875240265935568  0.7395105287783704  0.7665894136772822  1.3036489536316505
  G9  0.5958003825885131  1.7092788822579812  0.6575371327833991  0.3329769660881695  1.0092061769720981  0.2588110927523207  1.7152733437371928  0.7197384843776439  1.2101544458522782  0.518110868873396
  G10 1.3386373953873634  0.829889498625954 0.7013115941652059  0.8935077793136789  0.5163333275847467  1.6108273430489 1.7310751655381715  0.22372774102116555 1.2343195860390155  0.2412611683717999
  
  Configuration:
  S4  0 S1
  S5  0 S2
  S6  0 S3
  S8  0 S7
  S9  -1  NA
  S10 -1  NA
  ....
  */
  
  @Test
  public void paired_ttest_produces_correct_pValue () throws Exception {
    Dataset dataset = new SimpleDatasetBuilder ().setParserFactories (asList (new SuperCsvParserFactory ()))
                                                 .setValueStoreBuilder (new MapBackedValueStoreBuilder ())
                                                 .build (new MockTsvInput ("mock", "\tS1\tS2\tS3\tS4\tS5\tS6\tS7\tS8\tS9\tS10\nG1\t1.395864250608167323e+00\t3.916231968392996921e-01\t6.802546076106781303e-01\t2.842920356539057058e-01\t1.167978791803114591e+00\t5.575493844636111307e-01\t6.182690292679594579e-01\t1.064234628122129322e+00\t1.625811787848802759e+00\t9.864857446509973649e-01\nG2\t1.150244510668744669e+00\t1.388079975162571866e+00\t1.341088373821840873e+00\t3.084720816624257900e+00\t2.581845080221241595e+00\t2.367430922421942974e+00\t1.032362182840793619e+00\t3.117787544453723747e+00\t4.617892882371386065e-01\t1.602827033075911567e+00\nG3\t4.391889940305381268e-01\t1.382298236091139110e+00\t5.729514752270051803e-01\t9.725989889844147696e-01\t1.248284009105965708e+00\t8.926578096766990544e-01\t1.319754154382377642e+00\t1.540298352646559454e+00\t1.133127415001959681e+00\t1.676162412992144635e+00\nG4\t1.905561360016599703e+00\t9.356241886510197059e-02\t2.380285018347454984e-01\t3.847407463184190135e+00\t2.356291503584246882e+00\t2.794509930794697450e+00\t6.741638547114860991e-01\t1.724432926869718052e+00\t1.195998720063262244e+00\t1.745233671408940257e+00\nG5\t1.719337409578133302e+00\t3.494846145050719555e-01\t1.167667630790075517e+00\t4.782230260685107481e-01\t4.399142365221563322e-01\t6.800108190568048272e-01\t7.253248227503550360e-01\t7.806689816392595738e-01\t6.925188107811194316e-01\t3.442408758869031082e-02\nG6\t1.436621744373217435e+00\t9.752049715130082230e-01\t1.124556273672220330e+00\t7.312997978809270272e-01\t1.297314694459754891e+00\t2.485989754520180339e-01\t2.137179543584942110e+00\t4.094669660443884807e-01\t4.575113164318556636e-01\t8.482968148400671371e-01\nG7\t8.715438551197591810e-01\t6.180182417077844104e-01\t1.169918270963537843e+00\t2.862357749855996003e-01\t1.308055771196735995e+00\t3.779460416723794403e-01\t7.388802606792711503e-01\t9.796859959757528991e-01\t1.243557217256898939e+00\t8.095219518818874693e-02\nG8\t1.243952955865607057e+00\t1.440989147751386978e+00\t2.302367921964460218e-01\t6.786898563519169203e-01\t1.177747249932315610e+00\t2.131045195664008407e+00\t7.875240265935568029e-01\t7.395105287783704240e-01\t7.665894136772821721e-01\t1.303648953631650453e+00\nG9\t5.958003825885130889e-01\t1.709278882257981191e+00\t6.575371327833990920e-01\t3.329769660881695126e-01\t1.009206176972098135e+00\t2.588110927523207261e-01\t1.715273343737192757e+00\t7.197384843776438723e-01\t1.210154445852278160e+00\t5.181108688733959688e-01\nG10\t1.338637395387363371e+00\t8.298894986259539586e-01\t7.013115941652059426e-01\t8.935077793136788626e-01\t5.163333275847467041e-01\t1.610827343048899962e+00\t1.731075165538171534e+00\t2.237277410211655493e-01\t1.234319586039015482e+00\t2.412611683717998945e-01"));
    Selection control = new SimpleSelection ("control", new Properties (), asList ("S1", "S2", "S3","S7"));
    Selection experiment = new SimpleSelection ("experiment", new Properties (), asList ("S4", "S5", "S6","S8"));

    dataset.dimension (COLUMN).selections ().put (control);
    dataset.dimension (COLUMN).selections ().put (experiment);

    TTest result =
                   new PairedTTestBuilder ().r (new ScriptEngineManager ().getEngineByName ("CliR"))
                        .composerFactory (new SuperCsvComposerFactory ())
                   .name ("paired")
                 .dataset (dataset)
                 .experimentSelection (experiment)
                 .controlSelection (control)
                 .multipleTestCorrectionFlag (false)
                 .build ();
    
    int checkEntry1=1;
    int checkEntry2=2;
    int index=1;
    for (Entry e : result.fullResults ()){
      if(index==checkEntry1){
        log.debug (e.toString ());
        assertEquals (0.9947,
                      e.pValue (),
                      pValTolerance);
      }
      if(index==checkEntry2){
        log.debug (e.toString ());
        assertEquals (0.00966,
                      e.pValue (),
                      pValTolerance);

      }
      index++;
    }

  }
  
  
}

