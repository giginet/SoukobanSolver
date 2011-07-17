/**
 * Map.java
 * SoukobanSolver
 *
 * Created by giginet on 2011/07/08
 * 
 */
package map;

import java.util.*;
import java.awt.*;
import util.Direction;

/**
 * @author giginet
 *
 */
public class Map{
  
  private Point chara = null;
  
  private int height = 0;
  private HashSet<Point> loads = null; 
  private HashMap<Point, Chip> map = null;
  private int width = 0;
  
  /**
   * コンストラクタ。大きさ0の空のマップを生成します
   */
  public Map(){
    map = new HashMap<Point, Chip>();
  }
  
  /**
   * コンストラクタ。渡されたHashMapを元に、マップを初期化します
   * @param start キャラクターの初期位置を表す座標
   * @param loads 荷物の初期位置を表すHashSet
   * @exception 渡されたマップのサイズが不正なとき、IllegalArgumentException
   */
  public Map(HashMap<Point, Chip> map, Point start, HashSet<Point> loads) throws IllegalArgumentException{
    this.map = map;
    this.chara = start;
    this.loads = loads;
    int goalCount = 0;
    int max = 0;
    if(map.size() == 0) throw new IllegalArgumentException("渡されたマップが空です");
    // マップの横幅を算出する
    Iterator<Point> itr = map.keySet().iterator();
    while(itr.hasNext()){
      Point p = itr.next();
      if(p.x > max) max = p.x;
      Chip chip = map.get(p);
      if(chip.isGoal()) ++goalCount;
    }
    // ゴールの数と荷物の数を比べる
    if(goalCount != loads.size()){
      throw new IllegalArgumentException("荷物とゴールの数が一致している必要があります");
    }
    if(loads.contains(chara)){
      throw new IllegalArgumentException("荷物とキャラクターは同じ座標には配置できません");
    }
    width = max+1;
    // マップの高さを算出する
    if(map.size()%width != 0){
      throw new IllegalArgumentException("渡されたマップのサイズが不正です");
    }
    height = map.size()/width;
  }
  
  /**
   * 2点間のマンハッタン距離を返します
   * @param p1 始点
   * @param p2 終点
   * @return マンハッタン距離
   */
  static public int manhattanDistance(Point p1, Point p2){
    return Math.abs(p2.x-p1.x) + Math.abs(p2.y-p1.y);
  }
  
  /**
   * 渡されたマップを表す文字列からマップを生成します
   * @param str マップを表す文字列を渡します。各記号は以下の意味を持ちます<br>
   * 各行は\nで区切られている必要があります<br>
   * <table>
   * <td><th>文字</th><th>生成されるもの</th></td>
   * <td><tr>.</tr><tr>床が生成されます</tr></td>
   * <td><tr>#</tr><tr>壁が生成されます</tr></td>
   * <td><tr>*</tr><tr>荷物が生成されます</tr></td>
   * <td><tr>@</tr><tr>キャラクターが生成されます</tr></td>
   * <td><tr>a</tr><tr>最終到達点とキャラクターが重なっている状態を表します</tr></td>
   * <td><tr>G</tr><tr>荷物の最終到達点が生成されます</tr></td>
   * <td><tr>+</tr><tr>最終到達点と荷物が重なっている状態を表します</tr></td>
   * </table>
   * @return 新しく生成されたマップ
   * @exception 以下の時、IllegalArgumentExceptionを投げます<br>
   * <li>
   * <ul>パースできない文字が含まれていたとき</ul>
   * <ul>キャラクターが1カ所のみに設置されていないとき</ul>
   * <ul>荷物が設置されていないとき</ul>
   * <ul>ゴールの数と荷物の数が一致しなかったとき</ul>
   * </li>
   */
  static public Map parse(String str) throws IllegalArgumentException{
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
    return new Map(map, chara, loads);
  }
  
  /**
   * 渡した点をDirectionの方向に動かした新しい点を生成します
   * @param point 元の点
   * @param d 動かしたいDirection
   * @return 動いた後の新しい点
   */
  static private Point movePoint(Point point, Direction d){
    Point p = (Point)point.clone();
    switch(d){
    case Up:
      p.translate(0, -1);
      break;
    case Right:
      p.translate(1, 0);
      break;
    case Down:
      p.translate(0, 1);
      break;
    case Left:
      p.translate(-1, 0);
      break;
    }
    return p;
  }
  
