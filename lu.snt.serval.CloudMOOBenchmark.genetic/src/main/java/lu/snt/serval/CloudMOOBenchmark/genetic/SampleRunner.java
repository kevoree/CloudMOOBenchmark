package lu.snt.serval.CloudMOOBenchmark.genetic;

/**
 * User: assaad.moawad
 * Date: 2/7/14
 * Time: ${Time}
 * University of Luxembourg - Snt
 * assaad.mouawad@gmail.com
 */
import kotlin._Assertions;
import lu.snt.serval.CloudMOOBenchmark.genetic.fitnesses.UtilFitness;
import lu.snt.serval.CloudMOOBenchmark.genetic.mutators.DeleteBlurMutator;
import lu.snt.serval.cloud.Cloud;
import org.kevoree.modeling.optimization.api.fitness.FitnessFunction;
import org.kevoree.modeling.optimization.api.metric.ParetoFitnessMetrics;
import org.kevoree.modeling.optimization.api.metric.ParetoMetrics;
import org.kevoree.modeling.optimization.api.solution.Solution;
import org.kevoree.modeling.optimization.engine.genetic.GeneticAlgorithm;
import org.kevoree.modeling.optimization.engine.genetic.GeneticEngine;
import org.kevoree.modeling.optimization.executionmodel.ExecutionModel;
import org.kevoree.modeling.optimization.util.ExecutionModelExporter;


import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: duke
 * Date: 07/08/13
 * Time: 16:00
 */
public class SampleRunner {


    public static void main(String[] args) throws Exception {

        for(int i=0; i<100; i++)
        {
        GeneticEngine<Cloud> engine = new GeneticEngine<Cloud>();

        engine.addOperator(new DeleteBlurMutator());

        engine.setAlgorithm(GeneticAlgorithm.EpsilonMOEA);


        engine.addFitnessFuntion(new UtilFitness());

        engine.setMaxGeneration(2000)  ;
      //  engine.setPopulationFactory(new DefaultPopulation().setSize(20));

        //engine.addFitnessMetric(new UtilFitness(), ParetoFitnessMetrics.MEAN);
        /*engine.addFitnessMetric(new RiskFitness(), ParetoFitnessMetrics.MIN);
        engine.addFitnessMetric(new RiskFitness(), ParetoFitnessMetrics.MAX);
        engine.addFitnessMetric(new RiskFitness(), ParetoFitnessMetrics.MEAN);

        engine.addFitnessMetric(new UtilFitness(), ParetoFitnessMetrics.MIN);
        engine.addFitnessMetric(new UtilFitness(), ParetoFitnessMetrics.MAX);
        engine.addFitnessMetric(new UtilFitness(), ParetoFitnessMetrics.MEAN);

        engine.addFitnessMetric(new ExecutionTime(), ParetoFitnessMetrics.MIN);
        engine.addFitnessMetric(new ExecutionTime(), ParetoFitnessMetrics.MAX);
        engine.addFitnessMetric(new ExecutionTime(), ParetoFitnessMetrics.MEAN);*/
        //engine.addFitnessMetric(new ExecutionTime(), ParetoFitnessMetrics.MEAN);


        engine.addFitnessMetric(new UtilFitness(), ParetoFitnessMetrics.BEST);
        engine.addParetoMetric(ParetoMetrics.HYPERVOLUME);

        long startTime = System.nanoTime();
        List<Solution<Cloud>> result = engine.solve();
        long endTime = System.nanoTime();
        long duration = endTime - startTime;


        for (Solution sol : result) {
            Set af  = sol.getFitnesses();
            Iterator iter = af.iterator();
            while (iter.hasNext())
            {
                FitnessFunction tf= (FitnessFunction) iter.next();
                System.out.print(tf.getClass().getName()+" "+ sol.getScoreForFitness(tf)+" ");
            }
            System.out.println();
        }
        System.out.println("Duration: "+(double)duration / 1000000000.0+" seconds");

        ExecutionModel model = engine.getExecutionModel();
        ExecutionModelExporter.instance$.exportMetrics(model,new File("results"));
        }




    }



}
