package lu.snt.serval.pla.genetic.mutators;

import jet.runtime.typeinfo.JetValueParameter;
import lu.snt.serval.pla.genetic.DomainConfiguration;
import org.jetbrains.annotations.NotNull;
import org.kevoree.ComponentInstance;
import org.kevoree.ContainerRoot;
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
public class ChangeBlurSettingMutator  extends DomainConfiguration implements MutationOperator<ContainerRoot> {

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
                ComponentInstance toChange =already.get(random.nextInt(already.size()));
                double min = Double.parseDouble(toChange.getDictionary().findValuesByID("min").getValue());
                double max= Double.parseDouble(toChange.getDictionary().findValuesByID("max").getValue());
                boolean isDouble = toChange.getDictionary().findValuesByID("isDouble").getValue().equals("true");
                double val=0;
                if(isDouble)
                    val=random.nextDouble()*(max-min)+min;
                else
                    val=random.nextInt((int)(max-min))+min;
                toChange.getDictionary().findValuesByID("value").setValue(Double.toString(val));
            }
        }
        catch (Exception ex)
        {
            Log.debug(ex.getMessage());
            System.out.println(ex.getMessage());
        }
    }
}