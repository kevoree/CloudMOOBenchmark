package lu.snt.serval.CloudMOOBenchmark.genetic.mutators;

import jet.runtime.typeinfo.JetValueParameter;
import lu.snt.serval.CloudMOOBenchmark.genetic.ContextUtilities;
import lu.snt.serval.cloud.*;
import lu.snt.serval.cloud.impl.DefaultCloudFactory;
import org.jetbrains.annotations.NotNull;
import org.kevoree.modeling.optimization.api.mutation.MutationOperator;
import org.kevoree.modeling.optimization.api.mutation.MutationParameters;
import org.kevoree.modeling.optimization.api.mutation.MutationVariable;
import org.kevoree.modeling.optimization.api.mutation.QueryVar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * User: assaad.moawad
 * Date: 2/3/14
 * Time: ${Time}
 * University of Luxembourg - Snt
 * assaad.mouawad@gmail.com
 */
public class AssignLoadToVm implements MutationOperator<Cloud> {
    private static DefaultCloudFactory dcf = new DefaultCloudFactory();
    private static Random random=new Random();

    @NotNull
    @Override
    public List<MutationVariable> enumerateVariables(@JetValueParameter(name = "model") @NotNull Cloud containerRoot) {
        return Arrays.asList((MutationVariable) new QueryVar("target", "*"));
    }

    @NotNull
    @Override
    public void mutate(@JetValueParameter(name = "model") @NotNull Cloud model, @JetValueParameter(name = "params") @NotNull MutationParameters mutationParameters) {
        try
        {
            /*System.out.println("Inside assign");
            if(model.getVmInstances().size()==0){
                System.out.println("No VM");
                return;}

            System.out.println("There is VM");*/

            VmInstance vmI= model.getVmInstances().get(random.nextInt(model.getVmInstances().size()));
            ResourceMetric rm = ContextUtilities.getAvailableResource(vmI);

            ArrayList<SoftwareThread> possible =ContextUtilities.getUnassignedThreads(model,rm);
           // System.out.println("Possible threads to add:"+ possible.size());

            if(possible.size()==0)
                return;

            SoftwareThread st = possible.get(random.nextInt(possible.size()));
            vmI.addThreads(st);



        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());

        }
    }
}