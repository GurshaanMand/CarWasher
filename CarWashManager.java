/**
 * @author Gurshaan Mand, #8037043
 * @version April 11, 2025
 *PURPOSE: This code manages a car wash by using two priority based linked lists: one for waiting cars and one for cars in service.
 *It tracks and processes cars
 *          
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CarWashManager{
	public static void main(String[] args) {
		handleQueue("carwash_details.txt");
	}

	public static void handleQueue(String inFile) {
		BufferedReader inStream;
		String line, notification;
		String[] words;
		int priority;

		QueueOfCars waitingQueue = new CarQueueLinkedList();
		QueueOfCars servicedQueue = new CarQueueLinkedList();

		// reads from file
		try {
		
			inStream = new BufferedReader(new FileReader(inFile));
			line = inStream.readLine();

			while (line != null) {
				words = line.split(",");

				if (words.length > 0) {

					if (words[0].equals("arrive") && words.length == 3) {
						priority = Integer.parseInt(words[1]);
						notification = waitingQueue.incrementCarPriority(words[2], priority);

						if (notification != null)
							System.out.println(notification);

					} else if (words[0].equals("service") && words.length == 3) {
						priority = Integer.parseInt(words[1]);

						notification = servicedQueue.incrementCarPriority(words[2], priority);
						if (notification != null)
							System.out.println(notification);

					} else if (words[0].equals("sort") && words.length == 2) {

						
						if (words[1].equals("service")) {
							servicedQueue.sort();
						}
						else if (words[1].equals("arrive")) {
							waitingQueue.sort();
						}
						else {
							System.out.println("Invalid command: " + line);
						}

					} else if (words[0].equals("print") && words.length == 2) {
						// TODO: print out the linked list contents
						if (words[1].equals("arrive")) {
							// print waiting queuw
							System.out.println(waitingQueue.toString());
						} else if (words[1].equals("service")) {
							// print waiting queuw
							System.out.println(servicedQueue.toString());
						} else {
							System.out.println("Invalid command: " + line);
						}
					} else {
						System.out.println("Invalid line: " + line);
					}
				}
				line = inStream.readLine();
			}
			inStream.close();
		} catch (NumberFormatException nfe) {
			System.out.println(nfe);
		} catch (IOException ioe) {
			System.out.println(ioe);
		}
	}
}

class Car implements Comparable<Car> {
	private String model;
	private int priority;

	public Car(String model, int priority) { // creates a car object with a String model and int priority
		this.model = model;
		this.priority = priority;
	}

	public int getPriority() {
		return priority;
	}

	public String getModel() {
		return model;
	}

	public String incrementPriority(int increasePriority) { // given the priority of it increments a car objects
															// priority
		String notification = null;
		if (increasePriority < 0) {
			notification = "Priority cannot be negative";
		} else {
			this.priority += increasePriority;

		}

		return notification;
	}

	public String decrementPriority(int decreasePriority) { // same thing just decrease the priority by the given amount
		String notification = null;

		if (decreasePriority < 0) {//check if the given priority is negative
			notification = "Priority cannot be negative";
		} else {
			if((this.priority - decreasePriority) >= 0){//check if we do decrease the priority will it into negative?

			 	priority -= decreasePriority; //if they dont then decrease the priority
				notification = null;
			}
			else{
				notification = "Priority cannot be negative"; //set it agian dont have to tho
			}
		}

		return notification;
	}

	public boolean matchModel(String model) { // return true if two car models match
		return this.model.equals(model);
	}

	public String toString() { // returns a formated string
		return this.priority + " - " + model;
	}
	/**
	 * PURPOSE: Compares this car with another to decide order based on priority.
	 * If priorities match, compares by model name alphabetically.
	 */
	@Override
	public int compareTo(Car o) {

		boolean test = false;
		int val = 0;
		
		if(this.priority > o.priority){
			val = -1;
		}
		else if(this.priority < o.priority){
			val = 1;
		}
		else if(val == 0){
			String m1 = this.model;
    		String m2 = o.model;

			for(int i = 0; i < m1.length() && i < m2.length() && !test; i++){
				char c1 = m1.charAt(i);
        		char c2 = m2.charAt(i);

				if (c1 < c2) {
					val = -1;
					test = true;
				} else if (c1 > c2) {
					val = 1;
					test = true;
				}
			}
			//If the two string are exactly the same  then it is based on whoch string is shorter
			if (val == 0) {
				if (m1.length() < m2.length()) {
					val = -1;
				} else if (m1.length() > m2.length()) {
					val = 1;
				}
			}
		}
		return val;
	}
}

