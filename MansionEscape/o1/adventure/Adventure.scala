package o1.adventure


/** The class `Adventure` represents text adventure games. An adventure consists of a player and 
  * a number of areas that make up the game world. It provides methods for playing the game one
  * turn at a time and for checking the state of the game.
  *
  * N.B. This version of the class has a lot of "hard-coded" information which pertain to a very 
  * specific adventure game that involves a small trip through a twisted forest. All newly created 
  * instances of class `Adventure` are identical to each other. To create other kinds of adventure 
  * games, you will need to modify or replace the source code of this class. */
class Adventure {

  /** The title of the adventure game. */
  val title = "The Mansion Escape"
  
  // Basement areas:
  private val cellar      = new Area("Cellar", 
                            "The room is like a closet, only a bit larger. The walls are covered in shelves\n" +
                            "with full of wine bottles. It looks like you are in a a wine cellar. The cellar\n" +
                            "is gloomy, because there is only dim ceiling light.\n" + 
                            "There is a cardboard box on the floor.")
  private val passage     = new Area("Narrow corridor", 
                            "You are in a narrow corridor. Walls are made of stones. The stones are covered\n" +
                            "in mold and it smells musty here. It is pretty dark, you can barely see.")
  private val storage     = new Area("Storage room", 
                            "You use the flashlight to search the dark room. It is some kind of storage\n" +
                            "room. The room is full of garbage, not many interesting items here. There is\n" +
                            "a metal door with combination lock to the west. Steep wooden stairs lead up\n" +
                            "from the storage room.")
  private val darkStorage = new Area("Dark storage room", 
                            "You really can't see much. It is too dark. You hear strange noices coming from\n" +
                            "the west.")
  private val dissection  = new Area("Dissection room", 
                            "Oh shit! This room is covered in blood. There are body parts everywhere. A body\n" +
                            "bag is hanging from the ceiling.")
  
  // 1st floor:
  private val hall        = new Area("Hall 1st floor", 
                            "You are in the hall. A staircase leads up to the second floor and steep stairs\n" +
                            "lead down back to the basement. The outside door of the mansion is to the\n" +
                            "north. It has strong rusty padlock hanging in it. The kitchen is to the east\n" +
                            "and living room to the west.")
  private val kitchen     = new Area("Kitchen", 
                            "You are in the kitchen. There is a wooden table and four chairs around it.\n" +
                            "Kitchen countertops are filled with dirty dishes. A refrigerator is in the\n" +
                            "corner.")
  private val livingroom  = new Area("Living room",
                            "This is the living room. An antique sofa with an arched back is in the middle\n" +
                            "of the room. There is a fireplace made of bricks in the front of the sofa.")
  private val library     = new Area("Library", 
                            "This seems to be a library. Shelves are covered in books. Upholstered antique\n" +
                            "chair is placed into a corner of the room.")
  private val yard        = new Area("Yard", 
                            "You got out of the mansion to the front yard. You better call the police, there\n" +
                            "is something really wrong with this house.")
  
  //2nd floor:
  private val hall2       = new Area("Hall 2nd floor", 
                            "You are on the 2nd floor. The staircase leads down to the main hall. Bedroom is\n" +
                            "to the east and bathroom is to the west.")
  private val bedroom     = new Area("Bedroom", 
                            "This is the master bedroom. There is antique double bed made of mahogany.\n" +
                            "A bronze statue is placed besides the window.")
  private val bathroom    = new Area("Bathroom", 
                            "The bathroom looks very dirty. There is a porcelain bathtub, small sink and a\n" +
                            "toilet. Above the sink there is a mirror which has writing in it. It says\n" +
                            "'I can't sleep'.")

  
  /** The character that the player controls in the game. */
  val player = new Player(cellar)
  
  /** Non-Player Characters in the game: */
  val dog = new NPC(livingroom, "dog")
  dog.addDescriptions(Vector("The dog is happily eating the steak. The path to south is free now.", 
                             "Large black dog is lying on the floor in the front of the exit to the south.",
                             "The dog is standing. It growls at you.",
                             "The dog is angry and it's about to attack you. Don't do anything stupid!",
                             "The dog jumps at your throat and bites. You die!"))
  
  val corpse = new NPC(dissection, "corpse")
  corpse.addDescriptions(Vector("The corpse is now really dead.",
                                "A corpse is lying on the table. It's stomach is cut open and guts are coming out.",
                                "The corpse has opened it's eyes!",
                                "The corpse is sitting on the table",
                                "The corpse is standing by the table.",
                                "The corpse is walking slowly towards you.",
                                "The corpse approaches. It makes awful growling sounds.",
                                "The corpse approaches.",
                                "The corpse is almost within arm's reach of you!",
                                "The corpse attacks you! You die."))
  //corpse.changeAlert
  
