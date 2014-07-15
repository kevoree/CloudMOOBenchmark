package lu.snt.serval.CloudMOOBenchmark.genetic.fitnesses;


import lu.snt.serval.cloud.Cloud;
import lu.snt.serval.cloud.LoadBalancer;
import org.kevoree.modeling.optimization.api.GenerationContext;
import org.kevoree.modeling.optimization.api.fitness.FitnessFunction;
import org.kevoree.modeling.optimization.api.fitness.FitnessOrientation;

/**
 * User: assaad.moawad
 * Date: 2/17/14
 * Time: ${Time}
 * University of Luxembourg - Snt
 * assaad.mouawad@gmail.com
 */
public class RedunduncyFitness implements FitnessFunction<Cloud> {

    @Override
    public double evaluate(Cloud model, GenerationContext<Cloud> context) {
        double total=0;
        for(LoadBalancer lb: model.getLoadBalancers()){
            total+=lb.getSoftwareThreads().size();
        }
        if(total!=0)
            total=(total-model.getLoadBalancers().size())/total;
        else
            total=0;


        return total;
    }

   /* @Override
    public double max() {
        return 1;
    }

    @Override
    public double min() {
        return 0;
    }


    public FitnessOrientation orientation() {
        return FitnessOrientation.MAXIMIZE;
    }*/
}