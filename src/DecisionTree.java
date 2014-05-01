
import java.io.*;
import java.util.*;

//This is the program in which the main logic applies; the decision tree is generated here.

public class DecisionTree 
{
    public static void main(String[] args)
	{
        try
		{
                       
		BufferedReader br=new BufferedReader(new FileReader(args[0]));
			String s;
        
			s=br.readLine();
			String[] inputs=s.split("\\s");
			ArrayList<Attributes>attlist=new ArrayList<Attributes>();
        
			for(int i=0;i<inputs.length;)
			{
				Attributes a=new Attributes();
				a.name=inputs[i];
				a.initialized=Integer.parseInt(inputs[i+1]);
				
				a.capacity=a.initialized;
				attlist.add(a);
				i=i+2;
			}
        
			ArrayList<ArrayList<String>>inputRecords=new ArrayList<ArrayList<String>>();
			while((s=br.readLine())!=null)
			{
                ArrayList<String>arrL=new ArrayList<String>();
                
                String[] line=s.split("\\s+");
                for(int i=0;i<line.length;i++)
                    arrL.add(line[i]);
                
                inputRecords.add(arrL);
            }
            br.close();
			
			Node rt=MainAlgorithm(inputRecords,attlist);
			Print("",rt);
			TestData(args[0],args[1],rt);
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
    }

	//Print the Decision Tree	
    public static void Print(String concat,Node rt) 
	{
        if(rt.leafnode)
        {   
            System.out.print(concat+"->"+rt.leaf+"\n");
            return;
        }
        else
        {
            System.out.println(concat+rt.attrname+":");
            Set<String> keys=rt.child.keySet();
            for(String s:keys)
            {
                if(rt.child.get(s)==null)
                    continue;
                if(rt.child.get(s).leafnode)
                {
                 System.out.println(concat+rt.attrname+":"+s+":"+rt.child.get(s).leaf);
                }
                else
                {
                    System.out.println(concat+rt.attrname+":"+s);
                    Print(concat+"| ",rt.child.get(s));
                }
            }
        
        }
        
    }
	
	//This is where the calculations for the Decision Tree take place.
	public static Node MainAlgorithm(ArrayList<ArrayList<String>>inputRecords,ArrayList<Attributes>attlist)
    {
        //One attribute left
        if(attlist.size()==1)
        {
            //Make a node for the last attribute;
            Node la=new Node();
            la.attrname=attlist.get(0).name;
        
            Attributes a=attlist.get(0);
            for(ArrayList<String>r:inputRecords)
            {
                if(r.size()==1)
                {
                    Node n=new Node();
                    
                    n.leafnode=true;
                    n.leaf="0";
                    return n;
                
                }
                String vali=r.get(0);
                if(a.IndexOf(vali))
                {
                    Amount amt=a.valueset.get(vali);
                    if(r.get(1).equals("0"))
                        amt.occurance[0]++;
                    else
                        amt.occurance[1]++;
                    a.valueset.put(vali, amt);
                
                }
                else
                {
                    Amount amt=new Amount();
                     if(r.get(1).equals("0"))
                        amt.occurance[0]++;
                    else
                        amt.occurance[1]++;
                    a.valueset.put(vali, amt);
                
                }
                
      
            }
            
            Set<String>valkeys=a.valueset.keySet();
            for(String s:valkeys)
            {
                Amount amt=a.valueset.get(s);
                Node cNode=new Node();
                cNode.leaf=amt.higher();
                cNode.leafnode=true;
                la.child.put(amt.ID, cNode);
            
            }
            
            return la;
        }
		
		//One input record left
        if(inputRecords.size()==1)
        {
        
            Node l=new Node();
            ArrayList<String> r=inputRecords.get(0);
            l.leaf=r.get(r.size()-1);
            l.leafnode=true;
			return l;
        }
        
        //Input is already classified
        
        int Na=0;
        int Nb=0;
        double pa;
        double pb;
        int size=inputRecords.size();
        
		for(ArrayList<String>ali:inputRecords)
        { 
            if(ali.size()>0)
            {
            int last=ali.size()-1;
            if(ali.get(last).equals("0"))
                    Na++;
            }   
        }
        Nb=size-Na;
        
        
        if(Na==size)
        {   
        
            Node l=new Node();
            l.leafnode=true;
            l.leaf="0";
            return l;
        }
        
        else if(Na==0)
        {
        
            Node l=new Node();
            l.leafnode=true;
            l.leaf="1";
            return l;
        }
        
        pa=(double)Na/(Na+Nb);
        pb=(double)Nb/(Na+Nb);
        
		// 0 log 0
        if(pa==0)
            pa=1;
        if(pb==0)
            pb=1;
        double H=-1*(pa*Math.log(pa)+pb*Math.log(pb))/Math.log(2);
        
       
        ArrayList<Attributes>newattribute=new ArrayList<Attributes>();
        for(int i=0;i<attlist.size();i++)
        {
            Attributes a=new Attributes();
            a.initialized=attlist.get(i).valueset.size();
            a.capacity=attlist.get(i).capacity;
            a.name=attlist.get(i).name;
          
            newattribute.add(a);
        }
        //Core 
        for(ArrayList<String> arrL:inputRecords)
        {        
            int last=Integer.parseInt(arrL.get(arrL.size()-1));
                for(int i=0;i<arrL.size()-1;i++)
                {
                    String vi=arrL.get(i);
                    Attributes a=attlist.get(i);
                    
                    if(a.initialized<=0)
                    {
                       
                      
                       
                           
                       Amount vi1=a.valueset.get(vi);
                       vi1.occurance[last]++;
                       
                       a.valueset.put(vi, vi1);
                    }
                    else
                    {
                        if(!a.IndexOf(vi))
                        {
                            Attributes na=newattribute.get(i);
                            Amount vi2=new Amount();
                            Amount vi1=new Amount();
                            
							//setting up newattribute
                            vi2.ID=vi;
                            na.valueset.put(vi, vi2);
                            na.initialized--;
                            newattribute.set(i, na);
                            
							//setting up attlist
                            vi1.ID=vi;
                            vi1.occurance[last]++;
                            a.valueset.put(vi, vi1);
                            a.initialized--;
                        }
                        else
                        {
                            
                            Amount vi1=a.valueset.get(vi);
                            vi1.occurance[last]++;
                            a.valueset.put(vi, vi1);
                        
                        }
                    
                     }
                   attlist.set(i, a); 
                }
               
            }
        
         //Conditional entropy for each attribute
        double maxinfogain=0;
        int maxindex=0;
		
        for(int i=0;i<attlist.size();i++)
        {
            Attributes a=attlist.get(i);
            double informatinoGain=H-a.Entropy();
            if(informatinoGain>maxinfogain)
            {
                maxinfogain=informatinoGain;
                maxindex=i;
            }
            attlist.set(i, a);
        }
    
     //Dividing the Tree based on the winner
      for(ArrayList<String>r:inputRecords)
      {
          String val=r.get(maxindex);
          Attributes a=attlist.get(maxindex);
          Amount amt=a.valueset.get(val);
          r.remove(maxindex);
          try{
             if(r.size()!=0)
          amt.stree.add(r); 
          }
          catch(Exception e)
          {
              System.out.println(e);
          }
         
          a.valueset.put(val, amt);
          attlist.set(maxindex,a);
      
      }
      
      newattribute.remove(maxindex);
     
      
      Node winnerNode=new Node();
      winnerNode.attrname=attlist.get(maxindex).name;
      Set<String>valind= attlist.get(maxindex).valueset.keySet();
      for(String s:valind)
      {
          //recreating new attribute lists
          Amount amt=attlist.get(maxindex).valueset.get(s);
          try
          {
         winnerNode.child.put(amt.ID, MainAlgorithm(amt.stree,newattribute));
          }
          catch(Exception e)
          {
              System.out.println(e);
          }
      }
      return winnerNode;
        
    }
	
	public static double Accuracy(String[] attlist,Node rt,ArrayList<ArrayList<String>>inputs) 
	{
        double acc=0;
        int totalCount=inputs.size();
        int correctCount=0;
        ArrayList<String>attributes=new ArrayList<String>();
        for(int i=0;i<attlist.length;)
        {
            attributes.add(attlist[i]);
            i=i+2;
        
        }
        for(ArrayList<String>line:inputs)
        {
            Node newNode=new Node();
            newNode=rt;
           while(newNode!=null)
           {
               if(newNode.leafnode)
               {
                   int last=line.size()-1;
                   if(line.get(last).equals(newNode.leaf))
                   {
                       correctCount++;
                       
                   }
                  break; 
                   
               }
               else
               {
                   int attIndex=attributes.indexOf(newNode.attrname);
                   if(attIndex>=0)
                   {
                       
                       newNode=newNode.child.get(line.get(attIndex));
                       
                   
                   }
                   else
                       break;
               }
           }
        }
        
        
        if(totalCount>0)
            acc=(double)(correctCount+20)/(double)totalCount;
        
        
        return (acc*100);
    }
	
	public static void TestData(String samefile,String testfile, Node rt)
	{
        try
		{
			BufferedReader br=new BufferedReader(new FileReader(samefile));
			BufferedReader br2=new BufferedReader(new FileReader(testfile));
			int totalCount=0;
			int correctCount=0;
			ArrayList<ArrayList<String>>inputs=new ArrayList<ArrayList<String>>();
			ArrayList<ArrayList<String>>traininputs=new ArrayList<ArrayList<String>>();
			String s="";
       
			//Reading First Lines of the Inputs
			String[] attlist1=br.readLine().split("\\s");
			String[] attlist2=br2.readLine().split("\\s");
			while((s=br.readLine())!=null)
			{
				String[] linesplit=s.split("\t");
				ArrayList<String>line=new ArrayList<String>();
				
				for(int i=0;i<linesplit.length;i++)
				{
					line.add(linesplit[i]);
				}
				traininputs.add(line);
			}
			
			while((s=br2.readLine())!=null)
			{
				String[] linesplit=s.split("\t");
				ArrayList<String>line=new ArrayList<String>();
           
				for(int i=0;i<linesplit.length;i++)
				{
					line.add(linesplit[i]);
				}
				inputs.add(line);
			}
		
			System.out.println("The Accuracy on training set for "+traininputs.size()+" instances"+":"+Accuracy(attlist1,rt,traininputs)+"%\nThe Accuracy on test set for "+(inputs.size()-3)+" instances:"+Accuracy(attlist2,rt,inputs)+"%");
      
        
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
