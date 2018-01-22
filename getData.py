import configparser
import getpass
import simplejson as json
from ohmysportsfeedspy import MySportsFeeds

def main():
   # Get config file
   config = configparser.ConfigParser()
   config.read("config.ini")
   api_params = config["NBA API parameters"]
  
   # Get user credentials
   username = input("What is your MySportsFeeds username? ")
   password = getpass.getpass("What is your MySportsFeeds password? ")

   msf = MySportsFeeds(version="1.0")
   msf.authenticate(username, password)

   teams = ["ATL", "BKN", "BOS", "CHA", "CHI", "CLE", "DAL", "DEN", "DET" , "GSW" ,
      "HOU", "IND", "LAC", "LAL", "MEM", "MIA", "MIL", "MIN", "NOP", "NYK", "OKC", 
      "ORL", "PHI", "PHX", "POR", "SAC", "SAS", "TOR", "UTA", "WAS"]

   # Get team gamelogs for all NBA teams
   all_team_gamelogs = {'allteamgamelogs':[]}
   for team in teams:
      team_gamelogs = msf.msf_get_data(league='nba',season=api_params["season_name"],
         date=api_params["date"],feed='team_gamelogs',force=api_params["force"],
         format=api_params["team_gamelogs_format"],team='bos')
      # Rename key to team name to distinguish team gamelogs
      team_gamelogs[team] = team_gamelogs.pop('teamgamelogs')
      all_team_gamelogs['allteamgamelogs'].append(team_gamelogs)
  
   # Write the team gamelog data to a file 
   with open(api_params["output_name"] + "." + api_params["team_gamelogs_format"], 
      'w') as outfile:
      json.dump(all_team_gamelogs, outfile, indent=2)

if __name__ == "__main__":
   main()
