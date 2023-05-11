package o1.adventure

import scala.collection.mutable.Map

  
/** A `Player` object represents a player character controlled by the real-life user of the program. 
  *
  * A player object's state is mutable: the player's location and possessions can change, for instance.
  *
  * @param startingArea  the initial location of the player */
class Player(startingArea: Area) {

  private var currentLocation = startingArea        // gatherer: changes in relation to the previous location
  private var quitCommandGiven = false              // one-way flag
  private var inv = Map[String, Item]()
  private val commands = "go, open, close, unlock, use, quit, get, drop, give, kill, examine, inventory"
  
  /** Determines if the player has indicated a desire to quit the game. */
  def hasQuit = this.quitCommandGiven

  
  /** Returns the current location of the player. */
  def location = this.currentLocation
  
  def setLocation(newLocation: Area) = this.currentLocation = newLocation

  /** Attempts to move the player in the given direction. This is successful if there 
    * is an exit from the player's current location towards the direction name and
    * there isn't Non-Player Character or door blocking the direction. 
    * Returns a description of the results of the attempt. */
  def go(direction: String) = {
    val npcBlocks = this.location.returnNPC match {
      case Some(npc) => npc.blockingDir
      case None => ""
    }
    val doorBlocks = this.location.returnDoor match {
      case Some(door) => door.blockingDir
      case None => ""
    }
    
    // Check if NPC blocks the direction you want to go:
    if (npcBlocks == direction) {
      this.location.returnNPC match {
        case Some(npc) => {
          npc.increaseCounter
          "The direction is blocked. " + npc.returnDescription
        }
        case None => ""
      }
    } 
    //Check if door blocks the direction you want to go:
    else if (doorBlocks == direction) {
      this.location.returnDoor match {
        case Some(door) => if (door.isLocked) "The door is locked." else "The door is closed."
        case None => ""
      }
    } 
    else {
      val destination = this.location.neighbor(direction)
      this.currentLocation = destination.getOrElse(this.currentLocation) 
      if (destination.isDefined) "You go " + direction + "." else "You can't go " + direction + "."
    }
  }

  
  /** Causes the player to rest for a short while (this has no substantial effect in game terms).
    * Returns a description of what happened. */
  def rest() = {
    "You rest for a while. Better get a move on, though." 
  }
  
  /** If the player wants to sleep, return description */
  def sleep() = {
    "There is no time to sleep. You have to get out of here!"
  }
  
  /** Signals that the player wants to quit the game. Returns a description of what happened within 
    * the game as a result (which is the empty string, in this case). */
  def quit() = {
    this.quitCommandGiven = true
    ""
  }

  /** Drops the item the player has in the inventory. The flashlight can't be dropped. */
  def drop(itemName: String): String = {
    if (inv.contains(itemName)) {
      var esine = this.inv(itemName)
      if(itemName != "flashlight") {
        this.inv.remove(itemName)
        this.location.addItem(esine)
        "You drop the " + itemName + "."
      }
      else "You don't want to drop the flashlight. It's important for your survival."
    }
    else
      "You don't have that."
  }
  
  def examine(itemName: String): String = {
    (inv ++ this.location.allItemsInArea).get(itemName) match {
      case Some(item: Usable) => "You take a look on the " + itemName + ".\n" + (if (!item.isInUse) item.description else item.description2)
      case Some(item: Item) => "You look closely at the " + itemName + ".\n" + item.description
      case None => {
        if (this.location.containsCont(itemName)) {
          this.location.returnContainer(itemName).map(_.description).getOrElse("")
        } else if (this.location.returnNPC.isDefined) {
          this.location.returnNPC.map(_.returnDescription).getOrElse("")
        } else if (this.location.returnDoor.isDefined) {
          this.location.returnDoor.map(_.description).getOrElse("")
        } else "You don't see any " + itemName + " here."
      }
    }
  }
  
  /** Takes the item if it's in the Area */
  def get(itemName: String): String = {
    this.location.removeItem(itemName) match {
      case Some(item) => {
        this.inv += itemName -> item
        "You pick up the " + itemName + "."
      }
      case None => if (!itemName.isEmpty) "You can't take the " + itemName + "." else "Take what?"
    }
  }
  
  /** Opens a container or a door if there is one in the Area */
  def open(thing: String): String = {
    //Check if the "thing" is in the area and can it be opened. If the "thing" contains item, the player takes it.
    if (this.location.containsCont(thing) && this.location.returnContainer(thing).exists(_.canBeOpened)) {
      if (!this.location.returnContainer(thing).exists(_.isOpen)) {
        this.location.returnContainer(thing).foreach(_.open)
        var itemName = this.location.returnContainer(thing).flatMap(_.pickItem) match {
          case Some(item) => {
            this.inv += item.name -> item
            this.location.returnContainer(thing).foreach(_.removeItem)
            item.name
          }
          case None => "nothing"
        }
        "You open the " + thing + ". There is " + itemName + " inside. " + (if (itemName != "nothing") "You take the " + itemName + "." else "")
      }
      else "It is already open."
    }
    
    // Check is there a door and can the player open it:
    else if (this.location.returnDoor.exists(_.name == thing)) {
      if (this.location.returnDoor.exists(_.isLocked)) {
        "The door is locked."
      }
      else if (this.location.returnDoor.exists(_.isOpen)) {
        "The door is already open."
      }
      else {
        this.location.returnDoor.foreach(_.open)
        "You open the " + thing + "."
      }
    }
    else {
      "You can't open that."
    }   
  }
  
