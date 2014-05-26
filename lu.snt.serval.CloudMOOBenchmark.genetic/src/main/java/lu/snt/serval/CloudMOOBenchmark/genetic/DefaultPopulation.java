package lu.snt.serval.pla.genetic;

import org.kevoree.ContainerRoot;
import org.kevoree.cloner.DefaultModelCloner;
import org.kevoree.compare.DefaultModelCompare;
import org.kevoree.loader.JSONModelLoader;
import org.kevoree.modeling.api.ModelCloner;
import org.kevoree.modeling.api.compare.ModelCompare;
import org.kevoree.modeling.optimization.api.PopulationFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * User: assaad.moawad
 * Date: 2/7/14
 * Time: ${Time}
 * University of Luxembourg - Snt
 * assaad.mouawad@gmail.com
 */
public class DefaultPopulation extends DomainConfiguration  implements PopulationFactory<ContainerRoot> {


    private Integer size=4;
    public static int MAX=12;

    public DefaultPopulation setSize(Integer nSize) {
        size = nSize;
        return this;
    }

    @Override
    public List<ContainerRoot> createPopulation() {
        ArrayList<ContainerRoot> populations =  new ArrayList<ContainerRoot>();
        ContainerRoot cr;

        for (int i = 0; i < size; i++) {
            JSONModelLoader loader = new JSONModelLoader();
            cr= (ContainerRoot) loader.loadModelFromStream(DefaultPopulation.class.getClassLoader().getResourceAsStream("kevInit.json")).get(0);
            populations.add(cr);
        }
        return populations;
    }

    @Override
    public ModelCloner getCloner() {
        return new DefaultModelCloner();
    }

    @Override
    public ModelCompare getModelCompare() {
        return new DefaultModelCompare();
    }
}
