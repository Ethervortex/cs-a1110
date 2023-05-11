package o1.adventure


/** The class `Action` represents actions that a player may take in a text adventure game.
  * `Action` objects are constructed on the basis of textual commands and are, in effect, 
  * parsers for such commands. An action object is immutable after creation.
  * @param input  a textual in-game command such as "go east" or "rest" */
class Action(input: String) {

  private val commandText = input.trim.toLowerCase
  private val verb        = commandText.takeWhile( _ != ' ' )
  private val modifiers   = commandText.drop(verb.length).trim

  
  /** Causes the given player to take the action represented by this object, assuming 
    * that the command was understood. Returns a description of what happened as a result 
    * of the action (such as "You go west."). The description is returned in an `Option` 
    * wrapper; if the command was not recognized, `None` is returned. */
  def execute(actor: Player) = {                             

    if (this.verb == "go" || this.verb == "walk") {
      Some(actor.go(this.modifiers))
    } else if (this.verb == "north" || this.verb == "east" || this.verb == "south" || this.verb == "west" || this.verb == "up" || this.verb == "down") {
      Some(actor.go(this.verb))
    } else if (this.verb == "wait" || this.verb == "rest") {
      Some(actor.rest())
    } else if (this.verb == "sleep") {
      Some(actor.sleep())
    } else if (this.verb == "quit" || this.verb == "exit") {
      Some(actor.quit())
    } else if (this.verb == "drop" || this.verb == "throw") {
      Some(actor.drop(this.modifiers))  
    } else if (this.verb == "examine" || this.verb == "x" || this.verb == "inspect" || this.verb == "look") {
      Some(actor.examine(this.modifiers))
    } else if (this.verb == "get" || this.verb == "take") {
      Some(actor.get(this.modifiers))
    } else if (this.verb == "open") {
      Some(actor.open(this.modifiers))
    } else if (this.verb == "close") {
      Some(actor.close(this.modifiers))
    } else if (this.verb == "unlock") {
      Some(actor.unlock(this.modifiers))
    } else if (this.verb == "lock") {
      Some(actor.lock(this.modifiers))
    } else if (this.verb == "use") {
      Some(actor.use(this.modifiers))
    } else if (this.verb == "put") {
      Some(actor.put(this.modifiers))
    } else if (this.verb == "eat") {
      Some(actor.eat(this.modifiers))
    } else if (this.verb == "give") {
      Some(actor.give(this.modifiers))
    } else if (this.verb == "kill") {
      Some(actor.kill(this.modifiers))
    } else if (this.verb == "sing") {
      Some(actor.sing())
    } else if (this.verb == "listen") {
      Some(actor.listen())
    } else if (this.verb == "break") {
      Some(actor.break(this.modifiers))
    } else if (this.verb == "inventory" || this.verb == "i" || this.verb == "inv") {
      Some(actor.inventory)
    } else if (this.verb == "help") {
      Some(actor.help)
    } else {
      None
    }
    
  }


  /** Returns a textual description of the action object, for debugging purposes. */
  override def toString = this.verb + " (modifiers: " + this.modifiers + ")"  

  
}

