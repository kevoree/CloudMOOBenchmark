package lu.snt.serval.pla.genetic.fitnesses;

import jet.runtime.typeinfo.JetValueParameter;
import lu.snt.serval.pla.Blurring;
import lu.snt.serval.pla.genetic.DomainConfiguration;
import org.jetbrains.annotations.NotNull;
import org.kevoree.ComponentInstance;
import org.kevoree.ContainerRoot;
import org.kevoree.modeling.optimization.api.GenerationContext;
import org.kevoree.modeling.optimization.api.fitness.FitnessFunction;
import org.kevoree.modeling.optimization.api.fitness.FitnessOrientation;

import java.util.ArrayList;

/**
 * User: assaad.moawad
 * Date: 2/17/14
 * Time: ${Time}
 * University of Luxembourg - Snt
 * assaad.mouawad@gmail.com
 */
public class UtilFitness extends DomainConfiguration implements FitnessFunction<ContainerRoot> {

    @Override
    public double evaluate(@JetValueParameter(name = "model") @NotNull ContainerRoot model, @JetValueParameter(name = "context") @NotNull GenerationContext<ContainerRoot> containerRootGenerationContext) {
        ArrayList<ComponentInstance> ls = getSensors(model);
        double localmax=1;
        double cur=1;
        for(ComponentInstance sensor: ls)
        {
            cur=1;
            ArrayList<ComponentInstance> bl = getAttachedBlurComp(model,sensor);
            for(ComponentInstance blur:bl)
            {
                String name = blur.getTypeDefinition().getName();
                double value = Double.parseDouble(blur.getDictionary().findValuesByID("value").getValue());
                cur=cur*getUtil(name, value);
            }
            if(cur<localmax)
                localmax=cur;
        }
        return localmax;
    }

    private double getUtil(String name, double value) {
        for(Blurring b: domain.getBlurrings())
        {
            if(b.getName().equals(name))
            {
                double aa= (b.getUtilMax()-b.getUtilMin())/(b.getParamMax()-b.getParamMin());
                double bb= b.getUtilMax()-aa*b.getParamMax();
                double util = aa*value+bb;
                return util;

            }
        }
        return 1;

    }

    @Override
    public double max() {
        return 1;
    }

    @Override
    public double min() {
        return 0;
    }

    @NotNull
    @Override
    public FitnessOrientation orientation() {
        return FitnessOrientation.MAXIMIZE;
    }
}