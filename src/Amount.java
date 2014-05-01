
import java.util.*;

// This class is used ot calculate the entropy of the attributes.
public class Amount 
{
    String ID="";
    ArrayList<ArrayList<String>> stree=new ArrayList<ArrayList<String>>();
    int[] occurance=new int[2];
    
	public double getEntropy()
    {
        int Na=occurance[0];
        int Nb=occurance[1];
        double pa=(double)Na/(Na+Nb);
        double pb=(double)Nb/(Na+Nb);
		 
         //Handling the 0log0 case
        if(pa==0)
		{
            pa=1;
		}
        if(pb==0)
		{
            pb=1;
		}	 
         
         double Hi=-1*(pa*Math.log(pa)+pb*Math.log(pb))/Math.log(2);
         
         return Hi;
     
    }
	 
    String higher()
    {
        if(occurance[0]<occurance[1])
            return "1";
        else
            return "0";
    }
            
}
