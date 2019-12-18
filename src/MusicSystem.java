import javax.swing.*;
import javax.swing.UIManager;
import java.awt.event.*;
import java.io.*; //input and output tools
import java.awt.*;

public class MusicSystem extends JFrame implements ActionListener {

	//choosing the font
	final Font titleFont;
	final Font userFont;

	//JPanels
//	final JPanel mainMenu; //main menu (choice between student and administrator)
	final JPanel instrumentIn; //sign-in panel
	final JPanel instrumentOut; //sign-out panel
//	final JPanel admin; //editor and admin controls
//	final JPanel passwordPrompt; //password prompt for the administrator

	//JLabels
	final JLabel systemTitle;
//	final JLabel studentLabel;
//	final JLabel adminLabel;
	final JLabel signinInstrumentLabel;
	final JLabel studentIDLabel;
	final JLabel signoutInstrumentLabel;
	final JLabel instrumentIDLabel;

	//JButtons
	final JButton signOut;
	final JButton signIn;
	final JButton moveToSignIn;

	//JTextFields
	//for student panel
	JTextField studentIDSignOut;
	JTextField instrumentIDSignOut;
	JTextField instrumetIDSignIn;

	//system
	static StudentSign systemData;

	/**
	* CONSTRUCTOR METHOD
	*/
	public MusicSystem() throws IOException {
		try { //set the UI to the system default
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {};
          
		instrumentIn = new JPanel();
		instrumentOut = new JPanel();

        titleFont = new Font("Oxygen", Font.BOLD, 24); //create a font for title
        userFont = new Font("Oxygen", Font.BOLD, 27);

        systemTitle = new JLabel("Bayview Music System");
        signinInstrumentLabel = new JLabel("Student Instrument Sign Out");
        studentIDLabel = new JLabel("Student ID:");
        studentIDLabel.setFont(userFont);
        signoutInstrumentLabel = new JLabel("");
        instrumentIDLabel = new JLabel("Instrument ID:");

        //creating buttons
        signOut = new JButton("Sign Out Instrument");
        signIn = new JButton("Sign In Instrument");
        moveToSignIn = new JButton("Move to Sign-in Page");

        //adding action listeners
        signOut.addActionListener(this);
        signIn.addActionListener(this);
        moveToSignIn.addActionListener(this);

        //creating the JTextFields to be used and setting opaque to true
        studentIDSignOut = new JTextField("");
        instrumentIDSignOut = new JTextField("");
        instrumetIDSignIn = new JTextField("");
        studentIDSignOut.setOpaque(false);
        instrumentIDSignOut.setOpaque(false);
        instrumetIDSignIn.setOpaque(false);

        //tab over action listener text field
		studentIDSignOut.addActionListener(new ActionListener() {
		   @Override
		    public void actionPerformed(ActionEvent e) { //transfer focus to instrument ID when enter button is pressed
				instrumentIDSignOut.requestFocusInWindow();    
		    }
		}); 

        //tab over action listener text field
		Action submitSignOut = new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e){ //what happens when a sign-out is attmpted, done as an abstractaction due to inability to catch exceptions otherwise
					attemptSignOut();
				}
		};

		instrumentIDSignOut.addActionListener(submitSignOut);
		signOut.addActionListener(submitSignOut);

        //creating layouts and applying them
		FlowLayout lay1 = new FlowLayout(FlowLayout.CENTER);
		GridLayout lay2 = new GridLayout(3,2,27,27);
		this.setLayout(lay1);
		instrumentOut.setLayout(lay2);

		//fixing alignments

		//adding components to panels
		this.add(instrumentOut);
		instrumentOut.add(studentIDLabel);
		instrumentOut.add(studentIDSignOut);
		instrumentOut.add(instrumentIDLabel);
		instrumentOut.add(instrumentIDSignOut);
		instrumentOut.add(signOut);
		instrumentOut.add(moveToSignIn);

        //set settings
        setTitle("Music System");
        setSize(500,500);
        setResizable(true);
        setVisible(true);
        instrumentOut.setVisible(true);
        instrumentIn.setVisible(false);
	}

    /**
    * ACTION PERFORMED (ACTIONLISTENER)
    */
    public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand(); //get the command name
		if (command.equals("Move to Sign-in Page")){
						System.out.println("move");
		} 
	}

	/**
	* METHOD NAME: attemptSignOut
	* PARAMETERS: none
	* RETURN-TYPE: void
	* DESCRIPTION: attempts to sign out the instrument and runs input data through various checks
	*/
	public void attemptSignOut(){
		try {
			//receive current date and time, in addition to inputted student and instrument IDs
			systemData.updateSystem();
		    String time = StudentSign.timeOutput();
    		String date = StudentSign.dateOutput();
       		String studID = String.valueOf(studentIDSignOut.getText()); //get text input
			String instID = String.valueOf(instrumentIDSignOut.getText()); //get text input

			//check if student id/instrument id is inputted (not empty)
			if (instID.equals("") || studID.equals("")) {
				JOptionPane.showMessageDialog(null, "Please input both student ID and instrument ID.");
			} else {
				boolean check = systemData.findInst(instID) != -1 && systemData.findStud(studID) != -1; //check if the instrument and student id are both registered in the system
				if (!check) { //output if the system does not recognize student or instrument id
					JOptionPane.showMessageDialog(null, "Ensure that the student and instrument are registered in the system.");
				} else { //if the instrument and student are in the system, proceed with one last check
					if (!systemData.checkCurrent(instID)) { //final check to see if 
						String signingOut = systemData.signOut(date, time, studID, instID);
						JOptionPane.showMessageDialog(null, (signingOut));
		        	} else {
						JOptionPane.showMessageDialog(null, systemData.instruments.get(systemData.findInst(instID)).getSerial()
								+ " has already been signed out by " + systemData.getName(systemData.instruments.get(systemData.findInst(instID))) + ".");
		        	}
		        }
        	}
       	} catch (Exception x) {
			JOptionPane.showMessageDialog(null, x);			
		}
        //clear input fields
	    studentIDSignOut.setText("");
	    instrumentIDSignOut.setText("");
		studentIDSignOut.requestFocusInWindow(); //changes focus
	}

	/**
	* MAIN METHOD
	*/
	public static void main(String[] args) throws IOException {
		systemData = new StudentSign();
		systemData.updateSystem();
		MusicSystem frame = new MusicSystem();
	}
}