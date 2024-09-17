package io.go7.hackathon.bedrockproxy.lambda;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {

  private static final ObjectMapper objectMapper = new ObjectMapper()
      .registerModule(new JavaTimeModule())
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


  @SneakyThrows
  public static <T> T readJson(String json, Class<T> type) {
    return objectMapper.readValue(json, type);
  }

  @SneakyThrows
  public static String toJson(Object value) {
    return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
  }
}
