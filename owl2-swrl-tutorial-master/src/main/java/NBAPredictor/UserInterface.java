package NBAPredictor;

import java.util.Scanner;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;

public class UserInterface {

	public static void main(String[] args) {
		String homeTeam = "", awayTeam = "";
		Scanner in = new Scanner(System.in);
		
		System.out.println(" _   _ ______  ___    _____                       ______             _ _      _             \n" + 
				"| \\ | || ___ \\/ _ \\  |  __ \\                      | ___ \\           | (_)    | |            \n" + 
				"|  \\| || |_/ / /_\\ \\ | |  \\/ __ _ _ __ ___   ___  | |_/ / __ ___  __| |_  ___| |_ ___  _ __ \n" + 
				"| . ` || ___ \\  _  | | | __ / _` | '_ ` _ \\ / _ \\ |  __/ '__/ _ \\/ _` | |/ __| __/ _ \\| '__|\n" + 
				"| |\\  || |_/ / | | | | |_\\ \\ (_| | | | | | |  __/ | |  | | |  __/ (_| | | (__| || (_) | |   \n" + 
				"\\_| \\_/\\____/\\_| |_/  \\____/\\__,_|_| |_| |_|\\___| \\_|  |_|  \\___|\\__,_|_|\\___|\\__\\___/|_|   \n" + 
				"                                                                                            ");
		while (true) {
			System.out.println("Enter an option for NBA Game Predictor: \nPredict a game (p), List Valid NBA Teams (l), or Quit (q): ");
			String s = in.nextLine();
			if (s.trim().toLowerCase().equals("p") || s.trim().toLowerCase().equals("predict a game")) {
				System.out.println("Enter the home and away teams:");
				while (true) {
					System.out.println("Home team: ");
					homeTeam = in.nextLine().toUpperCase();
					if (NBATeams.isValidTeam(homeTeam)) {
						break;
					}
					else {
						System.out.println("Invalid home team entered");
					}
				}
				while (true) {
					System.out.println("Away team: ");
					awayTeam = in.nextLine().toUpperCase();
					if (homeTeam.equals(awayTeam)) {
						System.out.println("Away team cannot be the same as the home team");
					}
					else if (NBATeams.isValidTeam(awayTeam)) {
						break;
					}
					else {
						System.out.println("Invalid away team entered");
					}
				}
				
				///////////////////////////////
				// PREDICTOR LOGIC GOES HERE //
				///////////////////////////////
				try {
					Predictor predictor = new Predictor();
				} catch (OWLOntologyCreationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if (s.trim().toLowerCase().equals("l") || s.trim().toLowerCase().equals("list valid nba teams")) {
				NBATeams.printTeams();
			}
			else if  (s.trim().toLowerCase().equals("q") || s.trim().toLowerCase().equals("quit")) {
				System.out.println("Goodbye!");
				break;
			}
			else {
				System.out.println("Invalid option entered");
			}
		}
		in.close();
	}

}
