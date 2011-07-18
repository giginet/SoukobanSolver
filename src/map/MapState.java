/**
 * 
 */
package map;

import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import util.Direction;

/**
 * 現在の荷物とキャラの位置を管理するクラスです
 * @author giginet
 */
public class MapState{
  private Point chara = null;
  private HashSet<Point> loads = null;
  private Map map = null;
  
  /**
   * コンストラクタ。マップデータ、キャラの位置、荷物の位置から現在のマップの状態を生成します
   * @param map マップデータ
   * @param chara キャラクターの位置
   * @param loads 荷物の位置を含んだArrayList
   * @throws IllegalArgumentException
   */
  public MapState(Map map, Point chara, HashSet<Point> loads) throws IllegalArgumentException{
    this.map = map;
    this.chara = chara;
    this.loads = loads;
    // ゴールの数と荷物の数を比べる
    if(this.map.getGoals().size() != this.loads.size()){
      throw new IllegalArgumentException("荷物とゴールの数が一致している必要があります");
    }
    if(loads.contains(this.chara)){
      throw new IllegalArgumentException("荷物とキャラクターは同じ座標には配置できません");
    }
  }
  
  /**
   * 渡されたマップを表す文字列からマップ状態を生成します
   * @param str マップを表す文字列を渡します。各記号は以下の意味を持ちます<br>
   * 各行は\nで区切られている必要があります<br>
   * <table>
   * <tr><th>文字</th><th>生成されるもの</th></tr>
   * <tr><td>.</td><td>床が生成されます</td></tr>
   * <tr><td>#</td><td>壁が生成されます</td></tr>
   * <tr><td>*</td><td>荷物が生成されます</td></tr>
   * <tr><td>@</td><td>キャラクターが生成されます</td></tr>
   * <tr><td>a</td><td>最終到達点とキャラクターが重なっている状態を表します</td></tr>
   * <tr><td>G</td><td>荷物の最終到達点が生成されます</td></tr>
   * <tr><td>+</td><td>最終到達点と荷物が重なっている状態を表します</td></tr>
   * </table>
   * @return 新しく生成されたマップ
   * @exception 以下の時、IllegalArgumentExceptionを投げます<br>
   * <ul>
   * <li>パースできない文字が含まれていたとき</li>
   * <li>キャラクターが1カ所のみに設置されていないとき</li>
   * <li>荷物が設置されていないとき</li>
   * <li>ゴールの数と荷物の数が一致しなかったとき</li>
   * </ul>
   */
  static public MapState parse(String str) throws IllegalArgumentException{
    String chars[] = str.split("");
    HashMap<Point, Chip> map = new HashMap<Point, Chip>();
    Point chara = null;
    HashSet<Point> loads = new HashSet<Point>();
    int goalCount = 0;
    for(int i=0, x=0, y=0;i<chars.length;++i){
      String c = chars[i];
      if(c.equals("\n")){
        x = 0;
        ++y;
        continue;
      }
      Point p = new Point(x, y);
      if(c.equals(".")){
        map.put(p, new Floor(p));
      }else if(c.equals("#")){
        map.put(p, new Wall(p));
      }else if(c.equals("*")){
        loads.add(p);
        map.put(p, new Floor(p));
      }else if(c.equals("@") || c.equals("a")){
        if(chara==null){
          map.put(p, new Floor(p));
          chara = p;
          if(c.equals("a")){
            map.put(p, new Goal(p));
            ++goalCount;
          }
        }else{
          throw new IllegalArgumentException("キャラクターは1カ所にしか設置できません");
        }
      }else if(c.equals("G")){
        map.put(p, new Goal(p));
        ++goalCount;
      }else if(c.equals("+")){
        map.put(p, new Goal(p));
        loads.add((Point)p.clone());
        ++goalCount;
      }else if(c.equals("")){
        continue;
      }else{
        throw new IllegalArgumentException("不正な文字\"" + c + "\"が含まれています");
      }
      ++x;
    }
    if(chara == null){
      throw new IllegalArgumentException("キャラクターを設置する必要があります");
    }
    if(loads.isEmpty()){
      throw new IllegalArgumentException("荷物を１つは設置する必要があります");
    }else if(loads.size() != goalCount){
      throw new IllegalArgumentException("荷物とゴールの数が一致している必要があります");
    }
    Map newMap = new Map(map);
    return new MapState(newMap, chara, loads);
  }
  
  /**
   * ある荷物、またはキャラクターを指定した方向に動かせるかどうかを返します
   * @param obj 動かす物体のある座標
   * @param d 物体を動かす方向
   * @return この物体を動かせるかどうか
   */
  public boolean canMove(Point obj, Direction d){
    switch(d){
    case North:
      return canThrough(new Point(obj.x, obj.y-1));
    case East:
      return canThrough(new Point(obj.x+1, obj.y));
    case South:
      return canThrough(new Point(obj.x, obj.y+1));
    case West:
      return canThrough(new Point(obj.x-1, obj.y));
    }
    return false;
  }
  
  /**
   * キャラクターを指定した方向に動かせるかどうかを返します
   * @param d 動かす方向
   * @return 動かせるかどうか
   */
  public boolean canMoveChara(Direction d){
    if(d == Direction.SouthEast || d == Direction.NorthEast || d == Direction.SouthWest || d == Direction.NorthWest) return false;
    Point next = Map.movePoint(this.chara, d);
    return canMove(this.chara, d) || (this.hasLoads(next) && canMove(next, d));
  }
  
