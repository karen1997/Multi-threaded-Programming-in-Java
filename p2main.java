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
public class p2main{
	private static ArrayList<Station> allstations;
	private static int numstations;
	
	
		private final Lock conveyorlock = new ReentrantLock();
		int stationnumber;
		int workload;
		int conveyor0;
		int conveyor1;
		public void Station(int stationnumber, int conveyor0, int workload){
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
	
	
	class work implements Runnable{
		Station currentstation;
		Station previousstation;
		public work(Station cstation){
			
			this.currentstation = cstation;
			int i = allstations.indexOf(cstation);
			if(i != 0){
				previousstation = allstations.get(i - 1);
			}
			else{
				previousstation = allstations.get(allstations.size()-1);
			}
		}
		@Override
		public void run() {
			System.out.println("Routing Station "+this.currentstation.stationnumber+": In connection set to conveyor "+this.currentstation.conveyor0+".");
			fileoutput("Routing Station "+this.currentstation.stationnumber+": In connection set to conveyor "+this.currentstation.conveyor0+".");
			System.out.println("Routing Station "+this.currentstation.stationnumber+": Out connection set to conveyor "+this.currentstation.conveyor1+".");
			fileoutput("Routing Station "+this.currentstation.stationnumber+": Out connection set to conveyor "+this.currentstation.conveyor1+".");
			System.out.println("Routing Station "+this.currentstation.stationnumber+": Workload set. Station "+this.currentstation.stationnumber+" has "+this.currentstation.workload+" package groups to move.");
			fileoutput("Routing Station "+this.currentstation.stationnumber+": Workload set. Station "+this.currentstation.stationnumber+" has "+this.currentstation.workload+" package groups to move.");
			
			while(currentstation.workload != 0){
				Random r = new Random();
				try {
                    Thread.sleep(r.nextInt(20));
                } catch (InterruptedException e) {}
				currentstation.doWork(previousstation);
			}
			if(currentstation.workload == 0){
				System.out.println("\n* * Station "+this.currentstation.stationnumber+": Workload successfully completed. * *\n");
				fileoutput("\r\n* * Station "+this.currentstation.stationnumber+": Workload successfully completed. * *\r\n");
			}
			
		}
	}
	
	private static void fileoutput(String output){
		/*String s="output.txt";
		try(FileWriter fw = new FileWriter("output.txt", true);
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
			out= new PrintStream(new FileOutputStream("output.txt",true));
			out.println(output);
		} catch (IOException e) {	System.out.println("Not writing");	}
	}
	private static void fileread(){
		FileReader file= null;
		BufferedReader r= null;
		
		try{
			file= new FileReader("config.txt");
			r = new BufferedReader(file);
			String line;
			numstations = Integer.parseInt(r.readLine());
			int previous;
			int i =0;
			
			while((line = r.readLine()) != null){
				
				if(i == 0) previous = numstations - 1;
				else previous = i - 1;
				Station currstation = new Station(i,previous,Integer.valueOf(line));
				allstations.add(currstation);
				
				++i;
			}
		}catch (FileNotFoundException e) {
			System.out.println("Not opening");
		} catch (IOException e) {
			System.out.println("Not reading");
		}
	}
	public static void main(String[] args){
		allstations = new ArrayList<Station>();
		fileread();
		System.out.println("* * * SIMULATION BEGINS * * *\n\n\n* * * SIMULATION ENDS * * *\n");
		fileoutput("* * * SIMULATION BEGINS * * *\r\n\r\n\r\n* * * SIMULATION ENDS * * *\r\n\r\n");
		for(Station current: allstations){
			
			new Thread(new p2main().new work(current)).start();
						

		}
		
	}		
}