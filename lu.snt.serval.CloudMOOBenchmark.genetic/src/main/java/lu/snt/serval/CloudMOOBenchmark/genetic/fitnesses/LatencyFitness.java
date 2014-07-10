package lu.snt.serval.CloudMOOBenchmark.genetic.fitnesses;


import lu.snt.serval.CloudMOOBenchmark.genetic.ContextUtilities;
import lu.snt.serval.cloud.Cloud;
import lu.snt.serval.cloud.LoadBalancer;
import lu.snt.serval.cloud.SoftwareThread;
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
public class LatencyFitness extends FitnessFunction<Cloud> {

    @Override
    public double evaluate(Cloud model, GenerationContext<Cloud> context) {
        double total=0;
        double count=0;
        for(LoadBalancer lb: model.getLoadBalancers()){
            for(SoftwareThread softwareThread: lb.getSoftwareThreads()){
                count++;
                total+= ContextUtilities.calculateLatency(softwareThread);
            }
        }
       if(count!=0)
           total=total/count;

        return total;
    }

    @Override
    public double max() {
        return ContextUtilities.INFINITLATENCY;
    }

    @Override
    public double min() {
        return 0;
    }


    public FitnessOrientation orientation() {
        return FitnessOrientation.MINIMIZE;
    }
}