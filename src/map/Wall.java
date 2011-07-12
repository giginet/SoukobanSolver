/**
 * Wall.java
 * SoukobanSolver
 *
 * Created by giginet on 2011/07/08
 * 
 */
package map;

/**
 * 壁を表すクラスです。全ての人物、荷物は進入できません
 * @author giginet
 */
public class Wall extends Chip{
  protected final boolean goal = false;
  protected final boolean through = false;
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString(){
    return "#";
  }
}
