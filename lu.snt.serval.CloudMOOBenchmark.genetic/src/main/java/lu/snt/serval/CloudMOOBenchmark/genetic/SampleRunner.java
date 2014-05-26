package lu.snt.serval.pla.genetic;

/**
 * User: assaad.moawad
 * Date: 2/7/14
 * Time: ${Time}
 * University of Luxembourg - Snt
 * assaad.mouawad@gmail.com
 */
import kotlin._Assertions;
import lu.snt.serval.pla.*;
import lu.snt.serval.pla.genetic.fitnesses.ExecutionTime;
import lu.snt.serval.pla.genetic.fitnesses.RiskFitness;
import lu.snt.serval.pla.genetic.fitnesses.UtilFitness;
import lu.snt.serval.pla.genetic.mutators.AddBlurMutator;
import lu.snt.serval.pla.genetic.mutators.ChangeBlurSettingMutator;
import lu.snt.serval.pla.genetic.mutators.DeleteBlurMutator;
import lu.snt.serval.pla.impl.DefaultPlaFactory;
import org.kevoree.ContainerRoot;
import org.kevoree.modeling.optimization.api.fitness.FitnessFunction;
import org.kevoree.modeling.optimization.api.metric.ParetoFitnessMetrics;
import org.kevoree.modeling.optimization.api.metric.ParetoMetrics;
import org.kevoree.modeling.optimization.api.solution.Solution;
import org.kevoree.modeling.optimization.engine.genetic.GeneticAlgorithm;
import org.kevoree.modeling.optimization.engine.genetic.GeneticEngine;
import org.kevoree.modeling.optimization.executionmodel.ExecutionModel;
import org.kevoree.modeling.optimization.util.ExecutionModelExporter;
import org.kevoree.modeling.optimization.web.Server;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: duke
 * Date: 07/08/13
 * Time: 16:00
 */
public class SampleRunner {


