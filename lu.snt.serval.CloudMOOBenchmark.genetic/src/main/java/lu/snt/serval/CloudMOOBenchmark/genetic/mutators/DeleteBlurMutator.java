package lu.snt.serval.pla.genetic.mutators;

import jet.runtime.typeinfo.JetValueParameter;
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
public class DeleteBlurMutator extends DomainConfiguration implements MutationOperator<ContainerRoot> {

    @NotNull
    @Override
    public List<MutationVariable> enumerateVariables(@JetValueParameter(name = "model") @NotNull ContainerRoot containerRoot) {
        return Arrays.asList((MutationVariable) new QueryVar("target", "*"));
    }

    @NotNull
    @Override
    public void mutate(@JetValueParameter(name = "model") @NotNull ContainerRoot model, @JetValueParameter(name = "params") @NotNull MutationParameters mutationParameters) {
        try
        {
            ArrayList<ComponentInstance> ls = getSensors(model);
            ComponentInstance sensor= ls.get(random.nextInt(ls.size()));
            ArrayList<ComponentInstance> already= getAttachedBlurComp(model, sensor);
            if(already.size()>0)
            {
                ComponentInstance toRemove =already.get(random.nextInt(already.size()));
                Channel before=toRemove.getProvided().get(0).getBindings().get(0).getHub();
                Channel after=toRemove.getRequired().get(0).getBindings().get(0).getHub();

                ComponentInstance next=toRemove;

                for(MBinding c:after.getBindings())
                {
                    Port d = c.getPort();
                    next= (ComponentInstance)  d.eContainer();
                    if(next==toRemove)
                        continue;
                    else
                        break;
                }
                KevScriptEngine engine = new KevScriptEngine();

                String command = "unbind node0."+toRemove.getName()+"."+toRemove.getProvided().get(0).getPortTypeRef().getName() +" "+before.getName();
                engine.execute(command,model);
                command = "unbind node0."+toRemove.getName()+"."+toRemove.getRequired().get(0).getPortTypeRef().getName() +" "+after.getName();
                engine.execute(command,model);
                command = "remove node0." +toRemove.getName();
                engine.execute(command,model);
                command = "unbind node0."+next.getName()+"."+next.getProvided().get(0).getPortTypeRef().getName() +" "+after.getName();
                engine.execute(command,model);
                command = "bind node0."+next.getName()+"."+next.getProvided().get(0).getPortTypeRef().getName() +" "+before.getName();
                engine.execute(command,model);

            }
        }
        catch (Exception ex)
        {
            Log.debug(ex.getMessage());
            System.out.println(ex.getMessage());

        }
    }
}