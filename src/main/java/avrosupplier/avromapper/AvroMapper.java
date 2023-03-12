package avrosupplier.avromapper;

import com.liquorstore.purchase.PurchaseOrder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.springframework.stereotype.Component;

/** Works like an object mapper for Avro objects. */
@Slf4j
@Component
public class AvroMapper {

  /**
   * Parses a byte array to a Purchase Order.
   *
   * @param data byte array.
   * @return Purchase Order.
   * @throws IOException IO Exception.
   */
  public PurchaseOrder byteArrayToPurchaseOrder(byte[] data) throws IOException {
    PurchaseOrder purchaseOrder;
    // try with resources to avoid OBL_UNSATISFIED_OBLIGATION
    try (InputStream schemaStream = getClass().getResourceAsStream("/avro/Purchase.avsc")) {
      Schema schema = new Schema.Parser().parse(schemaStream);
      DatumReader<PurchaseOrder> datumReader = new SpecificDatumReader<>(schema);
      purchaseOrder = datumReader.read(null, DecoderFactory.get().binaryDecoder(data, null));
    }
    return purchaseOrder;
  }

  /**
   * Parses a Generic Record into a byte array.
   *
   * @param datum Generic Record.
   * @return byte array.
   * @throws IOException IO Exception.
   */
  public byte[] objectToByteArray(GenericRecord datum) throws IOException {
    DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(datum.getSchema());
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    Encoder encoder = EncoderFactory.get().binaryEncoder(stream, null);
    datumWriter.write(datum, encoder);
    encoder.flush();
    return stream.toByteArray();
  }
}
