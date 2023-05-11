package o1.adventure

class Door(val name: String, val description: String) {
  private var block = ""
  private var onAuki = false
  private var onLukossa = true
  
  def setBlock(direction: String) = this.block = direction
  
  def blockingDir: String = {
    if (!onAuki) block else ""
  }
  
  def unlock = this.onLukossa = false
  
  def isLocked: Boolean = this.onLukossa
  
  def open = this.onAuki = true
  
  def close = this.onAuki = false
  
  def isOpen: Boolean = this.onAuki
  
  override def toString = this.name
}