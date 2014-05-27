package lu.snt.serval.CloudMOOBenchmark.genetic.fitnesses;


import lu.snt.serval.cloud.Cloud;
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
public class UtilFitness  extends FitnessFunction<Cloud> {

    @Override
    public double evaluate(Cloud model, GenerationContext<Cloud> context) {
        return 0;
    }

    @Override
    public double max() {
        return 1;
    }

    @Override
    public double min() {
        return 0;
    }


    public FitnessOrientation orientation() {
        return FitnessOrientation.MAXIMIZE;
    }
}