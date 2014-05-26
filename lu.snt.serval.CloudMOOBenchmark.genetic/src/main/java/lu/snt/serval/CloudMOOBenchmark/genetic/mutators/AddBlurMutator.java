package lu.snt.serval.pla.genetic.mutators;

import jet.runtime.typeinfo.JetValueParameter;
import lu.snt.serval.pla.Blurring;
import lu.snt.serval.pla.genetic.DomainConfiguration;
import org.jetbrains.annotations.NotNull;
import org.kevoree.*;
import org.kevoree.kevscript.KevScriptEngine;
import org.kevoree.log.Log;
import org.kevoree.modeling.optimization.api.mutation.MutationOperator;
import org.kevoree.modeling.optimization.api.mutation.MutationParameters;
import org.kevoree.modeling.optimization.api.mutation.MutationVariable;
import org.kevoree.modeling.optimization.api.mutation.QueryVar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: assaad.moawad
 * Date: 2/3/14
 * Time: ${Time}
 * University of Luxembourg - Snt
 * assaad.mouawad@gmail.com
 */
public class AddBlurMutator extends DomainConfiguration implements MutationOperator<ContainerRoot> {

    @NotNull
    @Override
    public List<MutationVariable> enumerateVariables(@JetValueParameter(name = "model") @NotNull ContainerRoot containerRoot) {
        return Arrays.asList((MutationVariable) new QueryVar("target", "*"));
    }

    @NotNull
    @Override
    public void mutate(@JetValueParameter(name = "model") @NotNull ContainerRoot model, @JetValueParameter(name = "params") @NotNull MutationParameters mutationParameters) {
        ArrayList<ComponentInstance> ls = getSensors(model);
        ComponentInstance sensor= ls.get(random.nextInt(ls.size()));
        ArrayList<Blurring> possible=getPossibleBlur(sensor.getDictionary().findValuesByID("id").getValue());

        ArrayList<String> already= getAttachedBlur(model,sensor);
        if(already.size()>0)
        {
            for(String s:already)
            {
                for(Blurring b: possible)
                {
                    if(b.getName().equals(s))
                    {
                        possible.remove(b);
                        break;
                    }
                }
            }
        }


        if(possible.size()>0)
        {
            Blurring toAdd = possible.get(random.nextInt(possible.size()));
            //System.out.println(toAdd.getName());
            KevScriptEngine engine = new KevScriptEngine();
            try
            {

                String compName= toAdd.getName().substring(0,8)+ random.nextInt(10000);
                String command = "add node0."+compName+" : "+toAdd.getName();
                engine.execute(command,model);

                command = "set node0."+compName+".min = \""+toAdd.getParamMin()+"\"";
                engine.execute(command,model);
                command = "set node0."+compName+".max = \""+toAdd.getParamMax()+"\"";
                engine.execute(command,model);
                command = "set node0."+compName+".value = \""+toAdd.getParamValue()+"\"";
                engine.execute(command,model);
                command = "set node0."+compName+".paramName = \""+toAdd.getParamName()+"\"";
                engine.execute(command,model);

                if(toAdd.getIsDouble())
                    command = "set node0."+compName+".isDouble = \"true\"";
                else
                    command = "set node0."+compName+".isDouble = \"false\"";
                engine.execute(command,model);
                command = "set node0."+compName+".started = \"true\"";
                engine.execute(command,model);

                Channel channel = sensor.getRequired().get(0).getBindings().get(0).getHub();
                ComponentInstance next=sensor;

                for(MBinding c:channel.getBindings())
                {
                    Port d = c.getPort();
                    next= (ComponentInstance)  d.eContainer();
                    if(next==sensor)
                        continue;
                    else
                        break;
                }
                command = "unbind  node0."+ next.getName()+"."+next.getProvided().get(0).getPortTypeRef().getName() +" " +channel.getName();
                engine.execute(command,model);

                command = "bind node0."+ compName+".sensorIn "  +channel.getName();
                engine.execute(command,model);

                String channel1;
                channel1="AsyncBroa"+random.nextInt(10000);

                command = "add "+channel1 +" : " +"AsyncBroadcast";
                engine.execute(command,model);
                command = "set "+channel1 +".started = \"true\"";
                engine.execute(command,model);

                command = "bind node0."+ compName+".blurringOut "  +channel1;
                engine.execute(command,model);

                command = "bind node0."+ next.getName()+"."+next.getProvided().get(0).getPortTypeRef().getName() +" "  +channel1;
                engine.execute(command,model);
                //System.out.println("done");

            }
            catch (Exception ex)
            {
                Log.debug(ex.getMessage());
                System.out.println(ex.getMessage());
            }
        }
    }
}
