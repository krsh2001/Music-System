/**
* FILENAME: StudentSign.java
* AUTHOR: KASHFIA MAHMOOD (BAYVIEW S.S GRADUATING CLASS OF 2019)
* DATE: JUN 29//19
* DESC: THIS CLASS IS ABLE TO REGISTER AND CONFIRM THE SIGNING IN AND OUT OF INSTRUMENTS
*		THIS CLASS IS RUN FROM THE GUI
*/

import java.util.*; //basic tools
import java.io.*; //input and output tools
import java.time.format.DateTimeFormatter; //for formatting, duh
import java.time.LocalDateTime; //to get local date and time from computer


public class StudentSign {

	//creating the variables to be accessed throughout
	public static ArrayList<Student> students = new ArrayList<>();
	public static ArrayList<Instrument> instruments = new ArrayList<>();
	public static ArrayList<Instrument> currentlyOut = new ArrayList<>();


	/**
	* METHOD: MAIN METHOD
	* DESCRIPTION: TESTER (FOR NOW)
	*/

	public static void main(String[] args) throws IOException {
		System.out.println("Begin."); //and so it starts!

		StudentSign s = new StudentSign();

		s.updateSystem();

		Scanner read = new Scanner(System.in);
		System.out.println("Hello. Press 1 to sign out an instrument. Press 2 to sign in an instrument.");
		int choice = read.nextInt();

		if (choice == 1){ //giving choices just while the system is based in cmd prompt
			System.out.println("Input Student ID.");
			read.nextLine();
			String studentID = read.nextLine();
			System.out.println("Input instrument to sign out.");
			String instrumentID = read.nextLine();
//			System.out.println(getInstrument(instrumentID) + " signed out at " + timeOutput() + " on " + dateOutput() + " by " + getName(studentID, "null", false));
			System.out.println(s.signOut(dateOutput(), timeOutput(), studentID, instrumentID));
		} else if (choice == 2) {
			System.out.println("Input instrument to sign in.");
			String instrumentID = read.nextLine();
			instrumentID = read.nextLine();
			int x = s.findInst(instrumentID);

			// I didn't know what to output, so I put the instrumentSerial for now
			System.out.println(instruments.get(x).getSerial() + " signed in by " + getName(instruments.get(x)));

			s.signIn(dateOutput(), timeOutput(), instrumentID);
		} else {
			System.out.println("Invalid input, try again");
		}
	}

	/**
	 * findInst consumes an instrument's ID, and produces the index of the instrument in the "instruments"
	 * ArrayList
	 * @param id the instrument's ID
	 * @return the integer index of the instrument in the instruments list, -1 if error in finding
	 */
	public int findInst (String id){
		for (int i = 0; i < instruments.size(); i++){
			if (instruments.get(i).getSerial().equals(id)){
				return i;
			}
		}
		return -1;
	}

	/**
	 * Same thing as findInst but for students
	 * @param id
	 * @return
	 */
	public int findStud (String id) {
		for (int i = 0; i < students.size(); i++) {
			if (students.get(i).id.equals(id))
				return i;
		}

		return -1;
	}

	/**
	* METHOD NAME: updateSystem
	* PARAMETERS: NONE
	* RETURN-TYPE: void
	* DESCRIPTION: Updates the ArrayLists to be used throughout the system
	*/

	public void updateSystem() throws IOException {
		BufferedReader updater = new BufferedReader(new FileReader("instruments.txt"));

		/**
		 * Updates the "instruments" ArrayList
		 */
		while (updater.ready()){
			String store = updater.readLine();
			String[]temp = store.split(",");
			instruments.add(new Instrument(temp[0], temp[1], temp[2], temp[3], temp[4]));
		}

		updater = new BufferedReader(new FileReader("currentlyOut.txt"));

		/**
		 * Updates the "currentlyOut" ArrayList, and updates the data on the instruments ArrayList
		 */
		while (updater.ready()){
			String store = updater.readLine();
			String[]temp = store.split(",");
			int x = findInst(temp[0]);

			if (x == -1){
				System.out.println("Error: Instrument Doesn't Exist");
				break;
			}

			instruments.get(x).setDateOut(temp[1]);
			instruments.get(x).setTimeOut(temp[2]);

			/**
			 * This list seems redundant as the instruments ArrayList is being updated, but I'll keep it for your
			 * convenience
			 */

			currentlyOut.add(instruments.get(x));
		}

		updater = new BufferedReader(new FileReader("students.txt"));

		/**
		 * Updates the "students" ArrayList
		 */
		while (updater.ready()){
			String store = updater.readLine();
			String[]temp = store.split(",");
			int x = findInst(temp[1]);

			if (x == -1){
				students.add(new Student(temp[0], null, temp[2], temp[3]));
			} else {
				students.add(new Student(temp[0], instruments.get(x), temp[2], temp[3]));
			}



		}
	}

	/**
	* METHOD NAME: dateOutput
	* PARAMETERS: NONE
	* RETURN-TYPE: String
	* DESCRIPTION: Returns the date of sign-in or sign-out (depending on where the method is called)
	*/

	public static String dateOutput(){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY"); //set the formatting to only output the time (hours:minutes)
		LocalDateTime currentDate = LocalDateTime.now(); //retrieve the current date
		return(formatter.format(currentDate)); //return the date as a String
	} //end dateOutput

	/**
	* METHOD NAME: timeOutput
	* PARAMETERS: NONE
	* RETURN-TYPE: String
	* DESCRIPTION: Returns the time of sign-in or sign-out (depending on where the method is called)
	*/

