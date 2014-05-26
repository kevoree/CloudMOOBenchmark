import lu.snt.serval.pla.*;
import lu.snt.serval.pla.genetic.DomainConfiguration;
import lu.snt.serval.pla.genetic.RiskCalculation;
import lu.snt.serval.pla.impl.DefaultPlaFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by assaad.moawad on 1/21/14.
 */
public class AbstractTest {

    private static DefaultPlaFactory factory = new DefaultPlaFactory();

    private static Domain domain;

    public static void init()
    {
        domain = factory.createDomain();
        domain.setName("Abstract Test");


        ///Set up possible sensor
        Blurring blrTrim = factory.createBlurring();
        blrTrim.setName("Trim");
        domain.addBlurrings(blrTrim);

        Blurring blrThresholdLower= factory.createBlurring();
        blrThresholdLower.setName("ThresholdLower");
        domain.addBlurrings(blrThresholdLower);

        Blurring blrThresholdGreater= factory.createBlurring();
        blrThresholdGreater.setName("ThresholdGreater");
        domain.addBlurrings(blrThresholdGreater);

        Blurring blrThresholdNoise= factory.createBlurring();
        blrThresholdNoise.setName("ThresholdNoise");
        domain.addBlurrings(blrThresholdNoise);

        Blurring blrFreqReducer= factory.createBlurring();
        blrFreqReducer.setName("FreqReducer");
        domain.addBlurrings(blrFreqReducer);

        Blurring blrAveraging= factory.createBlurring();
        blrAveraging.setName("Averaging");
        domain.addBlurrings(blrAveraging);


        //Setup Sensor A
        SensorKind sensorA = factory.createSensorKind();
        sensorA.setId("sensorA");
        sensorA.setType("sensorA");
        domain.addSensors(sensorA);

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
        sensorA.addRisks(risk1);

        Risk risk2=factory.createRisk();
        risk2.setId("risk2");
        risk2.setDescription("Risk 2");
        risk2.setWeight(1);
        sensorA.addRisks(risk2);

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
    }

    public static void main(String [] args) {
        init();
        DomainConfiguration.setDomain(domain);

        List<Blurring> lb=new ArrayList<Blurring>();

       Blurring bc = factory.createBlurring();
        bc.setName("Trim");
        bc.setParamName("digit");
        bc.setParamValue(5.0);
        lb.add(bc);

        double risk = RiskCalculation.calculateRiskOnSensor("sensorA",lb);
        System.out.println(risk);


    }
}
