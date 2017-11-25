package seg2105.uottawa.com.taskmanager.source;

/**
 * Created by bedard on 2017-11-25.
 */

public class CartItem extends Item {
   public enum ItemType {Material, Grocery}
   private ItemType material;

   public CartItem(String name, ItemType material){
      this.material = material;
      itemName = name;
   }
    public ItemType getIsAMaterial(){
      return material;
    }
    public void setIsAMaterial(ItemType material){
       material = material;
    }

}