  /**
   * ある座標に荷物やキャラが進入可能かどうかを返します
   * @param p 調べる座標
   * @return 進入可能かどうか
   */
  public boolean canThrough(Point p){
    return 0 <= p.x && p.x < map.getWidth() && 0 <= p.y && p.y < map.getHeight() && map.getChipAt(p).canThrough() && !loads.contains(p);
  }
  
  /**
   * マップ状態のコピーを返します
   * @return コピーされたマップ状態
   * @exception コピーできないとき、CloneNotSupportedExceptionを返します
   */
  public MapState deepClone(){
    try{
      return (MapState)clone();
    }catch(CloneNotSupportedException e){
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    }
  }
  
  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj){
    if(this == obj)
      return true;
    if(obj == null)
      return false;
    if(getClass() != obj.getClass())
      return false;
    MapState other = (MapState) obj;
    if(chara == null){
      if(other.chara != null)
        return false;
    }else if(!chara.equals(other.chara))
      return false;
    if(loads == null){
      if(other.loads != null)
        return false;
    }else if(!loads.equals(other.loads))
      return false;
    if(map == null){
      if(other.map != null)
        return false;
    }else if(!map.equals(other.map))
      return false;
    return true;
  }

  /**
   * 現在のキャラクターの位置を返します
   * @return キャラクターの位置
   */
  public Point getChara(){
    return chara;
  }

  /**
   * 現在の荷物の一覧を返します
   * @return 荷物の場所を格納したHashMap
   */
  public HashSet<Point> getLoads(){
    return loads;
  }
  
  /**
   * マップ内に存在する荷物の数を返します
   * @return 荷物の数
   */
  public int getLoadsCount(){
    return loads.size();
  }
 
  /**
   * マップを返します
   * @return マップ
   */
  public Map getMap(){
    return map;
  }
  
  /**
   * 渡された場所に荷物があるかどうかを返します
   * @param p 調べる点
   * @return 荷物があるかどうか
   */
  public boolean hasLoads(Point p){
    return loads.contains(p);
  }

  /**
   * このマップが探索終了状態に到達しているかどうかを判定します（全ての荷物がゴール地点上に乗っているかをチェックします）
   * @return このマップが探索終了状態かどうか
   */
  public boolean isGoal(){
    Iterator<Point> itr = loads.iterator();
    while(itr.hasNext()){
      Point p = itr.next();
      if(!map.getChipAt(p).isGoal()) return false;
    }
    return true;
  }

  /**
   * キャラクターを指定した方向に移動したときの状態を返します。<br>
   * 進行方向に荷物があり、その荷物も動かせる場合は同時に荷物も動かします。<br>
   * 移動できない場合は自分自身を返します
   * @param d キャラクターを動かす方向
   * @return キャラクターを動かした後の新しいマップ。動かせない場合は自分自身
   */
  public MapState moveChara(Direction d){
    try{
      MapState newState = (MapState)this.clone();
      Point next = Map.movePoint(newState.chara, d);
      if(this.hasLoads(next)){
        // 移動先に荷物があった場合
        // その荷物をさらにdに移動できなかった場合、自分自身を返す
        if(!canMove(next, d)) return this;
        // 移動できる場合、荷物とキャラを同時に動かす
        newState = newState.moveLoad((Point)next.clone(), d);
        newState.chara = (Point)next.clone();
        return newState;
      }else{
        // 移動先に荷物がなかった場合
        if(!canMove(chara, d)) return this;
        newState.chara = next;
        return newState;
      }
    }catch(CloneNotSupportedException e){
      e.printStackTrace();
      return this;
    }
  }

  /**
   * ある荷物を指定した方向に動かしたときの状態を返します。存在しない荷物を動かそうとしたり、動かせない場合は自分自身を返します
   * @param load 動かす荷物のある座標
   * @param d 荷物を動かす方向
   * @return 荷物を動かした後の新しいマップ。動かせない場合は自分自身
   */
  public MapState moveLoad(Point load, Direction d){
    if(!canMove(load, d) || !this.loads.contains(load)) return this;
    try{
      MapState newState = (MapState)this.clone();
      newState.loads.remove(load);
      newState.loads.add(Map.movePoint(load, d));
      return newState;
    }catch(CloneNotSupportedException e){
      e.printStackTrace();
      return this;
    }
  }
  
  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString(){
    String result = "";
    for(int y=0;y<map.getHeight();++y){
      for(int x=0;x<map.getWidth();++x){
        Point p = new Point(x, y);
        Chip c = this.map.getChipAt(p);
        if(c.getClass() == Wall.class){
          result += c.toString();
        }else if(c.getClass() == Goal.class){
          if(p.equals(chara)){
            result += "a";
          }else if(loads.contains(p)){
            result += "+";
          }else{
            result += c.toString();
          }
        }else if(c.getClass() == Floor.class){
          if(p.equals(chara)){
            result += "@";
          }else if(loads.contains(p)){
            result += "*";
          }else{
            result += c.toString();
          }
        }
      }
      result += "\n";
    }
    result = result.substring(0, result.length()-1); // 行末の\nを削除
    return result;
  }

  /**
   * @see java.lang.Object#clone()
   */
  @Override
  protected Object clone() throws CloneNotSupportedException{
    HashSet<Point> newLoads = new HashSet<Point>();
    Iterator<Point> itr = this.loads.iterator();
    while(itr.hasNext()){
      newLoads.add((Point)itr.next().clone());
    }
    return new MapState(this.map, (Point)this.chara.clone(), newLoads);
  }
  
  
}
