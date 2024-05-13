package dbserver.desafioviacep.desafioviacep.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Menssagem {

  private String status;
  private String title;
  private String descricao;

}
