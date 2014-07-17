
class cloudcontext.CloudContext  {
    @contained
    cloudProviders : cloudcontext.CloudProvider[0,*]
    @contained
    softwares : cloudcontext.Software[0,*]
    @contained
    latencies : cloudcontext.Latency[0,*]
    @contained
    softwarestoRun : cloudcontext.SoftwareToRun[0,*]
}

class cloudcontext.CloudProvider  {
    @id
    name : String
    @contained
    virtualMachines : cloudcontext.VirtualMachine[0,*] oppositeOf cloudProvider
}

class cloudcontext.VirtualMachine  {
    @id
    name : String
    cpu : Double
    ram : Double
    disk : Double
    network : Double
    price : Double
    cloudProvider : cloudcontext.CloudProvider oppositeOf virtualMachines
}

class cloudcontext.Software  {
    @id
    name : String
    cpuPerUser : Double
    diskPerUser : Double
    ramPerUser : Double
    networkPerUser : Double
    discompatibilities : cloudcontext.VirtualMachine[0,*]
    dependencies : cloudcontext.Software[0,*]
    @contained
    requirements : cloudcontext.Requirement[0,*]
}

class cloudcontext.Requirement  {
}

class cloudcontext.Latency  {
    delay : Double
    from : cloudcontext.CloudProvider
    to : cloudcontext.CloudProvider
}

class cloudcontext.SoftwareToRun  {
    users : Int
    software : cloudcontext.Software
}
