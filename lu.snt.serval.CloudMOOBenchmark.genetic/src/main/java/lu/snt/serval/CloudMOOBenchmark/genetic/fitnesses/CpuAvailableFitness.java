package lu.snt.serval.CloudMOOBenchmark.genetic.fitnesses;


import lu.snt.serval.CloudMOOBenchmark.genetic.ContextUtilities;
import lu.snt.serval.cloud.Cloud;
import lu.snt.serval.cloud.ResourceMetric;
import lu.snt.serval.cloud.VmInstance;
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
public class CpuAvailableFitness implements FitnessFunction<Cloud> {

    @Override
    public double evaluate(Cloud model, GenerationContext<Cloud> context) {
        double total=0;
        for(VmInstance vmInstance: model.getVmInstances()){
            total+=vmInstance.getResource().getCpu();
        }

        double cpu=ContextUtilities.getAllSoftwareResources().getCpu();

        if(total>3*cpu)
            return 3;
        else
            return total/cpu;
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