package lu.snt.serval.CloudMOOBenchmark.genetic;

import lu.snt.serval.cloud.Cloud;
import lu.snt.serval.cloud.LoadBalancer;
import lu.snt.serval.cloud.cloner.DefaultModelCloner;
import lu.snt.serval.cloud.compare.DefaultModelCompare;
import lu.snt.serval.cloud.impl.DefaultCloudFactory;
import lu.snt.serval.cloudcontext.SoftwareToRun;
import org.jetbrains.annotations.NotNull;
import org.kevoree.modeling.api.ModelCloner;
import org.kevoree.modeling.api.compare.ModelCompare;
import org.kevoree.modeling.optimization.api.PopulationFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ace Shooting on 7/8/2014.
 */
public class CloudPopulationFactory implements PopulationFactory<Cloud> {



    private DefaultCloudFactory cloudfactory = new DefaultCloudFactory();

    private Integer size = 5;

    public CloudPopulationFactory setSize(Integer nSize) {
        size = nSize;
        return this;
    }


    @NotNull
    @Override
    public List<Cloud> createPopulation() {
        ArrayList<Cloud> populations = new ArrayList<Cloud>();
        for (int i = 0; i < size; i++) {
            Cloud cloud = cloudfactory.createCloud();
            for(SoftwareToRun str: ContextUtilities.cloudContext.getSoftwarestoRun()){
                LoadBalancer lb = cloudfactory.createLoadBalancer();
                cloud.addLoadBalancers(lb);
                lb.setSoftwareToRunName(str.getSoftware().getName());
                lb.setUsers(str.getUsers());
                // We have to create reccursively the software threads and their dependencies.
                lb.addSoftwareThreads(ContextUtilities.createSoftwareThread(str.getSoftware().getName(),str.getUsers()));
            }
            populations.add(cloud);

        }
        return populations;

    }



    @NotNull
    @Override
    public ModelCloner getCloner() {
        return new DefaultModelCloner();
    }

    @NotNull
    @Override
    public ModelCompare getModelCompare() {
        return new DefaultModelCompare();
    }
}
