package Dataverse.DataverseJSONFieldClasses;

public abstract class CompoundDateJSONField extends CompoundJSONField{
    public abstract String getStartDate();
    public abstract String getEndDate();
    public abstract void setStartDate(String start);
    public abstract void setEndDate(String end);
}
