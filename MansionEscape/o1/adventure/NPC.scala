package o1.adventure

import scala.collection.mutable.Map

class NPC (startingArea: Area, val name: String) {
  
  private var isAlert = true
  private var block = ""
  private var counter = 1
  private val descriptions = Map[Int, String]()
  
  def returnAlert: Boolean = isAlert
  
  def setBlock(direction: String) = this.block = direction
  
  def blockingDir: String = {
    if (isAlert) block else ""
  }
  
  def changeAlert = {
    isAlert = false
    block = ""
    counter = 0 // if isAlert == false, description for NPC can be found in index 0
  }
  
  def addDescriptions(descr: Vector[String]) = {
    descriptions ++= descr.zipWithIndex.map(_.swap).toMap
  }
  
  def increaseCounter = if (counter < descriptions.size - 1) counter += 1
  
  def resetCounter = counter = 0
  
  def killsPlayer: Boolean = {
    if (counter == descriptions.size - 1) true else false
  }
  
  def returnDescription: String = {
    this.descriptions.get(counter) match {
      case Some(npc) => npc
      case None => ""
    }
  }
  
  override def toString = this.name
}