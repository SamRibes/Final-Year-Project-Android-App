package fyp.sam.fypapp.ExpertSystem;

//Class for fact objects
class Fact
{
    private String Name;
    Boolean TrueOrFalse = null;
    Double Value = null;

    //Three constructors for different kinds of facts

    //Name EXISTS
    //Example: LightOverMax
    Fact(String name)
    {
        Name = name;
    }

    //Name IS Value
    //Example: Light IS 50
    Fact(String name, double value)
    {
        Name = name;
        Value = value;
    }

    //Name IS trueOrFalse
    //Example: ItIsDay IS true
    Fact(String name, Boolean trueOrFalse)
    {
        Name = name;
        TrueOrFalse = trueOrFalse;
    }

    //Returns the Name of the fact
    String GetName()
    {
        return Name;
    }
}