    public static Domain init()
    {
        DefaultPlaFactory factory = new DefaultPlaFactory();
        Domain domain;

        domain = factory.createDomain();
        domain.setName("AAL Test");


        ///Set up possible sensor
        Blurring blrTrim = factory.createBlurring();
        blrTrim.setName("CompTrim");
        blrTrim.setIsDouble(false);
        blrTrim.setParamMin(1.0);
        blrTrim.setParamMax(8.0);
        blrTrim.setParamValue(4.0);
        blrTrim.setParamName("digit");
        blrTrim.setExecTimeMin(500);
        blrTrim.setExecTimeMax(200);
        blrTrim.setUtilMin(0.5);
        blrTrim.setUtilMax(1.0);
        domain.addBlurrings(blrTrim);

        Blurring blrThresholdLower= factory.createBlurring();
        blrThresholdLower.setName("CompThresholdLower");
        blrThresholdLower.setIsDouble(true);
        blrThresholdLower.setParamMin(0.0);
        blrThresholdLower.setParamMax(30.0);
        blrThresholdLower.setParamValue(8.0);
        blrThresholdLower.setParamName("threshold");
        blrThresholdLower.setExecTimeMin(100);
        blrThresholdLower.setExecTimeMax(100);
        blrThresholdLower.setUtilMin(0.4);
        blrThresholdLower.setUtilMax(1.0);
        domain.addBlurrings(blrThresholdLower);

        Blurring blrThresholdGreater= factory.createBlurring();
        blrThresholdGreater.setName("CompThresholdGreater");
        blrThresholdGreater.setIsDouble(true);
        blrThresholdGreater.setParamMin(0.0);
        blrThresholdGreater.setParamMax(30.0);
        blrThresholdGreater.setParamValue(8.0);
        blrThresholdGreater.setParamName("threshold");
        blrThresholdGreater.setExecTimeMin(100);
        blrThresholdGreater.setExecTimeMax(100);
        blrThresholdGreater.setUtilMin(1.0);
        blrThresholdGreater.setUtilMax(0.3);
        domain.addBlurrings(blrThresholdGreater);

        Blurring blrThresholdNoise= factory.createBlurring();
        blrThresholdNoise.setName("CompThresholdNoise");
        blrThresholdNoise.setIsDouble(true);
        blrThresholdNoise.setParamMin(0.0);
        blrThresholdNoise.setParamMax(3.0);
        blrThresholdNoise.setParamValue(1.0);
        blrThresholdNoise.setParamName("variance");
        blrThresholdNoise.setExecTimeMin(500);
        blrThresholdNoise.setExecTimeMax(3000);
        blrThresholdNoise.setUtilMin(1.0);
        blrThresholdNoise.setUtilMax(0.4);
        domain.addBlurrings(blrThresholdNoise);

        Blurring blrFreqReducer= factory.createBlurring();
        blrFreqReducer.setName("CompFreqReducer");
        blrFreqReducer.setIsDouble(false);
        blrFreqReducer.setParamMin(1.0);
        blrFreqReducer.setParamMax(24 * 3600 * 1000.0);
        blrFreqReducer.setParamValue(15 * 60 * 1000.0);
        blrFreqReducer.setParamName("timewindow");
        blrFreqReducer.setExecTimeMin(100);
        blrFreqReducer.setExecTimeMax(3000);
        blrFreqReducer.setUtilMin(1.0);
        blrFreqReducer.setUtilMax(0.2);
        domain.addBlurrings(blrFreqReducer);

        Blurring blrAveraging= factory.createBlurring();
        blrAveraging.setName("CompAveraging");
        blrAveraging.setIsDouble(false);
        blrAveraging.setParamMin(1.0);
        blrAveraging.setParamMax(24 * 3600 * 1000.0);
        blrAveraging.setParamValue(15 * 60 * 1000.0);
        blrAveraging.setParamName("timewindow");
        blrAveraging.setExecTimeMin(100);
        blrAveraging.setExecTimeMax(3000);
        blrAveraging.setUtilMin(1.0);
        blrAveraging.setUtilMax(0.2);
        domain.addBlurrings(blrAveraging);


        //Setup Sensor A
        SensorKind sensorA = factory.createSensorKind();
        sensorA.setId("1");
        sensorA.setType("Temperature");
        domain.addSensors(sensorA);

        //Setup Sensor B
        SensorKind sensorB = factory.createSensorKind();
        sensorB.setId("2");
        sensorB.setType("Humidity");
        domain.addSensors(sensorB);

        //Setup Sensor A
        SensorKind sensorC = factory.createSensorKind();
        sensorC.setId("3");
        sensorC.setType("HeartRate");
        domain.addSensors(sensorC);

        //Setup 3 risks on Sensor A
        Risk risk0=factory.createRisk();
        risk0.setId("risk0");
        risk0.setDescription("Risk 0");
        risk0.setWeight(1);
        sensorA.addRisks(risk0);

        Risk risk1=factory.createRisk();
        risk1.setId("risk1");
        risk1.setDescription("Risk 1");
        risk1.setWeight(1);
        sensorB.addRisks(risk1);

        Risk risk2=factory.createRisk();
        risk2.setId("risk2");
        risk2.setDescription("Risk 2");
        risk2.setWeight(1);
        sensorC.addRisks(risk2);

        //Setup 3 countermeasures for risk 0
        CounterMeasure cm00 = factory.createCounterMeasure();
        cm00.setId("CM00");
        cm00.setDescription("Counter Measure 0 for risk 0");
        cm00.setBlurring(blrTrim);
        RiskReductionProfile setting00= factory.createRiskReductionProfile();
        setting00.setParamName("digit");
        setting00.setParamValue0(3.0);
        setting00.setImpact0(1.0);
        setting00.setParamValue1(5.0);
        setting00.setImpact1(0.000001);
        setting00.setProfile(Profile.LINEAR);
        cm00.setSetting(setting00);
        cm00.setRisk(risk0);

        CounterMeasure cm01 = factory.createCounterMeasure();
        cm01.setId("CM01");
        cm01.setDescription("Counter Measure 1 for risk 0");
        cm01.setBlurring(blrThresholdLower);
        RiskReductionProfile setting01= factory.createRiskReductionProfile();
        setting01.setParamName("threshold");
        setting01.setParamValue0(44.0);
        setting01.setImpact0(1.0);
        setting01.setParamValue1(90.0);
        setting01.setImpact1(0.0);
        setting01.setProfile(Profile.LINEAR);
        cm01.setSetting(setting01);
        cm01.setRisk(risk0);

        CounterMeasure cm02 = factory.createCounterMeasure();
        cm02.setId("CM02");
        cm02.setDescription("Counter Measure 2 for risk 0");
        cm02.setBlurring(blrAveraging);
        RiskReductionProfile setting02= factory.createRiskReductionProfile();
        setting02.setParamName("timewindow");
        setting02.setParamValue0(5000.0);
        setting02.setImpact0(1.0);
        setting02.setParamValue1(1.0);
        setting02.setImpact1(0.0);
        setting02.setProfile(Profile.LINEAR);
        cm02.setSetting(setting02);
        cm02.setRisk(risk0);

        //Setup 3 countermeasures for risk 1
        CounterMeasure cm10 = factory.createCounterMeasure();
        cm10.setId("CM10");
        cm10.setDescription("Counter Measure 0 for risk 1");
        cm10.setBlurring(blrTrim);
        RiskReductionProfile setting10= factory.createRiskReductionProfile();
        setting10.setParamName("digit");
        setting10.setParamValue0(2.0);
        setting10.setImpact0(1.0);
        setting10.setParamValue1(6.0);
        setting10.setImpact1(0.000001);
        setting10.setProfile(Profile.LINEAR);
        cm10.setSetting(setting10);
        cm10.setRisk(risk1);

        CounterMeasure cm11 = factory.createCounterMeasure();
        cm11.setId("CM11");
        cm11.setDescription("Counter Measure 1 for risk 1");
        cm11.setBlurring(blrThresholdLower);
        RiskReductionProfile setting11= factory.createRiskReductionProfile();
        setting11.setParamName("threshold");
        setting11.setParamValue0(55.0);
        setting11.setImpact0(1.0);
        setting11.setParamValue1(100.0);
        setting11.setImpact1(0.0);
        setting11.setProfile(Profile.LINEAR);
        cm11.setSetting(setting11);
        cm11.setRisk(risk1);

        CounterMeasure cm12 = factory.createCounterMeasure();
        cm12.setId("CM12");
        cm12.setDescription("Counter Measure 2 for risk 1");
        cm12.setBlurring(blrAveraging);
        RiskReductionProfile setting12= factory.createRiskReductionProfile();
        setting12.setParamName("timewindow");
        setting12.setParamValue0(10000.0);
        setting12.setImpact0(1.0);
        setting12.setParamValue1(1.0);
        setting12.setImpact1(0.0);
        setting12.setProfile(Profile.LINEAR);
        cm12.setSetting(setting12);
        cm12.setRisk(risk1);

        //Setup 3 countermeasures for risk 2
        CounterMeasure cm20 = factory.createCounterMeasure();
        cm20.setId("CM20");
        cm20.setDescription("Counter Measure 0 for risk 2");
        cm20.setBlurring(blrTrim);
        RiskReductionProfile setting20= factory.createRiskReductionProfile();
        setting20.setParamName("digit");
        setting20.setParamValue0(3.0);
        setting20.setImpact0(1.0);
        setting20.setParamValue1(8.0);
        setting20.setImpact1(0.000001);
        setting20.setProfile(Profile.LINEAR);
        cm20.setSetting(setting20);
        cm20.setRisk(risk2);

        CounterMeasure cm21 = factory.createCounterMeasure();
        cm21.setId("CM21");
        cm21.setDescription("Counter Measure 1 for risk 2");
        cm21.setBlurring(blrThresholdLower);
        RiskReductionProfile setting21= factory.createRiskReductionProfile();
        setting21.setParamName("threshold");
        setting21.setParamValue0(40.0);
        setting21.setImpact0(1.0);
        setting21.setParamValue1(60.0);
        setting21.setImpact1(0.0);
        setting21.setProfile(Profile.LINEAR);
        cm21.setSetting(setting21);
        cm21.setRisk(risk2);

        CounterMeasure cm22 = factory.createCounterMeasure();
        cm22.setId("CM22");
        cm22.setDescription("Counter Measure 2 for risk 2");
        cm22.setBlurring(blrAveraging);
        RiskReductionProfile setting22= factory.createRiskReductionProfile();
        setting22.setParamName("timewindow");
        setting22.setParamValue0(20000.0);
        setting22.setImpact0(1.0);
        setting22.setParamValue1(5000.0);
        setting22.setImpact1(0.0);
        setting22.setProfile(Profile.LINEAR);
        cm22.setSetting(setting22);
        cm22.setRisk(risk2);
        return domain;
    }