	public static String timeOutput(){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm"); //set the formatting to only output the time (hours:minutes)
		LocalDateTime currentTime = LocalDateTime.now(); //retrieve the current time
		return(formatter.format(currentTime)); //return the time as a String
	} //end timeOutput

	/**
	* METHOD NAME: getName 
	* PARAMETERS: String studentID, String instrumentID, boolean getID
	* RETURN-TYPE: String
	* DESCRIPTION: Returns the name of the user, whether they have inputted instrument name OR student name: boolean is false if student ID is wanted
	*/

	public static String getName(Instrument inst) {

		for (int i = 0; i < students.size(); i++) {
			if (students.get(i).inst.equals(inst))
				return students.get(i).firstName;
		}

		return "No student has this instrument out";
	}

	/**
	* METHOD NAME: signOut
	* PARAMETERS: String date, String time, String studentID, String instrumentID
	* RETURN-TYPE: String 
	* DESCRIPTION: Marks down sign-out data and updates currentlyOut data
	*/

	public String signOut(String date, String time, String studentID, String instrumentID) throws IOException {
		File output = new File("signOut.txt"); //hopefully adds onto the end, new file
		File update = new File("currentlyOut.txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter (output, true)); //wrapping to create new line with BufferedWriter object
		BufferedWriter writerUpdate = new BufferedWriter(new FileWriter (update, true)); //wrapping to create new line with BufferedWriter object
		writer.write(studentID + "," + instrumentID + "," + date + "," + time); //print out data
		writer.newLine(); //create new line to separate the next entry
		writer.close(); //close FileWriter
		writerUpdate.write(studentID + "," + instrumentID + "," + date + "," + time); //print out data
		writerUpdate.newLine();
		writerUpdate.close(); //necessary for the file to update

		int x = findInst(instrumentID);
		if (x == -1)
			return "No instrument is currently signed out with that ID";
		else
			return (instruments.get(x).getSerial() + " signed out at " + timeOutput() + " on " + dateOutput() + " by " + getName(instruments.get(x)))

	} //end signOut

	/**
	* METHOD NAME: signIn
	* PARAMETERS: String date, String time, String instrumentID
	* RETURN-TYPE: void
	* DESCRIPTION: Marks down sign-in data and updates currentlyOut data
	*/

	public void signIn(String date, String time, String instrumentID) throws IOException{ //have to update and add in studentID processing
		File output = new File("signIn.txt"); //hopefully adds onto the end, new file
		File update = new File("currentlyOut.txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter (output, true)); //wrapping to create new line with BufferedWriter object
		BufferedReader updater = new BufferedReader(new FileReader(update));

		int n = 1;
		boolean exists = false; //check that the instrument needed to be signed in
		while (updater.ready() && n<currentlyOut.size()){ //check the file for the instrument (may take more time with longer databases)
			String temp = updater.readLine();
            if (temp.contains(instrumentID)){

            	int x = findInst(instrumentID);

            	instruments.get(x).setDateOut(null);
            	instruments.get(x).setTimeOut(null);

            	currentlyOut.remove(n); //must reprint the files again DO THIS LATER
            	exists = true;
            }
            n++;
		}


		/**
		 * I'm confused at what you are trying to do here, so I kind of left it.
		 */

		if (exists==true){ //in the case that the instrument was signed out
			BufferedWriter updateFile = new BufferedWriter(new FileWriter(update, false));
			for (int i=0; i < currentlyOut.size(); i++){ //update the file (currentlyOut.txt) with the information that the instrument has been removed
				updateFile.write((String)currentlyOut.get(i).getSerial() + "," + (String)currentlyOut.get(i).get(1) + "," + (String)currentlyOut.get(i).get(2) + "," + (String)currentlyOut.get(i).get(3));
				updateFile.newLine();
			}
			updateFile.newLine();
			updateFile.close();

			String studentID = getName("null", instrumentID, true); //append to the sign-in data
			writer.write(studentID + "," + instrumentID + "," + date + "," + time); //print out data
			writer.newLine(); //create new line to separate the next entry
			writer.close(); //close FileWrite
		} else {
			System.out.println("Error! This instrument was not signed out.");
		}

	}

	/**
	 *
	 * The reason this was removed was because findInst was more efficient.
	* METHOD NAME: checkExists
	* PARAMETERS: String studID, String instID
	* RETURN-TYPE: boolean
	* DESCRIPTION: check that the instrument AND student are registered in the system

	public boolean checkExists(String studID, String instID) {
		boolean exists = false;
		for (int i=0; i < instruments.size(); i++){ //run through the instruments ArrayList to ensure that the instrument id is present
			if (instruments.get(i).get(0).equals(instID)){ //comparison
				exists = true;
			}
		}

		if (exists == true) { //verifying that the student id exists in the database, and if it doesn't, exists will return false
			for (int n=0; n < students.size(); n++){ //update the file (currentlyOut.txt) with the information that the instrument has been removed
				if (students.get(n).get(0).equals(studID)){ //comparison
					exists = true;
					break; //if it is found, exit loop
				} else {
					exists = false;
				}
			}
		}

		return exists; //return the value of the boolean: if either student id or instrument id do not exist, it will return false
	}*/

	/**
	* METHOD NAME: checkCurrent
	* PARAMETERS: String instID
	* RETURN-TYPE: boolean
	* DESCRIPTION: checks if instrument is currently signed out
	*/
	public boolean checkCurrent(String instID) {
		for (int i = 0; i < currentlyOut.size(); i++){
			if (currentlyOut.get(i).getSerial().equals(instID))
				return true;
		}

		return false;
	}


/**	public boolean getIOError(){
*		System.out.println("Error.");
*		return(true);
*	}
*/

}
