package lu.snt.serval.CloudMOOBenchmark.genetic.mutators;

import jet.runtime.typeinfo.JetValueParameter;
import lu.snt.serval.CloudMOOBenchmark.genetic.ContextUtilities;
import lu.snt.serval.cloud.Cloud;
import lu.snt.serval.cloud.LoadBalancer;
import lu.snt.serval.cloud.SoftwareThread;
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
public class DivideLoad implements MutationOperator<Cloud> {
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
            LoadBalancer lb= model.getLoadBalancers().get(random.nextInt(model.getLoadBalancers().size()));
            SoftwareThread st = lb.getSoftwareThreads().get(random.nextInt(lb.getSoftwareThreads().size()));
            int current = st.getUsers();
            int next1 = (random.nextInt(current*2)+current)/4;
            int next2 =current-next1;
            SoftwareThread st1 = ContextUtilities.createSoftwareThread(st.getSoftwareName(),next1,model);
            SoftwareThread st2 = ContextUtilities.createSoftwareThread(st.getSoftwareName(),next2,model);
            ContextUtilities.terminateSoftwareThread(st,model);
            lb.removeSoftwareThreads(st);
            lb.addSoftwareThreads(st1);
            lb.addSoftwareThreads(st2);

        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());

        }
    }
}