    public static void main(String[] args) throws Exception {
        DomainConfiguration.setDomain(SampleRunner.init());


        for(int i=0; i<100; i++)
        {
        GeneticEngine<ContainerRoot> engine = new GeneticEngine<ContainerRoot>();
        engine.addOperator(new AddBlurMutator());
        engine.addOperator(new ChangeBlurSettingMutator());
        engine.addOperator(new DeleteBlurMutator());

        engine.setAlgorithm(GeneticAlgorithm.EpsilonMOEA);


        engine.addFitnessFuntion(new UtilFitness());
        engine.addFitnessFuntion(new RiskFitness());
        engine.addFitnessFuntion(new ExecutionTime());
        engine.setMaxGeneration(2000)  ;
        engine.setPopulationFactory(new DefaultPopulation().setSize(20));

        //engine.addFitnessMetric(new UtilFitness(), ParetoFitnessMetrics.MEAN);
        /*engine.addFitnessMetric(new RiskFitness(), ParetoFitnessMetrics.MIN);
        engine.addFitnessMetric(new RiskFitness(), ParetoFitnessMetrics.MAX);
        engine.addFitnessMetric(new RiskFitness(), ParetoFitnessMetrics.MEAN);

        engine.addFitnessMetric(new UtilFitness(), ParetoFitnessMetrics.MIN);
        engine.addFitnessMetric(new UtilFitness(), ParetoFitnessMetrics.MAX);
        engine.addFitnessMetric(new UtilFitness(), ParetoFitnessMetrics.MEAN);

        engine.addFitnessMetric(new ExecutionTime(), ParetoFitnessMetrics.MIN);
        engine.addFitnessMetric(new ExecutionTime(), ParetoFitnessMetrics.MAX);
        engine.addFitnessMetric(new ExecutionTime(), ParetoFitnessMetrics.MEAN);*/
        //engine.addFitnessMetric(new ExecutionTime(), ParetoFitnessMetrics.MEAN);

        engine.addFitnessMetric(new RiskFitness(), ParetoFitnessMetrics.BEST);
        engine.addFitnessMetric(new UtilFitness(), ParetoFitnessMetrics.BEST);
        engine.addFitnessMetric(new ExecutionTime(), ParetoFitnessMetrics.BEST);
        engine.addParetoMetric(ParetoMetrics.HYPERVOLUME);

        long startTime = System.nanoTime();
        List<Solution<ContainerRoot>> result = engine.solve();
        long endTime = System.nanoTime();
        long duration = endTime - startTime;


        for (Solution sol : result) {
            Set af  = sol.getFitnesses();
            Iterator iter = af.iterator();
            while (iter.hasNext())
            {
                FitnessFunction tf= (FitnessFunction) iter.next();
                System.out.print(tf.getClass().getName()+" "+ sol.getScoreForFitness(tf)+" ");
            }
            System.out.println();
        }
        System.out.println("Duration: "+(double)duration / 1000000000.0+" seconds");

        ExecutionModel model = engine.getExecutionModel();
        ExecutionModelExporter.instance$.exportMetrics(model,new File("results"));
        }




    }



}
