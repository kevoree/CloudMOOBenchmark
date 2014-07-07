package lu.snt.serval.CloudMOOBenchmark.genetic;

import lu.snt.serval.cloudcontext.*;

/**
 * User: assaad.moawad
 * Date: 2/7/14
 * Time: ${Time}
 * University of Luxembourg - Snt
 * assaad.mouawad@gmail.com
 */


public class SampleRunner {



    public static void display(CloudContext cc){
        System.out.println("Cloud providers");
        for(CloudProvider cp: cc.getCloudProviders()){
            System.out.println(cp.getName());
            for(VirtualMachine vm:cp.getVirtualMachines()){
                System.out.println("  ---- "+vm.getName()+" cpu:"+vm.getCpu()+ " Ram:"+vm.getRam()+" Disk"+vm.getDisk()+" price:"+vm.getPrice());
            }
        }

        System.out.println();
        System.out.println("Latencies");
        for(Latency l: cc.getLatencies()){
            System.out.println(l.getFrom().getName()+" ->"+l.getTo().getName()+": "+l.getDelay()+" ms");
        }


        System.out.println();
        System.out.println("Software database");
        for(Software s: cc.getSoftwares()){
            System.out.println(s.getName()+" CPU: "+s.getCpuPerUser()+" RAM: "+s.getRamPerUser());
            for(Software t:s.getDependencies()){
                System.out.println("  ---- "+t.getName());
            }
        }

        System.out.println();
        System.out.println("Softwares to run");
       for(SoftwareToRun str: cc.getSoftwarestoRun()){
           System.out.println(str.getSoftware().getName()+" :"+str.getUsers()+" users");
       }

    }

    public static void main(String[] args) throws Exception {
        CloudContext cc=ContextLoader.load();
        display(cc);























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
