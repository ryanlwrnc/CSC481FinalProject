package NBAPredictor;

import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
import com.google.common.collect.Multimap;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.dlsyntax.renderer.DLSyntaxObjectRenderer;
import org.semanticweb.owlapi.formats.PrefixDocumentFormat;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.search.EntitySearcher;

import java.util.*;

public class Predictor {
    private static final String filePath = "file:///Users/Ryan/Documents/Cal_Poly_Coursework/Senior_year/CSC481/FinalProject/basketball.owl"; 
    private OWLObjectRenderer renderer = new DLSyntaxObjectRenderer();
    private OWLOntologyManager manager;
    private OWLOntology ontology; 
    private OWLReasonerFactory reasonerFactory;
    private OWLReasoner reasoner;
    private OWLDataFactory factory;
    private PrefixDocumentFormat pm;
    
    public Predictor() throws OWLOntologyCreationException {
    		manager = OWLManager.createOWLOntologyManager();
        ontology = manager.loadOntologyFromOntologyDocument(IRI.create(filePath));
        reasonerFactory = PelletReasonerFactory.getInstance();
        reasoner = reasonerFactory.createReasoner(ontology, new SimpleConfiguration());
        factory = manager.getOWLDataFactory();
        pm = manager.getOntologyFormat(ontology).asPrefixOWLOntologyFormat();
        pm.setDefaultPrefix("http://www.semanticweb.org/rey/ontologies/2018/1/untitled-ontology-5" + "#");
    }
    
    public String PredictWinner(String homeTeamAbbr, String awayTeamAbbr) {
    		int awayStatsAggregate, homeStatsAggregate;
		Map<String, OWLNamedIndividual> homeAndAwayTeam = getHomeAndAwayTeam(homeTeamAbbr, awayTeamAbbr);
		OWLNamedIndividual awayTeam = homeAndAwayTeam.get("away");
		OWLNamedIndividual homeTeam = homeAndAwayTeam.get("home");
    	
    		awayStatsAggregate = GetStatsAggregate(awayTeam);
    		//System.out.println("Away team (" + awayTeamAbbr + ") aggregate = " + awayStatsAggregate);
    		homeStatsAggregate = GetStatsAggregate(homeTeam);
    		//System.out.println("Home team (" + homeTeamAbbr + ") aggregate = " + homeStatsAggregate);
    		
    		if (homeStatsAggregate > awayStatsAggregate) {
    			return homeTeamAbbr;
    		}
    		else if (homeStatsAggregate < awayStatsAggregate) {
    			return awayTeamAbbr;
    		}
    		else {
    			return "TIE";
    		}
    }
    
    private int GetStatsAggregate(OWLNamedIndividual team) {
    		int statsAggregate = 0;
    		OWLObjectProperty hasStatsProperty = factory.getOWLObjectProperty(":hasStats", pm);
        for (OWLNamedIndividual statProperty : reasoner.getObjectPropertyValues(team, hasStatsProperty).getFlattened()) {
            //System.out.println("Team has stats of: " + renderer.render(statProperty));

            List<OWLDataProperty> dataProps = new ArrayList<>();
            dataProps.add(factory.getOWLDataProperty("PointsGroup", pm));
            dataProps.add(factory.getOWLDataProperty("AssistsGroup", pm));
            dataProps.add(factory.getOWLDataProperty("PointsAgainstGroup", pm));
            dataProps.add(factory.getOWLDataProperty("ReboundsGroup", pm));
            dataProps.add(factory.getOWLDataProperty("TurnoversGroup", pm));
            dataProps.add(factory.getOWLDataProperty("WinGroup", pm));
            dataProps.add(factory.getOWLDataProperty("FieldGoal2MadeGroup", pm));
            dataProps.add(factory.getOWLDataProperty("FieldGoal2PercentGroup", pm));
            dataProps.add(factory.getOWLDataProperty("FieldGoal3MadeGroup", pm));
            dataProps.add(factory.getOWLDataProperty("FieldGoal3PercentGroup", pm));
            dataProps.add(factory.getOWLDataProperty("FoulsCommittedGroup", pm));
            dataProps.add(factory.getOWLDataProperty("FoulsDrawnGroup", pm));
            dataProps.add(factory.getOWLDataProperty("FreeThrowMadeGroup", pm));
            dataProps.add(factory.getOWLDataProperty("FreeThrowPercentGroup", pm));
            
            for (OWLDataProperty dataProp : dataProps) {
            		for (OWLLiteral teamDataProperty : reasoner.getDataPropertyValues(statProperty, dataProp)) {
                    statsAggregate += teamDataProperty.parseInteger();
                }
            }
        }
        return statsAggregate;
    }
    
    private Map<String, OWLNamedIndividual> getHomeAndAwayTeam(String homeTeamAbbr, String awayTeamAbbr) {
	    Map<String, OWLNamedIndividual> homeTeamAndAwayTeam = new HashMap<>();
	    //get class and its individuals
	    OWLClass teamClass = factory.getOWLClass(":Team", pm);
	    boolean foundHome = false, foundAway = false;
	    for (OWLNamedIndividual team : reasoner.getInstances(teamClass, false).getFlattened()) {
	    		String teamRendered = renderer.render(team); //.split("#")[1];
	    		// If the instance is an actual team
	    		if (teamRendered.length() < 13) {       			
	    			Multimap<OWLDataPropertyExpression, OWLLiteral> assertedValues = EntitySearcher.getDataPropertyValues(team, ontology);
	    			for (OWLDataPropertyExpression exp : assertedValues.keySet()) {
	    				
	    				Set<OWLLiteral> teamProperties = reasoner.getDataPropertyValues(team, exp.asOWLDataProperty());
	    				for (OWLLiteral teamProperty : teamProperties) {
	    					// If it's the team abbreviation
	    					if (teamProperty.getLiteral().length() == 3) {
	    						if (teamProperty.getLiteral().equals(homeTeamAbbr)) {
	    							homeTeamAndAwayTeam.put("home", team);
	    							foundHome = true;
	    							break;
	    						}
	    						else if (teamProperty.getLiteral().equals(awayTeamAbbr)) {
	    							homeTeamAndAwayTeam.put("away", team);
	    							foundAway = true;
	    							break;
	    						}
	    					}
	    				}
	    			}
	    		}
			if (foundHome && foundAway) {
				break;
			}
	    }
	    return homeTeamAndAwayTeam;
    }
}