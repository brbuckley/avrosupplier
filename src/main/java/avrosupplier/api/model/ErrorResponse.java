package avrosupplier.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** ErrorResponse. */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonPropertyOrder({"error_code", "description"})
public class ErrorResponse {

  @JsonProperty(value = "error_code")
  private String errorCode = null;

  private String description = null;
}
