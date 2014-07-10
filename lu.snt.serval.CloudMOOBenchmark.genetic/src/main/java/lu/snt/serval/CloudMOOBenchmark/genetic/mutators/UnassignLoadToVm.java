package lu.snt.serval.CloudMOOBenchmark.genetic.mutators;

import jet.runtime.typeinfo.JetValueParameter;
import lu.snt.serval.cloud.Cloud;
import lu.snt.serval.cloud.SoftwareThread;
import lu.snt.serval.cloud.VmInstance;
import lu.snt.serval.cloud.impl.DefaultCloudFactory;
import org.jetbrains.annotations.NotNull;
import org.kevoree.modeling.optimization.api.mutation.MutationOperator;
import org.kevoree.modeling.optimization.api.mutation.MutationParameters;
import org.kevoree.modeling.optimization.api.mutation.MutationVariable;
import org.kevoree.modeling.optimization.api.mutation.QueryVar;

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
public class UnassignLoadToVm implements MutationOperator<Cloud> {
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
            if(model.getVmInstances().size()==0)
                return;

            VmInstance vmI= model.getVmInstances().get(random.nextInt(model.getVmInstances().size()));
            if(vmI.getThreads().size()==0)
                return;
            SoftwareThread softwareThread = vmI.getThreads().get(random.nextInt(vmI.getThreads().size()));
            vmI.removeThreads(softwareThread);
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());

        }
    }
}