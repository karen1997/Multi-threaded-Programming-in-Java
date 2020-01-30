# Multi-threaded-Programming-in-Java
To practice programming an application with multiple threads of execution and  synchronizing their access to necessary shared objects.
In this programming assignment you will simulate the package shipping management 

system for an automated package shipping operation similar to the one depicted here:

This example package shipping operation has five routing stations (S0 – S4), each of which has an 

input and output conveyor connecting to conveyor lines (C0 – C4) that go elsewhere in the system. 

Resources were limited when the system was built so each conveyor going to the rest of the facility 

must be shared between two routing stations. Since each routing station simultaneously needs an 

input and output connection to function, access to the shared conveyor lines must be strictly 

regulated. Flow direction in not important in our simulation.

You have been hired to design a simulator for a new package management system being built with 

the same design, but possibly fewer/more stations. You are to implement this simulator in Java and 

have each routing station function in its own thread. A routing station moves packages from one of 

its connected conveyors to the other. A station’s workload is the number of times that a routingstation needs to have exclusive access to the input and output conveyors during the simulation. 

Once a routing station is granted access to both conveyors it calls its doWork()method during 

which it will flow packages down each of its connected conveyors (of course it must verify that it 

has access and isn’t in conflict with another routing station). After the packages-in and packages-

out methods are run, the workload of the routing station is reduced by 1 and the routing station will 

release both of the conveyors and signal waiting routing stations that the conveyors are available. 

After executing a flow and releasing its conveyors, a routing station should sleep for some random 

period of time. A routing station’s thread stops running when its workload reaches 0. To prevent 

deadlock, ensure that each routing station acquires locks on the conveyors it needs in increasing 

numerical order.
