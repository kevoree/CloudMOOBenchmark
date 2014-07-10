package lu.snt.serval.CloudMOOBenchmark.genetic;


import lu.snt.serval.cloudcontext.*;
import lu.snt.serval.cloudcontext.impl.DefaultCloudContextFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Assaad Moawad on 6/24/2014.
 */
public class ContextLoader {
    private static DefaultCloudContextFactory dccf = new DefaultCloudContextFactory();
    private static CloudContext cc= dccf.createCloudContext();

   public static CloudContext load() {
       cc = dccf.createCloudContext();


       //Load list of clouds
       ClassLoader classloader = Thread.currentThread().getContextClassLoader();

       try {

           //Load Cloud Provider
           InputStream is = classloader.getResourceAsStream("cloudProviders.txt");
           BufferedReader br = new BufferedReader(new InputStreamReader(is));

           for (String line; (line = br.readLine()) != null; ) {
               line = line.trim();
               if (line.startsWith("#"))
                   continue;
               String[] fields = line.split(",");
               //#Provider , name  , vCPU , Memory (GiB) , Instance Storage (GB) , Networking Performance , Price
               String provider = fields[0].trim();
               String name = fields[1].trim();
               String cpu = fields[2].trim();
               String ram = fields[3].trim();
               String disk = fields[4].trim();
               String network = fields[5].trim();
               String price = fields[6].trim();

               CloudProvider cp = getCloudProvider(provider);
               VirtualMachine vm = dccf.createVirtualMachine();
               vm.setName(name);

               if (cpu.equals("") == false)
                   vm.setCpu(Double.parseDouble(cpu));
               if (ram.equals("") == false)
                   vm.setRam(Double.parseDouble(ram));

               if (disk.equals("") == false)
                   vm.setDisk(Double.parseDouble(disk));

               if (network.equals("") == false)
                   vm.setNetwork(Double.parseDouble(network));

               if (price.equals("") == false) {
                   price = price.replace("$", "");
                   vm.setPrice(Double.parseDouble(price));
               }
               vm.setCloudProvider(cp);

           }

           System.out.println("cloud loaded");

           //Load softwares
           InputStream isoft = classloader.getResourceAsStream("softwares.txt");
           BufferedReader brSoft = new BufferedReader(new InputStreamReader(isoft));

           for (String line; (line = brSoft.readLine()) != null; ) {
               line = line.trim();
               if (line.startsWith("#"))
                   continue;
               String[] fields = line.split(",");
               //#Name , CpuPerUser , RamPerUser

               String name = fields[0].trim();
               String cpu = fields[1].trim();
               String ram = fields[2].trim();

               lu.snt.serval.cloudcontext.Software software = dccf.createSoftware();
               software.setName(name);
               software.setCpuPerUser(Double.parseDouble(cpu));
               software.setRamPerUser(Double.parseDouble(ram));
               cc.addSoftwares(software);
           }

           brSoft.close();
           isoft.close();
           System.out.println("software loaded");
           //Load dependencies
           InputStream idep = classloader.getResourceAsStream("dependencies.txt");
           BufferedReader brDep = new BufferedReader(new InputStreamReader(idep));

           for (String line; (line = brDep.readLine()) != null; ) {
               line = line.trim();
               if (line.startsWith("#"))
                   continue;
               String[] fields = line.split(",");
               String soft1 = fields[0].trim();
               String soft2 = fields[1].trim();
               getSoftware(soft1).addDependencies(getSoftware(soft2));
           }

           idep.close();
           brDep.close();
           System.out.println("dependencies loaded");

           //Load latencies
           InputStream ilat = classloader.getResourceAsStream("latencies.txt");
           BufferedReader brlat = new BufferedReader(new InputStreamReader(ilat));

           for (String line; (line = brlat.readLine()) != null; ) {
               line = line.trim();
               if (line.startsWith("#"))
                   continue;
               //#From, to, latency
               String[] fields = line.split(",");
               String fromcloud = fields[0].trim();
               String tocloud = fields[1].trim();
               String latency = fields[2].trim();
               addLatency(fromcloud,tocloud,latency);
           }
           ilat.close();
           brlat.close();
           System.out.println("latencies loaded");

           //Load Softwares to run
           InputStream isoftrun = classloader.getResourceAsStream("softwaresToRun.txt");
           BufferedReader brsoftrun = new BufferedReader(new InputStreamReader(isoftrun));

           for (String line; (line = brsoftrun.readLine()) != null; ) {
               line = line.trim();
               if (line.startsWith("#"))
                   continue;
               //#software, users
               String[] fields = line.split(",");
               String software = fields[0].trim();
               String users = fields[1].trim();
               SoftwareToRun str = dccf.createSoftwareToRun();
               str.setUsers(Integer.parseInt(users));
               str.setSoftware(getSoftware(software));
               cc.addSoftwarestoRun(str);
           }
           isoftrun.close();
           brsoftrun.close();
           System.out.println("Softwares to run loaded");

       } catch (Exception ex) {
           System.out.println(ex.getMessage());
       }
       return cc;
   }

    private static void addLatency(String fromcloud, String tocloud, String latency) {
        Latency lt = dccf.createLatency();
        lt.setFrom(getCloudProvider(fromcloud));
        lt.setTo(getCloudProvider(tocloud));
        lt.setDelay(Double.parseDouble(latency));
        cc.addLatencies(lt);


    }

    private static Software getSoftware(String name){
        for(Software s: cc.getSoftwares()){
            if(s.getName().equals(name))
                return s;
        }
        return null;
    }


    private static CloudProvider getCloudProvider(String name){
        for (CloudProvider c: cc.getCloudProviders() ){
            if(c.getName().equals(name))
                return c;
        }
        CloudProvider cp= dccf.createCloudProvider();
        cp.setName(name);
        cc.addCloudProviders(cp);
        return cp;
    }

}


