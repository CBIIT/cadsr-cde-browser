package gov.nih.nci.cadsr.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import gov.nih.nci.cadsr.common.util.DBUtil;
import gov.nih.nci.cadsr.dao.model.ProgramAreaModel;
import gov.nih.nci.cadsr.service.model.context.ContextNode;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: lerner
 * Date: 3/21/15
 * Time: 2:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class UnitTestCommon
{

    /**
     * Sample "Program Area" data
     */
    public ArrayList<ProgramAreaModel> initSampleProgramAreas()
    {
        Gson gson = new GsonBuilder().create();

        String json = null;

        try
        {
            json = DBUtil.readFile( "src/test/java/gov/nih/nci/cadsr/service/programAreaModelTest.data" );
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }
        return gson.fromJson( json, new TypeToken<ArrayList<ProgramAreaModel>>()
        {
        }.getType() );
    }

    public ContextNode initContextTree()
    {
        int programArea = 5;
        String names[] = {"Alpha", "Bravo", "Charlie", "Delta", "Echo", "Foxtrot", "Golf", "Hotel"};
        ContextNode[] nodes = new ContextNode[names.length];
        for( int f = 0; f < names.length; f++)
        {
            nodes[f] = new ContextNode( 3, true, names[f], f, "Hover Text " + f );
            nodes[f].setHref( "cdebrowserServer/oneContextData?contextId=C4C5E899-8698-1624-E034-0003BA12F5E7&programArea=" + programArea + "&folderType=0" );
            if( f > 0 )
            {
                //Add this node as a child of the previous node
                nodes[f-1].addChildNode( nodes[f] );
            }
        }
        return nodes[0];
    }

}
