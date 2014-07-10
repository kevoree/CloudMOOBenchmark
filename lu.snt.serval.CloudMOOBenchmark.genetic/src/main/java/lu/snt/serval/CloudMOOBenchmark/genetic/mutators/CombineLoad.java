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
public class CombineLoad implements MutationOperator<Cloud> {
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
            ArrayList<LoadBalancer> loads = new ArrayList<LoadBalancer>();
            for(LoadBalancer lb: model.getLoadBalancers()){
                if(lb.getSoftwareThreads().size()>1)
                    loads.add(lb);
            }

            if(loads.size()==0)
                return;
            LoadBalancer lb= loads.get(random.nextInt(loads.size()));
            int[] array = new int[lb.getSoftwareThreads().size()];
            for(int j=0;j<array.length;j++)
                array[j]=j;

            ContextUtilities.shuffleArray(array);

            SoftwareThread st1 = lb.getSoftwareThreads().get(array[0]);
            SoftwareThread st2 = lb.getSoftwareThreads().get(array[1]);
            int next=st1.getUsers()+st2.getUsers();
            SoftwareThread st = ContextUtilities.createSoftwareThread(st1.getSoftwareName(),next,model);
            ContextUtilities.terminateSoftwareThread(st1,model);
            ContextUtilities.terminateSoftwareThread(st2,model);

            lb.removeSoftwareThreads(st1);
            lb.removeSoftwareThreads(st2);
            lb.addSoftwareThreads(st);

            //Redistribute here

        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());

        }
    }
}