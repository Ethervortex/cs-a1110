package o1.adventure

import scala.collection.mutable.Map

/** The class `Area` represents locations in a text adventure game world. A game world 
  * consists of areas. In general, an "area" can be pretty much anything: a room, a building, 
  * an acre of forest, or something completely different. What different areas have in 
  * common is that players can be located in them and that they can have exits leading to 
  * other, neighboring areas. An area also has a name and a description. 
  * @param name         the name of the area 
  * @param description  a basic description of the area (typically not including information about items) */
class Area(var name: String, var description: String) {
  
  private var neighbors = Map[String, Area]()
  private val items = Map[String, Item]()
  private val sceneryItems = Map[String, Item]()
  private val containers = Map[String, Container]()
  private var npcInArea: Option[NPC] = None
  private var doorInArea: Option[Door] = None
  
  /** Returns the area that can be reached from this area by moving in the given direction. The result 
    * is returned in an `Option`; `None` is returned if there is no exit in the given direction. */
  def neighbor(direction: String) = this.neighbors.get(direction)

  
  /** Adds an exit from this area to the given area. The neighboring area is reached by moving in 
    * the specified direction from this area. */
  def setNeighbor(direction: String, neighbor: Area) = {
    this.neighbors += direction -> neighbor
  }

  
  /** Adds exits from this area to the given areas. Calling this method is equivalent to calling 
    * the `setNeighbor` method on each of the given direction--area pairs.
    * @param exits  contains pairs consisting of a direction and the neighboring area in that direction
    * @see [[setNeighbor]] */
  def setNeighbors(exits: Vector[(String, Area)]) = {
    this.neighbors ++= exits
  }
  
  def removeNeighbor(direction: String) = {
    this.neighbors -= direction
  }
  
  /** Returns a multi-line description of the area as a player sees it. This includes a basic 
    * description of the area as well as information about npcs, exits and items. */
  def fullDescription = {
    val exitList = "\n\nExits available: " + this.neighbors.keys.mkString(" ")
    val itemList = if(!this.items.isEmpty) "\nYou see here: " + this.items.keys.mkString("  ") else ""
    val npcDescription = this.npcInArea match {
      case Some(npc) => "\n" + npc.returnDescription
      case None => ""
    }
    this.description + npcDescription + itemList + exitList
  } 
  
  def addItem(item: Item): Unit = {
    this.items += item.name -> item
  }
  
  def removeItem(itemName: String): Option[Item] = {
    this.items.remove(itemName)
  }
  
  def contains(itemName: String): Boolean = {
    this.items.contains(itemName)
  }
  
  def addContainer(cont: Container): Unit = {
    this.containers += cont.name -> cont
  }
  
  def returnContainer(cont: String): Option[Container] = {
    this.containers.get(cont)
  }
  def containsCont(cont: String): Boolean = {
    this.containers.contains(cont)
  }
  
/*  def pickCont(cont: String): Container = {
    this.containers.get(cont) match {
      case Some(cont) => cont
    }
  } */
  
  def addNPC(npc: NPC) = {
    this.npcInArea = Some(npc)
  }
  
  def returnNPC: Option[NPC] = this.npcInArea
  
  def addDoor(door: Door) = {
    this.doorInArea = Some(door)
  }
  
  def returnDoor: Option[Door] = this.doorInArea
  
  def addSceneryItem(item: Item): Unit = {
    this.sceneryItems += item.name -> item
  }
  
  def allItemsInArea = {
    items ++ sceneryItems
  }
  
  /** Returns a single-line description of the area for debugging purposes. */
  override def toString = this.name + ": " + this.description.replaceAll("\n", " ").take(150)

  
  
}
