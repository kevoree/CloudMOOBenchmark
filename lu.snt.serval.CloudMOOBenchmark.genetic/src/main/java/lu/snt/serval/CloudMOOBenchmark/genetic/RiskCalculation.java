package lu.snt.serval.pla.genetic;

import lu.snt.serval.pla.*;

import java.util.Iterator;
import java.util.List;

/**
 * User: assaad.moawad
 * Date: 20/01/14
 * Time: 12:39
 * University of Luxembourg - Snt
 * assaad.mouawad@gmail.com
 */
public class RiskCalculation extends DomainConfiguration {


    private static double calculateImpact1cm(RiskReductionProfile setting, Blurring blurring)
    {
        if(blurring.getParamName().equals(setting.getParamName()))
        {
            if(setting.getParamValue0()<setting.getParamValue1())
            {
                if(blurring.getParamValue()<setting.getParamValue0())
                    return setting.getImpact0();
                if(blurring.getParamValue()>setting.getParamValue1())
                    return setting.getImpact1();
            }
            else
            {
                if(blurring.getParamValue()>setting.getParamValue0())
                    return setting.getImpact0();
                if(blurring.getParamValue()<setting.getParamValue1())
                    return setting.getImpact1();
            }
            double x0=setting.getParamValue0();
            double x1= setting.getParamValue1();
            double y0=setting.getImpact0();
            double y1=setting.getImpact1();

            if(setting.getProfile()==Profile.LINEAR)
            {
                double a=(y0-y1)/(x0-x1);
                double b=y0-a*x0;
                double res=a*blurring.getParamValue()+b;
                return  res;
            }
            else if  (setting.getProfile()==Profile.EXPONENTIAL)
            {

            }
        }
        else
            return 0;

     return 0;
    }


    private static SensorKind getSensorById(String sensorId)
    {
        if(domain.getSensors().size()==0)
            return null;

        Iterator iter = domain.getSensors().iterator();
        while (iter.hasNext())
        {
            SensorKind sk= (SensorKind) iter.next();
            if(sk.getId().equals(sensorId))
                return sk;
        }
        return null;
    }


    private  static Blurring findBlurringByName( List<Blurring> blrList,  String name)
    {
      Iterator blurIterator = blrList.iterator();
        while (blurIterator.hasNext())
        {
            Blurring b = (Blurring) blurIterator.next();
            if(b.getName().equals(name))
                return b;
        }
        return null;
    }



    private static double calculateRisk(Risk risk, List<Blurring> blrList)
    {
        List<CounterMeasure> lc = risk.getCounterMeasures();
        if(lc.size()==0|| blrList.size()==0)
            return 1;

        Iterator cmIterator= lc.iterator();
        Double minRisk=1.0;
        while (cmIterator.hasNext())
        {
            CounterMeasure cm= (CounterMeasure) cmIterator.next();
            Blurring b = findBlurringByName(blrList, cm.getBlurring().getName());
            if(b!=null)
            {
                 double current = 1- calculateImpact1cm(cm.getSetting(),b);
               minRisk = Math.min(minRisk,current);
            }
        }
        if(minRisk<0)
            minRisk=0.0;
        return minRisk;
    }

    public static double calculateRiskOnSensor(String sensorId, List<Blurring> blrList) throws Exception {
        SensorKind sensorKind = getSensorById(sensorId);
        if(sensorKind==null)
            throw new Exception("Sensor null");

        List<Risk> lr = sensorKind.getRisks();
        if(lr.size()==0)
            return 0;

        Iterator riskIterator = lr.iterator();
        int totalWeight=0;
        double finalrisk=0;
        while (riskIterator.hasNext())
        {
            Risk risk = (Risk) riskIterator.next();
            totalWeight+= risk.getWeight();
            finalrisk += risk.getWeight()*calculateRisk(risk, blrList);
        }
        finalrisk = finalrisk/totalWeight;

        return finalrisk;


    }

    public static double calculateRisk(Architecture current)
    {
        double risk=0;

        for(Chain c: current.getChains())
        {
            try {
                risk+= calculateRiskOnSensor(c.getSensor().getId(),c.getBlurringList());
                if(risk<0)
                    throw new Exception("Risk is negatif! for sensor "+ c.getSensor().getId());
            }
            catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }
        return risk/current.getChains().size();

    }
}
