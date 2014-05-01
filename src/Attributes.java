
import java.util.*;

// This class contains the structure of the attributes that are provided in the training and the test file. It also calculates the entropy of the same.
public class Attributes 
{
    String name;
    int total=0;
    HashMap<String,Amount>valueset=new HashMap<String,Amount>();
    int initialized;
    int capacity;
  
	public double Entropy() 
	{
        double ent=0;
        Set <String> s=valueset.keySet();
		
        for(String si:s)
        {
            Amount a=valueset.get(si);
            
            ent=ent+a.getEntropy()*(a.occurance[0]+a.occurance[1]);
            
            total+=a.occurance[0]+a.occurance[1];
         //   System.out.println("string is  "+ si);
          //  System.out.println("a[0]'s occurance "+ a.occurance[0]);
           // System.out.println("a[1]'s occurance "+ a.occurance[1]);
            //System.out.println("entropy is  "+ ent);
        }
        ent=ent/total;
    
        return ent;
    }
	
    public boolean IndexOf(String vi) 
	{
        
        if(valueset.containsKey(vi))
            return true;
		else
			return false;
    }

    
    
}
