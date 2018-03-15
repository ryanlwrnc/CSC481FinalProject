package NBAPredictor;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;

public class NBATeams {
	public static Map<String, Boolean> getTeams() {
		Map<String, Boolean> teams = new LinkedHashMap<>();
		teams.put("ATL", true);
		// BKN has no data
		//teams.put("BKN", true);
		teams.put("BOS", true);
		teams.put("CHA", true);
		teams.put("CHI", true);
		teams.put("CLE", true);
		teams.put("DAL", true);
		teams.put("DEN", true);
		teams.put("DET", true);
		teams.put("GSW", true);
		teams.put("HOU", true);
		teams.put("IND", true);
		teams.put("LAC", true);
		teams.put("LAL", true);
		teams.put("MEM", true);
		teams.put("MIA", true);
		teams.put("MIL", true);
		teams.put("MIN", true);
		teams.put("NOP", true);
		teams.put("NYK", true);
		// OKC has no data
		//teams.put("OKC", true);
		teams.put("ORL", true);
		teams.put("PHI", true);
		teams.put("PHX", true);
		teams.put("POR", true);
		teams.put("SAC", true);
		teams.put("SAS", true);
		teams.put("TOR", true);
		teams.put("UTA", true);
		teams.put("WAS", true);
		return teams;
	}
	public static boolean isValidTeam(String team) {
		Map<String, Boolean> teams = getTeams();
		if (teams.get(team) != null) {
			return true;
		}
		return false;
	}
	public static void printTeams() {
		Map<String, Boolean> teams = getTeams();
		
		Set<String> keys = teams.keySet();
	    String[] arr = keys.toArray(new String[0]);
	 
	    System.out.println("Valid teams: ");
	    for (int i = 0; i < arr.length; i++) {
	        System.out.println(arr[i]);
	    }
	}
}