interface QueueOfCars {
	// 3 interface methods
	String incrementCarPriority(String model, int priority);

	String decrementCarPriority(String model, int priority);

	void sort(); // a sort method
}

class CarQueueArrayList implements QueueOfCars {
	private ArrayList<Car> queue;

	public CarQueueArrayList() {
		this.queue = new ArrayList<Car>();
	}

	public String incrementCarPriority(String model, int priority) {
		Car car;
		String notification = null;
		car = find(model);
		if (car == null) {
			car = new Car(model, priority);
			this.queue.add(car);
		} else {
			notification = car.incrementPriority(priority);
		}

		return notification;
	}

	public String decrementCarPriority(String model, int priority) {
		Car car;
		String notification = null;
		car = find(model);
		if (car == null) {
			notification = "Not in queue";
		} else {
			notification = car.decrementPriority(priority);
		}
		return notification;
	}

	private Car find(String model) {
		Car result = null;
		int pos = 0;
		while (result == null && pos < this.queue.size()) {

			if (this.queue.get(pos).matchModel(model))
				result = this.queue.get(pos);
			else {
				pos++;
			}
		}
		return result;
	}

	public void sort() {
		// Acidently did merge sort with a arrayList :(((

		// sortArrayList(queue);
		// private void sortArrayList(ArrayList<Car> c){

		// if(c.size() <= 1){ //we have reached our base case
		// return;
		// }
		// else{
		// int mid = c.size() / 2;
		// ArrayList<Car> left = new ArrayList<>();
		// ArrayList<Car> right = new ArrayList<>();

		// for(int i = 0; i < mid; i++){
		// left.add(c.get(i));
		// }
		// for(int i = mid; i < c.size(); i++){
		// right.add(c.get(i));
		// }

		// sortArrayList(left);
		// sortArrayList(right);

		// mergeSort(c, left, right);
		// }
		// }

		// private void mergeSort(ArrayList<Car> merge, ArrayList<Car> left,
		// ArrayList<Car> right){

		// int i = 0, j = 0;
		// merge.clear();

		// while(i < left.size() && j < right.size()){

		// Car carLeft = left.get(i);
		// Car carRight = right.get(j);

		// int p1 = carLeft.getPriority();
		// int p2 = carRight.getPriority();

		// if (p1 > p2) {
		// merge.add(carLeft); // higher priority comes first
		// i++;
		// }
		// else if (p1 < p2) {
		// merge.add(carRight);
		// j++;
		// }
		// else{ // if they are equal
		// String m1 = carLeft.getModel();
		// String m2 = carRight.getModel();

		// if (m1.compareTo(m2) <= 0) {// if: m1 < m2 === -1 ex A compareTo B ==== -1
		// merge.add(carLeft);
		// i++;
		// } else { //Z compareTo B ==== +1
		// merge.add(carRight);
		// j++;
		// }

		// }

		// }
		// while(i < left.size()){
		// merge.add(left.get(i));
		// i++;
		// }
		// while(j < right.size()){
		// merge.add(right.get(j));
		// j++;
		// }
		// }

	}

	public String toString() {
		String result = "";
		for (int i = 0; i < this.queue.size(); i++) {
			result += this.queue.get(i);
			if (i < this.queue.size() - 1) {
				result += "\n";
			}
		}
		return result;
	}

	// TODO: Implement your CarQueueLinkedList

}


class CarQueueLinkedList implements QueueOfCars {
	private Node top;
	private int numElements;

	public CarQueueLinkedList() {
		this.top = null;
		this.numElements = 0;
	}
	/**
	 * PURPOSE: Adds a new car to the end of the linked list.
	 * Updates the list by attaching the new car at the last spot.
	 */

