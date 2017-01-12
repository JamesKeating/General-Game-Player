package DescriptionProcessing;

/**
 * Created by siavj on 09/01/2017.
 */

import SylmbolTable.Description;
import SylmbolTable.DescriptionTable;
import SylmbolTable.Fact;

import java.util.List;

public final class PropNetBuilder
{
    public PropNetBuilder() {
    }


    public PropNet create(DescriptionTable gameDescription)
    {
        try {
            List<Description> flattenedGameDescription = new Flattener(gameDescription).flatten();


            return null;
            //TODO: return new PropNetConverter().convert(Role.computeRoles(description), flatDescription);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}