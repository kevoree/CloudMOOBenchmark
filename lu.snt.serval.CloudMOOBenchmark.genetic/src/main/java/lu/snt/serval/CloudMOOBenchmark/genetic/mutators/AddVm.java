package lu.snt.serval.CloudMOOBenchmark.genetic.mutators;

import jet.runtime.typeinfo.JetValueParameter;
import lu.snt.serval.CloudMOOBenchmark.genetic.ContextUtilities;
import lu.snt.serval.cloud.Cloud;
import lu.snt.serval.cloud.ResourceMetric;
import lu.snt.serval.cloud.VmInstance;
import lu.snt.serval.cloud.impl.DefaultCloudFactory;
import lu.snt.serval.cloudcontext.VirtualMachine;
import org.jetbrains.annotations.NotNull;
import org.kevoree.modeling.optimization.api.mutation.MutationOperator;
import org.kevoree.modeling.optimization.api.mutation.MutationParameters;
import org.kevoree.modeling.optimization.api.mutation.MutationVariable;
import org.kevoree.modeling.optimization.api.mutation.QueryVar;

import java.util.Arrays;
import java.util.List;

/**
 * User: assaad.moawad
 * Date: 2/3/14
 * Time: ${Time}
 * University of Luxembourg - Snt
 * assaad.mouawad@gmail.com
 */
public class AddVm implements MutationOperator<Cloud> {
    private static DefaultCloudFactory dcf = new DefaultCloudFactory();

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
           VirtualMachine vm= ContextUtilities.getRandomMachine();
            VmInstance vmInstance= dcf.createVmInstance();
            vmInstance.setVirtualMachineName(vm.getName());
            vmInstance.setCloudProviderName(vm.getCloudProvider().getName());
            vmInstance.setPrice(vm.getPrice());
            ResourceMetric ressourceMetric = dcf.createResourceMetric();
            ressourceMetric.setCpu(vm.getCpu());
            ressourceMetric.setDisk(vm.getDisk());
            ressourceMetric.setRam(vm.getRam());
            ressourceMetric.setNetwork(vm.getNetwork());
            model.addResources(ressourceMetric);
            vmInstance.setResource(ressourceMetric);

            model.addVmInstances(vmInstance);
            //Redistribution here of tasks
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());

        }
    }
}