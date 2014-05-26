package lu.snt.serval.pla.genetic;

import lu.snt.serval.pla.*;
import lu.snt.serval.pla.impl.DefaultPlaFactory;
import org.kevoree.*;

import java.util.ArrayList;
import java.util.Random;

/**
 * User: assaad.moawad
 * Date: 2/10/14
 * Time: ${Time}
 * University of Luxembourg - Snt
 * assaad.mouawad@gmail.com
 */
public class DomainConfiguration {
    protected static DefaultPlaFactory factory = new DefaultPlaFactory();
    protected static Domain domain;
    protected static Random random=new Random();
    protected static double tolerated = 0.35;




    public static ArrayList<Blurring> getPossibleBlur(String sensorID)
    {
        ArrayList<Blurring> blurs = new ArrayList<Blurring>();
        SensorKind sensor= domain.findSensorsByID(sensorID);
        try{
            for(Risk r:sensor.getRisks() )
            {
                for(CounterMeasure c: r.getCounterMeasures())
                {
                    blurs.add(c.getBlurring());
                }
            }
        }
        catch (Exception ex)
        {

        }
        return blurs;
    }

    public static ArrayList<ComponentInstance> getSensors(ContainerRoot model)
    {
        ContainerNode ncurrent =  model.getNodes().get(0);
        ArrayList<ComponentInstance> sensors = new ArrayList<ComponentInstance>();

        for(ComponentInstance c:ncurrent.getComponents())
        {
            if(c.getTypeDefinition().getName().toString().equals("Sensor"))
                sensors.add(c);
        }
        return sensors;
    }

    public static ArrayList<String> getAttachedBlur(ContainerRoot model, ComponentInstance sensor)
    {
        ArrayList<String> res= new ArrayList<String>();
        ComponentInstance init;
        ComponentInstance next=sensor;

        do{
            init=next;

            Channel tempChanel=init.getRequired().get(0).getBindings().get(0).getHub();
            for(MBinding c:tempChanel.getBindings())
            {
                Port d = c.getPort();
                next= (ComponentInstance)  d.eContainer();
                if(next==init)
                    continue;
                else
                {
                    if(next.getTypeDefinition().getName().toString().equals("SensorGateway"))
                    {
                        break;
                    }
                   res.add(next.getTypeDefinition().getName());
                    break;

                }
            }

        } while (next.getTypeDefinition().getName().toString().equals("SensorGateway")==false);

        return res;

    }

    public static ArrayList<ComponentInstance> getAttachedBlurComp(ContainerRoot model, ComponentInstance sensor)
    {
        ArrayList<ComponentInstance> res= new ArrayList<ComponentInstance>();
        ComponentInstance init;
        ComponentInstance next=sensor;

        do{
            init=next;

            Channel tempChanel=init.getRequired().get(0).getBindings().get(0).getHub();
            for(MBinding c:tempChanel.getBindings())
            {
                Port d = c.getPort();
                next= (ComponentInstance)  d.eContainer();
                if(next==init)
                    continue;
                else
                {
                    if(next.getTypeDefinition().getName().toString().equals("SensorGateway"))
                    {
                        break;
                    }
                    res.add(next);
                    break;

                }
            }

        } while (next.getTypeDefinition().getName().toString().equals("SensorGateway")==false);

        return res;

    }


    public static Architecture getArchitecture(ContainerRoot model)
    {
        Architecture arc = factory.createArchitecture();

        // System.out.println(sensors.size());
        ArrayList<ComponentInstance> sensors =getSensors(model);



        for(ComponentInstance sensor: sensors)
        {

            ComponentInstance init;
            ComponentInstance next=sensor;
            SensorKind sens=domain.findSensorsByID(sensor.getDictionary().findValuesByID("id").getValue());
            Chain chain = factory.createChain();
            chain.setSensor(sens);

            do{
                init=next;

                Channel tempChanel=init.getRequired().get(0).getBindings().get(0).getHub();
                for(MBinding c:tempChanel.getBindings())
                {
                    Port d = c.getPort();
                    next= (ComponentInstance)  d.eContainer();
                    if(next==init)
                        continue;
                    else
                    {
                        if(next.getTypeDefinition().getName().toString().equals("SensorGateway"))
                        {
                            break;
                        }
                        Blurring b = factory.createBlurring();
                        b.setName(next.getTypeDefinition().getName());
                        b.setParamName(next.getDictionary().findValuesByID("paramName").getValue());
                        b.setParamMin(Double.parseDouble((next.getDictionary().findValuesByID("min").getValue())));
                        b.setParamMax(Double.parseDouble((next.getDictionary().findValuesByID("max").getValue())));
                        b.setParamValue(Double.parseDouble((next.getDictionary().findValuesByID("value").getValue())));
                        b.setIsDouble(next.getDictionary().findValuesByID("isDouble").getValue().toString().equals("true"));
                        chain.addBlurringList(b);
                        break;

                    }
                }

            } while (next.getTypeDefinition().getName().toString().equals("SensorGateway")==false);

            arc.addChains(chain);

        }
        return arc;
    }


    public static void setDomain(Domain domain)
    {
        DomainConfiguration.domain=domain;
    }
}
