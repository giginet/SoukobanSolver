/**
 * Goal.java
 * SoukobanSolver
 *
 * Created by giginet on 2011/07/12
 * 
 */
package map;

import java.awt.Point;

/**
 * 荷物の最終的な配置位置を場所のクラスです
 * @author giginet
 */
public class Goal extends Chip{
  protected final boolean goal = true;
  protected final boolean through = true;
  
  public Goal(Point location){
    super(location);
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString(){
    return "G";
  }
}
