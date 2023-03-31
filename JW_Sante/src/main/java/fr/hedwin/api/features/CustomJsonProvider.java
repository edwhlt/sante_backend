package fr.hedwin.api.features;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import java.util.List;
import java.util.stream.Collectors;

@Produces(MediaType.APPLICATION_JSON)
public class CustomJsonProvider extends JacksonJaxbJsonProvider {

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new SimpleModule().setSerializerModifier(new BeanSerializerModifier() {
            @Override
            public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
                return beanProperties.stream().map(bpw -> new BeanPropertyWriter(bpw) {
                    @Override
                    public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov) throws Exception {
                        try {
                            super.serializeAsField(bean, gen, prov);
                        } catch (Exception e) {
                            System.out.printf("ignoring %s for field '%s' of %s instance%n", e.getClass().getName(), this.getName(), bean.getClass().getName());
                        }
                    }
                }).collect(Collectors.toList());
            }
        }));
    }

    public CustomJsonProvider() {
        super();
        setMapper(mapper);
    }
}