  /**
   * ある荷物、またはキャラクターを指定した方向に動かせるかどうかを返します
   * @param obj 動かす物体のある座標
   * @param d 物体を動かす方向
   * @return この物体を動かせるかどうか
   */
  public boolean canMove(Point obj, Direction d){
    switch(d){
    case Up:
      return canThrough(new Point(obj.x, obj.y-1));
    case Right:
      return canThrough(new Point(obj.x+1, obj.y));
    case Down:
      return canThrough(new Point(obj.x, obj.y+1));
    case Left:
      return canThrough(new Point(obj.x-1, obj.y));
    }
    return false;
  }
  
  /**
   * ある座標に荷物やキャラが進入可能かどうかを返します
   * @param p 調べる座標
   * @return 進入可能かどうか
   */
  public boolean canThrough(Point p){
    return 0 <= p.x && p.x < width && 0 <= p.y && p.y < height && getChipAt(p).canThrough() && !loads.contains(p);
  }
  
  /**
   * このマップの完全なるコピーを返します
   * @return コピーされたマップ
   */
  public Map deepClone() throws CloneNotSupportedException{
    try{
      return (Map)this.clone();
    }catch(CloneNotSupportedException e){
      e.printStackTrace();
      return null;
    }
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj){
    Map other = (Map)obj;
    Iterator<Point> itr = map.keySet().iterator();
    while(itr.hasNext()){
      Point key = itr.next();
      if(!(this.getChipAt(key).equals(other.getChipAt(key)))) return false;
    }
    itr = this.loads.iterator();
    while(itr.hasNext()){
      if(!(other.loads.contains(itr.next()))) return false;
    }
    return this.getLoadsCount() == other.getLoadsCount() && this.getWidth() == other.getWidth() && this.getHeight() == other.getHeight() && other.getChara().equals(this.chara);
  }
  
  /**
   * 現在のキャラクターの位置を返します
   * @return キャラクターの位置
   */
  public Point getChara(){
    return chara;
  }

  /**
   * 指定された座標にあるマスを取り出します。見つからない場合はnullを返します
   * @param p 取り出したい座標
   * @return その座標にあるマス。ない場合はnull
   */
  public Chip getChipAt(Point p){
    if(this.map.containsKey(p)){
      return this.map.get(p);
    }else{
      return null;
    }
  }

  /**
   * マップの高さを返します
   * @return マップ高さ
   */
  public int getHeight(){
    return height;
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
   * マップの幅を返します
   * @return マップ幅
   */
  public int getWidth(){
    return width;
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
      if(!getChipAt(p).isGoal()) return false;
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
  public Map moveChara(Direction d){
    try{
      Map newMap = (Map)this.clone();
      Point next = Map.movePoint(newMap.chara, d);
      if(this.hasLoads(next)){
        // 移動先に荷物があった場合
        // その荷物をさらにdに移動できなかった場合、自分自身を返す
        if(!canMove(next, d)) return this;
        // 移動できる場合、荷物とキャラを同時に動かす
        newMap = newMap.moveLoad((Point)next.clone(), d);
        newMap.chara = (Point)next.clone();
        return newMap;
      }else{
        // 移動先に荷物がなかった場合
        if(!canMove(chara, d)) return this;
        newMap.chara = next;
        return newMap;
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
  public Map moveLoad(Point load, Direction d){
    if(!canMove(load, d) || !this.loads.contains(load)) return this;
    try{
      Map newMap = (Map)this.clone();
      newMap.loads.remove(load);
      newMap.loads.add(Map.movePoint(load, d));
      return newMap;
    }catch(CloneNotSupportedException e){
      e.printStackTrace();
      return this;
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString(){
    String result = "";
    for(int y=0;y<height;++y){
      for(int x=0;x<width;++x){
        Point p = new Point(x, y);
        Chip c = this.map.get(p);
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

  /* (non-Javadoc)
   * @see java.lang.Object#clone()
   */
  @Override
  protected Object clone() throws CloneNotSupportedException{
    HashMap<Point, Chip> newMap = new HashMap<Point, Chip>();
    HashSet<Point> newLoads = new HashSet<Point>();
    Iterator<Point> itr = map.keySet().iterator();
    while(itr.hasNext()){
      Point key = itr.next();
      newMap.put((Point)key.clone(), (Chip)map.get(key).clone());
    }
    itr = this.loads.iterator();
    while(itr.hasNext()){
      newLoads.add((Point)itr.next().clone());
    }
    return new Map(newMap, (Point)this.chara.clone(), newLoads);
  }
}
