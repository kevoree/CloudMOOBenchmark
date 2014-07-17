
class cloud.Cloud  {
    @contained
    vmInstances : cloud.VmInstance[0,*]
    @contained
    loadBalancers : cloud.LoadBalancer[0,*]
}

class cloud.VmInstance  {
    virtualMachineName : String
    price : Double
    cloudProviderName : String
    threads : cloud.SoftwareThread[0,*] oppositeOf host
    @contained
    resource : cloud.ResourceMetric
}

class cloud.LoadBalancer  {
    softwareToRunName : String
    users : Int
    @contained
    softwareThreads : cloud.SoftwareThread[0,*]
}

class cloud.SoftwareThread  {
    softwareName : String
    users : Int
    @contained
    softwareThreadsDependencies : cloud.SoftwareThread[0,*]
    host : cloud.VmInstance oppositeOf threads
    @contained
    resource : cloud.ResourceMetric
}

class cloud.ResourceMetric  {
    cpu : Double
    ram : Double
    network : Double
    disk : Double
}
