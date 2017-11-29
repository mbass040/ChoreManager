package seg2105.uottawa.com.taskmanager.source;

/**
 * Created by bedard on 2017-11-25.
 */

public class CartItem extends Item {
   public enum ItemType {Grocery, Material}
   private  int id;
   private ItemType type;

   public CartItem(int id,String name, ItemType type){
      this.type = type;
      itemName = name;
      this.id = id;
   }
    public ItemType getIsAMaterial(){
      return type;
    }
    public void setIsAMaterial(ItemType material){type= material;
    }

}
