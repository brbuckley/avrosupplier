package avrosupplier;

import avrosupplier.avromapper.AvroMapper;
import com.liquorstore.purchase.OrderLine;
import com.liquorstore.purchase.PurchaseOrder;
import com.liquorstore.supplier.SupplierOrder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SupplierResponseUtil {

  public static PurchaseOrder defaultPurchaseOrder() {
    List<OrderLine> items = new ArrayList<>();
    items.add(new OrderLine(1, "PRD0000001"));
    items.add(new OrderLine(2, "PRD0000002"));
    return new PurchaseOrder("PUR0000001", items);
  }

  public static SupplierOrder defaultSupplierOrder() {
    return new SupplierOrder("SUC0000001", "PUR0000001");
  }

  public static byte[] defaultPayload() throws IOException {
    PurchaseOrder purchaseOrder = defaultPurchaseOrder();
    return new AvroMapper().objectToByteArray(purchaseOrder);
  }
}
