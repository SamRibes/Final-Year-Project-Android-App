package fyp.sam.fypapp.ExpertSystem;

import java.util.ArrayList;

//Class for rule objects
public class Rule
{
    private String FactToCheck;
    private String Operator;
    private String CheckAgainst;
    Fact FactToAdd;
    private String FactToCheck2;

    //Two constructors for different kind of rules

    //IF factToCheck IS operator THEN ADD factToAdd TO FACTBASE
    Rule(String factToCheck, String operator, String checkAgainst, Fact factToAdd)
    {
        FactToCheck = factToCheck;
        Operator = operator;
        CheckAgainst = checkAgainst;
        FactToAdd = factToAdd;
    }

    //IF factToCheck1 EXISTS AND factToCheck2 EXISTS THEN ADD factToAdd TO FACTBASE
    Rule(String factToCheck1, String factToCheck2, Fact factToAdd)
    {
        FactToCheck = factToCheck1;
        FactToCheck2 = factToCheck2;
        FactToAdd = factToAdd;
    }

    //Returns the rule as an ArrayList of Strings
    ArrayList<String> getRule()
    {
        ArrayList<String> ruleAsString = new ArrayList<>();

        ruleAsString.add(FactToCheck);

        if(Operator != null)
        {
            ruleAsString.add(Operator);
        }
        if(CheckAgainst != null)
        {
            ruleAsString.add(CheckAgainst);
        }
        if(FactToCheck2 != null)
        {
            ruleAsString.add(FactToCheck2);
        }
        if(FactToAdd != null)
        {
            ruleAsString.add(FactToAdd.GetName());
        }
        return ruleAsString;
    }
}
