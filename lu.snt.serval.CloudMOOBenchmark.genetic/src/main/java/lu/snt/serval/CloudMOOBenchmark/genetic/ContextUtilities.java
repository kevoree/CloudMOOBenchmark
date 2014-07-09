package lu.snt.serval.CloudMOOBenchmark.genetic;

import lu.snt.serval.cloud.*;
import lu.snt.serval.cloud.impl.DefaultCloudFactory;
import lu.snt.serval.cloudcontext.*;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Ace Shooting on 7/9/2014.
 */
public class ContextUtilities {
    private static DefaultCloudFactory cloudfactory = new DefaultCloudFactory();
    private static Random rand=new Random();

    public static CloudContext cloudContext;
    private static ArrayList<VirtualMachine> allvm=null;
    private static ResourceMetric allSoftwareResources=null;



    //Should be replaced by a faster getter
    public static Software getSoftware (String name){
        for(Software sw: cloudContext.getSoftwares()){
            if(sw.getName().equals(name))
                return sw;
        }
        return null;
    }

    //Reccursively create software thread and threads for the dependencies
    public static SoftwareThread createSoftwareThread(String softwareName, int users){
        SoftwareThread st=cloudfactory.createSoftwareThread();
        st.setUsers(users);
        st.setSoftwareName(softwareName);
        Software sw = getSoftware(softwareName);
        ResourceMetric ressourceMetric = cloudfactory.createResourceMetric();
        ressourceMetric.setNetwork(sw.getNetworkPerUser()*users);
        ressourceMetric.setRam(sw.getRamPerUser()*users);
        ressourceMetric.setDisk(sw.getDiskPerUser()*users);
        ressourceMetric.setCpu(sw.getCpuPerUser()*users);
        st.setResource(ressourceMetric);
        for (Software s: sw.getDependencies()){
            st.addSoftwareThreadsDependencies(createSoftwareThread(s.getName(),users));
        }
        return st;
    }

    private static void InitializeVm(){
        if (allvm==null){
            allvm = new ArrayList<VirtualMachine>();
            for(CloudProvider cp: cloudContext.getCloudProviders()){
                for(VirtualMachine vm: cp.getVirtualMachines())
                    allvm.add(vm);
            }
        }
    }


    public static VirtualMachine getMaxCPUMachine(){
        InitializeVm();
        if(allvm.size()==0)
            return null;

        double cpu= allvm.get(0).getCpu();
        VirtualMachine current = allvm.get(0);
        for(VirtualMachine vm: allvm){
            if(vm.getCpu()>cpu){
                current=vm;
                cpu=current.getCpu();
            }
        }
        return current;
    }

    public static VirtualMachine getRandomMachine(){
        InitializeVm();
        return allvm.get(rand.nextInt(allvm.size()));
    }

    public static VirtualMachine getVirtualMachine(String name){
        InitializeVm();
        for(VirtualMachine vm: allvm){
            if(vm.getName().equals(name))
                return vm;
        }
        return null;
    }



    //Displays for Debug purposes

    public static void displayCloud(Cloud cloud){
        //Display load balancer
        System.out.println();
        System.out.println("Printing the current state of cloud");
        System.out.println("Displaying Load Balancers");
        for(LoadBalancer lb: cloud.getLoadBalancers()){
            ResourceMetric rm = cloudfactory.createResourceMetric();
            for(SoftwareThread st: lb.getSoftwareThreads()){
                rm=addResource(rm,getResource(st));
            }
            System.out.println("-Load Balancer for " + lb.getSoftwareToRunName()+", serving "+lb.getUsers()+" users "+"[Total CPU]: "+ String.format("%.2f",rm.getCpu())+" [Total Ram]: "+String.format("%.2f",rm.getRam()));
            for(SoftwareThread st: lb.getSoftwareThreads()){
                displaySoftwareThread(st, 1);
            }
            System.out.println();

        }
        System.out.println("Displaying sum of required resources: ");
        displayResource(getAllSoftwareResources());
        System.out.println("Displaying Virtual machines Instances");
        for(VmInstance vm: cloud.getVmInstances()){
            System.out.println("-VM "+vm.getVirtualMachineName()+" ID:"+vm.getGenerated_KMF_ID()+" Remaining CPU"+ String.format("%.2f",vm.getResource().getCpu())+ " Remaining RAM: "+String.format("%.2f",vm.getResource().getRam()));
            for(SoftwareThread softwareThread: vm.getThreads()){
                System.out.println("---- Thread "+softwareThread.getSoftwareName()+" serving "+softwareThread.getUsers());
            }
        }

        System.out.println("Unassigned threads: "+getUnassignedThreads(cloud,null).size());


    }

