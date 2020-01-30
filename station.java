/*
	Name: Karen Dorvil
	Course: CNT 4714 Spring 2019
	Assignment title: Project 2 – Multi-threaded programming in Java
	Date: February 17, 2019
	Class: Enterprise Computing
*/
package project2;
import java.io.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
class Station{
		final Lock conveyorlock = new ReentrantLock();
		int stationnumber;
		int workload;
		int conveyor0;
		int conveyor1;
		public Station(int stationnumber, int conveyor0, int workload){
			this.conveyor0= conveyor0;
			this.conveyor1= stationnumber;
			this.stationnumber= stationnumber;
			this.workload= workload;
		}
		public boolean available(Station nextstation){
			boolean conveyorlock1 = false;
			boolean conveyorlock2 = false;
			try{
				if(conveyorlock1 = this.conveyorlock.tryLock()) {
					System.out.println("Routing Station "+stationnumber+": granted access to conveyor "+this.conveyor0+".");
					fileoutput("Routing Station "+stationnumber+": granted access to conveyor "+this.conveyor0+".");
				}
				if(conveyorlock2 = nextstation.conveyorlock.tryLock()){
					System.out.println("Routing Station "+stationnumber+": granted access to conveyor "+this.conveyor1+".");
					fileoutput("Routing Station "+stationnumber+": granted access to conveyor "+this.conveyor1+".");
				}
			}finally{
				if(!(conveyorlock1 && conveyorlock2)){
					if(conveyorlock1){
						this.conveyorlock.unlock();
						System.out.println("Routing Station "+stationnumber+": released access to conveyor "+this.conveyor0+".");
						fileoutput("Routing Station "+stationnumber+": released access to conveyor "+this.conveyor0+".");
					}
					if(conveyorlock2){
						nextstation.conveyorlock.unlock();
						System.out.println("Routing Station "+stationnumber+": released access to conveyor "+this.conveyor1+".");
						fileoutput("Routing Station "+stationnumber+": released access to conveyor "+this.conveyor1+".");
					}
				}
			}
			return conveyorlock1 && conveyorlock2;
		}
		public void doWork(Station nextstation){
		
			if(available(nextstation)){
				try{
					int left=workload-1;
					System.out.println("Routing Station "+stationnumber+": successfully moves packages on conveyor "+conveyor0+".");
					fileoutput("Routing Station "+stationnumber+": successfully moves packages on conveyor "+conveyor0+".");
					System.out.println("Routing Station "+stationnumber+": successfully moves packages on conveyor "+conveyor1+".");
					fileoutput("Routing Station "+stationnumber+": successfully moves packages on conveyor "+conveyor1+".");
					System.out.println("Routing Station "+stationnumber+": has "+left+" packages left to move.");
					fileoutput("Routing Station "+stationnumber+": has "+left+" packages left to move.");
					Random r = new Random();
					try {
						Thread.sleep(r.nextInt(15));
					} catch (InterruptedException e) {}
					workload -= 1;
				}finally{
					this.conveyorlock.unlock();
					nextstation.conveyorlock.unlock();
					System.out.println("Routing Station "+stationnumber+": released access to conveyor "+this.conveyor0+".");
					fileoutput("Routing Station "+stationnumber+": released access to conveyor "+this.conveyor0+".");
					System.out.println("Routing Station "+stationnumber+": released access to conveyor "+this.conveyor1+".");
					fileoutput("Routing Station "+stationnumber+": released access to conveyor "+this.conveyor0+".");
				}
				
			}
		}
		private static void fileoutput(String output){
			String s="output.txt";
		/*try(FileWriter fw = new FileWriter("output.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw)){
			
			//FileWriter writefile = new FileWriter(s,true);
			//BufferedWriter outputing = new BufferedWriter(writefile);
			out.println(output);
			//outputing.newLine();
			fw.flush();
			fw.close();
			bw.flush();
			bw.close();
			out.flush();
			out.close();
		} catch (IOException e) {	System.out.println("Not writing");	}
		*/
		PrintStream out = null;
		try{
			out= new PrintStream(new FileOutputStream("output.txt", true));
			out.println(output);
		} catch (IOException e) {	System.out.println("Not writing");	}
		}
	}