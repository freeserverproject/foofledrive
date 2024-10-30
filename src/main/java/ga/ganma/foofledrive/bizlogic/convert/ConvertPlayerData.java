package ga.ganma.foofledrive.bizlogic.convert;

import ga.ganma.foofledrive.FoofleDrivePlan;
import ga.ganma.foofledrive.plan;
import ga.ganma.foofledrive.playerdata.PlayerDriveData;
import ga.ganma.foofledrive.playerdata.Playerdata;

public class ConvertPlayerData {

    public PlayerDriveData fromPlayerData(Playerdata playerdata) {
        return new PlayerDriveData(playerdata.getMcid(), playerdata.getInv(), ConvertPlan(playerdata.getPlan()));
    }

    private FoofleDrivePlan ConvertPlan(plan plan) {
        return switch (plan) {
            case FREE -> FoofleDrivePlan.FREE;
            case LIGHT -> FoofleDrivePlan.LIGHT;
            case MIDDLE -> FoofleDrivePlan.MIDDLE;
            case LARGE -> FoofleDrivePlan.LARGE;
        };
    }
}