    private static void displaySoftwareThread(SoftwareThread st, int i) {
        System.out.print("--");
        for(int j=0; j<i;j++)
            System.out.print("--");
        String vm="N/A";
        if(st.getHost()!=null)
            vm=st.getHost().getVirtualMachineName()+" Id:"+st.getHost().getGenerated_KMF_ID();

        System.out.println(" Thread "+st.getSoftwareName()+" serving "+st.getUsers()+" users running on: "+vm+ ", [CPU]: "+String.format("%.2f",st.getResource().getCpu())+", [RAM]: "+String.format("%.2f", st.getResource().getRam()));
        for(SoftwareThread dep: st.getSoftwareThreadsDependencies())
            displaySoftwareThread(dep,i+1);

    }

    public static void shuffleArray(int[] ar)
    {
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rand.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    public static void displayCloudContext(){
        System.out.println("Cloud providers");
        for(CloudProvider cp: cloudContext.getCloudProviders()){
            System.out.println(cp.getName());
            for(VirtualMachine vm:cp.getVirtualMachines()){
                System.out.println("  ---- "+vm.getName()+" cpu:"+vm.getCpu()+ " Ram:"+vm.getRam()+" Disk"+vm.getDisk()+" price:"+vm.getPrice());
            }
        }

        System.out.println();
        System.out.println("Latencies");
        for(Latency l: cloudContext.getLatencies()){
            System.out.println(l.getFrom().getName()+" ->"+l.getTo().getName()+": "+l.getDelay()+" ms");
        }


        System.out.println();
        System.out.println("Software database");
        for(Software s: cloudContext.getSoftwares()){
            System.out.println(s.getName()+" CPU: "+s.getCpuPerUser()+" RAM: "+s.getRamPerUser());
            for(Software t:s.getDependencies()){
                System.out.println("  ---- "+t.getName());
            }
        }

        System.out.println();
        System.out.println("Softwares to run");
        for(SoftwareToRun str: cloudContext.getSoftwarestoRun()){
            System.out.println(str.getSoftware().getName()+" :"+str.getUsers()+" users");
        }

    }

    public static void displayResource(ResourceMetric resourceMetric){
        System.out.println("[CPU]: "+ String.format("%.2f",resourceMetric.getCpu())+ " [RAM]: "+ String.format("%.2f",resourceMetric.getRam())+" [Network]: "+String.format("%.2f",resourceMetric.getNetwork())+ " [Disk]: "+String.format("%.2f",resourceMetric.getDisk()));
    }



    private static double maxPrice=0;

    public static double getMaxPrice(){
        if(maxPrice==0) {
            double max = 0;
            ResourceMetric rm = getAllSoftwareResources();
            VirtualMachine vm = getMaxCPUMachine();
            double factor = Math.max(rm.getCpu() / vm.getCpu(), rm.getRam() / rm.getRam());
            maxPrice = (factor * 10) * vm.getPrice();
        }
        return maxPrice;
    }

    public static ResourceMetric getAllSoftwareResources(){
        if(allSoftwareResources!=null)
            return allSoftwareResources;

        allSoftwareResources=cloudfactory.createResourceMetric();

        for(SoftwareToRun str: cloudContext.getSoftwarestoRun()){
           SoftwareThread st = ContextUtilities.createSoftwareThread(str.getSoftware().getName(),str.getUsers());
            allSoftwareResources=addResource(allSoftwareResources,getResource(st));
        }
        return allSoftwareResources;
    }


    public static boolean canContain(VmInstance vm, SoftwareThread software){
        return canContain(getAvailableResource(vm),software.getResource());
    }

    public static boolean canContain(ResourceMetric vm, ResourceMetric software){
        return ((vm.getCpu()>=software.getCpu())&&(vm.getRam()>=software.getRam())&&(vm.getNetwork()>=software.getNetwork())&&(vm.getDisk()>=software.getDisk()));
    }

    public static ResourceMetric getAvailableResource(VmInstance vm){
        return substract(vm.getResource(), getUsedResource(vm));

    }

    public static ResourceMetric substract (ResourceMetric vm, ResourceMetric used){
        ResourceMetric rm = cloudfactory.createResourceMetric();
        rm.setCpu(vm.getCpu()-used.getCpu());
        rm.setRam(vm.getRam() - used.getRam());
        rm.setDisk(vm.getDisk()-used.getDisk());
        rm.setNetwork(vm.getNetwork()-used.getNetwork());

        if(rm.getCpu()<0||rm.getRam()<0||rm.getNetwork()<0||rm.getDisk()<0)
            System.out.println("Negative value detected !! VM " + vm.getGenerated_KMF_ID());
        return rm;
    }

    public static ResourceMetric getUsedResource(VmInstance vm){
        ResourceMetric rm = cloudfactory.createResourceMetric();
        for(SoftwareThread st: vm.getThreads()){
            rm=addResource(rm,st.getResource());
        }
        return rm;
    }



    public static ArrayList<SoftwareThread> getAllThreads(Cloud model){
        ArrayList<SoftwareThread> allthreads=new ArrayList<SoftwareThread>();
        for(LoadBalancer lb: model.getLoadBalancers()){
            for(SoftwareThread st: lb.getSoftwareThreads()){
                addThreads(st, allthreads);
            }
        }
        return allthreads;
    }

    private static void addThreads(SoftwareThread st, ArrayList<SoftwareThread> allthreads) {
        allthreads.add(st);
        for(SoftwareThread dep:st.getSoftwareThreadsDependencies()){
            addThreads(dep, allthreads);
        }
    }



    public static ArrayList<SoftwareThread> getUnassignedThreads(Cloud model, ResourceMetric comparator){
        ArrayList<SoftwareThread> unassigned=new ArrayList<SoftwareThread>();
        for(LoadBalancer lb: model.getLoadBalancers()){
            for(SoftwareThread st: lb.getSoftwareThreads()){
                addUnassigned(st,unassigned, comparator);
            }

        }
        return unassigned;
    }

    private static void addUnassigned(SoftwareThread st, ArrayList<SoftwareThread> unassigned, ResourceMetric comparator) {
        if(st.getHost()==null){
            if(comparator==null||canContain(comparator,st.getResource()))
                unassigned.add(st);
        }
        for(SoftwareThread dep:st.getSoftwareThreadsDependencies()){
            addUnassigned(dep,unassigned, comparator);
        }
    }


    public static ResourceMetric getResource(SoftwareThread thread){
        ResourceMetric result = cloudfactory.createResourceMetric();
        result=addResource(result,thread.getResource());
        for (SoftwareThread st: thread.getSoftwareThreadsDependencies())
        {
            result=addResource(result,getResource(st));
        }
        return result;
    }

    public static ResourceMetric addResource(ResourceMetric a, ResourceMetric b){
        ResourceMetric result = cloudfactory.createResourceMetric();
        result.setCpu(a.getCpu()+b.getCpu());
        result.setDisk(a.getDisk()+b.getDisk());
        result.setRam(a.getRam()+b.getRam());
        result.setNetwork(a.getNetwork()+b.getNetwork());
        return result;
    }


    public static void terminateSoftwareThread(SoftwareThread st) {
        VmInstance vm= st.getHost();
        if(vm!=null)
            vm.removeThreads(st);
        for(SoftwareThread dep: st.getSoftwareThreadsDependencies())
            terminateSoftwareThread(dep);
    }
}
