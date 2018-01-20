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

   '''# Get all game ids from the start of the season until yesterday
   game_id_output = msf.msf_get_data(league='nba',season=api_params["season_name"],
      date=api_params["date"],feed='full_game_schedule',
      format=api_params["game_id_format"])
   games = game_id_output['fullgameschedule']['gameentry']
   game_ids = [game['id'] for game in games]

   
   all_game_box_scores = {'gameboxscores':[]}
   # Get the box score data for all the game ids
   for i in range(len(game_ids)):
      box_score_output = msf.msf_get_data(league='nba',season=api_params["season_name"],
         force=api_params["force"],feed='game_boxscore',gameid=game_ids[i],
         format=api_params["box_score_format"])

      all_game_box_scores['gameboxscores'].append(box_score_output['gameboxscore'])
      print(i)
   with open(api_params["output_name"] + "." + api_params["box_score_format"], 
      'w') as outfile:
      json.dump(all_game_box_scores, outfile, indent=2)
         '''
   '''overall_team_standings = msf.msf_get_data(league='nba',
      season=api_params["season_name"],feed='overall_team_standings',
      force=api_params["force"],format=api_params["overall_team_standings_format"])
   
   teams = [entry["Abbreviation"] for entry in overall_team_standings\
      ["overall_team_standings"]["teamstandingsentry"]]
'''

   teams = ["ATL", "BKN", "BOS", "CHA", "CHI", "CLE", "DAL", "DEN", "DET" , "GSW" ,
      "HOU", "IND", "LAC", "LAL", "MEM", "MIA", "MIL", "MIN", "NOP", "NYK", "OKC", 
      "ORL", "PHI", "PHX", "POR", "SAC", "SAS", "TOR", "UTA"]
   all_team_gamelogs = {'allteamgamelogs':[]}
   for team in teams:
      team_gamelogs = msf.msf_get_data(league='nba',season=api_params["season_name"],
         date=api_params["date"],feed='team_gamelogs',force=api_params["force"],
         format=api_params["team_gamelogs_format"],team='bos')
      team_gamelogs[team] = team_gamelogs.pop('teamgamelogs')
      all_team_gamelogs['allteamgamelogs'].append(team_gamelogs)
      #print(team_gamelogs)
   
   with open(api_params["output_name"] + "." + api_params["team_gamelogs_format"], 
      'w') as outfile:
      json.dump(all_team_gamelogs, outfile, indent=2)

if __name__ == "__main__":
   main()
