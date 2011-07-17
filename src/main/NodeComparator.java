/**
 * 
 */
package main;

import java.util.Comparator;

/**
 * Nodeの優先順位を定義するクラスです
 * @author giginet
 *
 */
@SuppressWarnings("rawtypes")
public class NodeComparator implements Comparator{
  
  public int compare(Object obj1, Object obj2){
    Node node0 = (Node)obj1;
    Node node1 = (Node)obj2;
    if(node0.getCost() < node1.getCost()){
      return 1;
    }else if(node0.getCost() > node1.getCost()){
      return -1;
    }else{
      return 0;
    }
  }
}
