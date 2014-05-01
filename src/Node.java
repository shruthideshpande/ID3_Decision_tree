
import java.util.*;

// This class contains the structure of the node. Each node has an name, a child and may or may not be a leaf.
public class Node 
{
    String attrname;
    String leaf="";
    HashMap <String,Node>child=new HashMap<String,Node>();
    boolean leafnode=false;
}