  //Items:
  private val flashlight = new Usable("flashlight", "It's a flashlight. It is turned off.", "It's a flashlight. It is turned on." )   
  private val fishingrod = new Usable("fishing rod", "It's a fishing rod. However, it is not real, it looks like a mockup.", "")
  private val key = new Item("key", "It's an oxidized silver key. It looks very old.", true)
  private val steak = new Item("steak", "It's a huge delicious looking ribeye steak.", true)
  private val paper = new Item("paper", "On the paper there are numbers written with red ink. The numbers are: 2974", true)
  private val sword = new Usable("sword", "It's a katana. Looks sharp and dangerous.", "")
  
  //Transformable items:
  private val box = new Container("box", "It's a cardboard box around 50x50x50 cm in size.", true)
  box.addItem(flashlight)
  private val refrigerator = new Container("refrigerator", "It's very old refrigerator.", true)
  refrigerator.addItem(steak)
  private val statue = new Container("statue", "It's a fisherman statue. It's posing like it's fishing.", false)
  statue.addItem(paper)
  
  //Doors:
  private val storageDoor = new Door("door", "It's a metal door with combination lock.")
  private val outDoor = new Door("door", "It's an outside door with rusty padlock.")
  
  //Scenery objects. The player can't pick these items, only descriptions available if examined:
  private val floor = new Item("floor", "Nobody has cleaned here for a long time.", false)
  private val wine = new Item("wine", "It's wine.", false)
  private val bottles = new Item("bottles", "They are old wine bottles.", false)
  private val shelves = new Item("shelves", "The shelves are made of wood and filled with wine bottles.", false)
  private val light = new Item("light", "The light is very dim. You can barely see here.", false)
  private val walls = new Item("walls", "The walls are made of stones.", false)
  private val stones = new Item("stones", "The stones are wet and covered in mold.", false)
  private val garbage = new Item("garbage", "Yes, it's mostly trash. They are broken items dumped in to this room.", false)
  private val stairs = new Item("stairs", "They are wooden stairs between the basement and the 1st floor.", false)
  private val combLock = new Item("lock", "It's a combination lock. It has a numeric keypad.", false)
  private val dissTable = new Item("table", "It's white patient examination table. At least it used to be white, now it has blood stains everywhere.", false)
  private val bodyBag = new Item("bag", "It's a brown bag. It looks like there is a human body inside.", false)
  private val bodyParts = new Item("body parts", "Arms, legs, internal organs...", false)
  private val staircase = new Item("staircase", "It's a straight staircase between 1st and 2nd floor.", false)
  private val padlock = new Item("padlock", "An old rusty padlock. You are not able to break it.", false)
  private val countertop = new Item("countertop", "A kitchen countertop made of oak. There are dirty dishes on it.", false)
  private val dishes = new Item("dishes", "A pile of disgusting dirty dishes.", false)
  private val kitchenTable = new Item("table", "A massive oak dining table.", false)
  private val chairs = new Item("chairs", "Four chairs are arranged around the kitchen table.", false)
  private val sofa = new Item("sofa", "An antique sofa. It doesn't look very confortable.", false)
  private val fireplace = new Item("fireplace", "It's an empty fireplace made of bricks.", false)
  private val bricks = new Item("bricks", "Red bricks.", false)
  private val libraryShelves = new Item("shelves", "The room is entirely devoted to store books. There are book shelves everywhere.", false)
  private val books = new Item("books", "Old books with different titles.", false)
  private val chair = new Item("chair", "An upholstered antique chair used to sit and read books.", false)
  private val bathtub = new Item("bathtub", "A porcelain bathtub. There are blood stains in it.", false)
  private val sink = new Item("sink", "The sink is dirty. The drain is blocked with hair.", false)
  private val toilet = new Item("toilet", "It's full of skid marks. Smells bad.", false)
  private val mirror = new Item("mirror", "You see your tired face. The writing in the mirror says: 'I can't sleep'. It's written with lipstick.", false)
  private val bed = new Item("bed", "It's an antique double bed made of mahogany.", false)
  private val window = new Item("window", "For some reason the window has steel bars in it.", false)
  
  cellar.addContainer(box)
  cellar.addSceneryItem(wine)
  cellar.addSceneryItem(bottles)
  cellar.addSceneryItem(shelves)
  cellar.addSceneryItem(light)
  cellar.addSceneryItem(floor)
  
  passage.addSceneryItem(walls)
  passage.addSceneryItem(stones)
  passage.addSceneryItem(floor)
  