  /** Closes a container or a door if there is one in the Area */
  def close(thing: String): String = {
    // Check if the "thing" is in the area and can it be closed.
    if (this.location.containsCont(thing) && this.location.returnContainer(thing).exists(_.canBeOpened)) {
      if (this.location.returnContainer(thing).exists(_.isOpen)) {
        this.location.returnContainer(thing).foreach(_.close)
        "You close the " + thing + "."
      }
      else "It is closed."
    }
    
    // Check is there a door and can the player close it:
    else if (this.location.returnDoor.exists(_.name == thing)) {
      if (this.location.returnDoor.exists(!_.isOpen)) {
        "The door is closed."
      }
      else {
        this.location.returnDoor.foreach(_.close)
        "You close the " + thing + "."
      }
    }
    else {
      "You can't close that."
    }
  }
  
  /** Unlock door if conditions are met. */
  def unlock(thing: String) = {
    if (this.location.returnDoor.exists(_.name == thing)) {
      if (this.location.name == "Storage room" && this.has("paper") && this.location.returnDoor.exists(_.isLocked)) {
        this.location.returnDoor.foreach(_.unlock)
        this.location.returnDoor.foreach(_.open)
        "You look on the numbers written on the paper and type them in to the combination lock. The door opens."
      }
      else if (this.location.name == "Hall 1st floor" && this.has("key") && this.location.returnDoor.exists(_.isLocked)) {
        this.location.returnDoor.foreach(_.unlock)
        "You unlock the padlock of the door with silver key."
      } 
      else if (this.location.returnDoor.exists(!_.isLocked)) {
        "The door is already unlocked."
      } else "You don't have anything that you could use to unlock the door."
    }
    else {
      "You can't unlock that."
    }
  }
  
  def lock(thing: String) = {
    "Why do you want to lock it?"
  }
  
  /** Try to use the item. */
  def use(itemName: String) = {
    inv.get(itemName) match {
      case Some(item: Usable) => {
        if (!item.isInUse && item.name == "flashlight") {
          item.turnOn
          "You use the " + itemName + "." 
        } else if (item.isInUse && item.name == "flashlight") {
          "You are already using it."
        } else if (item.name == "fishing rod" && this.location.containsCont("statue")) {
          this.put(item.name)
        } else if (item.name == "sword") {
          this.location.returnNPC match {
            case Some(npc) => kill(npc.name)
            case None => "You don't know how to use it here."
          }
        } else "You don't have any idea how to use it here."    
      }
      case Some(item: Item) => "You can't use the " + itemName + "."
      case None => "You don't have the " + itemName + "."
    }
  }
  
  /** Put item into something i.e fishing rod in to statue in this game. */
  def put(itemName: String) = {
    inv.get(itemName) match {
      case Some(item) =>
        if (item.name == "fishing rod" && this.location.containsCont("statue")) {
          this.location.returnContainer("statue").flatMap(_.pickItem) match {
            case Some(itemInside) => { 
              this.inv += itemInside.name -> itemInside
              this.location.returnContainer("statue").foreach(_.removeItem)
              this.inv.remove(itemName)
              "You place the rod in to statues hands. A secret drawer opens from the side of the statue. It contains a piece of paper which you take."
            }
            case None => "Nothing happens."
          }
        } else "You don't know where to put the " + itemName + "."
      case None => "You don't have the " + itemName + "."
    }
  }
  
  def eat(itemName: String) = {
    inv.get(itemName) match {
      case Some(item) =>
        if(item.name == "steak")
          "You are not hungry. Besides you might need it later."
        else
          "You can't eat that."
      case None => "You don't have the " + itemName + "."
    }
  }
  
  /** Give item to NPC. The dog is the only one in this game who you can give an item. */
  def give(itemName: String) = {
    inv.get(itemName) match {
      case Some(item) => {
        this.location.returnNPC match {
          case Some(npc) =>
            if(item.name == "steak" && npc.name == "dog") {
              npc.changeAlert
              this.inv.remove(itemName)
              "You give the steak to the dog."
            } else "You don't want to give it away because you might need it later."
          case None => "You can't give the " + item.name + ", because there is nobody here."
        }  
      }
      case None => "You don't have the " + itemName + "."
    }
  }
  
  /** Kill NPC. The corpse can be killed in this game with the sword. The corpse drops a key when killed. */
  def kill(someone: String) = {
    this.location.returnNPC match {
      case Some(npc) => {
        if (npc.name == someone) {
          if(npc.name == "corpse" && this.has("sword") && npc.returnAlert) {
            val key = new Item("key", "It's an oxidized silver key. It looks very old.", true)
            npc.changeAlert
            this.location.addItem(key)
            "You swing the sword at the corpse. You cut its head off!\n" + "The corpse drops a " + key.name + "."
          } else if(npc.name == "corpse" && !this.has("sword") && npc.returnAlert) {
            "You can't kill it with bare hands!"
          } else "You don't want to kill it."
        } else "Who is " + someone + "?"
      }
      case None => "There is nobody here who you could kill."
    }
  }
  
  def sing() = {
    "AAAAIIiikuinen nainen! Kaiken me jaamme, toisiamme tukekaamme..."
  }
  
  def listen() = {
    "You don't hear anything."
  }
  
  def break(itemName: String): String = {
    "You don't want to break things."
  }
  
  def has(itemName: String): Boolean = {
    this.inv.contains(itemName)
  }
  
  def inventory: String = {
    if (inv.isEmpty)
      "You are empty-handed."
    else
      "You are carrying:\n" + inv.keys.mkString("\n")
  }
    
  def help: String = {
    "Your mission is to escape from this mansion.\n" +
    "The text parser understands sentences with 'verb + noun'. Some commands are\n" +
    "used by just typing a 'verb'.\n" +
    "These are the most important verbs you can use in this game:\n" + commands
  }
  /** Returns a brief description of the player's state, for debugging purposes. */
  override def toString = "Now at: " + this.location.name   


}


