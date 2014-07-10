package lu.snt.serval.CloudMOOBenchmark.genetic;

import lu.snt.serval.CloudMOOBenchmark.genetic.fitnesses.*;
import lu.snt.serval.CloudMOOBenchmark.genetic.mutators.*;
import lu.snt.serval.cloud.Cloud;
import lu.snt.serval.cloudcontext.CloudContext;
import org.kevoree.modeling.optimization.api.fitness.FitnessFunction;
import org.kevoree.modeling.optimization.api.mutation.MutationParameters;
import org.kevoree.modeling.optimization.api.solution.Solution;
import org.kevoree.modeling.optimization.engine.genetic.GeneticAlgorithm;
import org.kevoree.modeling.optimization.engine.genetic.GeneticEngine;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * User: assaad.moawad
 * Date: 2/7/14
 * Time: ${Time}
 * University of Luxembourg - Snt
 * assaad.mouawad@gmail.com
 */


public class SampleRunner {


    public static void test(){
        Scanner userInputScanner = new Scanner(System.in);
        System.out.println("Displaying the cloud context");
        ContextUtilities.displayCloudContext();
        userInputScanner.nextLine();

        CloudPopulationFactory cpf = new CloudPopulationFactory().setSize(1);
        Cloud cloud = cpf.createPopulation().get(0);

        System.out.println("Displaying initial population");
        ContextUtilities.displayCloud(cloud);
        userInputScanner.nextLine();

        AddVm addVm = new AddVm();
        addVm.mutate(cloud,new MutationParameters());
        System.out.println("Displaying Add Vm Mutator");
        ContextUtilities.displayCloud(cloud);
        userInputScanner.nextLine();

        AssignLoadToVm asvm = new AssignLoadToVm();
        asvm.mutate(cloud,new MutationParameters());
        System.out.println("Displaying Assigning load to Vm Mutator");
        ContextUtilities.displayCloud(cloud);
        userInputScanner.nextLine();

        UnassignLoadToVm unasvm = new UnassignLoadToVm();
        unasvm.mutate(cloud,new MutationParameters());
        System.out.println("Displaying unassigning load to  Mutator");
        ContextUtilities.displayCloud(cloud);
        userInputScanner.nextLine();

        DivideLoad dl = new DivideLoad();
        dl.mutate(cloud,new MutationParameters());
        System.out.println("Displaying Divide load Mutator");
        ContextUtilities.displayCloud(cloud);
        userInputScanner.nextLine();

        CombineLoad cl = new CombineLoad();
        cl.mutate(cloud,new MutationParameters());
        System.out.println("Displaying Combine load Mutator");
        ContextUtilities.displayCloud(cloud);
        userInputScanner.nextLine();

        RemoveVm rv = new RemoveVm();
        rv.mutate(cloud,new MutationParameters());
        System.out.println("Displaying Remove vm Mutator");
        ContextUtilities.displayCloud(cloud);
        userInputScanner.nextLine();







    }




    public static void main(String[] args) throws Exception {
        CloudContext cc=ContextLoader.load();
        ContextUtilities.cloudContext=cc;

        test();

        GeneticEngine<Cloud> engine = new GeneticEngine<Cloud>();
        engine.setAlgorithm(GeneticAlgorithm.EpsilonNSGII);

        engine.addOperator(new AddVm());
        engine.addOperator(new RemoveVm());
        engine.addOperator(new DivideLoad());
        engine.addOperator(new CombineLoad());
        engine.addOperator(new AssignLoadToVm());
        engine.addOperator(new UnassignLoadToVm());

        engine.addFitnessFuntion(new AssignmentFitness());
        engine.addFitnessFuntion(new CpuUsageFitness());
        engine.addFitnessFuntion(new LatencyFitness());
        engine.addFitnessFuntion(new PriceFitness());
        engine.addFitnessFuntion(new RamUsageFitness());
        engine.addFitnessFuntion(new RedunduncyFitness());

        engine.setPopulationFactory(new CloudPopulationFactory().setSize(20));

        engine.setMaxGeneration(10000)  ;


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



























   /*     for(int i=0; i<100; i++)
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
        engine.addFitnessMetric(new ExecutionTime(), ParetoFitnessMetrics.MEAN);
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
        }*/




    }



}
