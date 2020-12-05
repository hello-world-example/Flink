package xyz.kail.demo.flink.tools.formats;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.table.factories.DeserializationSchemaFactory;
import org.apache.flink.table.factories.SerializationSchemaFactory;
import org.apache.flink.table.factories.TableFactory;
import org.apache.flink.table.factories.TableFormatFactoryBase;
import org.apache.flink.types.Row;

import java.io.IOException;
import java.util.Map;

public class RawTableFactory extends TableFormatFactoryBase<Row> implements TableFactory, SerializationSchemaFactory<Row>, DeserializationSchemaFactory<Row> {

    public RawTableFactory(String type, int version, boolean supportsSchemaDerivation) {
        super(type, version, supportsSchemaDerivation);
    }

    @Override
    public DeserializationSchema<Row> createDeserializationSchema(Map<String, String> properties) {
        return new DeserializationSchema<Row>() {
            @Override
            public Row deserialize(byte[] message) throws IOException {
                return null;
            }

            @Override
            public boolean isEndOfStream(Row nextElement) {
                return false;
            }

            @Override
            public TypeInformation<Row> getProducedType() {
                return null;
            }
        };
    }

    @Override
    public SerializationSchema<Row> createSerializationSchema(Map<String, String> properties) {
        return new SerializationSchema<Row>() {
            @Override
            public byte[] serialize(Row element) {
                return new byte[0];
            }
        };
    }
}
