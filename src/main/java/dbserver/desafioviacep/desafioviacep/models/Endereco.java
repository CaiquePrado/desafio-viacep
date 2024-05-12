package dbserver.desafioviacep.desafioviacep.models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Endereco {

  private String cep;
  private String logradouro;
  private String complemento;
  private String bairro;
  private String localidade;
  private String uf;
  private String ibge;
  private String gia;
  private String ddd;
  private String siafi;

}
