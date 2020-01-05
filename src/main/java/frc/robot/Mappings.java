package frc.robot;

public class Mappings {
    //Xbox controller ID's
    public final static int primaryController = 0;
    public final static int secondaryController = 1; 

    public static void setupDefaultMappings() {
        OI.getPrimaryController();

        OI.getSecondaryController();
        
    }
}
