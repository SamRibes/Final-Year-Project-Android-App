package fyp.sam.fypapp.ExpertSystem;

import java.util.ArrayList;
import java.util.Objects;

//Class is used to compare the factbase to the knowledgebase and return suggestions based on the final factbase
public class InferenceEngine {
    public static String run(ArrayList<Fact> FactBase, ArrayList<Rule> KnowledgeBase)
    {
        //Initialise empty ArrayList for suggestions to be added to
        StringBuilder suggestions = new StringBuilder();

        //Loops through the knowledgebase and compares each rule to the factbase
        for (int i = 0; i < KnowledgeBase.size(); i++) {
            //ArrayList to parse the rule
            ArrayList<String> ruleAsString;
            Rule rule = KnowledgeBase.get(i);
            ruleAsString = rule.getRule();

            //First checks that the factToAdd isn't already in the factbase
            if (!checkFactExists(FactBase, rule.FactToAdd.GetName())) {
                //If the rule is one that compares facts with values then...
                if (ruleAsString.size() == 4) {
                    Fact factToCheck = null;
                    String nameOfFactToCheck = ruleAsString.get(0),
                            operator = ruleAsString.get(1),
                            nameOfFactToCheckAgainst = null;
                    Boolean boolToCheckAgainst = null;

                    //Checks if the value were comparing too is a boolean or integer
                    switch (ruleAsString.get(2)) {
                        case "true":
                            boolToCheckAgainst = true;
                            break;
                        case "false":
                            boolToCheckAgainst = false;
                            break;
                        default:
                            nameOfFactToCheckAgainst = ruleAsString.get(2);
                            break;
                    }

                    //Finds the factToCheck in the factbase
                    for (Fact fact : FactBase) {
                        if (fact.GetName().equals(nameOfFactToCheck)) {
                            factToCheck = fact;
                            break;
                        }
                    }

                    Fact factToCheckAgainst = null;
                    //Find the boolean comparator and add new fact based on if it matches up, or...
                    if (boolToCheckAgainst != null) {
                        if (boolToCheckAgainst) {
                            //If the boolean value matches then we add the new fact to the factbase
                            if (Objects.requireNonNull(factToCheck).TrueOrFalse) {
                                FactBase.add(rule.FactToAdd);
                            }
                        } else {
                            if (!Objects.requireNonNull(factToCheck).TrueOrFalse) {
                                FactBase.add(rule.FactToAdd);
                            }
                        }
                    }//...or find the factToCheckAgainst in the factbase
                    else {
                        for (Fact fact : FactBase) {
                            if (fact.GetName().equals(nameOfFactToCheckAgainst)) {
                                factToCheckAgainst = fact;
                                break;
                            }
                        }

                        //Look for the type of comparison
                        switch (operator) {
                            case "==":
                                //Checks if the values are equal
                                if (Objects.requireNonNull(factToCheck).Value.equals(Objects.requireNonNull(factToCheckAgainst).Value)) {
                                    FactBase.add(rule.FactToAdd);
                                }
                                break;
                            case ">=":
                                //Checks if the the first value is >= to the second
                                if (Objects.requireNonNull(factToCheck).Value >= Objects.requireNonNull(factToCheckAgainst).Value) {
                                    FactBase.add(rule.FactToAdd);
                                }
                                break;
                            case "<=":
                                //Checks if the the first value is <= to the second
                                if (Objects.requireNonNull(factToCheck).Value <= Objects.requireNonNull(factToCheckAgainst).Value) {
                                    FactBase.add(rule.FactToAdd);
                                }
                                break;
                        }
                    }
                }

                //If the rule is one that check if two facts exists then...
                if (ruleAsString.size() == 3) {
                    String nameOfFactToCheck = ruleAsString.get(0),
                            nameOfFactToCheckAgainst = ruleAsString.get(1);

                    Fact factToCheck = null,
                            factToCheckAgainst = null,
                            factToAdd = rule.FactToAdd;

                    //Get first fact from factbase
                    for (Fact fact : FactBase) {
                        if (fact.GetName().equals(nameOfFactToCheck)) {
                            factToCheck = fact;
                            break;
                        }
                    }

                    //Get second fact from factbase
                    for (Fact fact : FactBase) {
                        if (fact.GetName().equals(nameOfFactToCheckAgainst)) {
                            factToCheckAgainst = fact;
                            break;
                        }
                    }

                    //If both facts exists then add the new fact
                    if (factToCheckAgainst != null && factToCheck != null) {
                        FactBase.add(factToAdd);
                    }
                }
            }
        }

        //Looks through the fact base to find certain facts
        //If a fact exists then we add the related suggestion to the suggestion ArrayList
        for (Fact fact : FactBase) {
            String nameOfFact = fact.GetName();
            if (nameOfFact.equals("LightOverMax")) {
                suggestions.append("Light is too bright.\n" +
                        "Too much light will cause the leaves to yellow and direct sun can even scorch them.\n" +
                        "Consider moving the plant to a more shaded area.\n\n");
            }
            else if (nameOfFact.equals("LightUnderMin")) {
                suggestions.append("Light is too dim.\n" +
                        "Not enough light will cause the plant to not flower.\n" +
                        "Consider moving the plant to a more well lit area.\n\n");
            }
            else if (nameOfFact.equals("LightIsGood")) {
                suggestions.append("Light is good.\n\n");
            }

            if (nameOfFact.equals("TempOverMax")) {
                suggestions.append("Temperature is too high.\n" +
                        "If the temperature is too high then leaf damage can occur.\n" +
                        "Consider moving the plant to a cooler area.\n\n");
            }
            else if (nameOfFact.equals("TempUnderMin")) {
                suggestions.append("Temperature is too low.\n" +
                        "If the temperature is too low then leaf damage can occur.\n" +
                        "Consider moving the plant to a warmer area  to prevent leaf damage.\n\n");
            }
            else if (nameOfFact.equals("TempIsGood")) {
                suggestions.append("Temperature is good.\n\n");
            }


            if (nameOfFact.equals("HumidOverMax")) {
                suggestions.append("Humidity is too high.\n" +
                        "If the humidity is high enough then the plant can start to rot or develop mould.\n" +
                        "Consider airing out the room for a bit.\n\n");
            }
            else if (nameOfFact.equals("HumidUnderMin")) {
                suggestions.append("Humidity is too low.\n" +
                        "If the humidity is too low then the leaf edges and tips can start to turn brown.\n" +
                        "Consider grouping several plants together to maintain a higher humidity. \n" +
                        "You could also give the plant a light spray of water every few hours.\n\n");
            }
            else if (nameOfFact.equals("HumidIsGood")) {
                suggestions.append("Humidity is good.\n\n");
            }


            if (nameOfFact.equals("MoistOverMax")) {
                suggestions.append("Soil moisture is too high.\n" +
                        "Too much water in the soil can cause the plant to drown or develop root rot, both of which can kill it.\n" +
                        "Make sure that the pot is draining well to remove excess moisture.\n\n");
            }
            else if (nameOfFact.equals("MoistUnderMin")) {
                suggestions.append("Soil moisture is too low.\n" +
                        "Plants require moisture to properly function. Too long without it will kill a plant.\n" +
                        "Now is a good time to water your plant.\n\n");
            }
            else if (nameOfFact.equals("MoistIsGood")) {
                suggestions.append("Soil moisture is good.\n" +
                        "It is better for this kind of plant to go a while between waterings.\n" +
                        "Let the soil dry out before watering again.\n\n");
            }
        }
        //Return populated suggestion ArrayList

        return suggestions.toString();
    }

    //Method used to check if the fact we're trying to append already exists in the factbase
    private static boolean checkFactExists(ArrayList<Fact> FactBase, String nameOfFactToCheckFor) {
        for (Fact fact : FactBase) {
            if (nameOfFactToCheckFor.equals(fact.GetName())) {
                return true;
            }
        }
        return false;
    }
}
