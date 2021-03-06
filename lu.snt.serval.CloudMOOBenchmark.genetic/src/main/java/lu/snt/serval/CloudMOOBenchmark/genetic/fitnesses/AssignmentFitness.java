package lu.snt.serval.CloudMOOBenchmark.genetic.fitnesses;


import lu.snt.serval.CloudMOOBenchmark.genetic.ContextUtilities;
import lu.snt.serval.cloud.Cloud;
import lu.snt.serval.cloud.SoftwareThread;
import org.kevoree.modeling.optimization.api.GenerationContext;
import org.kevoree.modeling.optimization.api.fitness.FitnessFunction;
import org.kevoree.modeling.optimization.api.fitness.FitnessOrientation;

import java.util.ArrayList;
import java.util.List;

/**
 * User: assaad.moawad
 * Date: 2/17/14
 * Time: ${Time}
 * University of Luxembourg - Snt
 * assaad.mouawad@gmail.com
 */
public class AssignmentFitness implements FitnessFunction<Cloud> {

    @Override
    public double evaluate(Cloud model, GenerationContext<Cloud> context) {
        List<SoftwareThread> allThreads = ContextUtilities.getSoftwareThreads(model);
       // System.out.println("size: "+allThreads.size());
        ArrayList<SoftwareThread> unassigned = ContextUtilities.getUnassignedThreads(model,null);

        int total=0;
        for(SoftwareThread st: allThreads){
            total+=st.getUsers();
        }

        int notAssigned=0;
        for(SoftwareThread st: unassigned){
            notAssigned+=st.getUsers();
        }

        double answer= (total-notAssigned);
        answer= answer/total;
        //System.out.println(answer);
        return answer;
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