	public void add(Car car) {

		if(top == null){ //if the lisst is empty create a new node and set it to be the null
			Node newNode = new Node(car, null);
			top = newNode;
		}
		else{//if list is not empty
			Node curr = top;
			Node prev = null;
		
			while (curr != null) {
				prev = curr; //holds the next elemnts
				curr = curr.next; // moves 1 element forward
			}
			
			Node newNode = new Node(car, curr);  //create the node with the new car object
		
			if(prev != null){
				prev.next = newNode; //make it the last node pointing to curr which is null
			}
		}
		
	}

	@Override
	public String incrementCarPriority(String model, int priority) {
		//we need to go throught the linked list find our car then update its priority given the given amt
		//then call the incrementPriority and send in the priority to be added
		//if we cannot find our car then we create it and add it to the linkedlist

		Car car;
		String notification = null;
		car = find(model);

		if (car == null) {
			
			car = new Car(model, priority);//!!!!!!!!!!!!!!!!!!!!!! this allows negative priority to go throught
			add(car);
		
		} else {
			notification = car.incrementPriority(priority);
		}

		return notification;
	}

	@Override
	public String decrementCarPriority(String model, int priority) {
		
		String notification = null;
		Car car = find(model);
		
		if (car == null) {
			notification = "Not in queue";
		} else {
			notification = car.decrementPriority(priority);
		}
		return notification;
	}

	// I am using selection sort
	@Override
	public void sort() {

		if (top != null) {
			if (top.next != null) {

				Node prev = top; // start from the start

				while (prev.next != null) {

					Node minNode = prev; // place holder for the biggest node

					// converting object to Car to int
					Car c1 = (Car) minNode.value;
					int minVal = c1.getPriority();

					Node curr = prev.next; // like j = i + 1

					while (curr != null) {
						Car c2 = (Car) curr.value;
						int currVal = c2.getPriority();

						int val = c2.compareTo((Car)minNode.value);

						if (val < 0) { // biggest to smallest c1 < c2 we want c1 to bigger
							minNode = curr;// the value at curr is the smallest so far
						}
						curr = curr.next; // go to the next node either way
					}
					// swaping values
					Object temp = prev.value;
					prev.value = minNode.value;
					minNode.value = temp;

					prev = prev.next; // move prev once u go throught the list
				}
			} else {
				System.out.println("You have only one element in you LinkedList"); // only one element in ur
																					// linkedlist
			}
		} else {
			// empty list
			System.out.println("You haveno element in you LinkedList");
		}
	}
	/**
	 * PURPOSE: Sorts the cars in the list by priority from highest to lowest.
	 * If two cars have the same priority, it sorts them by model name.
	 */
	public String toString() {
		String result = "Here are the cars and their priority:\n";

		Node curr = top;

		while(curr != null){
			Car car = null; //initialize to null
			try{
				car = (Car)curr.value; //in a try catch becausse what if curr.value is not a Car object
			}
			catch(java.lang.ClassCastException pikachu){
				System.out.println("Something went wrong whilst trying to retrive Node value" + pikachu.getMessage()); //print msg
			}

			//get the necessery data
			int priority = car.getPriority();
			String model = car.getModel();

			result += priority + "-" + model + "\n";

			curr = curr.next; //update pointer
		}
		return result; //finally return the string
	}
	/**
	 * PURPOSE: Searches the list for a car that matches the model name.
	 * Returns the car if found, otherwise gives back null.
	 */

	private Car find(String model) {

		Node curr = top;
		//Node prev = null;
		boolean wasFound = false;
		Car targetCar = null;

		while(curr != null && !wasFound){ //continue search unitl u run out of avaible spots to look at or u found ur target

			
			Car car = (Car)curr.value; 
			// String currModel = car.getModel(); //get the model of the current car obect we are looking

			if(car.matchModel(model)){ //found our 
				wasFound = true; //set to true to end the while loop
				targetCar = car; //mark the car
			}
			//updating
			curr = curr.next;
		}
		return targetCar;
	}

	class Node {
		Object value;
		Node next;

		public Node(Object value, Node next) {
			this.value = value;
			this.next = next;
		}

	}

}

