package hu.martin.chatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.math.BigInteger;

@Configuration
public class JsonBigintegerSerializationConfiguration {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> builder.serializers(new BigIntegerJsonSerializer());
    }

    private static final class BigIntegerJsonSerializer extends StdSerializer<BigInteger> {

        public BigIntegerJsonSerializer() {
            this(BigInteger.class);
        }

        public BigIntegerJsonSerializer(Class<BigInteger> t) {
            super(t);
        }

        @Override
        public void serialize(BigInteger value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(value.toString());
        }
    }
}
