package fyp.sam.fypapp.ExpertSystem;

import java.util.ArrayList;
import java.util.Calendar;

//Class used to build the initial factbase and knowledgebase
public class ExpertSystem
{
    //Method used to build the initial factbase
    public static ArrayList<Fact> BuildFactBase(String[] facts, String[] localPlantLimits)
    {
        //Initialise empty factbase
        ArrayList<Fact> factBase = new ArrayList<>();

        //Get the limits that are always the same
        int humidity_Max = Integer.parseInt(localPlantLimits[1]),
                humidity_Min = Integer.parseInt(localPlantLimits[2]),
                moisture_Max = Integer.parseInt(localPlantLimits[7]),
                moisture_Min = Integer.parseInt(localPlantLimits[8]);

        //Get the most recent sensor values
        double light = Double.valueOf(facts[2]),
                temp = Double.valueOf(facts[3]),
                humid = Double.valueOf(facts[4]),
                moist = Double.valueOf(facts[5]);

        //Initialise empty fact
        Fact newFact;

        //If it is day time then add ItIsDay as true
        if (GetIsItDay())
        {
            newFact = new Fact("ItIsDay", true);
            factBase.add(newFact);
        }
        else//else add ItIsDay as false
        {
            newFact = new Fact("ItIsDay", false);
            factBase.add(newFact);
        }

        //Add facts that are always true
        newFact = new Fact("MaxHumid", humidity_Max);
        factBase.add(newFact);

        newFact = new Fact("MinHumid", humidity_Min);
        factBase.add(newFact);

        newFact = new Fact("MaxMoist", moisture_Max);
        factBase.add(newFact);

        newFact = new Fact("MinMoist", moisture_Min);
        factBase.add(newFact);

        //Add recent sensor values as facts
        newFact = new Fact("Light", light);
        factBase.add(newFact);

        newFact = new Fact("Temp", temp);
        factBase.add(newFact);

        newFact = new Fact("Humid", humid);
        factBase.add(newFact);

        newFact = new Fact("Moist", moist);
        factBase.add(newFact);

        return factBase;
    }

    //Method used to build the knowledgebase (this won't change)
    public static ArrayList<Rule> BuildKnowledgeBases(String[] localPlantLimits)
    {
        //Initialise empty knowledgebase
        ArrayList<Rule> knowledgeBase = new ArrayList<>();

        //Get limits that change depending on time of day
        double light_Day_Max = Double.parseDouble(localPlantLimits[3]),
                light_Day_Min = Double.parseDouble(localPlantLimits[4]),
                light_Night_Max = Double.parseDouble(localPlantLimits[5]),
                light_Night_Min = Double.parseDouble(localPlantLimits[6]),
                temp_Day_Max = Double.parseDouble(localPlantLimits[9]),
                temp_Day_Min = Double.parseDouble(localPlantLimits[10]),
                temp_Night_Max = Double.parseDouble(localPlantLimits[11]),
                temp_Night_Min = Double.parseDouble(localPlantLimits[12]);

        //Initialise empty fact
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Add rules for light limits depending on time of day
        //Also an example of forward chaining
        Rule newRule = new Rule("ItIsDay", "==", "true", new Fact("MaxLight", light_Day_Max));
        knowledgeBase.add(newRule);
        newRule = new Rule("ItIsDay", "==", "true", new Fact("MinLight", light_Day_Min));
        knowledgeBase.add(newRule);
        newRule = new Rule("ItIsDay", "==", "false", new Fact("MaxLight", light_Night_Max));
        knowledgeBase.add(newRule);
        newRule = new Rule("ItIsDay", "==", "false", new Fact("MinLight", light_Night_Min));
        knowledgeBase.add(newRule);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Add rules for temperature limits depending on time of day
        newRule = new Rule("ItIsDay", "==", "true", new Fact("MaxTemp", temp_Day_Max));
        knowledgeBase.add(newRule);
        newRule = new Rule("ItIsDay", "==", "true", new Fact("MinTemp", temp_Day_Min));
        knowledgeBase.add(newRule);
        newRule = new Rule("ItIsDay", "==", "false", new Fact("MaxTemp", temp_Night_Max));
        knowledgeBase.add(newRule);
        newRule = new Rule("ItIsDay", "==", "false", new Fact("MinTemp", temp_Night_Min));
        knowledgeBase.add(newRule);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Add rules to check if the sensor values are outside of limits
        //Also an example of forward chaining continued
        newRule = new Rule("Light", ">=", "MaxLight", new Fact("LightOverMax"));
        knowledgeBase.add(newRule);
        newRule = new Rule("Light", "<=", "MaxLight", new Fact("LightUnderMax"));
        knowledgeBase.add(newRule);
        newRule = new Rule("Light", ">=", "MinLight", new Fact("LightOverMin"));
        knowledgeBase.add(newRule);
        newRule = new Rule("Light", "<=", "MinLight", new Fact("LightUnderMin"));
        knowledgeBase.add(newRule);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        newRule = new Rule("Temp", ">=", "MaxTemp", new Fact("TempOverMax"));
        knowledgeBase.add(newRule);
        newRule = new Rule("Temp", "<=", "MaxTemp", new Fact("TempUnderMax"));
        knowledgeBase.add(newRule);
        newRule = new Rule("Temp", ">=", "MinTemp", new Fact("TempOverMin"));
        knowledgeBase.add(newRule);
        newRule = new Rule("Temp", "<=", "MinTemp", new Fact("TempUnderMin"));
        knowledgeBase.add(newRule);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        newRule = new Rule("Humid", ">=", "MaxHumid", new Fact("HumidOverMax"));
        knowledgeBase.add(newRule);
        newRule = new Rule("Humid", "<=", "MaxHumid", new Fact("HumidUnderMax"));
        knowledgeBase.add(newRule);
        newRule = new Rule("Humid", ">=", "MinHumid", new Fact("HumidOverMin"));
        knowledgeBase.add(newRule);
        newRule = new Rule("Humid", "<=", "MinHumid", new Fact("HumidUnderMin"));
        knowledgeBase.add(newRule);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        newRule = new Rule("Moist", ">=", "MaxMoist", new Fact("MoistOverMax"));
        knowledgeBase.add(newRule);
        newRule = new Rule("Moist", "<=", "MaxMoist", new Fact("MoistUnderMax"));
        knowledgeBase.add(newRule);
        newRule = new Rule("Moist", ">=", "MinMoist", new Fact("MoistOverMin"));
        knowledgeBase.add(newRule);
        newRule = new Rule("Moist", "<=", "MinMoist", new Fact("MoistUnderMin"));
        knowledgeBase.add(newRule);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Add rules to check if limits are within limits
        newRule = new Rule("LightUnderMax", "LightOverMin", new Fact("LightIsGood"));
        knowledgeBase.add(newRule);
        newRule = new Rule("TempUnderMax", "TempOverMin", new Fact("TempIsGood"));
        knowledgeBase.add(newRule);
        newRule = new Rule("HumidUnderMax", "HumidOverMin", new Fact("HumidIsGood"));
        knowledgeBase.add(newRule);
        newRule = new Rule("MoistUnderMax", "MoistOverMin", new Fact("MoistIsGood"));
        knowledgeBase.add(newRule);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Return the populated knowledgebase
        return knowledgeBase;
    }

    //Method to see if it is daytime or nighttime
    private static boolean GetIsItDay() {
        int hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        //return hourOfDay > 6 && hourOfDay < 18;
        return true;
    }
}