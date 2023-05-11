package o1.adventure

import scala.collection.mutable.Map

class Container(val name: String, val description: String, val canBeOpened: Boolean) {
  private var onAuki = false
  private var itemInside: Option[Item] = None
  
  def open: Unit = this.onAuki = true
  
  def close: Unit = this.onAuki = false
  
  def isOpen: Boolean = this.onAuki
  
  def addItem(item: Item): Unit = {
    this.itemInside = Some(item)
  }
  
  def removeItem(): Unit = {
    if (itemInside != None) {
      this.itemInside = None
    }
  }
  
  def pickItem(): Option[Item] = {
    if (itemInside != None) itemInside else None
  }
  
  def containsItem(item: Item): Boolean = {
    this.itemInside == Some(item)
  }
  
  override def toString = this.name
}