  storage.addItem(fishingrod)
  storage.addDoor(storageDoor)
  storageDoor.setBlock("west")
  storage.addSceneryItem(garbage)
  storage.addSceneryItem(stairs)
  storage.addSceneryItem(combLock)
  storage.addSceneryItem(floor)
  
  dissection.addNPC(corpse)
  dissection.addSceneryItem(dissTable)
  dissection.addSceneryItem(floor)
  dissection.addSceneryItem(bodyBag)
  dissection.addSceneryItem(bodyParts)
  
  hall.addDoor(outDoor)
  hall.addSceneryItem(staircase)
  hall.addSceneryItem(stairs)
  hall.addSceneryItem(padlock)
  outDoor.setBlock("north")
  
  kitchen.addContainer(refrigerator)
  kitchen.addSceneryItem(countertop)
  kitchen.addSceneryItem(dishes)
  kitchen.addSceneryItem(kitchenTable)
  kitchen.addSceneryItem(chairs)
  
  livingroom.addNPC(dog)
  dog.setBlock("south")
  livingroom.addSceneryItem(sofa)
  livingroom.addSceneryItem(fireplace)
  livingroom.addSceneryItem(bricks)
  
  library.addItem(sword)
  library.addSceneryItem(libraryShelves)
  library.addSceneryItem(books)
  library.addSceneryItem(chair)
  
  hall2.addSceneryItem(staircase)
  
  bedroom.addContainer(statue)
  bedroom.addSceneryItem(bed)
  bedroom.addSceneryItem(window)
  
  bathroom.addSceneryItem(bathtub)
  bathroom.addSceneryItem(sink)
  bathroom.addSceneryItem(toilet)
  bathroom.addSceneryItem(mirror)
  
  //Neigbors:
  cellar.setNeighbors(Vector("north" -> passage))
  passage.setNeighbors(Vector("north" -> darkStorage, "south" -> cellar))
  darkStorage.setNeighbors(Vector("south" -> passage))
  storage.setNeighbors(Vector("south" -> passage, "west" -> dissection, "up" -> hall))
  dissection.setNeighbors(Vector("east" -> storage))
  hall.setNeighbors(Vector("down" -> storage, "east" -> kitchen, "west" -> livingroom, "up" -> hall2, "north" -> yard))
  kitchen.setNeighbors(Vector("west" -> hall))
  livingroom.setNeighbors(Vector("east" -> hall, "south" -> library))
  library.setNeighbors(Vector("north" -> livingroom))
  hall2.setNeighbors(Vector("down" -> hall, "east" -> bedroom, "west" -> bathroom))
  bathroom.setNeighbors(Vector("east" -> hall2))
  bedroom.setNeighbors(Vector("west" -> hall2))
  
  private val destination = yard

  /** Determines if player has died. */
  var playerKilled = false

  /** The number of turns that have passed since the start of the game. */
  var turnCount = 0
  /** The maximum number of turns that this adventure game allows before time runs out. */
  val timeLimit = 200
  

  /** Determines if the adventure is complete, that is, if the player has won. */
  def isComplete = this.player.location == this.destination

  /** Determines whether the player has won, lost, or quit, thereby ending the game. */ 
  def isOver = this.isComplete || this.player.hasQuit || this.playerKilled

  /** Returns a message that is to be displayed to the player at the beginning of the game. */
  def welcomeMessage = "You wake up on the floor. The room you are in doesn't look familiar.\n\n" +
                       "You remember that you went to a bar with your friends.\n" +
                       "But after that your memory is blank."

    
  /** Returns a message that is to be displayed to the player at the end of the game. The message 
    * will be different depending on whether or not the player has completed their quest. */
  def goodbyeMessage = {
    if (this.isComplete)
      "You escaped the mansion alive! Well done!"
    else if (this.playerKilled)
      "You are dead. Game over!"
    else  // game over due to player quitting
      "Until next time. See you soon." 
  }

  
  /** Plays a turn by executing the given in-game command, such as "go west". Returns a textual 
    * report of what happened, or an error message if the command was unknown. */
  def playTurn(command: String) = {
    val action = new Action(command)
    val outcomeReport = action.execute(this.player)
    
    if (player.location == darkStorage && player.has(flashlight.name) && flashlight.isInUse ) { 
      this.player.setLocation(storage)
    }
    
    if (player.location == dissection && corpse.returnAlert) {
      corpse.increaseCounter
    } else if (player.location != dissection && corpse.returnAlert) {
      corpse.resetCounter // Reset counter if player leaves dissection room and corpse is still alive
    }
    
    turnCount += 1
    this.playerKilled = this.player.location.returnNPC match {
      case Some(npc) => npc.killsPlayer
      case None => false
    }
    
    outcomeReport.getOrElse("Unknown command: \"" + command + "\".")
  }
  
  
}

